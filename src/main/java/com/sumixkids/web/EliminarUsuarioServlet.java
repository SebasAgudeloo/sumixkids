package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.service.EmailService;
import com.sumixkids.config.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Servlet para eliminar usuarios de forma segura.
 * Maneja tanto eliminación por administrador como auto-eliminación por usuario.
 */
public class EliminarUsuarioServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LogAuditoriaDAO logAuditoriaDAO = new LogAuditoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Verificar sesión
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String tipoEliminacion = req.getParameter("tipo");
        String userIdStr = req.getParameter("userId");
        
        System.out.println("[EliminarUsuarioServlet] Usuario: " + usuario.getNombres() + ", Rol: " + usuario.getRolId());
        System.out.println("[EliminarUsuarioServlet] Tipo: " + tipoEliminacion + ", UserId: " + userIdStr);
        
        // NUEVA LÓGICA DE SEGURIDAD MEJORADA
        boolean esAutoEliminacion = false;
        
        if ("usuario".equals(tipoEliminacion)) {
            // ===== CASO 1: AUTO-ELIMINACIÓN EXPLÍCITA =====
            if (usuario.getRolId() == 1) {
                System.out.println("[EliminarUsuarioServlet] DENEGADO: Admin no puede auto-eliminarse");
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Los administradores no pueden eliminar su propia cuenta por seguridad");
                return;
            }
            // Usuario no-admin quiere eliminar su propia cuenta
            userIdStr = String.valueOf(usuario.getId());
            esAutoEliminacion = true;
            System.out.println("[EliminarUsuarioServlet] PERMITIDO: Auto-eliminación de usuario no-admin ID: " + userIdStr);
            
        } else if (userIdStr != null && !userIdStr.isEmpty()) {
            // ===== CASO 2: ELIMINACIÓN DE OTRO USUARIO =====
            int targetUserId = Integer.parseInt(userIdStr);
            
            if (targetUserId == usuario.getId()) {
                // Intentando eliminar su propia cuenta sin usar tipo=usuario
                if (usuario.getRolId() == 1) {
                    System.out.println("[EliminarUsuarioServlet] DENEGADO: Admin no puede auto-eliminarse");
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Los administradores no pueden eliminar su propia cuenta por seguridad");
                    return;
                }
                // Usuario no-admin eliminando su propia cuenta
                esAutoEliminacion = true;
                System.out.println("[EliminarUsuarioServlet] PERMITIDO: Auto-eliminación indirecta de usuario ID: " + userIdStr);
            } else {
                // Intentando eliminar a OTRO usuario
                if (usuario.getRolId() != 1) {
                    System.out.println("[EliminarUsuarioServlet] DENEGADO: Usuario no-admin intenta eliminar otro usuario");
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los administradores pueden eliminar otros usuarios");
                    return;
                }
                esAutoEliminacion = false;
                System.out.println("[EliminarUsuarioServlet] PERMITIDO: Admin eliminando otro usuario ID: " + userIdStr);
            }
            
        } else {
            // ===== CASO 3: SIN PARÁMETROS - ASUMIR AUTO-ELIMINACIÓN =====
            if (usuario.getRolId() == 1) {
                System.out.println("[EliminarUsuarioServlet] DENEGADO: Admin sin parámetros - requiere userId");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Los administradores deben especificar el ID del usuario a eliminar");
                return;
            }
            // Usuario no-admin sin parámetros - asumir auto-eliminación
            userIdStr = String.valueOf(usuario.getId());
            esAutoEliminacion = true;
            System.out.println("[EliminarUsuarioServlet] PERMITIDO: Auto-eliminación por defecto de usuario ID: " + userIdStr);
        }
        
        req.setAttribute("esAutoEliminacion", esAutoEliminacion);
        
        String logIdStr = req.getParameter("logId");
        if (logIdStr != null) {
            try {
                Long logId = Long.parseLong(logIdStr);
                req.setAttribute("logId", logId);
            } catch (NumberFormatException e) {
                System.err.println("ID de log inválido: " + logIdStr);
            }
        }
        
        // Cargar información del usuario para mostrar en la vista de confirmación
        try {
            int userId = Integer.parseInt(userIdStr);
            Usuario usuarioAEliminar = usuarioDAO.findById(userId);
            if (usuarioAEliminar != null) {
                req.setAttribute("usuario", usuarioAEliminar);
            }
        } catch (NumberFormatException e) {
            System.err.println("ID de usuario inválido: " + userIdStr);
        } catch (SQLException e) {
            System.err.println("Error al cargar usuario: " + e.getMessage());
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

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String userIdStr = req.getParameter("userId");
        String password = req.getParameter("adminPassword");
        String esAutoEliminacionStr = req.getParameter("esAutoEliminacion");
        boolean esAutoEliminacion = "true".equals(esAutoEliminacionStr);

        if (userIdStr == null || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Parámetros inválidos");
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            
            // Verificar contraseña del usuario que ejecuta la acción
            Usuario usuarioVerificar = usuarioDAO.findById(usuario.getId());
            if (usuarioVerificar == null || !BCrypt.checkpw(password, usuarioVerificar.getPasswordHash())) {
                req.setAttribute("error", "❌ Contraseña incorrecta");
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                return;
            }
            
            // Verificar permisos
            if (esAutoEliminacion) {
                // Auto-eliminación: usuario puede eliminar solo su propia cuenta
                if (userId != usuario.getId()) {
                    req.setAttribute("error", "❌ No puedes eliminar una cuenta diferente a la tuya");
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                }
                if (usuario.getRolId() == 1) {
                    req.setAttribute("error", "❌ Los administradores no pueden eliminar su propia cuenta");
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                }
            } else {
                // Eliminación por admin: solo administradores pueden eliminar otras cuentas
                if (usuario.getRolId() != 1) {
                    req.setAttribute("error", "❌ Acceso denegado");
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                }
            }
            
            // Obtener información del usuario antes de eliminarlo
            Usuario usuarioAEliminar = usuarioDAO.findById(userId);
            if (usuarioAEliminar == null) {
                req.setAttribute("error", "Usuario no encontrado");
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                return;
            }

            // Verificar dependencias antes de intentar eliminar
            if (tieneRegistrosAsociados(userId)) {
                req.setAttribute("error", "❌ El usuario aún tiene registros asociados. Use la opción 'Ver Registros Asociados' para eliminarlos primero.");
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
                    registrarAuditoria(usuario, req.getRemoteAddr(), "ELIMINACION_FALLIDA", 
                                     "usuarios", "Eliminación bloqueada", 
                                     "El usuario tiene registros asociados en el sistema", "FALLIDO", userId);
                    req.setAttribute("error", "❌ No se puede eliminar este usuario porque tiene registros asociados en el sistema (auditoría, actividades, etc.). Por seguridad, estos registros deben preservarse.");
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                } else {
                    throw sqlEx;
                }
            }
            
            if (eliminado) {
                // Enviar correo de notificación si no es auto-eliminación
                if (!esAutoEliminacion) {
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
                            "<li style='margin-bottom: 8px;'><strong>🛡️ Eliminado por:</strong> " + usuario.getNombres() + " " + usuario.getApellidos() + " (Administrador)</li>" +
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
                    }
                }
                
                String accionAuditoria = esAutoEliminacion ? "AUTO_ELIMINACION" : "ELIMINAR_USUARIO";
                String descripcionAuditoria = esAutoEliminacion ? "Usuario eliminó su propia cuenta" : "Usuario eliminado por administrador";
                registrarAuditoria(usuario, req.getRemoteAddr(), accionAuditoria, 
                                 "usuarios", descripcionAuditoria, 
                                 "Usuario eliminado correctamente", "EXITOSO", userId);
                
                // Si es auto-eliminación, cerrar sesión
                if (esAutoEliminacion) {
                    session.invalidate();
                    resp.sendRedirect(req.getContextPath() + "/login?mensaje=Tu cuenta ha sido eliminada correctamente");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/bienvenida?mensaje=Usuario eliminado correctamente");
                }
            } else {
                String accionAuditoria = esAutoEliminacion ? "AUTO_ELIMINACION_FALLIDA" : "ELIMINACION_FALLIDA";
                registrarAuditoria(usuario, req.getRemoteAddr(), accionAuditoria, 
                                 "usuarios", "Eliminación fallida", 
                                 "No se pudo eliminar el usuario", "FALLIDO", userId);
                req.setAttribute("error", "No se pudo eliminar el usuario");
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
            }
        } catch (SQLException sqlEx) {
            System.err.println("Error SQL en eliminación: " + sqlEx.getMessage());
            
            String mensajeError = "❌ Error al eliminar el usuario. ";
            if (sqlEx.getMessage().contains("foreign key constraint") || 
                sqlEx.getMessage().contains("FOREIGN KEY")) {
                mensajeError += "El usuario tiene registros asociados que deben preservarse.";
            } else {
                mensajeError += "Por favor, contacte al administrador del sistema.";
            }
            
            String accionAuditoria = esAutoEliminacion ? "ERROR_AUTO_ELIMINACION" : "ERROR_ELIMINACION";
            registrarAuditoria(usuario, req.getRemoteAddr(), accionAuditoria, 
                              "usuarios", "Error SQL", 
                              mensajeError, "ERROR", Integer.parseInt(userIdStr));
            req.setAttribute("error", mensajeError);
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Error inesperado en eliminación: " + e.getMessage());
            
            String accionAuditoria = esAutoEliminacion ? "ERROR_AUTO_ELIMINACION" : "ERROR_ELIMINACION";
            registrarAuditoria(usuario, req.getRemoteAddr(), accionAuditoria, 
                              "usuarios", "Error general", 
                              "Error inesperado", "ERROR", Integer.parseInt(userIdStr));
            req.setAttribute("error", "❌ Ocurrió un error inesperado. Por favor, intente nuevamente o contacte al administrador.");
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
        }
    }

    private void registrarAuditoria(Usuario usuario, String ip, String accion, 
                                  String tabla, String valorAnterior, 
                                  String descripcion, String estado, int userId) {
        try {
            LogAuditoria log = new LogAuditoria();
            log.setFechaHora(LocalDateTime.now());
            log.setIdUsuario(usuario.getId());
            log.setNombreUsuario(usuario.getUsername());
            log.setIpUsuario(ip);
            log.setAccion(accion);
            log.setTablaAfectada(tabla);
            log.setValorAnterior(valorAnterior);
            log.setValorNuevo(String.valueOf(userId));
            log.setDescripcion(descripcion);
            log.setEstado(estado);
            
            logAuditoriaDAO.registrarLog(log);
        } catch (Exception e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
        }
    }

    private boolean tieneRegistrosAsociados(int userId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            return verificarOtrasTablas(conn, userId);
        } catch (SQLException e) {
            System.err.println("Error al verificar registros asociados: " + e.getMessage());
        }
        return false;
    }

    private boolean verificarOtrasTablas(Connection conn, int userId) throws SQLException {
        String[] tablasVerificar = {
            "log_auditoria WHERE id_usuario = ?",
        };
        
        for (String consulta : tablasVerificar) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM " + consulta)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}