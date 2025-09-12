package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.util.PasswordUtil;

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
				// Contraseña correcta: reiniciamos contador y guardamos usuario en sesión.
				usuarioDAO.updateLoginSuccess(u.getId());
				HttpSession session = req.getSession(true);
				session.setAttribute("usuario", u);
				resp.sendRedirect(req.getContextPath() + "/bienvenida");
			} else {
				usuarioDAO.updateLoginFailure(u.getId(), 5); // Si falla sumamos un intento (límite 5).
				req.setAttribute("error", "Usuario o contraseña incorrectos");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}
		} catch (SQLException e) {
			throw new ServletException("Error en login", e);
		}
	}

	private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
