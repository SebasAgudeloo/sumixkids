package com.sumixkids.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PasswordResetDAO {
    public static void guardarCodigo(int usuarioId, String code, LocalDateTime expiresAt) throws SQLException {
    String sql = "INSERT INTO password_resets_codes (usuario_id, code, expires_at, used) VALUES (?, ?, ?, 0)";
        try (Connection cn = com.sumixkids.config.DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, code);
            ps.setTimestamp(3, Timestamp.valueOf(expiresAt));
            ps.executeUpdate();
        }
    }

    // Elimina códigos expirados o ya usados
    public static void eliminarCodigosExpiradosYUsados() throws SQLException {
    String sql = "DELETE FROM password_resets_codes WHERE used = 1 OR expires_at < ?";
        try (Connection cn = com.sumixkids.config.DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime()));
            ps.executeUpdate();
        }
    }

    public static boolean validarCodigo(int usuarioId, String code) throws SQLException {
    String sql = "SELECT * FROM password_resets_codes WHERE usuario_id = ? AND code = ? AND used = 0 AND expires_at > ?";
        try (Connection cn = com.sumixkids.config.DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, code);
            ps.setTimestamp(3, Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void marcarComoUsado(int usuarioId, String code) throws SQLException {
    String sql = "UPDATE password_resets_codes SET used = 1 WHERE usuario_id = ? AND code = ?";
        try (Connection cn = com.sumixkids.config.DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, code);
            ps.executeUpdate();
        }
    }
}
