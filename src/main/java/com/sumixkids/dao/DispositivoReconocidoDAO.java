package com.sumixkids.dao;

import com.sumixkids.config.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * DAO para gestionar dispositivos reconocidos en el sistema 2FA
 */
public class DispositivoReconocidoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(DispositivoReconocidoDAO.class);
    
    /**
     * Genera un device_id único basado en IP + User-Agent
     */
    public static String generarDeviceId(String ip, String userAgent) {
        String combined = ip + "|" + (userAgent != null ? userAgent : "unknown");
        return java.util.UUID.nameUUIDFromBytes(combined.getBytes()).toString();
    }
    
    /**
     * Verifica si un dispositivo necesita 2FA
     * @param usuarioId ID del usuario
     * @param ip IP del cliente
     * @param userAgent User-Agent del navegador
     * @return true si necesita 2FA, false si el dispositivo está reconocido
     */
    public boolean necesita2FA(int usuarioId, String ip, String userAgent) throws SQLException {
        String deviceId = generarDeviceId(ip, userAgent);
        
        String sql = "SELECT contador_2fa, fecha_ultimo_2fa " +
                     "FROM dispositivos_reconocidos " +
                     "WHERE usuario_id = ? AND device_id = ?";
            
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, usuarioId);
            ps.setString(2, deviceId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    // Dispositivo no registrado, necesita 2FA
                    return true;
                }
                
                int contador = rs.getInt("contador_2fa");
                Timestamp ultimoTimestamp = rs.getTimestamp("fecha_ultimo_2fa");
                
                // Si no ha completado 3 veces el 2FA, lo necesita
                if (contador < 3) {
                    return true;
                }
                
                // Si ya completó 3 veces, verificar si han pasado 15 días
                if (ultimoTimestamp != null) {
                    LocalDateTime ultimo2FA = ultimoTimestamp.toLocalDateTime();
                    LocalDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
                    
                    // Si han pasado 15 días o más, necesita 2FA nuevamente
                    return ultimo2FA.plusDays(15).isBefore(ahora);
                }
                
                return false;
            }
        }
    }
    
    /**
     * Registra un intento exitoso de 2FA para un dispositivo
     * @param usuarioId ID del usuario
     * @param ip IP del cliente
     * @param userAgent User-Agent del navegador
     */
    public void registrar2FAExitoso(int usuarioId, String ip, String userAgent) throws SQLException {
        logger.info("Iniciando registro 2FA exitoso - Usuario: {}, IP: {}", usuarioId, ip);
        
        String deviceId = generarDeviceId(ip, userAgent);
        LocalDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
        
        String sqlCheck = "SELECT contador_2fa FROM dispositivos_reconocidos " +
                          "WHERE usuario_id = ? AND device_id = ?";
            
        try (Connection cn = DatabaseManager.getConnection()) {
            logger.info("Conexión a BD obtenida para registro 2FA - DeviceId: {}", deviceId);
            // Verificar si el dispositivo ya existe
            try (PreparedStatement psCheck = cn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, usuarioId);
                psCheck.setString(2, deviceId);
                
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        // Dispositivo existe, actualizar contador y fecha
                        int contadorActual = rs.getInt("contador_2fa");
                        int nuevoContador = Math.min(contadorActual + 1, 3); // Máximo 3
                        logger.info("Dispositivo existente encontrado - Contador actual: {}, Nuevo: {}", contadorActual, nuevoContador);
                        
                        String sqlUpdate = "UPDATE dispositivos_reconocidos " +
                                          "SET contador_2fa = ?, fecha_ultimo_2fa = ? " +
                                          "WHERE usuario_id = ? AND device_id = ?";
                            
                        try (PreparedStatement psUpdate = cn.prepareStatement(sqlUpdate)) {
                            psUpdate.setInt(1, nuevoContador);
                            psUpdate.setTimestamp(2, Timestamp.valueOf(ahora));
                            psUpdate.setInt(3, usuarioId);
                            psUpdate.setString(4, deviceId);
                            psUpdate.executeUpdate();
                        }
                        
                        logger.info("Dispositivo actualizado - Usuario: {}, DeviceID: {}, Contador: {}", 
                                   usuarioId, deviceId, nuevoContador);
                    } else {
                        // Dispositivo nuevo, insertar
                        logger.info("Dispositivo nuevo, insertando en BD");
                        String sqlInsert = "INSERT INTO dispositivos_reconocidos " +
                                           "(usuario_id, device_id, user_agent, fecha_ultimo_2fa, contador_2fa, fecha_registro) " +
                                           "VALUES (?, ?, ?, ?, 1, ?)";
                            
                        try (PreparedStatement psInsert = cn.prepareStatement(sqlInsert)) {
                            psInsert.setInt(1, usuarioId);
                            psInsert.setString(2, deviceId);
                            psInsert.setString(3, userAgent);
                            psInsert.setTimestamp(4, Timestamp.valueOf(ahora));
                            psInsert.setTimestamp(5, Timestamp.valueOf(ahora));
                            psInsert.executeUpdate();
                        }
                        
                        logger.info("Nuevo dispositivo registrado - Usuario: {}, DeviceID: {}", 
                                   usuarioId, deviceId);
                    }
                }
            }
        }
    }
    
    /**
     * Reinicia el contador de 2FA para un dispositivo (útil cuando pasan 15 días)
     * @param usuarioId ID del usuario
     * @param ip IP del cliente  
     * @param userAgent User-Agent del navegador
     */
    public void reiniciarContador2FA(int usuarioId, String ip, String userAgent) throws SQLException {
        String deviceId = generarDeviceId(ip, userAgent);
        LocalDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime();
        
        String sql = "UPDATE dispositivos_reconocidos " +
                     "SET contador_2fa = 1, fecha_ultimo_2fa = ? " +
                     "WHERE usuario_id = ? AND device_id = ?";
            
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(ahora));
            ps.setInt(2, usuarioId);
            ps.setString(3, deviceId);
            ps.executeUpdate();
            
            logger.info("Contador 2FA reiniciado - Usuario: {}, DeviceID: {}", usuarioId, deviceId);
        }
    }
    
    /**
     * Limpia dispositivos antiguos (más de 30 días sin uso)
     */
    public void limpiarDispositivosAntiguos() throws SQLException {
        String sql = "DELETE FROM dispositivos_reconocidos " +
                     "WHERE fecha_ultimo_2fa < DATE_SUB(NOW(), INTERVAL 30 DAY)";
            
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            int eliminados = ps.executeUpdate();
            if (eliminados > 0) {
                logger.info("Dispositivos antiguos eliminados: {}", eliminados);
            }
        }
    }
    
    /**
     * Obtiene información de un dispositivo para debugging
     */
    public String getInfoDispositivo(int usuarioId, String ip, String userAgent) throws SQLException {
        String deviceId = generarDeviceId(ip, userAgent);
        
        String sql = "SELECT contador_2fa, fecha_ultimo_2fa, fecha_registro " +
                     "FROM dispositivos_reconocidos " +
                     "WHERE usuario_id = ? AND device_id = ?";
            
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, usuarioId);
            ps.setString(2, deviceId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return String.format("DeviceID: %s, Contador: %d, Último 2FA: %s, Registrado: %s", 
                                       deviceId, 
                                       rs.getInt("contador_2fa"),
                                       rs.getTimestamp("fecha_ultimo_2fa"),
                                       rs.getTimestamp("fecha_registro"));
                } else {
                    return "Dispositivo no registrado - DeviceID: " + deviceId;
                }
            }
        }
    }
}