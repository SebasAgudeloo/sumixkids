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
        System.out.println("=================================================================");
        System.out.println("[ELIMINAR_USUARIO_SERVLET] doGet() INICIADO");
        System.out.println("[ELIMINAR_USUARIO_SERVLET] URL completa: " + req.getRequestURL() + "?" + req.getQueryString());
        System.out.println("=================================================================");
        
        // Verificar sesión
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            System.out.println("[ELIMINAR_USUARIO_SERVLET] Sesión inválida - redirigiendo a login");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String tipoEliminacion = req.getParameter("tipo");
        
        // Obtener el ID del usuario a eliminar - soportar tanto 'userId' como 'id'
        String userIdStr = req.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            userIdStr = req.getParameter("id");
        }
        
        System.out.println("[DEBUG] Usuario sesión: " + usuario.getNombres() + " (ID=" + usuario.getId() + "), Rol: " + usuario.getRolId());
        System.out.println("[DEBUG] Parámetros recibidos - tipo: '" + tipoEliminacion + "', userId: '" + req.getParameter("userId") + "', id: '" + req.getParameter("id") + "'");
        System.out.println("[DEBUG] userIdStr final: '" + userIdStr + "'");
        
        // LÓGICA SIMPLIFICADA Y CLARA
        boolean esAutoEliminacion = false;
        int targetUserId = 0;
        
        // Determinar si es auto-eliminación o eliminación de otro usuario
        if ("usuario".equals(tipoEliminacion)) {
            // AUTO-ELIMINACIÓN EXPLÍCITA (desde menú desplegable)
            if (usuario.getRolId() == 1) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Los administradores no pueden eliminar su propia cuenta por seguridad");
                return;
            }
            targetUserId = usuario.getId();
            esAutoEliminacion = true;
            System.out.println("[DEBUG] CASO: Auto-eliminación explícita - targetUserId: " + targetUserId);
            
        } else if (userIdStr != null && !userIdStr.trim().isEmpty()) {
            // ELIMINACIÓN CON ID ESPECÍFICO
            try {
                targetUserId = Integer.parseInt(userIdStr.trim());
                System.out.println("[DEBUG] CASO: Eliminación con ID específico - targetUserId: " + targetUserId);
                
                if (targetUserId == usuario.getId()) {
                    // Eliminando su propia cuenta (pero sin tipo=usuario)
                    if (usuario.getRolId() == 1) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Los administradores no pueden eliminar su propia cuenta por seguridad");
                        return;
                    }
                    esAutoEliminacion = true;
                    System.out.println("[DEBUG] Es su propia cuenta - auto-eliminación");
                } else {
                    // Eliminando otra cuenta
                    if (usuario.getRolId() != 1) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los administradores pueden eliminar otros usuarios");
                        return;
                    }
                    esAutoEliminacion = false;
                    System.out.println("[DEBUG] Es otra cuenta - eliminación de admin");
                }
            } catch (NumberFormatException e) {
                System.err.println("[ERROR] ID de usuario inválido: " + userIdStr);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de usuario inválido: " + userIdStr);
                return;
            }
            
        } else {
            // SIN PARÁMETROS
            if (usuario.getRolId() == 1) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Los administradores deben especificar el ID del usuario a eliminar");
                return;
            }
            targetUserId = usuario.getId();
            esAutoEliminacion = true;
            System.out.println("[DEBUG] CASO: Sin parámetros - auto-eliminación por defecto - targetUserId: " + targetUserId);
        }
        
        // Establecer atributos para la JSP
        req.setAttribute("esAutoEliminacion", esAutoEliminacion);
        req.setAttribute("userId", String.valueOf(targetUserId));
        
        System.out.println("[DEBUG] Estableciendo atributos - esAutoEliminacion: " + esAutoEliminacion + ", userId: " + targetUserId);
        
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
            System.out.println("[DEBUG] Cargando información del usuario con ID: " + targetUserId);
            Usuario usuarioAEliminar = usuarioDAO.findById(targetUserId);
            if (usuarioAEliminar != null) {
                req.setAttribute("usuario", usuarioAEliminar);
                System.out.println("[DEBUG] Usuario cargado: " + usuarioAEliminar.getNombres() + " (ID=" + usuarioAEliminar.getId() + ")");
            } else {
                System.err.println("[ERROR] No se encontró usuario con ID: " + targetUserId);
                req.setAttribute("error", "Usuario no encontrado con ID: " + targetUserId);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error al cargar usuario con ID " + targetUserId + ": " + e.getMessage());
            req.setAttribute("error", "Error al cargar información del usuario");
        }
        
        System.out.println("=================================================================");
        System.out.println("[ELIMINAR_USUARIO_SERVLET] Haciendo forward a eliminar_confirmar.jsp");
        System.out.println("[ELIMINAR_USUARIO_SERVLET] Atributos establecidos:");
        System.out.println("  - userId: " + req.getAttribute("userId"));
        System.out.println("  - esAutoEliminacion: " + req.getAttribute("esAutoEliminacion"));
        System.out.println("  - usuario: " + (req.getAttribute("usuario") != null ? ((Usuario)req.getAttribute("usuario")).getId() : "null"));
        System.out.println("=================================================================");
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
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            userIdStr = req.getParameter("id");
        }
        String password = req.getParameter("adminPassword");
        String esAutoEliminacionStr = req.getParameter("esAutoEliminacion");
        boolean esAutoEliminacion = "true".equals(esAutoEliminacionStr);
        
        System.out.println("[DEBUG POST] userIdStr: '" + userIdStr + "', esAutoEliminacion: " + esAutoEliminacion);

        if (userIdStr == null || userIdStr.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Parámetros inválidos");
            req.setAttribute("userId", userIdStr);
            req.setAttribute("esAutoEliminacion", esAutoEliminacion);
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr.trim());
            
            // Verificar contraseña del usuario que ejecuta la acción
            Usuario usuarioVerificar = usuarioDAO.findById(usuario.getId());
            if (usuarioVerificar == null || !BCrypt.checkpw(password, usuarioVerificar.getPasswordHash())) {
                // Registrar intento fallido de eliminación por contraseña incorrecta
                String accionFallida = esAutoEliminacion ? "Auto Eliminacion Fallida" : "Eliminacion Fallida";
                String descripcionError = esAutoEliminacion ? "Contraseña incorrecta para auto eliminacion" : "Contraseña de administrador incorrecta";
                registrarAuditoria(usuario, req.getRemoteAddr(), accionFallida, 
                                 "usuarios", "ID: " + userIdStr + " - Intento fallido", 
                                 descripcionError, "FALLIDO", Integer.parseInt(userIdStr));
                
                req.setAttribute("error", "Contraseña incorrecta");
                req.setAttribute("userId", userIdStr);
                req.setAttribute("esAutoEliminacion", esAutoEliminacion);
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                return;
            }
            
            // Verificar permisos
            if (esAutoEliminacion) {
                // Auto-eliminación: usuario puede eliminar solo su propia cuenta
                if (userId != usuario.getId()) {
                    req.setAttribute("error", "No puedes eliminar una cuenta diferente a la tuya");
                    req.setAttribute("userId", userIdStr);
                    req.setAttribute("esAutoEliminacion", esAutoEliminacion);
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                }
                if (usuario.getRolId() == 1) {
                    req.setAttribute("error", "Los administradores no pueden eliminar su propia cuenta");
                    req.setAttribute("userId", userIdStr);
                    req.setAttribute("esAutoEliminacion", esAutoEliminacion);
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                }
            } else {
                // Eliminación por admin: solo administradores pueden eliminar otras cuentas
                if (usuario.getRolId() != 1) {
                    req.setAttribute("error", "Acceso denegado");
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                }
            }
            
            // Obtener información del usuario antes de eliminarlo
            Usuario usuarioAEliminar = usuarioDAO.findById(userId);
            if (usuarioAEliminar == null) {
                req.setAttribute("error", "Usuario no encontrado");
                req.setAttribute("userId", userIdStr);
                req.setAttribute("esAutoEliminacion", esAutoEliminacion);
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                return;
            }

            // Limpiar registros temporales automáticamente antes de verificar dependencias
            limpiarRegistrosTemporales(userId);
            
            // Verificar dependencias críticas después de limpiar registros temporales
            if (tieneRegistrosAsociados(userId)) {
                req.setAttribute("error", "El usuario aún tiene registros asociados que impiden su eliminación. Use la opción 'Ver Registros Asociados' para revisarlos y eliminarlos primero.");
                req.setAttribute("userId", userIdStr);
                req.setAttribute("esAutoEliminacion", esAutoEliminacion);
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
                    // NO registrar auditoría aquí para evitar ciclos infinitos
                    // El registro de auditoría impediría futuras eliminaciones
                    System.err.println("[INFO] Foreign key constraint impide eliminación de usuario " + userId + ": " + errorMsg);
                    req.setAttribute("error", "No se puede eliminar este usuario porque tiene registros asociados en el sistema. Use la opción 'Ver Mis Registros Asociados' para revisar y limpiar los registros necesarios primero.");
                    req.setAttribute("userId", userIdStr);
                    req.setAttribute("esAutoEliminacion", esAutoEliminacion);
                    req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                    return;
                } else {
                    throw sqlEx;
                }
            }
            
            if (eliminado) {
                // Enviar correo de notificación apropiado
                try {
                    java.util.Properties props = new java.util.Properties();
                    try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                        if (in != null) props.load(in);
                    }
                    String mailUser = props.getProperty("mail.smtp.user");
                    String mailPass = props.getProperty("mail.smtp.pass");
                    EmailService emailService = new EmailService(mailUser, mailPass);
                    
                    if (esAutoEliminacion) {
                        // Correo de auto-eliminación 
                        String mensaje = EmailService.generateSelfDeletionEmail(
                            usuarioAEliminar.getNombres(), 
                            usuarioAEliminar.getApellidos(), 
                            usuarioAEliminar.getUsername()
                        );
                        emailService.sendHtmlEmail(usuarioAEliminar.getEmail(), 
                                                 "¡Hasta pronto! - Cuenta eliminada - SumixKids", 
                                                 mensaje);
                    } else {
                        // Correo de eliminación por administrador
                        emailService.sendAccountDeletionEmail(usuarioAEliminar.getEmail(), 
                                                            usuarioAEliminar.getNombres(), 
                                                            usuarioAEliminar.getApellidos(), 
                                                            usuario.getUsername());
                    }
                } catch (Exception emailEx) {
                    System.err.println("No se pudo enviar correo de notificación de eliminación: " + emailEx.getMessage());
                    emailEx.printStackTrace();
                }
                
                String accionAuditoria = esAutoEliminacion ? "Auto Eliminacion" : "Eliminar Usuario";
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
                // NO registrar auditoría de eliminación fallida para evitar ciclos infinitos
                System.err.println("[INFO] Eliminación de usuario " + userId + " falló por razones técnicas");
                req.setAttribute("error", "❌ No se pudo eliminar el usuario. Puede que tenga registros asociados.");
                req.setAttribute("userId", userIdStr);
                req.setAttribute("esAutoEliminacion", esAutoEliminacion);
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Error al parsear ID de usuario: " + userIdStr);
            req.setAttribute("error", "ID de usuario inválido. Por favor, verifique los parámetros.");
            req.setAttribute("userId", userIdStr);
            req.setAttribute("esAutoEliminacion", esAutoEliminacionStr != null && esAutoEliminacionStr.equals("true"));
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
        } catch (SQLException sqlEx) {
            System.err.println("Error SQL en eliminación: " + sqlEx.getMessage());
            
            String mensajeError = "❌ Error al eliminar el usuario. ";
            if (sqlEx.getMessage().contains("foreign key constraint") || 
                sqlEx.getMessage().contains("FOREIGN KEY")) {
                mensajeError += "El usuario tiene registros asociados que deben preservarse.";
            } else {
                mensajeError += "Por favor, contacte al administrador del sistema.";
            }
            
            // Validar userIdStr antes de parsearlo para auditoría
            try {
                if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                    String accionAuditoria = esAutoEliminacion ? "Error Auto Eliminacion" : "Error Eliminacion";
                    registrarAuditoria(usuario, req.getRemoteAddr(), accionAuditoria, 
                                      "usuarios", "Error SQL", 
                                      mensajeError, "ERROR", Integer.parseInt(userIdStr));
                }
            } catch (NumberFormatException nfe) {
                System.err.println("No se pudo registrar auditoría - ID de usuario inválido: " + userIdStr);
            }
            req.setAttribute("error", mensajeError);
            req.setAttribute("userId", userIdStr);
            req.setAttribute("esAutoEliminacion", esAutoEliminacion);
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Error inesperado en eliminación: " + e.getMessage());
            
            // Validar userIdStr antes de parsearlo para auditoría
            try {
                if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                    String accionAuditoria = esAutoEliminacion ? "Error Auto Eliminacion" : "Error Eliminacion";
                    registrarAuditoria(usuario, req.getRemoteAddr(), accionAuditoria, 
                                      "usuarios", "Error general", 
                                      "Error inesperado", "ERROR", Integer.parseInt(userIdStr));
                }
            } catch (NumberFormatException nfe) {
                System.err.println("No se pudo registrar auditoría - ID de usuario inválido: " + userIdStr);
            }
            req.setAttribute("error", "Ocurrió un error inesperado. Por favor, intente nuevamente o contacte al administrador.");
            req.setAttribute("userId", userIdStr);
            req.setAttribute("esAutoEliminacion", esAutoEliminacion);
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

    /**
     * Limpia automáticamente registros temporales que no impiden la eliminación del usuario
     */
    private void limpiarRegistrosTemporales(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);
            
            // Limpiar password resets expirados o del usuario
            try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM password_resets_codes WHERE usuario_id = ? OR expires_at < NOW()")) {
                pstmt.setInt(1, userId);
                int deleted = pstmt.executeUpdate();
                if (deleted > 0) {
                    System.out.println("[INFO] Eliminados " + deleted + " registros de password_resets_codes para usuario " + userId);
                }
            } catch (SQLException e) {
                System.out.println("[WARNING] No se pudo limpiar password_resets_codes: " + e.getMessage());
            }
            
            // Limpiar códigos 2FA expirados o del usuario
            try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM two_factor_codes WHERE usuario_id = ? OR expires_at < NOW()")) {
                pstmt.setInt(1, userId);
                int deleted = pstmt.executeUpdate();
                if (deleted > 0) {
                    System.out.println("[INFO] Eliminados " + deleted + " códigos 2FA para usuario " + userId);
                }
            } catch (SQLException e) {
                System.out.println("[WARNING] No se pudo limpiar two_factor_codes: " + e.getMessage());
            }
            
            // Limpiar sesiones activas del usuario
            try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM sesiones_activas WHERE usuario_id = ?")) {
                pstmt.setInt(1, userId);
                int deleted = pstmt.executeUpdate();
                if (deleted > 0) {
                    System.out.println("[INFO] Eliminadas " + deleted + " sesiones activas para usuario " + userId);
                }
            } catch (SQLException e) {
                System.out.println("[WARNING] No se pudo limpiar sesiones_activas: " + e.getMessage());
            }
            
            // Limpiar dispositivos reconocidos del usuario (son específicos del usuario)
            try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM dispositivos_reconocidos WHERE usuario_id = ?")) {
                pstmt.setInt(1, userId);
                int deleted = pstmt.executeUpdate();
                if (deleted > 0) {
                    System.out.println("[INFO] Eliminados " + deleted + " dispositivos reconocidos para usuario " + userId);
                }
            } catch (SQLException e) {
                System.out.println("[WARNING] No se pudo limpiar dispositivos_reconocidos: " + e.getMessage());
            }
            
            // Limpiar registros de auditoría de eliminación fallida que causan ciclos infinitos
            try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM log_auditoria WHERE id_usuario = ? AND accion IN ('Eliminacion Fallida', 'Auto Eliminacion Fallida', 'Error Eliminacion', 'Error Auto Eliminacion')")) {
                pstmt.setInt(1, userId);
                int deleted = pstmt.executeUpdate();
                if (deleted > 0) {
                    System.out.println("[INFO] Eliminados " + deleted + " registros de auditoría de eliminación fallida para usuario " + userId);
                }
            } catch (SQLException e) {
                System.out.println("[WARNING] No se pudo limpiar registros de auditoría fallidos: " + e.getMessage());
            }
            
            conn.commit();
            System.out.println("[INFO] Registros temporales limpiados exitosamente para usuario " + userId);
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("[ERROR] Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.err.println("[ERROR] Error al limpiar registros temporales para usuario " + userId + ": " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[ERROR] Error al cerrar conexión: " + e.getMessage());
                }
            }
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
        // Lista de tablas que realmente IMPIDEN la eliminación del usuario
        // Excluimos registros temporales que se limpian automáticamente:
        // - password_resets: se limpian automáticamente
        // - sesiones_activas: se limpian automáticamente  
        // - two_factor_codes: se limpian automáticamente
        // - dispositivos_reconocidos: se limpian automáticamente (son específicos del usuario)
        // También excluimos log_auditoria que se preserva pero no impide eliminación
        String[] tablasVerificar = {
            "cargas_masivas WHERE usuario_id = ?",
            "log_acceso WHERE usuario_id = ?",
            "mantenimiento_programado WHERE creado_por = ?",
            "usuario_roles WHERE usuario_id = ?",
            "usuario_roles WHERE asignado_por = ?"
        };
        
        for (String consulta : tablasVerificar) {
            String sqlCompleto = "SELECT COUNT(*) FROM " + consulta;
            System.out.println("[DEBUG EliminarUsuario] Ejecutando SQL: " + sqlCompleto + " con userId: " + userId);
            
            try (PreparedStatement ps = conn.prepareStatement(sqlCompleto)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        System.out.println("[DEBUG EliminarUsuario] Tabla: " + consulta + ", Registros encontrados: " + count);
                        if (count > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}