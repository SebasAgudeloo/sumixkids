package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.dao.DispositivoReconocidoDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.util.PasswordUtil;
import com.sumixkids.util.TwoFactorUtil;
import com.sumixkids.util.ClienteUtil;
import com.sumixkids.service.EmailService;
import com.sumixkids.dao.TwoFactorCodeDAO;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Atiende la pantalla de inicio de sesión.
 * GET: muestra el formulario.
 * POST: revisa usuario/clave y crea la sesión si todo está bien.
 */
public class LoginServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	private final UsuarioDAO usuarioDAO = new UsuarioDAO();
	private final DispositivoReconocidoDAO dispositivoDAO = new DispositivoReconocidoDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
	        throws ServletException, IOException {
	    
	    String timeout = req.getParameter("timeout");
	    if ("true".equals(timeout)) {
	        req.setAttribute("mensaje", "Su sesión ha expirado por inactividad");
	    }
	    
	    // CAPTCHA siempre visible para mayor seguridad
	    req.setAttribute("showCaptcha", true);
	    
	    req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Procesa el envío del formulario de login.
		req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("username"); // puede ser usuario o correo
		String password = req.getParameter("password");
		String captchaInput = req.getParameter("captcha");

		if (isBlank(username) || isBlank(password)) {
			req.setAttribute("error", "Usuario y contraseña son obligatorios");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
			return;
		}
		
		// Validar CAPTCHA siempre (seguridad permanente)
		HttpSession session = req.getSession(true);
		
		// Siempre validar CAPTCHA
		{
			String captchaCode = (String) session.getAttribute("captchaCode");
			
			if (isBlank(captchaInput)) {
				req.setAttribute("error", "Por favor, resuelve la operación matemática");
				req.setAttribute("username", username);
				req.setAttribute("showCaptcha", true);
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
				return;
			}
			
			if (captchaCode == null || !captchaInput.trim().equals(captchaCode)) {
				req.setAttribute("error", "La respuesta del CAPTCHA es incorrecta");
				req.setAttribute("username", username);
				req.setAttribute("showCaptcha", true);
				// Limpiar CAPTCHA para forzar uno nuevo
				session.removeAttribute("captchaCode");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
				return;
			}
			
			// CAPTCHA correcto, limpiar de la sesión
			session.removeAttribute("captchaCode");
			session.removeAttribute("showCaptcha");
		}

		try {
			Usuario u = usuarioDAO.findByUsernameOrEmail(username); // Buscamos por nombre o correo.
			int maxIntentos = 3; // Número máximo de intentos permitidos
			if (u == null) {
				req.setAttribute("error", "Usuario o contraseña incorrectos");
				req.setAttribute("username", username);
				req.setAttribute("showCaptcha", true);
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
				return;
			}
			// Si la cuenta ya está bloqueada
			if (Boolean.TRUE.equals(u.getBloqueado())) {
				req.setAttribute("error", "Cuenta bloqueada por múltiples intentos fallidos");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
				return;
			}

			if (PasswordUtil.verify(password, u.getPasswordHash())) {
									
				// Contraseña correcta: reiniciamos contador de intentos fallidos
				usuarioDAO.updateLoginSuccess(u.getId());
				
				// Login exitoso: limpiar flags de CAPTCHA
				session.removeAttribute("showCaptcha");
				session.removeAttribute("captchaCode");
				session.setAttribute("usuario", u);

				// Obtener información del dispositivo
				String clienteIP = ClienteUtil.getClienteIP(req);
				String userAgent = ClienteUtil.getUserAgent(req);
				
				// Verificar si este dispositivo necesita 2FA (con fallback seguro)
				boolean necesita2FA = true; // Por defecto, requerir 2FA por seguridad
				try {
					necesita2FA = dispositivoDAO.necesita2FA(u.getId(), clienteIP, userAgent);
					logger.info("Verificación 2FA completada - Usuario: {}, necesita2FA: {}", u.getUsername(), necesita2FA);
				} catch (Exception e) {
					logger.warn("Error verificando necesidad de 2FA para usuario {}: {}. Aplicando 2FA por seguridad.", 
							   u.getUsername(), e.getMessage());
					necesita2FA = true; // Fallback seguro: siempre pedir 2FA si hay error
				}
				
				if (!necesita2FA) {
					// Dispositivo reconocido, login exitoso directo
					session.setAttribute("2fa_passed", true);
					logger.info("Login exitoso sin 2FA - Usuario: {} desde dispositivo reconocido", u.getUsername());
					resp.sendRedirect(req.getContextPath() + "/bienvenida");
					return;
				}

				// Dispositivo no reconocido o necesita 2FA, generar código 2FA
				logger.info("Generando código 2FA para usuario: {}", u.getUsername());
				String code = TwoFactorUtil.generateCode();
				java.time.LocalDateTime expiresAt = java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Bogota")).plusMinutes(15).toLocalDateTime();
				logger.info("Código 2FA generado: {}, guardando en BD", code);
				try {
					TwoFactorCodeDAO.guardarCodigo(u.getId(), code, expiresAt);
					TwoFactorCodeDAO.eliminarCodigosExpiradosYUsados();
					logger.info("Código 2FA guardado exitosamente en BD");
				} catch (SQLException e) {
					logger.error("Error al guardar el código 2FA", e);
					req.setAttribute("error", "Error al guardar el código 2FA. Intenta de nuevo más tarde.");
					req.getRequestDispatcher("/login.jsp").forward(req, resp);
					return;
				}

				session.setAttribute("2fa_passed", false);

				// Leer config de correo y crear EmailService
				logger.info("Preparando envío de email 2FA para usuario: {}", u.getUsername());
				Properties props2 = new Properties();
				try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
					if (in != null) props2.load(in);
				}
				String mailUser2 = props2.getProperty("mail.smtp.user");
				String mailPass2 = props2.getProperty("mail.smtp.pass");
				logger.info("Configuración de email cargada, creando EmailService");
				EmailService emailService2 = new EmailService(mailUser2, mailPass2);
				try {
					// Obtener información del dispositivo para mostrar en el email
					String infoDispositivo;
					try {
						infoDispositivo = dispositivoDAO.getInfoDispositivo(u.getId(), clienteIP, userAgent);
					} catch (SQLException e) {
						infoDispositivo = "Información no disponible";
						logger.warn("No se pudo obtener información del dispositivo", e);
					}
					
					// Usar el método específico del EmailService para 2FA
					logger.info("Enviando email 2FA a: {}", u.getEmail());
					emailService2.send2FAEmail(u.getEmail(), u.getNombres(), u.getApellidos(), 
											 code, clienteIP, infoDispositivo);
					logger.info("Email 2FA enviado exitosamente");
				} catch (Exception ex) {
					logger.error("No se pudo enviar el código 2FA a {}", u.getEmail(), ex);
					req.setAttribute("error", "No se pudo enviar el código 2FA a tu correo. Intenta de nuevo más tarde.");
					req.getRequestDispatcher("/login.jsp").forward(req, resp);
					return;
				}
				logger.info("Redirigiendo a página 2FA para usuario: {}", u.getUsername());
				resp.sendRedirect(req.getContextPath() + "/2fa");
			} else {
				// Calcular si será bloqueado ANTES de actualizar para enviar correo si es necesario
				int intentosActuales = u.getIntentosFallidos() + 1; // Nuevos intentos fallidos tras este fallo
				boolean seraBoqueado = intentosActuales >= maxIntentos;
				
				// Actualizar la BD con el nuevo intento fallido
				usuarioDAO.updateLoginFailure(u.getId(), maxIntentos); // Si falla sumamos un intento (límite 3).
				logger.info("Intento fallido para usuario: {}. Intentos actuales: {}, máximo permitido: {}", 
				           u.getUsername(), intentosActuales, maxIntentos);
				
				// Calcular mensaje y enviar correo si corresponde
				String advertencia = "Usuario o contraseña incorrectos.";
				if (!seraBoqueado) {
					int intentosRestantes = maxIntentos - intentosActuales;
					advertencia += " Tu cuenta será bloqueada en " + intentosRestantes + (intentosRestantes == 1 ? " intento erróneo." : " intentos erróneos.");
					advertencia += " Si no recuerdas tu contraseña, te sugiero que la cambies para evitar el bloqueo de tu cuenta.";
				} else {
					advertencia = "Cuenta bloqueada por múltiples intentos fallidos.";
					// Enviar correo de alerta por bloqueo usando EmailService
					logger.info("Enviando correo de alerta de bloqueo para usuario: {}", u.getUsername());
					try {
						Properties props3 = new Properties();
						try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
							if (in != null) {
								props3.load(in);
								logger.debug("Configuración de correo cargada correctamente");
							} else {
								logger.error("No se pudo cargar el archivo config.properties");
							}
						}
						String mailUser3 = props3.getProperty("mail.smtp.user");
						String mailPass3 = props3.getProperty("mail.smtp.pass");
						
						if (mailUser3 == null || mailPass3 == null) {
							logger.error("Credenciales de correo no configuradas. mailUser: {}, mailPass: {}", 
							           mailUser3, mailPass3 != null ? "[CONFIGURADA]" : "[NO CONFIGURADA]");
							return;
						}
						
						logger.debug("Creando EmailService con usuario: {}", mailUser3);
						EmailService emailService3 = new EmailService(mailUser3, mailPass3);
						
						// Usar el método específico del EmailService para cuenta bloqueada
						String ipBloqueo = ClienteUtil.getClienteIP(req);
						logger.debug("IP de bloqueo detectada: {}", ipBloqueo);
						logger.debug("Enviando correo a: {} ({} {})", u.getEmail(), u.getNombres(), u.getApellidos());
						
						emailService3.sendAccountLockedEmail(u.getEmail(), u.getNombres(), 
														   u.getApellidos(), ipBloqueo);
						logger.info("Correo de alerta de bloqueo enviado exitosamente a: {}", u.getEmail());
					} catch (jakarta.mail.MessagingException me) {
						logger.error("Error de mensajería al enviar correo de bloqueo para usuario: {} - Detalles: {}", 
								   u.getUsername(), me.getMessage(), me);
					} catch (Exception e) {
						logger.error("Error general al enviar correo de alerta de bloqueo de cuenta para usuario: " + u.getUsername(), e);
					}
				}
				req.setAttribute("error", advertencia);
				req.setAttribute("username", username);
				req.setAttribute("showCaptcha", true);
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}
		} catch (SQLException e) {
			logger.error("Error en login", e);
			throw new ServletException("Error en login", e);
		}
	}

	private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
