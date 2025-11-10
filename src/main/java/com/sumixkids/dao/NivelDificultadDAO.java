package com.sumixkids.dao;

import com.sumixkids.model.NivelDificultad;
import com.sumixkids.config.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar operaciones de la entidad NivelDificultad
 */
public class NivelDificultadDAO {
    
    /**
     * Crear un nuevo nivel de dificultad
     */
    public int crear(NivelDificultad nivel) throws SQLException {
        String sql = "INSERT INTO niveles_dificultad (nombre_nivel, orden, descripcion, " +
                    "color_hex, icono, activo) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, nivel.getNombreNivel());
            stmt.setInt(2, nivel.getOrden());
            stmt.setString(3, nivel.getDescripcion());
            stmt.setString(4, nivel.getColorHex());
            stmt.setString(5, nivel.getIcono());
            stmt.setBoolean(6, nivel.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            return 0;
        }
    }
    
    /**
     * Actualizar un nivel de dificultad existente
     */
    public boolean actualizar(NivelDificultad nivel) throws SQLException {
        String sql = "UPDATE niveles_dificultad SET nombre_nivel = ?, orden = ?, " +
                    "descripcion = ?, color_hex = ?, icono = ?, activo = ? WHERE id_nivel_dificultad = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nivel.getNombreNivel());
            stmt.setInt(2, nivel.getOrden());
            stmt.setString(3, nivel.getDescripcion());
            stmt.setString(4, nivel.getColorHex());
            stmt.setString(5, nivel.getIcono());
            stmt.setBoolean(6, nivel.isActivo());
            stmt.setInt(7, nivel.getIdNivelDificultad());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Eliminar (desactivar) un nivel de dificultad y reordenar automáticamente
     */
    public boolean eliminar(int idNivel) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Obtener el orden del nivel a eliminar
            String getOrdenSql = "SELECT orden FROM niveles_dificultad WHERE id_nivel_dificultad = ?";
            int ordenEliminado;
            try (PreparedStatement stmt = conn.prepareStatement(getOrdenSql)) {
                stmt.setInt(1, idNivel);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    conn.rollback();
                    return false;
                }
                ordenEliminado = rs.getInt("orden");
            }
            
            // 2. Desactivar el nivel
            String eliminarSql = "UPDATE niveles_dificultad SET activo = false WHERE id_nivel_dificultad = ?";
            try (PreparedStatement stmt = conn.prepareStatement(eliminarSql)) {
                stmt.setInt(1, idNivel);
                stmt.executeUpdate();
            }
            
            // 3. Actualizar el orden de los niveles posteriores (restar 1)
            String reordenarSql = "UPDATE niveles_dificultad SET orden = orden - 1 WHERE orden > ? AND activo = true";
            try (PreparedStatement stmt = conn.prepareStatement(reordenarSql)) {
                stmt.setInt(1, ordenEliminado);
                stmt.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    /**
     * Activar un nivel de dificultad
     */
    public boolean activar(int idNivel) throws SQLException {
        String sql = "UPDATE niveles_dificultad SET activo = true WHERE id_nivel_dificultad = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idNivel);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Buscar un nivel de dificultad por su ID
     */
    public NivelDificultad findById(int idNivel) throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad WHERE id_nivel_dificultad = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idNivel);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNivelDificultad(rs);
            }
            return null;
        }
    }
    
    /**
     * Eliminar permanentemente un nivel de dificultad y reordenar automáticamente
     */
    public boolean delete(int idNivel) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Obtener el orden del nivel a eliminar
            String getOrdenSql = "SELECT orden FROM niveles_dificultad WHERE id_nivel_dificultad = ?";
            int ordenEliminado;
            try (PreparedStatement stmt = conn.prepareStatement(getOrdenSql)) {
                stmt.setInt(1, idNivel);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    conn.rollback();
                    return false;
                }
                ordenEliminado = rs.getInt("orden");
            }
            
            // 2. Eliminar permanentemente el nivel
            String eliminarSql = "DELETE FROM niveles_dificultad WHERE id_nivel_dificultad = ?";
            try (PreparedStatement stmt = conn.prepareStatement(eliminarSql)) {
                stmt.setInt(1, idNivel);
                stmt.executeUpdate();
            }
            
            // 3. Actualizar el orden de los niveles posteriores (restar 1)
            String reordenarSql = "UPDATE niveles_dificultad SET orden = orden - 1 WHERE orden > ?";
            try (PreparedStatement stmt = conn.prepareStatement(reordenarSql)) {
                stmt.setInt(1, ordenEliminado);
                stmt.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    /**
     * Actualizar un nivel de dificultad existente
     */
    public boolean update(NivelDificultad nivel) throws SQLException {
        String sql = "UPDATE niveles_dificultad SET nombre = ?, descripcion = ?, " +
                    "orden = ?, activo = ? WHERE id_nivel_dificultad = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nivel.getNombreNivel());
            stmt.setString(2, nivel.getDescripcion());
            stmt.setInt(3, nivel.getOrden());
            stmt.setBoolean(4, nivel.isActivo());
            stmt.setInt(5, nivel.getIdNivelDificultad());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Buscar nivel de dificultad por nombre
     */
    public NivelDificultad findByNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad WHERE nombre_nivel = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNivelDificultad(rs);
            }
            return null;
        }
    }
    
    /**
     * Listar todos los niveles de dificultad ordenados por orden
     */
    public List<NivelDificultad> findAll() throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad ORDER BY orden";
        
        List<NivelDificultad> niveles = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                niveles.add(mapResultSetToNivelDificultad(rs));
            }
        }
        
        return niveles;
    }
    
    /**
     * Listar todos los niveles de dificultad activos ordenados por orden
     */
    public List<NivelDificultad> findActivos() throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad WHERE activo = true ORDER BY orden";
        
        List<NivelDificultad> niveles = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                niveles.add(mapResultSetToNivelDificultad(rs));
            }
        }
        
        return niveles;
    }
    
    /**
     * Listar niveles de dificultad inactivos
     */
    public List<NivelDificultad> findInactivos() throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad WHERE activo = false ORDER BY orden";
        
        List<NivelDificultad> niveles = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                niveles.add(mapResultSetToNivelDificultad(rs));
            }
        }
        
        return niveles;
    }
    
    /**
     * Listar niveles de dificultad por rango de orden
     */
    public List<NivelDificultad> findByRangoOrden(int ordenMinimo, int ordenMaximo) throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad " +
                    "WHERE orden BETWEEN ? AND ? AND activo = true " +
                    "ORDER BY orden";
        
        List<NivelDificultad> niveles = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ordenMinimo);
            stmt.setInt(2, ordenMaximo);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                niveles.add(mapResultSetToNivelDificultad(rs));
            }
        }
        
        return niveles;
    }
    
    /**
     * Obtener el nivel más fácil (menor orden)
     */
    public NivelDificultad getNivelMasFacil() throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad WHERE activo = true " +
                    "ORDER BY orden LIMIT 1";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNivelDificultad(rs);
            }
            return null;
        }
    }
    
    /**
     * Obtener el nivel más difícil (mayor orden)
     */
    public NivelDificultad getNivelMasDificil() throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad WHERE activo = true " +
                    "ORDER BY orden DESC LIMIT 1";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNivelDificultad(rs);
            }
            return null;
        }
    }
    
    /**
     * Obtener el siguiente nivel de dificultad
     */
    public NivelDificultad getSiguienteNivel(int ordenActual) throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad " +
                    "WHERE orden > ? AND activo = true " +
                    "ORDER BY orden LIMIT 1";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ordenActual);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNivelDificultad(rs);
            }
            return null;
        }
    }
    
    /**
     * Obtener el nivel anterior de dificultad
     */
    public NivelDificultad getNivelAnterior(int ordenActual) throws SQLException {
        String sql = "SELECT * FROM niveles_dificultad " +
                    "WHERE orden < ? AND activo = true " +
                    "ORDER BY orden DESC LIMIT 1";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ordenActual);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNivelDificultad(rs);
            }
            return null;
        }
    }
    
    /**
     * Verificar si existe un nivel con el mismo nombre
     */
    public boolean existeNombre(String nombre, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM niveles_dificultad " +
                    "WHERE nombre_nivel = ? AND id_nivel_dificultad != ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
    
    /**
     * Verificar si existe un nivel con el mismo orden
     */
    public boolean existeOrden(int orden, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM niveles_dificultad " +
                    "WHERE orden = ? AND id_nivel_dificultad != ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orden);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
    
    /**
     * Contar cuántos escenarios usan este nivel de dificultad
     * TEMPORALMENTE DESHABILITADO - La funcionalidad de escenarios se implementará después
     */
    public int contarEscenarios(int idNivel) throws SQLException {
        // Retornar 0 hasta implementar escenarios
        return 0;
    }
    
    /**
     * Mapear ResultSet a objeto NivelDificultad
     */
    private NivelDificultad mapResultSetToNivelDificultad(ResultSet rs) throws SQLException {
        NivelDificultad nivel = new NivelDificultad();
        
        nivel.setIdNivelDificultad(rs.getInt("id_nivel_dificultad"));
        nivel.setNombreNivel(rs.getString("nombre_nivel"));
        nivel.setOrden(rs.getInt("orden"));
        nivel.setDescripcion(rs.getString("descripcion"));
        nivel.setColorHex(rs.getString("color_hex"));
        nivel.setIcono(rs.getString("icono"));
        nivel.setActivo(rs.getBoolean("activo"));
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            nivel.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        Timestamp fechaActualizacion = rs.getTimestamp("fecha_actualizacion");
        if (fechaActualizacion != null) {
            nivel.setFechaActualizacion(fechaActualizacion.toLocalDateTime());
        }
        
        return nivel;
    }
}
