package com.sumixkids.dao;

import com.sumixkids.config.DatabaseManager;
import com.sumixkids.model.LogAuditoria;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogAuditoriaDAO {
    
    // Inserta un registro en la tabla log_auditoria
    public void registrarLog(LogAuditoria log) throws SQLException {
        String sql = "INSERT INTO log_auditoria (fecha_hora, id_usuario, nombre_usuario, " +
                    "ip_usuario, accion, tabla_afectada, valor_anterior, valor_nuevo, " +
                    "descripcion, estado, aprobado_por_admin_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(log.getFechaHora()));
            stmt.setInt(2, log.getIdUsuario());
            stmt.setString(3, log.getNombreUsuario());
            stmt.setString(4, log.getIpUsuario());
            stmt.setString(5, log.getAccion());
            stmt.setString(6, log.getTablaAfectada());
            stmt.setString(7, log.getValorAnterior());
            stmt.setString(8, log.getValorNuevo());
            stmt.setString(9, log.getDescripcion());
            stmt.setString(10, log.getEstado());
            
            if (log.getAprobadoPorAdminId() != null) {
                stmt.setInt(11, log.getAprobadoPorAdminId());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }
            
            stmt.executeUpdate();
        }
    }

    // Recupera registros de log_auditoria
    public List<LogAuditoria> buscarLogs(String filtro, LocalDateTime desde, 
                                        LocalDateTime hasta, Integer idUsuario) throws SQLException {
        String sql = "SELECT * FROM log_auditoria ORDER BY fecha_hora DESC";
        List<LogAuditoria> logs = new ArrayList<>();
        
        System.out.println("Debug: Ejecutando consulta SQL: " + sql); // Debug
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                LogAuditoria log = new LogAuditoria();
                log.setId(rs.getLong("id"));
                log.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                log.setIdUsuario(rs.getInt("id_usuario"));
                log.setNombreUsuario(rs.getString("nombre_usuario"));
                log.setIpUsuario(rs.getString("ip_usuario"));
                log.setAccion(rs.getString("accion"));
                log.setTablaAfectada(rs.getString("tabla_afectada"));
                log.setValorAnterior(rs.getString("valor_anterior"));
                log.setValorNuevo(rs.getString("valor_nuevo"));
                log.setDescripcion(rs.getString("descripcion"));
                log.setEstado(rs.getString("estado"));
                log.setAprobadoPorAdminId(rs.getInt("aprobado_por_admin_id"));
                
                logs.add(log);
            }
        }
        
        System.out.println("Debug: Registros encontrados: " + logs.size()); // Debug
        return logs;
    }

    /**
     * Registra un intento de acceso no autorizado
     * @param ip IP del usuario
     * @param username Nombre de usuario que intentó acceder
     * @param tipoIntento Tipo de intento (LOGIN_FALLIDO, ACCESO_DENEGADO)
     * @param detalles Detalles adicionales del intento
     */
    public void registrarIntentoAcceso(String ip, String username, 
                                     String tipoIntento, String detalles) throws SQLException {
        LogAuditoria log = new LogAuditoria();
        log.setFechaHora(LocalDateTime.now());
        log.setIpUsuario(ip);
        log.setNombreUsuario(username);
        log.setAccion(tipoIntento);
        log.setTablaAfectada("seguridad");
        log.setDescripcion(detalles);
        log.setEstado("NO_AUTORIZADO");
        
        // Establecer un ID por defecto para usuarios desconocidos
        log.setIdUsuario(0);  // O cualquier otro valor que represente usuario desconocido
        
        registrarLog(log);
    }

    /**
     * Busca intentos no autorizados
     */
    public List<LogAuditoria> buscarIntentosNoAutorizados(LocalDateTime desde, 
                                                         LocalDateTime hasta, 
                                                         String ip) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM log_auditoria WHERE estado = 'NO_AUTORIZADO'");
        List<Object> params = new ArrayList<>();
        
        if (desde != null) {
            sql.append(" AND fecha_hora >= ?");
            params.add(Timestamp.valueOf(desde));
        }
        
        if (hasta != null) {
            sql.append(" AND fecha_hora <= ?");
            params.add(Timestamp.valueOf(hasta));
        }
        
        if (ip != null && !ip.trim().isEmpty()) {
            sql.append(" AND ip_usuario = ?");
            params.add(ip);
        }
        
        sql.append(" ORDER BY fecha_hora DESC");
        
        List<LogAuditoria> logs = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LogAuditoria log = new LogAuditoria();
                    log.setId(rs.getLong("id"));
                    log.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                    log.setIpUsuario(rs.getString("ip_usuario"));
                    log.setNombreUsuario(rs.getString("nombre_usuario"));
                    log.setAccion(rs.getString("accion"));
                    log.setDescripcion(rs.getString("descripcion"));
                    log.setEstado(rs.getString("estado"));
                    logs.add(log);
                }
            }
        }
        return logs;
    }
}
