package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.service.EmailService;
import com.sumixkids.util.ValidacionUtil;
import com.sumixkids.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Servlet para crear usuarios desde el panel de administración.
 */
public class CrearUsuarioServlet extends HttpServlet {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Verificar que sea administrador
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario admin = (Usuario) session.getAttribute("usuario");
        if (admin.getRolId() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los administradores pueden crear usuarios");
            return;
        }
        
        // Redirigir al panel principal donde está el modal
        resp.sendRedirect(req.getContextPath() + "/bienvenida");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Verificar que sea administrador
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario admin = (Usuario) session.getAttribute("usuario");
        if (admin.getRolId() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los administradores pueden crear usuarios");
            return;
        }
        
        // Obtener parámetros del formulario
        String username = req.getParameter("username");
        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String rolIdStr = req.getParameter("rolId");
        String grado = req.getParameter("grado");
        
        List<String> errores = new ArrayList<>();

        // Validaciones
        if (!ValidacionUtil.esNombreValido(nombres)) {
            errores.add(ValidacionUtil.getMensajeError("nombres", nombres));
        }
        
        if (!ValidacionUtil.esApellidoValido(apellidos)) {
            errores.add(ValidacionUtil.getMensajeError("apellidos", apellidos));
        }
        
        if (!ValidacionUtil.esUsernameValido(username)) {
            errores.add(ValidacionUtil.getMensajeError("username", username));
        }
        
        if (!ValidacionUtil.esCorreoValido(email)) {
            errores.add(ValidacionUtil.getMensajeError("email", email));
        }
        
        if (!ValidacionUtil.esPasswordValida(password)) {
            errores.add(ValidacionUtil.getMensajePasswordDetallado(password));
        }
        
        int rolId;
        try {
            rolId = Integer.parseInt(rolIdStr);
            if (rolId < 1 || rolId > 4) {
                errores.add("Rol inválido");
            }
        } catch (NumberFormatException e) {
            errores.add("Rol inválido");
            rolId = 3; // Default a estudiante
        }
        
        // Validar grado solo si es estudiante
        if (rolId == 3 && grado != null && !grado.trim().isEmpty() && !ValidacionUtil.esGradoValido(grado)) {
            errores.add(ValidacionUtil.getMensajeError("grado", grado));
        }

        // Si hay errores, volver al formulario
        if (!errores.isEmpty()) {
            StringBuilder errorStr = new StringBuilder();
            for (String error : errores) {
                errorStr.append(error).append("; ");
            }
            resp.sendRedirect(req.getContextPath() + "/bienvenida?error=" + errorStr.toString());
            return;
        }

        try {
            // Verificar si el usuario o email ya existe
            if (usuarioDAO.existsByUsernameOrEmail(username, email)) {
                resp.sendRedirect(req.getContextPath() + "/bienvenida?error=Usuario o correo ya existe");
                return;
            }

            // Crear el usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setNombres(nombres);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPasswordHash(PasswordUtil.hash(password));
            nuevoUsuario.setRolId(rolId);
            nuevoUsuario.setGrado((rolId == 3 && grado != null) ? grado.trim() : "");
            nuevoUsuario.setFechaRegistro(ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime());

            int id = usuarioDAO.createUser(nuevoUsuario);
            
            if (id > 0) {
                // Enviar correo de bienvenida
                enviarCorreoBienvenida(nuevoUsuario, admin, password);
                
                String mensaje = "Usuario creado exitosamente: " + username;
                resp.sendRedirect(req.getContextPath() + "/bienvenida?mensaje=" + mensaje);
            } else {
                resp.sendRedirect(req.getContextPath() + "/bienvenida?error=No se pudo crear el usuario");
            }
        } catch (SQLException e) {
            resp.sendRedirect(req.getContextPath() + "/bienvenida?error=Error al crear el usuario: " + e.getMessage());
        }
    }
    
    private void enviarCorreoBienvenida(Usuario usuario, Usuario admin, String password) {
        try {
            ResourceBundle config = ResourceBundle.getBundle("config");
            String mailUser = config.getString("mail.smtp.user");
            String mailPass = config.getString("mail.smtp.pass");
            EmailService emailService = new EmailService(mailUser, mailPass);
            
            String asunto = "🎉 ¡Bienvenido a SumixKids! - Cuenta creada por administrador";
            String fechaHora = EmailService.getCurrentFormattedDateTime();
            
            String rolNombre = "";
            String icono = "";
            String color = "";
            switch (usuario.getRolId()) {
                case 1: rolNombre = "Administrador"; icono = "👑"; color = "#9C27B0"; break;
                case 2: rolNombre = "Docente"; icono = "👨‍🏫"; color = "#2196F3"; break;
                case 3: rolNombre = "Estudiante"; icono = "🎓"; color = "#4CAF50"; break;
                case 4: rolNombre = "Padre/Madre"; icono = "👨‍👩‍👧‍👦"; color = "#FF9800"; break;
                default: rolNombre = "Usuario"; icono = "👤"; color = "#757575";
            }
            
            String mensaje = EmailService.getEmailHeader() +
                "<h2 style='color: " + color + "; margin-bottom: 20px;'>¡Hola " + usuario.getNombres() + " " + usuario.getApellidos() + "! 👋</h2>" +
                "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
                "<p style='font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 15px;'>¡Bienvenido a <strong>SumixKids</strong>! 🌟</p>" +
                "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Un administrador ha creado una cuenta para ti el <strong>" + fechaHora + "</strong>.</p>" +
                "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
                "<p style='margin: 0 0 15px 0; color: #0D47A1; font-weight: bold;'>📊 Detalles de tu cuenta:</p>" +
                "<ul style='margin: 0; color: #0D47A1; list-style: none; padding: 0;'>" +
                "<li style='margin-bottom: 8px;'><strong>👤 Usuario:</strong> " + usuario.getUsername() + "</li>" +
                "<li style='margin-bottom: 8px;'><strong>📧 Email:</strong> " + usuario.getEmail() + "</li>" +
                "<li style='margin-bottom: 8px;'><strong>🎯 Rol asignado:</strong> " + icono + " " + rolNombre + "</li>" +
                (usuario.getGrado() != null && !usuario.getGrado().isEmpty() ? 
                "<li style='margin-bottom: 8px;'><strong>📚 Grado:</strong> " + usuario.getGrado() + "</li>" : "") +
                "<li style='margin-bottom: 8px;'><strong>🛡️ Creado por:</strong> " + admin.getNombres() + " " + admin.getApellidos() + "</li>" +
                "</ul>" +
                "</div>" +
                "<div style='background-color: #FFEBEE; padding: 20px; border-radius: 8px; border: 2px solid #F44336; margin: 20px 0;'>" +
                "<p style='margin: 0 0 10px 0; color: #C62828; font-weight: bold;'>🔐 Credenciales de acceso:</p>" +
                "<p style='margin: 0; color: #C62828; font-size: 14px;'><strong>Contraseña temporal:</strong> " + password + "</p>" +
                "<p style='margin: 10px 0 0 0; color: #C62828; font-size: 12px;'>⚠️ Te recomendamos cambiar esta contraseña en tu primer inicio de sesión</p>" +
                "</div>" +
                "<div style='background-color: #E8F5E8; padding: 15px; border-radius: 8px; border-left: 4px solid #4CAF50; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #2E7D32; font-weight: bold;'>💡 Primeros pasos:</p>" +
                "<ul style='margin: 10px 0 0 0; color: #2E7D32;'>" +
                "<li>Inicia sesión con tus credenciales</li>" +
                "<li>Explora las funcionalidades de tu rol</li>" +
                "<li>Cambia tu contraseña por una personal</li>" +
                "<li>¡Comienza a disfrutar la plataforma!</li>" +
                "</ul>" +
                "</div>" +
                "</div>" +
                EmailService.getEmailFooter() +
                EmailService.getEmailCloser();
            
            emailService.sendHtmlEmail(usuario.getEmail(), asunto, mensaje);
        } catch (Exception e) {
            // No interrumpir el proceso si falla el correo
            e.printStackTrace();
        }
    }
}