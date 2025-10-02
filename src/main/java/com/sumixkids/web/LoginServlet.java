package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.util.PasswordUtil;
import com.sumixkids.util.TwoFactorUtil;
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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
	        throws ServletException, IOException {
	    
	    String timeout = req.getParameter("timeout");
	    if ("true".equals(timeout)) {
	        req.setAttribute("mensaje", "Su sesión ha expirado por inactividad");
	    }
	    
	    req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Procesa el envío del formulario de login.
		req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("username"); // puede ser usuario o correo
		String password = req.getParameter("password");

		if (isBlank(username) || isBlank(password)) {
			req.setAttribute("error", "Usuario y contraseña son obligatorios");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
			return;
		}

		try {
			Usuario u = usuarioDAO.findByUsernameOrEmail(username); // Buscamos por nombre o correo.
			int maxIntentos = 3; // Número máximo de intentos permitidos
			if (u == null) {
				req.setAttribute("error", "Usuario o contraseña incorrectos");
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
									
				// Contraseña correcta: reiniciamos contador y preparamos 2FA.
				usuarioDAO.updateLoginSuccess(u.getId());
				HttpSession session = req.getSession(true);
				session.setAttribute("usuario", u);

				// Generar código 2FA, guardarlo en la base de datos y enviarlo por correo
				String code = TwoFactorUtil.generateCode();
				java.time.LocalDateTime expiresAt = java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Bogota")).plusMinutes(15).toLocalDateTime();
				try {
					TwoFactorCodeDAO.guardarCodigo(u.getId(), code, expiresAt);
					TwoFactorCodeDAO.eliminarCodigosExpiradosYUsados();
				} catch (SQLException e) {
					logger.error("Error al guardar el código 2FA", e);
					req.setAttribute("error", "Error al guardar el código 2FA. Intenta de nuevo más tarde.");
					req.getRequestDispatcher("/login.jsp").forward(req, resp);
					return;
				}

				session.setAttribute("2fa_passed", false);

				// Leer config de correo
				Properties props2 = new Properties();
				try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
					if (in != null) props2.load(in);
				}
				String mailUser2 = props2.getProperty("mail.smtp.user");
				String mailPass2 = props2.getProperty("mail.smtp.pass");
				EmailService emailService2 = new EmailService(mailUser2, mailPass2);
				try {
					// Construir mensaje con el código
					String fechaHora = EmailService.getCurrentFormattedDateTime();
					String ipAddress = req.getRemoteAddr();
					String forwarded = req.getHeader("X-Forwarded-For");
					if (forwarded != null && !forwarded.isEmpty()) {
						ipAddress = forwarded.split(",")[0].trim();
					}
					
					String mensaje = EmailService.getEmailHeader() +
						"<h2 style='color: #2196F3; margin-bottom: 20px;'>¡Hola " + u.getNombres() + " " + u.getApellidos() + "! 👋</h2>" +
						"<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
						"<p style='font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 15px;'>Hemos detectado un intento de inicio de sesión en tu cuenta de <strong>SumixKids</strong>. 🔐</p>" +
						"<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 20px;'>Fecha y hora: <strong>" + fechaHora + "</strong></p>" +
						"<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 20px;'>IP de acceso: <strong>" + ipAddress + "</strong></p>" +
						"<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 20px;'>Para continuar, por favor ingresa el siguiente código de verificación en la pantalla de autenticación de dos factores (2FA):</p>" +
						"<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; text-align: center; border: 2px solid #2196F3; margin: 20px 0;'>" +
						"<p style='margin: 0 0 10px 0; color: #0D47A1; font-weight: bold; font-size: 14px;'>Código de verificación 2FA:</p>" +
						"<p style='font-size: 32px; font-weight: bold; color: #2196F3; margin: 10px 0; letter-spacing: 3px; font-family: monospace;'>" + code + "</p>" +
						"<p style='margin: 10px 0 0 0; color: #0D47A1; font-size: 12px;'>⏰ Válido por 15 minutos - Solo se puede usar una vez</p>" +
						"</div>" +
						"<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
						"<p style='margin: 0; color: #C62828; font-weight: bold;'>⚠️ Importante:</p>" +
						"<p style='margin: 10px 0 0 0; color: #C62828; font-size: 14px;'>Si tú no solicitaste este acceso, te recomendamos cambiar tu contraseña inmediatamente o contactar al soporte.</p>" +
						"</div>" +
						"</div>" +
						EmailService.getEmailFooter() +
						EmailService.getEmailCloser();
					emailService2.sendHtmlEmail(u.getEmail(), "🔐 Código de verificación 2FA - SumixKids", mensaje);
				} catch (Exception ex) {
					logger.error("No se pudo enviar el código 2FA a {}", u.getEmail(), ex);
					req.setAttribute("error", "No se pudo enviar el código 2FA a tu correo. Intenta de nuevo más tarde.");
					req.getRequestDispatcher("/login.jsp").forward(req, resp);
					return;
				}
				resp.sendRedirect(req.getContextPath() + "/2fa");
			} else {
										usuarioDAO.updateLoginFailure(u.getId(), maxIntentos); // Si falla sumamos un intento (límite 3).
										// ...registro de acceso fallido eliminado...
				// Obtener intentos restantes
				int intentosRestantes = maxIntentos - (u.getIntentosFallidos() + 1);
				String advertencia = "Usuario o contraseña incorrectos.";
				   if (intentosRestantes > 0) {
					   advertencia += " Tu cuenta será bloqueada en " + intentosRestantes + (intentosRestantes == 1 ? " intento erróneo." : " intentos erróneos.");
					   advertencia += " Si no recuerdas tu contraseña, te sugiero que la cambies para evitar el bloqueo de tu cuenta.";
				   } else {
					   advertencia = "Cuenta bloqueada por múltiples intentos fallidos.";
					   // Enviar correo de alerta por bloqueo
					   try {
						   Properties props3 = new Properties();
						   try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
							   if (in != null) props3.load(in);
						   }
						   String mailUser3 = props3.getProperty("mail.smtp.user");
						   String mailPass3 = props3.getProperty("mail.smtp.pass");
						   EmailService emailService3 = new EmailService(mailUser3, mailPass3);
						   String asunto = "🚨 Alerta de seguridad: Cuenta bloqueada - SumixKids";
						   
						   String fechaHora = EmailService.getCurrentFormattedDateTime();
						   String ipAddress = req.getRemoteAddr();
						   String forwarded = req.getHeader("X-Forwarded-For");
						   if (forwarded != null && !forwarded.isEmpty()) {
							   ipAddress = forwarded.split(",")[0].trim();
						   }
						   
						   String mensaje = EmailService.getEmailHeader() +
							   "<h2 style='color: #F44336; margin-bottom: 20px;'>¡Hola " + u.getNombres() + " " + u.getApellidos() + "! 👋</h2>" +
							   "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
							   "<div style='background-color: #FFEBEE; padding: 20px; border-radius: 8px; border: 2px solid #F44336; margin-bottom: 20px;'>" +
							   "<h3 style='color: #C62828; margin: 0 0 15px 0; text-align: center;'>🚨 ALERTA DE SEGURIDAD</h3>" +
							   "<p style='font-size: 16px; line-height: 1.6; color: #C62828; margin-bottom: 15px; text-align: center; font-weight: bold;'>Tu cuenta ha sido bloqueada automáticamente</p>" +
							   "</div>" +
							   "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 15px;'>Tu cuenta de <strong>SumixKids</strong> ha sido bloqueada por superar el número máximo de intentos fallidos de inicio de sesión permitidos.</p>" +
							   "<div style='background-color: #FFF3E0; padding: 15px; border-radius: 8px; margin: 20px 0;'>" +
							   "<p style='margin: 0 0 10px 0; color: #E65100; font-weight: bold;'>📊 Detalles del bloqueo:</p>" +
							   "<ul style='margin: 0; color: #E65100;'>" +
							   "<li><strong>Fecha y hora:</strong> " + fechaHora + "</li>" +
							   "<li><strong>IP de último intento:</strong> " + ipAddress + "</li>" +
							   "<li><strong>Motivo:</strong> Múltiples intentos fallidos consecutivos</li>" +
							   "</ul>" +
							   "</div>" +
							   "<div style='background-color: #E8F5E8; padding: 15px; border-radius: 8px; border-left: 4px solid #4CAF50; margin: 20px 0;'>" +
							   "<p style='margin: 0; color: #2E7D32; font-weight: bold;'>💡 ¿Qué puedes hacer?</p>" +
							   "<ul style='margin: 10px 0 0 0; color: #2E7D32;'>" +
							   "<li>Utiliza la opción de 'Recuperar contraseña' en la página de login</li>" +
							   "<li>Contacta con un docente encargado o administrador</li>" +
							   "<li>Espera un tiempo antes de intentar nuevamente</li>" +
							   "</ul>" +
							   "</div>" +
							   "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si no reconoces estos intentos, te recomendamos restablecer tu contraseña inmediatamente.</p>" +
							   "</div>" +
							   EmailService.getErrorEmailFooter() +
							   EmailService.getEmailCloser();
						   
						   emailService3.sendHtmlEmail(u.getEmail(), asunto, mensaje);
					   } catch (Exception e) {
						   logger.warn("No se pudo enviar correo de alerta de bloqueo de cuenta", e);
					   }
				   }
				req.setAttribute("error", advertencia);
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}
		} catch (SQLException e) {
			logger.error("Error en login", e);
			throw new ServletException("Error en login", e);
		}
	}

	private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
