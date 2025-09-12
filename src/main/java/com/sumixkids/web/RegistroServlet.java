package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Atiende el formulario de registro de nuevos usuarios.
 * GET: muestra el formulario.
 * POST: valida datos y crea el usuario.
 */
@WebServlet(name = "RegistroServlet", urlPatterns = {"/registro"})
public class RegistroServlet extends HttpServlet {

	private final UsuarioDAO usuarioDAO = new UsuarioDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Sólo mostrar el formulario de registro.
		req.getRequestDispatcher("/registro.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Procesar envío del formulario de registro.
		req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String role = req.getParameter("role");
		String password = req.getParameter("password");
		String confirm = req.getParameter("confirm");

		if (isBlank(username) || isBlank(email) || isBlank(password)) {
			req.setAttribute("error", "Todos los campos son obligatorios");
			req.getRequestDispatcher("/registro.jsp").forward(req, resp);
			return;
		}
		if (!password.equals(confirm)) {
			req.setAttribute("error", "Las contraseñas no coinciden");
			req.getRequestDispatcher("/registro.jsp").forward(req, resp);
			return;
		}

		try {
			// Revisamos el rol elegido (sólo aceptamos los definidos).
			String selectedRole = (role == null || role.isBlank()) ? "student" : role.trim();
			if (!("student".equals(selectedRole) || "parents".equals(selectedRole) || "docent".equals(selectedRole))) {
				req.setAttribute("error", "Rol inválido");
				req.getRequestDispatcher("/registro.jsp").forward(req, resp);
				return;
			}
			if (usuarioDAO.existsByUsernameOrEmail(username, email)) {
				req.setAttribute("error", "Usuario o correo ya existe");
				req.getRequestDispatcher("/registro.jsp").forward(req, resp);
				return;
			}
			Usuario u = new Usuario(); // Creamos el objeto y llenamos sus campos.
			u.setUsername(username);
			u.setEmail(email);
			u.setPasswordHash(PasswordUtil.hash(password));
			// Asignar rol según selección
			u.setRolId(usuarioDAO.resolveRolIdByName(selectedRole));
			u.setFechaRegistro(LocalDateTime.now());
			int id = usuarioDAO.createUser(u);
				if (id > 0) {
				req.setAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			} else {
				req.setAttribute("error", "No se pudo registrar el usuario");
				req.getRequestDispatcher("/registro.jsp").forward(req, resp);
			}
		} catch (SQLException e) {
			throw new ServletException("Error en registro", e);
		}
	}

	private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

