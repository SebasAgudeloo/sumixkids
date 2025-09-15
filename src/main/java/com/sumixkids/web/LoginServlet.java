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
import javax.servlet.annotation.WebServlet;
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
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	private final UsuarioDAO usuarioDAO = new UsuarioDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Sólo mostramos la página de login.
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
			int maxIntentos = 3;
			if (u == null) {
				req.setAttribute("error", "Usuario o contraseña incorrectos");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
				return;
			}
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
					String mensaje = "Hola " + u.getUsername() + ",\n\n" +
						"Hemos detectado un intento de inicio de sesión en tu cuenta de SumixKids.\n" +
						"Para continuar, por favor ingresa el siguiente código de verificación en la pantalla de autenticación de dos factores (2FA):\n\n" +
						"Código de verificación: " + code + "\n\n" +
						"Este código es válido por 15 minutos y solo puede usarse una vez.\n" +
						"Si tú no solicitaste este acceso, te recomendamos cambiar tu contraseña inmediatamente o contactar al soporte.\n\n" +
						"Gracias por confiar en SumixKids.\n\n" +
						"Atentamente,\nEl equipo de SumixKids";
					emailService2.sendEmail(u.getEmail(), "Código de verificación 2FA", mensaje);
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
						   String asunto = "Alerta de seguridad: Cuenta bloqueada";
						   String mensaje = "Hola " + u.getNombres() + ",\n\n" +
							   "Tu cuenta ha sido bloqueada automáticamente por superar el número máximo de intentos fallidos de inicio de sesión permitidos.\n" +
							   "Si no reconoces estos intentos, te recomendamos restablecer tu contraseña y contactar al soporte.\n\n" +
							   "Fecha y hora del bloqueo: " + java.time.LocalDateTime.now() + "\n" +
							   "Saludos,\nEl equipo de SumixKids";
						   emailService3.sendEmail(u.getEmail(), asunto, mensaje);
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
