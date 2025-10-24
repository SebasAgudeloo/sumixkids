package com.sumixkids.web;

import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.model.Usuario;
import com.sumixkids.service.EmailService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet para cambiar el estado (bloquear/desbloquear) de un usuario
 */
public class CambiarEstadoUsuarioServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(CambiarEstadoUsuarioServlet.class.getName());
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LogAuditoriaDAO logDAO = new LogAuditoriaDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        if (usuarioSesion.getRolId() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        
        String idStr = req.getParameter("id");
        String bloquearStr = req.getParameter("bloquear");
        
        if (idStr == null || idStr.trim().isEmpty() || bloquearStr == null) {
            session.setAttribute("mensajeError", "❌ Parámetros inválidos");
            resp.sendRedirect(req.getContextPath() + "/usuarios");
            return;
        }
        
        try {
            int userId = Integer.parseInt(idStr);
            boolean bloquear = Boolean.parseBoolean(bloquearStr);
            
            // No se puede cambiar el estado del propio usuario
            if (userId == usuarioSesion.getId()) {
                session.setAttribute("mensajeError", "❌ No puedes cambiar tu propio estado");
                resp.sendRedirect(req.getContextPath() + "/usuarios");
                return;
            }
            
            // Obtener usuario antes del cambio
            Usuario usuario = usuarioDAO.findById(userId);
            if (usuario == null) {
                session.setAttribute("mensajeError", "❌ Usuario no encontrado");
                resp.sendRedirect(req.getContextPath() + "/usuarios");
                return;
            }
            
            // Cambiar estado
            boolean actualizado = usuarioDAO.cambiarEstadoUsuario(userId, bloquear);
            
            if (actualizado) {
                // Registrar en auditoría
                String accion = bloquear ? "bloqueado" : "desbloqueado";
                LogAuditoria log = new LogAuditoria();
                log.setIdUsuario(usuarioSesion.getId());
                log.setNombreUsuario(usuarioSesion.getUsername());
                log.setAccion("ESTADO_USUARIO");
                log.setDescripcion(String.format(
                    "Usuario '%s' (ID: %d) %s por administrador '%s'",
                    usuario.getUsername(),
                    usuario.getId(),
                    accion,
                    usuarioSesion.getUsername()
                ));
                log.setEstado("EXITOSO");
                logDAO.registrarLog(log);
                
                // Enviar correo de notificación
                enviarCorreoNotificacion(usuario, usuarioSesion, bloquear);
                
                session.setAttribute("mensajeExito", 
                    String.format("✅ Usuario '%s' %s exitosamente", usuario.getUsername(), accion));
                
                logger.info(String.format(
                    "Usuario %s (ID: %d) %s por admin %s",
                    usuario.getUsername(), userId, accion, usuarioSesion.getUsername()
                ));
            } else {
                session.setAttribute("mensajeError", "❌ No se pudo cambiar el estado del usuario");
            }
            
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "ID inválido: " + idStr, e);
            session.setAttribute("mensajeError", "❌ ID de usuario inválido");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cambiar estado de usuario", e);
            session.setAttribute("mensajeError", "❌ Error al cambiar el estado: " + e.getMessage());
        }
        
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Redirigir a POST no está permitido
        resp.sendRedirect(req.getContextPath() + "/usuarios");
    }

    /**
     * Envía un correo de notificación al usuario cuando su cuenta es bloqueada o desbloqueada.
     */
    private void enviarCorreoNotificacion(Usuario usuario, Usuario admin, boolean bloquear) {
        try {
            // Leer config de correo
            java.util.Properties props = new java.util.Properties();
            try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) {
                    props.load(in);
                }
            }
            
            String mailUser = props.getProperty("mail.smtp.user");
            String mailPass = props.getProperty("mail.smtp.pass");
            
            if (mailUser == null || mailPass == null) {
                logger.warning("Configuración de correo no disponible");
                return;
            }
            
            EmailService emailService = new EmailService(mailUser, mailPass);
            
            if (bloquear) {
                // Usar método específico para cuenta deshabilitada
                emailService.sendStatusChangeEmail(usuario.getEmail(), usuario.getNombres(), 
                                                 usuario.getApellidos(), false, admin.getUsername());
            } else {
                // Usar método específico para cuenta habilitada/desbloqueada
                emailService.sendAccountUnblockedNotification(usuario.getEmail(), usuario.getNombres(), 
                                                            usuario.getApellidos(), usuario.getUsername(), 
                                                            admin.getUsername());
            }
            logger.info(String.format(
                "Correo de notificación enviado a %s (%s)",
                usuario.getUsername(), usuario.getEmail()
            ));
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo enviar el correo de notificación", e);
            // No fallar el proceso si el correo falla
        }
    }
}
