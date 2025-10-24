package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.service.EmailService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CambiarRolUsuarioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        // Solo admin puede cambiar roles
        Integer rolActual = ((com.sumixkids.model.Usuario) session.getAttribute("usuario")).getRolId();
        if (rolActual == null || rolActual != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo el administrador puede cambiar roles");
            return;
        }
        String userIdStr = req.getParameter("userId");
        String nuevoRolStr = req.getParameter("nuevoRol");
        if (userIdStr == null || nuevoRolStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parámetros");
            return;
        }
        int userId, nuevoRol;
        try {
            userId = Integer.parseInt(userIdStr);
            nuevoRol = Integer.parseInt(nuevoRolStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros inválidos");
            return;
        }
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.findById(userId);
            if (usuario == null) {
                resp.sendRedirect(req.getContextPath() + "/bienvenida?error=Usuario no encontrado");
                return;
            }
            usuarioDAO.cambiarRol(userId, nuevoRol);
            // Enviar correo de notificación
            String nuevoRolNombre = "";
            switch (nuevoRol) {
                case 1: nuevoRolNombre = "Administrador"; break;
                case 2: nuevoRolNombre = "Docente"; break;
                case 3: nuevoRolNombre = "Estudiante"; break;
                case 4: nuevoRolNombre = "Padre/Madre"; break;
                default: nuevoRolNombre = "Desconocido";
            }
            
            Usuario admin = (Usuario) session.getAttribute("usuario");
            
            // Leer config de correo
            java.util.Properties props = new java.util.Properties();
            try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) props.load(in);
            }
            String mailUser = props.getProperty("mail.smtp.user");
            String mailPass = props.getProperty("mail.smtp.pass");
            EmailService emailService = new EmailService(mailUser, mailPass);
            try {
                // Usar el método específico del EmailService para cambio de rol
                emailService.sendRoleChangeEmail(usuario.getEmail(), usuario.getNombres(), 
                                               usuario.getApellidos(), nuevoRolNombre, 
                                               admin.getUsername());
            } catch (Exception ex) {
                resp.sendRedirect(req.getContextPath() + "/bienvenida?error=Rol cambiado pero no se pudo enviar el correo de notificación");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/bienvenida?mensaje=Rol actualizado correctamente y notificado por correo");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/bienvenida?error=No se pudo cambiar el rol: " + e.getMessage());
        }
    }
}
