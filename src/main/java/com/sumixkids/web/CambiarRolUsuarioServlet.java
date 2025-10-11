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
            String icono = "";
            String color = "";
            switch (nuevoRol) {
                case 1: nuevoRolNombre = "Administrador"; icono = "👑"; color = "#9C27B0"; break;
                case 2: nuevoRolNombre = "Docente"; icono = "👨‍🏫"; color = "#2196F3"; break;
                case 3: nuevoRolNombre = "Estudiante"; icono = "🎓"; color = "#4CAF50"; break;
                case 4: nuevoRolNombre = "Padre/Madre"; icono = "👨‍👩‍👧‍👦"; color = "#FF9800"; break;
                default: nuevoRolNombre = "Desconocido"; icono = "❓"; color = "#757575";
            }
            
            String asunto = "🔄 Actualización de rol en SumixKids";
            String fechaHora = EmailService.getCurrentFormattedDateTime();
            Usuario admin = (Usuario) session.getAttribute("usuario");
            
            String mensaje = EmailService.getEmailHeader() +
                "<h2 style='color: " + color + "; margin-bottom: 20px;'>¡Hola " + usuario.getNombres() + " " + usuario.getApellidos() + "! 👋</h2>" +
                "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
                "<div style='background-color: #E8F5E8; padding: 20px; border-radius: 8px; border: 2px solid #4CAF50; margin-bottom: 20px; text-align: center;'>" +
                "<h3 style='color: #2E7D32; margin: 0 0 15px 0;'>🔄 ACTUALIZACIÓN DE ROL</h3>" +
                "<p style='font-size: 16px; line-height: 1.6; color: #2E7D32; margin: 0; font-weight: bold;'>Tu rol ha sido actualizado en SumixKids</p>" +
                "</div>" +
                "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>Te informamos que un administrador ha actualizado tu rol en la plataforma <strong>SumixKids</strong>.</p>" +
                "<div style='background-color: #FFF3E0; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
                "<p style='margin: 0 0 15px 0; color: #E65100; font-weight: bold;'>📊 Detalles del cambio:</p>" +
                "<ul style='margin: 0; color: #E65100; list-style: none; padding: 0;'>" +
                "<li style='margin-bottom: 8px;'><strong>🎯 Nuevo rol asignado:</strong> " + icono + " " + nuevoRolNombre + "</li>" +
                "<li style='margin-bottom: 8px;'><strong>📅 Fecha y hora:</strong> " + fechaHora + "</li>" +
                "<li style='margin-bottom: 8px;'><strong>👤 Actualizado por:</strong> " + admin.getNombres() + " " + admin.getApellidos() + "</li>" +
                "</ul>" +
                "</div>" +
                "<div style='background-color: #E3F2FD; padding: 15px; border-radius: 8px; border-left: 4px solid #2196F3; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #0D47A1; font-weight: bold;'>ℹ️ ¿Qué significa esto?</p>" +
                "<p style='margin: 10px 0 0 0; color: #0D47A1; font-size: 14px;'>Con tu nuevo rol tendrás acceso a diferentes funcionalidades y permisos dentro de la plataforma. Si tienes dudas sobre tus nuevos permisos, no dudes en contactarnos.</p>" +
                "</div>" +
                "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si no reconoces este cambio o tienes alguna inquietud, por favor comunícate con el equipo de soporte.</p>" +
                "</div>" +
                EmailService.getEmailFooter() +
                EmailService.getEmailCloser();
                
            // Leer config de correo
            java.util.Properties props = new java.util.Properties();
            try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) props.load(in);
            }
            String mailUser = props.getProperty("mail.smtp.user");
            String mailPass = props.getProperty("mail.smtp.pass");
            EmailService emailService = new EmailService(mailUser, mailPass);
            try {
                emailService.sendHtmlEmail(usuario.getEmail(), asunto, mensaje);
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
