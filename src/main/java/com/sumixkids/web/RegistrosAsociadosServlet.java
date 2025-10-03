package com.sumixkids.web;

import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Servlet para manejar la visualización y eliminación de registros asociados a un usuario
 * antes de proceder con su eliminación definitiva.
 */
public class RegistrosAsociadosServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegistrosAsociadosServlet.class);
    
    private UsuarioDAO usuarioDAO;
    private LogAuditoriaDAO logAuditoriaDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.usuarioDAO = new UsuarioDAO();
        this.logAuditoriaDAO = new LogAuditoriaDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        
        String userIdParam = request.getParameter("userId");
        String esAutoEliminacionParam = request.getParameter("esAutoEliminacion");
        boolean esAutoEliminacion = "true".equals(esAutoEliminacionParam);
        
        // Si es auto-eliminación, usar el ID del usuario de la sesión
        int userId;
        if (esAutoEliminacion) {
            userId = usuarioSesion.getId();
            userIdParam = String.valueOf(userId);
        } else {
            if (userIdParam == null || userIdParam.trim().isEmpty()) {
                logger.error("ID de usuario no válido: {}", userIdParam);
                request.setAttribute("error", "ID de usuario no válido - parámetro faltante");
                request.getRequestDispatcher("/registros_asociados.jsp").forward(request, response);
                return;
            }
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                logger.error("ID de usuario inválido: {}", userIdParam, e);
                request.setAttribute("error", "ID de usuario inválido - formato incorrecto");
                request.getRequestDispatcher("/registros_asociados.jsp").forward(request, response);
                return;
            }
        }
        
        logger.info("DEBUG - userIdParam: {}, esAutoEliminacionParam: {}, userId final: {}, usuarioSesion.id: {}, usuarioSesion.rolId: {}", 
                   userIdParam, esAutoEliminacionParam, userId, usuarioSesion.getId(), usuarioSesion.getRolId());
        
        try {
            // Verificar permisos:
            // - Administradores pueden ver cualquier usuario
            // - Usuarios normales solo pueden ver sus propios registros
            logger.info("DEBUG - Verificando permisos: usuarioSesion.getRolId()={}, usuarioSesion.getId()={}, userId solicitado={}", 
                       usuarioSesion.getRolId(), usuarioSesion.getId(), userId);
            
            if (usuarioSesion.getRolId() != 1 && usuarioSesion.getId() != userId) {
                logger.warn("Acceso denegado: Usuario {} (rol {}) intentó acceder a registros del usuario {}", 
                           usuarioSesion.getId(), usuarioSesion.getRolId(), userId);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para ver estos registros");
                return;
            }
            
            // Obtener información del usuario
            Usuario usuario = null;
            try {
                usuario = usuarioDAO.findById(userId);
            } catch (SQLException e) {
                logger.error("Error al buscar usuario por ID: {}", userId, e);
                request.setAttribute("error", "Error al buscar usuario en la base de datos");
                request.getRequestDispatcher("/registros_asociados.jsp").forward(request, response);
                return;
            }
            
            if (usuario == null) {
                request.setAttribute("error", "Usuario no encontrado");
                request.getRequestDispatcher("/registros_asociados.jsp").forward(request, response);
                return;
            }
            
            // Obtener registros de auditoría del usuario
            List<LogAuditoria> registrosAuditoria = obtenerRegistrosAuditoriaPorUsuario(userId);
            
            // Obtener otros registros asociados (placeholder para futuras funcionalidades)
            List<Map<String, Object>> otrosRegistros = obtenerOtrosRegistros(userId);
            
            // Establecer atributos para la vista
            request.setAttribute("usuario", usuario);
            request.setAttribute("registrosAuditoria", registrosAuditoria);
            request.setAttribute("otrosRegistros", otrosRegistros);
            request.setAttribute("esAutoEliminacion", esAutoEliminacion);
            request.setAttribute("usuarioSesion", usuarioSesion);
            
            logger.info("Mostrando registros asociados para usuario ID: {} por usuario: {} (esAutoEliminacion: {})", 
                       userId, usuarioSesion.getEmail(), esAutoEliminacion);
            
        } catch (NumberFormatException e) {
            logger.error("ID de usuario inválido: {}", userIdParam, e);
            request.setAttribute("error", "ID de usuario inválido");
        } catch (Exception e) {
            logger.error("Error al obtener registros asociados", e);
            request.setAttribute("error", "Error interno del servidor");
        }
        
        request.getRequestDispatcher("/registros_asociados.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        
        String action = request.getParameter("action");
        String userIdParam = request.getParameter("userId");
        String esAutoEliminacionParam = request.getParameter("esAutoEliminacion");
        boolean esAutoEliminacion = "true".equals(esAutoEliminacionParam);
        
        // Si es auto-eliminación, usar el ID del usuario de la sesión
        int userId;
        if (esAutoEliminacion) {
            userId = usuarioSesion.getId();
        } else {
            if (userIdParam == null || userIdParam.trim().isEmpty()) {
                request.setAttribute("error", "ID de usuario no válido");
                doGet(request, response);
                return;
            }
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                logger.error("ID de usuario inválido: {}", userIdParam, e);
                request.setAttribute("error", "ID de usuario inválido");
                doGet(request, response);
                return;
            }
        }
        
        try {
            // Verificar permisos:
            // - Administradores pueden eliminar registros de cualquier usuario
            // - Usuarios normales solo pueden eliminar sus propios registros
            if (usuarioSesion.getRolId() != 1 && usuarioSesion.getId() != userId) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para eliminar estos registros");
                return;
            }
            
            switch (action) {
                case "eliminar_auditoria":
                    eliminarRegistroAuditoria(request, response, userId, usuarioSesion);
                    break;
                    
                case "eliminar_todos_auditoria":
                    eliminarTodosRegistrosAuditoria(request, response, userId, usuarioSesion);
                    break;
                    
                case "eliminar_otro":
                    eliminarOtroRegistro(request, response, userId, usuarioSesion);
                    break;
                    
                default:
                    request.setAttribute("error", "Acción no válida");
                    doGet(request, response);
                    return;
            }
            
        } catch (NumberFormatException e) {
            logger.error("ID de usuario inválido: {}", userIdParam, e);
            request.setAttribute("error", "ID de usuario inválido");
            doGet(request, response);
        } catch (Exception e) {
            logger.error("Error al procesar acción: {}", action, e);
            request.setAttribute("error", "Error interno del servidor");
            doGet(request, response);
        }
    }
    
    /**
     * Elimina un registro específico de auditoría
     */
    private void eliminarRegistroAuditoria(HttpServletRequest request, HttpServletResponse response,
                                         int userId, Usuario usuarioSesion) 
            throws ServletException, IOException {
        
        String registroIdParam = request.getParameter("registroId");
        if (registroIdParam == null || registroIdParam.trim().isEmpty()) {
            request.setAttribute("error", "ID de registro no válido");
            doGet(request, response);
            return;
        }
        
        try {
            int registroId = Integer.parseInt(registroIdParam);
            
            boolean eliminado = eliminarRegistroAuditoriaEspecifico(registroId);
            
            if (eliminado) {
                request.setAttribute("mensaje", "Registro de auditoría eliminado exitosamente");
                logger.info("Registro de auditoría ID: {} eliminado por admin: {}", 
                           registroId, usuarioSesion.getEmail());
                
                // Registrar la acción en auditoría
                registrarAccionAuditoria(usuarioSesion.getId(), "ELIMINAR_AUDITORIA", 
                                        "log_auditoria", registroId, 
                                        request.getRemoteAddr());
            } else {
                request.setAttribute("error", "No se pudo eliminar el registro de auditoría");
            }
            
        } catch (NumberFormatException e) {
            logger.error("ID de registro inválido: {}", registroIdParam, e);
            request.setAttribute("error", "ID de registro inválido");
        }
        
        doGet(request, response);
    }
    
    /**
     * Elimina todos los registros de auditoría del usuario
     */
    private void eliminarTodosRegistrosAuditoria(HttpServletRequest request, HttpServletResponse response,
                                               int userId, Usuario usuarioSesion) 
            throws ServletException, IOException {
        
        try {
            int eliminados = eliminarTodosRegistrosAuditoriaPorUsuario(userId);
            
            if (eliminados > 0) {
                request.setAttribute("mensaje", 
                    String.format("Se eliminaron %d registros de auditoría", eliminados));
                logger.info("Eliminados {} registros de auditoría del usuario ID: {} por admin: {}", 
                           eliminados, userId, usuarioSesion.getEmail());
                
                // Registrar la acción en auditoría
                registrarAccionAuditoria(usuarioSesion.getId(), "ELIMINAR_AUDITORIA_MASIVA", 
                                        "log_auditoria", userId, 
                                        request.getRemoteAddr());
            } else {
                request.setAttribute("mensaje", "No se encontraron registros de auditoría para eliminar");
            }
            
        } catch (Exception e) {
            logger.error("Error al eliminar registros de auditoría masivamente", e);
            request.setAttribute("error", "Error al eliminar los registros de auditoría");
        }
        
        doGet(request, response);
    }
    
    /**
     * Elimina otro tipo de registro asociado
     */
    private void eliminarOtroRegistro(HttpServletRequest request, HttpServletResponse response,
                                    int userId, Usuario usuarioSesion) 
            throws ServletException, IOException {
        
        String tipo = request.getParameter("tipo");
        String registroIdParam = request.getParameter("registroId");
        
        if (tipo == null || registroIdParam == null) {
            request.setAttribute("error", "Parámetros de eliminación no válidos");
            doGet(request, response);
            return;
        }
        
        try {
            int registroId = Integer.parseInt(registroIdParam);
            
            // Aquí se implementaría la lógica para eliminar otros tipos de registros
            // Por ejemplo: actividades, sesiones, etc.
            boolean eliminado = eliminarRegistroPorTipo(tipo, registroId);
            
            if (eliminado) {
                request.setAttribute("mensaje", 
                    String.format("Registro de %s eliminado exitosamente", tipo));
                logger.info("Registro de {} ID: {} eliminado por admin: {}", 
                           tipo, registroId, usuarioSesion.getEmail());
                
                // Registrar la acción en auditoría
                registrarAccionAuditoria(usuarioSesion.getId(), "ELIMINAR_" + tipo.toUpperCase(), 
                                        tipo.toLowerCase(), registroId, 
                                        request.getRemoteAddr());
            } else {
                request.setAttribute("error", 
                    String.format("No se pudo eliminar el registro de %s", tipo));
            }
            
        } catch (NumberFormatException e) {
            logger.error("ID de registro inválido: {}", registroIdParam, e);
            request.setAttribute("error", "ID de registro inválido");
        }
        
        doGet(request, response);
    }
    
    /**
     * Obtiene registros de auditoría asociados al usuario
     */
    private List<LogAuditoria> obtenerRegistrosAuditoriaPorUsuario(int userId) {
        try {
            // Buscar registros donde el usuario fue el que realizó la acción
            return logAuditoriaDAO.buscarLogs(null, null, null, userId);
        } catch (Exception e) {
            logger.error("Error al obtener registros de auditoría para usuario: {}", userId, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Elimina un registro específico de auditoría
     */
    private boolean eliminarRegistroAuditoriaEspecifico(int registroId) {
        try {
            // Implementar eliminación directa en base de datos
            String sql = "DELETE FROM log_auditoria WHERE id = ?";
            try (Connection conn = com.sumixkids.config.DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, registroId);
                int filasAfectadas = stmt.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar registro de auditoría: {}", registroId, e);
            return false;
        }
    }
    
    /**
     * Elimina todos los registros de auditoría de un usuario
     */
    private int eliminarTodosRegistrosAuditoriaPorUsuario(int userId) {
        try {
            // Primero, contar cuántos registros existen
            String countSql = "SELECT COUNT(*) FROM log_auditoria WHERE id_usuario = ?";
            int totalRegistros = 0;
            
            try (Connection conn = com.sumixkids.config.DatabaseManager.getConnection();
                 PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                countStmt.setInt(1, userId);
                try (ResultSet rs = countStmt.executeQuery()) {
                    if (rs.next()) {
                        totalRegistros = rs.getInt(1);
                    }
                }
            }
            
            logger.info("Debug: Usuario {} tiene {} registros de auditoría antes de eliminar", userId, totalRegistros);
            
            // Ahora eliminar los registros
            String sql = "DELETE FROM log_auditoria WHERE id_usuario = ?";
            try (Connection conn = com.sumixkids.config.DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                int eliminados = stmt.executeUpdate();
                logger.info("Debug: Eliminados {} registros de auditoría del usuario {}", eliminados, userId);
                return eliminados;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar registros de auditoría del usuario: {}", userId, e);
            return 0;
        }
    }
    
    /**
     * Registra una acción en la auditoría
     */
    private void registrarAccionAuditoria(int usuarioId, String accion, String tabla, 
                                        int registroAfectado, String ip) {
        try {
            LogAuditoria log = new LogAuditoria();
            log.setIdUsuario(usuarioId);
            log.setAccion(accion);
            log.setTablaAfectada(tabla);
            log.setIpUsuario(ip);
            log.setDescripcion("Registro ID: " + registroAfectado);
            log.setEstado("COMPLETADO");
            
            logAuditoriaDAO.registrarLog(log);
        } catch (Exception e) {
            logger.error("Error al registrar acción en auditoría", e);
        }
    }
    
    /**
     * Obtiene otros registros asociados al usuario (placeholder para futuras funcionalidades)
     * Como actividades, sesiones, etc.
     */
    private List<Map<String, Object>> obtenerOtrosRegistros(int userId) {
        List<Map<String, Object>> registros = new ArrayList<>();
        
        // Placeholder - aquí se implementarían consultas a otras tablas
        // Ejemplo:
        // - Actividades del usuario
        // - Sesiones activas
        // - Configuraciones personalizadas
        // - etc.
        
        return registros;
    }
    
    /**
     * Elimina un registro específico según su tipo
     */
    private boolean eliminarRegistroPorTipo(String tipo, int registroId) {
        // Placeholder - aquí se implementaría la lógica específica según el tipo
        switch (tipo.toLowerCase()) {
            case "actividad":
                // return actividadDAO.eliminar(registroId);
                break;
            case "sesion":
                // return sesionDAO.eliminar(registroId);
                break;
            // Agregar más tipos según necesidades
            default:
                logger.warn("Tipo de registro no soportado: {}", tipo);
                return false;
        }
        
        return false;
    }
}