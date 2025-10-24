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
            
            // Usar el método específico para usuario creado por administrador
            emailService.sendAdminCreatedUserEmail(usuario.getEmail(), usuario.getNombres(), 
                                                 usuario.getApellidos(), usuario.getUsername(), 
                                                 password, usuario.getRolId(), usuario.getGrado(),
                                                 admin.getNombres(), admin.getApellidos());
        } catch (Exception e) {
            // No interrumpir el proceso si falla el correo
            e.printStackTrace();
        }
    }
}