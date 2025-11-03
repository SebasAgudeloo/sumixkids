package com.sumixkids.dao;

import com.sumixkids.model.EstudianteAcompañante;
import com.sumixkids.model.Estudiante;
import com.sumixkids.model.Acompañante;
import com.sumixkids.config.DatabaseManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar operaciones de la tabla transaccional estudiante_acompañante
 */
public class EstudianteAcompañanteDAO {
    
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final AcompañanteDAO acompañanteDAO = new AcompañanteDAO();
    
    /**
     * Crear una nueva relación estudiante-acompañante
     */
    public int crear(EstudianteAcompañante relacion) throws SQLException {
        String sql = "INSERT INTO estudiante_acompañante (estudiante_id, acompañante_id, tipo_relacion, " +
                    "fecha_vinculacion, activo) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, relacion.getEstudianteId());
            stmt.setInt(2, relacion.getAcompañanteId());
            stmt.setString(3, relacion.getTipoRelacion() != null ? 
                          relacion.getTipoRelacion().name() : "SECUNDARIO");
            stmt.setTimestamp(4, Timestamp.valueOf(relacion.getFechaVinculacion() != null ? 
                             relacion.getFechaVinculacion() : LocalDateTime.now()));
            stmt.setBoolean(5, relacion.isActivo());
            
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
     * Buscar relaciones por estudiante ID
     */
    public List<EstudianteAcompañante> findByEstudianteId(int estudianteId) throws SQLException {
        String sql = "SELECT * FROM estudiante_acompañante WHERE estudiante_id = ? AND activo = 1 " +
                    "ORDER BY CASE WHEN tipo_relacion = 'PRINCIPAL' THEN 1 " +
                    "WHEN tipo_relacion = 'SECUNDARIO' THEN 2 ELSE 3 END";
        
        List<EstudianteAcompañante> relaciones = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, estudianteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                EstudianteAcompañante relacion = mapResultSetToRelacion(rs);
                relaciones.add(relacion);
            }
        }
        
        return relaciones;
    }
    
    /**
     * Buscar relaciones por acompañante ID
     */
    public List<EstudianteAcompañante> findByAcompañanteId(int acompañanteId) throws SQLException {
        String sql = "SELECT * FROM estudiante_acompañante WHERE acompañante_id = ? AND activo = 1 " +
                    "ORDER BY tipo_relacion";
        
        List<EstudianteAcompañante> relaciones = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, acompañanteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                EstudianteAcompañante relacion = mapResultSetToRelacion(rs);
                relaciones.add(relacion);
            }
        }
        
        return relaciones;
    }
    
    /**
     * Buscar estudiantes de un acompañante (con información completa)
     */
    public List<Estudiante> findEstudiantesByAcompañanteId(int acompañanteId) throws SQLException {
        String sql = "SELECT e.*, u.nombres, u.apellidos, u.email " +
                    "FROM estudiante_acompañante ea " +
                    "JOIN estudiantes e ON ea.estudiante_id = e.id " +
                    "JOIN usuarios u ON e.usuario_id = u.id " +
                    "WHERE ea.acompañante_id = ? AND ea.activo = 1 " +
                    "ORDER BY ea.tipo_relacion, u.apellidos, u.nombres";
        
        List<Estudiante> estudiantes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, acompañanteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Estudiante estudiante = estudianteDAO.mapResultSetToEstudiante(rs);
                
                // Crear objeto Usuario con datos del join
                com.sumixkids.model.Usuario usuario = new com.sumixkids.model.Usuario();
                usuario.setId(estudiante.getUsuarioId());
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setEmail(rs.getString("email"));
                
                estudiante.setUsuario(usuario);
                estudiantes.add(estudiante);
            }
        }
        
        return estudiantes;
    }
    
    /**
     * Buscar acompañantes de un estudiante (con información completa)
     */
    public List<Acompañante> findAcompañantesByEstudianteId(int estudianteId) throws SQLException {
        String sql = "SELECT a.*, u.nombres, u.apellidos, u.email " +
                    "FROM estudiante_acompañante ea " +
                    "JOIN acompañantes a ON ea.acompañante_id = a.id " +
                    "JOIN usuarios u ON a.usuario_id = u.id " +
                    "WHERE ea.estudiante_id = ? AND ea.activo = 1 " +
                    "ORDER BY CASE WHEN ea.tipo_relacion = 'PRINCIPAL' THEN 1 " +
                    "WHEN ea.tipo_relacion = 'SECUNDARIO' THEN 2 ELSE 3 END";
        
        List<Acompañante> acompañantes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, estudianteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Acompañante acompañante = acompañanteDAO.mapResultSetToAcompañante(rs);
                
                // Crear objeto Usuario con datos del join
                com.sumixkids.model.Usuario usuario = new com.sumixkids.model.Usuario();
                usuario.setId(acompañante.getUsuarioId());
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setEmail(rs.getString("email"));
                
                acompañante.setUsuario(usuario);
                acompañantes.add(acompañante);
            }
        }
        
        return acompañantes;
    }
    
    /**
     * Buscar acompañante principal de un estudiante
     */
    public Acompañante findAcompañantePrincipal(int estudianteId) throws SQLException {
        String sql = "SELECT a.*, u.nombres, u.apellidos, u.email " +
                    "FROM estudiante_acompañante ea " +
                    "JOIN acompañantes a ON ea.acompañante_id = a.id " +
                    "JOIN usuarios u ON a.usuario_id = u.id " +
                    "WHERE ea.estudiante_id = ? AND ea.tipo_relacion = 'PRINCIPAL' AND ea.activo = 1";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, estudianteId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Acompañante acompañante = acompañanteDAO.mapResultSetToAcompañante(rs);
                
                com.sumixkids.model.Usuario usuario = new com.sumixkids.model.Usuario();
                usuario.setId(acompañante.getUsuarioId());
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setEmail(rs.getString("email"));
                
                acompañante.setUsuario(usuario);
                return acompañante;
            }
            return null;
        }
    }
    
    /**
     * Verificar si ya existe una relación específica
     */
    public boolean existeRelacion(int estudianteId, int acompañanteId, 
                                 EstudianteAcompañante.TipoRelacion tipoRelacion) throws SQLException {
        String sql = "SELECT COUNT(*) FROM estudiante_acompañante " +
                    "WHERE estudiante_id = ? AND acompañante_id = ? AND tipo_relacion = ? AND activo = 1";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, estudianteId);
            stmt.setInt(2, acompañanteId);
            stmt.setString(3, tipoRelacion.name());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
    
    /**
     * Desactivar una relación (soft delete)
     */
    public boolean desactivarRelacion(int id) throws SQLException {
        String sql = "UPDATE estudiante_acompañante SET activo = 0 WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Reactivar una relación
     */
    public boolean activarRelacion(int id) throws SQLException {
        String sql = "UPDATE estudiante_acompañante SET activo = 1 WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Mapear ResultSet a objeto EstudianteAcompañante
     */
    private EstudianteAcompañante mapResultSetToRelacion(ResultSet rs) throws SQLException {
        EstudianteAcompañante relacion = new EstudianteAcompañante();
        
        relacion.setId(rs.getInt("id"));
        relacion.setEstudianteId(rs.getInt("estudiante_id"));
        relacion.setAcompañanteId(rs.getInt("acompañante_id"));
        
        String tipo = rs.getString("tipo_relacion");
        if (tipo != null) {
            try {
                relacion.setTipoRelacion(EstudianteAcompañante.TipoRelacion.valueOf(tipo));
            } catch (IllegalArgumentException e) {
                relacion.setTipoRelacion(EstudianteAcompañante.TipoRelacion.SECUNDARIO);
            }
        }
        
        Timestamp fechaVinculacion = rs.getTimestamp("fecha_vinculacion");
        if (fechaVinculacion != null) {
            relacion.setFechaVinculacion(fechaVinculacion.toLocalDateTime());
        }
        
        relacion.setActivo(rs.getBoolean("activo"));
        
        return relacion;
    }
}