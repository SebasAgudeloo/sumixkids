package com.sumixkids.dao;

import com.sumixkids.model.GradoEscolar;
import com.sumixkids.config.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar operaciones de la entidad GradoEscolar
 */
public class GradoEscolarDAO {
    
    /**
     * Crear un nuevo grado escolar
     */
    public int crear(GradoEscolar grado) throws SQLException {
        String sql = "INSERT INTO grados_escolares (nombre_grado, nombre_grupo, nivel_educativo, " +
                    "descripcion, capacidad_maxima, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, grado.getNombreGrado());
            stmt.setString(2, grado.getNombreGrupo());
            stmt.setString(3, grado.getNivelEducativo() != null ? 
                          grado.getNivelEducativo().name() : "PRIMARIA");
            stmt.setString(4, grado.getDescripcion());
            
            if (grado.getCapacidadMaxima() != null) {
                stmt.setInt(5, grado.getCapacidadMaxima());
            } else {
                stmt.setInt(5, 30); // Valor por defecto
            }
            
            stmt.setBoolean(6, grado.isActivo());
            
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
     * Actualizar un grado escolar existente
     */
    public boolean actualizar(GradoEscolar grado) throws SQLException {
        String sql = "UPDATE grados_escolares SET nombre_grado = ?, nombre_grupo = ?, " +
                    "nivel_educativo = ?, descripcion = ?, capacidad_maxima = ?, activo = ? " +
                    "WHERE id_grado = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, grado.getNombreGrado());
            stmt.setString(2, grado.getNombreGrupo());
            stmt.setString(3, grado.getNivelEducativo() != null ? 
                          grado.getNivelEducativo().name() : "PRIMARIA");
            stmt.setString(4, grado.getDescripcion());
            
            if (grado.getCapacidadMaxima() != null) {
                stmt.setInt(5, grado.getCapacidadMaxima());
            } else {
                stmt.setInt(5, 30);
            }
            
            stmt.setBoolean(6, grado.isActivo());
            stmt.setInt(7, grado.getIdGrado());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Eliminar (desactivar) un grado escolar
     */
    public boolean eliminar(int idGrado) throws SQLException {
        String sql = "UPDATE grados_escolares SET activo = false WHERE id_grado = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idGrado);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Activar un grado escolar
     */
    public boolean activar(int idGrado) throws SQLException {
        String sql = "UPDATE grados_escolares SET activo = true WHERE id_grado = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idGrado);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Buscar grado escolar por ID
     */
    public GradoEscolar findById(int idGrado) throws SQLException {
        String sql = "SELECT * FROM grados_escolares WHERE id_grado = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idGrado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGradoEscolar(rs);
            }
            return null;
        }
    }
    
    /**
     * Eliminar permanentemente un grado escolar
     */
    public boolean delete(int idGrado) throws SQLException {
        String sql = "DELETE FROM grados_escolares WHERE id_grado = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idGrado);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Actualizar un grado escolar existente
     */
    public boolean update(GradoEscolar grado) throws SQLException {
        String sql = "UPDATE grados_escolares SET nombre_grado = ?, nombre_grupo = ?, " +
                    "nivel_educativo = ?, capacidad_maxima = ?, activo = ? WHERE id_grado = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, grado.getNombreGrado());
            stmt.setString(2, grado.getNombreGrupo());
            stmt.setString(3, grado.getNivelEducativo().name());
            stmt.setInt(4, grado.getCapacidadMaxima());
            stmt.setBoolean(5, grado.isActivo());
            stmt.setInt(6, grado.getIdGrado());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Listar todos los grados escolares
     */
    public List<GradoEscolar> findAll() throws SQLException {
        String sql = "SELECT * FROM grados_escolares ORDER BY nivel_educativo, nombre_grado, nombre_grupo";
        
        List<GradoEscolar> grados = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                grados.add(mapResultSetToGradoEscolar(rs));
            }
        }
        
        return grados;
    }
    
    /**
     * Listar todos los grados escolares activos
     */
    public List<GradoEscolar> findActivos() throws SQLException {
        String sql = "SELECT * FROM grados_escolares WHERE activo = true " +
                    "ORDER BY nivel_educativo, nombre_grado, nombre_grupo";
        
        List<GradoEscolar> grados = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                grados.add(mapResultSetToGradoEscolar(rs));
            }
        }
        
        return grados;
    }
    
    /**
     * Listar grados escolares por nivel educativo
     */
    public List<GradoEscolar> findByNivelEducativo(GradoEscolar.NivelEducativo nivel) throws SQLException {
        String sql = "SELECT * FROM grados_escolares WHERE nivel_educativo = ? AND activo = true " +
                    "ORDER BY nombre_grado, nombre_grupo";
        
        List<GradoEscolar> grados = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nivel.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                grados.add(mapResultSetToGradoEscolar(rs));
            }
        }
        
        return grados;
    }
    
    /**
     * Buscar grado escolar por nombre de grado y grupo
     */
    public GradoEscolar findByGradoYGrupo(String nombreGrado, String nombreGrupo) throws SQLException {
        String sql = "SELECT * FROM grados_escolares WHERE nombre_grado = ? AND nombre_grupo = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreGrado);
            stmt.setString(2, nombreGrupo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGradoEscolar(rs);
            }
            return null;
        }
    }
    
    /**
     * Verificar si existe un grado con el mismo nombre de grado y grupo
     */
    public boolean existeGradoYGrupo(String nombreGrado, String nombreGrupo, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM grados_escolares " +
                    "WHERE nombre_grado = ? AND nombre_grupo = ? AND id_grado != ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreGrado);
            stmt.setString(2, nombreGrupo);
            stmt.setInt(3, excludeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
    
    /**
     * Contar estudiantes asignados a un grado escolar
     */
    public int contarEstudiantes(int idGrado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM docente_estudiante " +
                    "WHERE id_grado_escolar = ? AND estado = 'ACTIVA'";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idGrado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    /**
     * Verificar si un grado está lleno (alcanzó capacidad máxima)
     */
    public boolean estaLleno(int idGrado) throws SQLException {
        GradoEscolar grado = findById(idGrado);
        if (grado == null) {
            return false;
        }
        
        int estudiantesActuales = contarEstudiantes(idGrado);
        return estudiantesActuales >= grado.getCapacidadMaxima();
    }
    
    /**
     * Mapear ResultSet a objeto GradoEscolar
     */
    private GradoEscolar mapResultSetToGradoEscolar(ResultSet rs) throws SQLException {
        GradoEscolar grado = new GradoEscolar();
        
        grado.setIdGrado(rs.getInt("id_grado"));
        grado.setNombreGrado(rs.getString("nombre_grado"));
        grado.setNombreGrupo(rs.getString("nombre_grupo"));
        
        String nivelEducativo = rs.getString("nivel_educativo");
        if (nivelEducativo != null) {
            try {
                grado.setNivelEducativo(GradoEscolar.NivelEducativo.valueOf(nivelEducativo));
            } catch (IllegalArgumentException e) {
                grado.setNivelEducativo(GradoEscolar.NivelEducativo.PRIMARIA);
            }
        }
        
        grado.setDescripcion(rs.getString("descripcion"));
        grado.setCapacidadMaxima(rs.getInt("capacidad_maxima"));
        grado.setActivo(rs.getBoolean("activo"));
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            grado.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        return grado;
    }
}
