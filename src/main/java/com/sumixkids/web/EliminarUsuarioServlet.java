package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.service.EmailService;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Servlet para eliminar usuarios de forma segura.
 * Recibe parámetro userId por POST.
 */
public class EliminarUsuarioServlet extends HttpServlet {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Mostrar página de confirmación
        String userIdStr = req.getParameter("userId");
        if (userIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro userId");
            return;
        }
        
        // Verificar que sea administrador
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario admin = (Usuario) session.getAttribute("usuario");
        if (admin.getRolId() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        
        String logIdStr = req.getParameter("logId");
        if (logIdStr != null) {
            try {
                Long logId = Long.parseLong(logIdStr);
                req.setAttribute("logId", logId);
            } catch (NumberFormatException e) {
                // Log error but continue
                System.err.println("ID de log inválido: " + logIdStr);
            }
        }
        
        req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Usuario admin = (Usuario) session.getAttribute("usuario");
        String userIdStr = req.getParameter("userId");
        String adminPassword = req.getParameter("adminPassword");

        if (userIdStr == null || adminPassword == null || adminPassword.trim().isEmpty()) {
            req.setAttribute("error", "Parámetros inválidos");
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            
            // Verificar contraseña del admin
            Usuario adminDB = usuarioDAO.findById(admin.getId());
            if (!BCrypt.checkpw(adminPassword, adminDB.getPasswordHash())) {
                System.out.println("Debug: Contraseña incorrecta"); // Debug log
                registrarAuditoria(admin, req.getRemoteAddr(), "ELIMINACION_FALLIDA", 
                                 "usuarios", "Intento fallido", 
                                 "Contraseña incorrecta", "FALLIDO", userId);
                req.setAttribute("error", "Contraseña incorrecta");
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                return;
            }

            // Obtener información del usuario antes de eliminarlo para enviar el correo
            Usuario usuarioAEliminar = usuarioDAO.findById(userId);
            if (usuarioAEliminar == null) {
                req.setAttribute("error", "Usuario no encontrado");
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                return;
            }

            boolean eliminado = false;
            try {
                eliminado = usuarioDAO.deleteUser(userId);
            } catch (SQLException sqlEx) {
                // Capturar error de foreign key constraint
                String errorMsg = sqlEx.getMessage();
                if (errorMsg != null && (errorMsg.contains("foreign key constraint") || 
                                        errorMsg.contains("FOREIGN KEY") ||
                                        errorMsg.contains("Cannot delete or update a parent row"))) {
                    System.out.println("Debug: Error de foreign key constraint"); // Debug log
                    registrarAuditoria(admin, req.getRemoteAddr(), "ELIMINACION_FALLIDA", 
                                     "usuarios", "Eliminación bloqueada", 
                                     "El usuario tiene registros asociados en el sistema", "FALLIDO", userId);
                    req.setAttribute("error", "❌ No se puede eliminar este usuario porque tiene registros asociados en el sistema (auditoría, actividades, etc.). Por seguridad, estos registros deben preservarse.");
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                } else {
                    // Otro tipo de error SQL
                    throw sqlEx;
                }
            }
            
            if (eliminado) {
                System.out.println("Debug: Usuario eliminado correctamente"); // Debug log
                
                // Enviar correo de notificación al usuario eliminado
                try {
                    java.util.Properties props = new java.util.Properties();
                    try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                        if (in != null) props.load(in);
                    }
                    String mailUser = props.getProperty("mail.smtp.user");
                    String mailPass = props.getProperty("mail.smtp.pass");
                    EmailService emailService = new EmailService(mailUser, mailPass);
                    
                    String asunto = "🚨 Notificación: Cuenta eliminada - SumixKids";
                    String fechaHora = EmailService.getCurrentFormattedDateTime();
                    
                    String mensaje = EmailService.getEmailHeader() +
                        "<h2 style='color: #F44336; margin-bottom: 20px;'>¡Hola " + usuarioAEliminar.getNombres() + " " + usuarioAEliminar.getApellidos() + "! 👋</h2>" +
                        "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
                        "<div style='background-color: #FFEBEE; padding: 20px; border-radius: 8px; border: 2px solid #F44336; margin-bottom: 20px; text-align: center;'>" +
                        "<h3 style='color: #C62828; margin: 0 0 15px 0;'>🚨 NOTIFICACIÓN IMPORTANTE</h3>" +
                        "<p style='font-size: 16px; line-height: 1.6; color: #C62828; margin: 0; font-weight: bold;'>Tu cuenta ha sido eliminada</p>" +
                        "</div>" +
                        "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>Te informamos que un administrador ha eliminado tu cuenta de la plataforma <strong>SumixKids</strong>.</p>" +
                        "<div style='background-color: #FFF3E0; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
                        "<p style='margin: 0 0 15px 0; color: #E65100; font-weight: bold;'>📊 Detalles de la eliminación:</p>" +
                        "<ul style='margin: 0; color: #E65100; list-style: none; padding: 0;'>" +
                        "<li style='margin-bottom: 8px;'><strong>👤 Usuario eliminado:</strong> " + usuarioAEliminar.getUsername() + "</li>" +
                        "<li style='margin-bottom: 8px;'><strong>📅 Fecha y hora:</strong> " + fechaHora + "</li>" +
                        "<li style='margin-bottom: 8px;'><strong>🛡️ Eliminado por:</strong> " + admin.getNombres() + " " + admin.getApellidos() + " (Administrador)</li>" +
                        "</ul>" +
                        "</div>" +
                        "<div style='background-color: #E8F5E8; padding: 15px; border-radius: 8px; border-left: 4px solid #4CAF50; margin: 20px 0;'>" +
                        "<p style='margin: 0; color: #2E7D32; font-weight: bold;'>💡 ¿Qué significa esto?</p>" +
                        "<p style='margin: 10px 0 0 0; color: #2E7D32; font-size: 14px;'>Tu cuenta y todos los datos asociados han sido eliminados permanentemente de nuestro sistema. Ya no podrás acceder a la plataforma con estas credenciales.</p>" +
                        "</div>" +
                        "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si consideras que esta eliminación fue realizada por error o tienes alguna consulta, por favor contacta con nuestro equipo de soporte.</p>" +
                        "</div>" +
                        EmailService.getErrorEmailFooter() +
                        EmailService.getEmailCloser();
                    
                    emailService.sendHtmlEmail(usuarioAEliminar.getEmail(), asunto, mensaje);
                } catch (Exception emailEx) {
                    System.err.println("No se pudo enviar correo de notificación de eliminación: " + emailEx.getMessage());
                    // No interrumpir el proceso si falla el correo
                }
                
                registrarAuditoria(admin, req.getRemoteAddr(), "ELIMINAR_USUARIO", 
                                 "usuarios", "Usuario eliminado", 
                                 "Usuario eliminado correctamente", "EXITOSO", userId);
                resp.sendRedirect(req.getContextPath() + "/bienvenida?mensaje=Usuario eliminado correctamente");
            } else {
                System.out.println("Debug: No se pudo eliminar el usuario"); // Debug log
                registrarAuditoria(admin, req.getRemoteAddr(), "ELIMINACION_FALLIDA", 
                                 "usuarios", "Eliminación fallida", 
                                 "No se pudo eliminar el usuario", "FALLIDO", userId);
                req.setAttribute("error", "No se pudo eliminar el usuario");
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
            }
        } catch (SQLException sqlEx) {
            System.err.println("Debug: Error SQL en eliminación: " + sqlEx.getMessage());
            sqlEx.printStackTrace();
            
            // Mensaje amigable para errores SQL
            String mensajeError = "❌ Error al eliminar el usuario. ";
            if (sqlEx.getMessage().contains("foreign key constraint") || 
                sqlEx.getMessage().contains("FOREIGN KEY")) {
                mensajeError += "El usuario tiene registros asociados que deben preservarse.";
            } else {
                mensajeError += "Por favor, contacte al administrador del sistema.";
            }
            
            registrarAuditoria(admin, req.getRemoteAddr(), "ERROR_ELIMINACION", 
                              "usuarios", "Error SQL", 
                              mensajeError, "ERROR", Integer.parseInt(userIdStr));
            req.setAttribute("error", mensajeError);
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Debug: Error inesperado en eliminación: " + e.getMessage());
            e.printStackTrace();
            
            registrarAuditoria(admin, req.getRemoteAddr(), "ERROR_ELIMINACION", 
                              "usuarios", "Error general", 
                              "Error inesperado", "ERROR", Integer.parseInt(userIdStr));
            req.setAttribute("error", "❌ Ocurrió un error inesperado. Por favor, intente nuevamente o contacte al administrador.");
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
        }
    }

    private void registrarAuditoria(Usuario admin, String ip, String accion, 
                                  String tabla, String valorAnterior, 
                                  String descripcion, String estado, int userId) {
        try {
            LogAuditoria log = new LogAuditoria();
            log.setFechaHora(LocalDateTime.now());
            log.setIdUsuario(admin.getId());
            log.setNombreUsuario(admin.getUsername());
            log.setIpUsuario(ip);
            log.setAccion(accion);
            log.setTablaAfectada(tabla);
            log.setValorAnterior("ID: " + userId + " - " + valorAnterior);
            log.setDescripcion(descripcion);
            log.setEstado(estado);
            log.setAprobadoPorAdminId(admin.getId());

            LogAuditoriaDAO logDAO = new LogAuditoriaDAO();
            logDAO.registrarLog(log);
            
            System.out.println("Debug: Log de auditoría registrado exitosamente"); // Debug log
            System.out.println("Debug: Detalles del log:");
            System.out.println("- Acción: " + log.getAccion());
            System.out.println("- Usuario: " + log.getNombreUsuario());
            System.out.println("- Estado: " + log.getEstado());
        } catch (SQLException e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
}
