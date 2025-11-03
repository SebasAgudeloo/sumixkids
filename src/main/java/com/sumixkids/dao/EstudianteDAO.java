package com.sumixkids.dao;

import com.sumixkids.model.Estudiante;
import com.sumixkids.model.Usuario;
import com.sumixkids.config.DatabaseManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar operaciones de la entidad Estudiante
 */
public class EstudianteDAO {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    /**
     * Crear un nuevo estudiante
     */
    public int crear(Estudiante estudiante) throws SQLException {
        String sql = "INSERT INTO estudiantes (usuario_id, numero_estudiante, grado_actual, seccion, " +
                    "promedio_general, nivel_lectura, nivel_matematicas, fecha_ingreso, " +
                    "tutor_principal_id, estado_academico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, estudiante.getUsuarioId());
            stmt.setString(2, estudiante.getNumeroEstudiante());
            stmt.setString(3, estudiante.getGradoActual());
            stmt.setString(4, estudiante.getSeccion());
            
            if (estudiante.getPromedioGeneral() != null) {
                stmt.setBigDecimal(5, estudiante.getPromedioGeneral());
            } else {
                stmt.setNull(5, Types.DECIMAL);
            }
            
            stmt.setString(6, estudiante.getNivelLectura());
            stmt.setString(7, estudiante.getNivelMatematicas());
            
            if (estudiante.getFechaIngreso() != null) {
                stmt.setDate(8, Date.valueOf(estudiante.getFechaIngreso()));
            } else {
                stmt.setDate(8, Date.valueOf(LocalDate.now()));
            }
            
            if (estudiante.getTutorPrincipalId() != null) {
                stmt.setInt(9, estudiante.getTutorPrincipalId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }
            
            stmt.setString(10, estudiante.getEstadoAcademico() != null ? 
                          estudiante.getEstadoAcademico().name() : "ACTIVO");
            
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
     * Buscar estudiante por ID de usuario
     */
    public Estudiante findByUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM estudiantes WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Estudiante estudiante = mapResultSetToEstudiante(rs);
                // Cargar información del usuario
                estudiante.setUsuario(usuarioDAO.findById(usuarioId));
                return estudiante;
            }
            return null;
        }
    }
    
    /**
     * Buscar estudiante por ID
     */
    public Estudiante findById(int id) throws SQLException {
        String sql = "SELECT * FROM estudiantes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Estudiante estudiante = mapResultSetToEstudiante(rs);
                // Cargar información del usuario
                estudiante.setUsuario(usuarioDAO.findById(estudiante.getUsuarioId()));
                return estudiante;
            }
            return null;
        }
    }
    
    /**
     * Buscar estudiante por número de estudiante
     */
    public Estudiante findByNumeroEstudiante(String numeroEstudiante) throws SQLException {
        String sql = "SELECT * FROM estudiantes WHERE numero_estudiante = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroEstudiante);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Estudiante estudiante = mapResultSetToEstudiante(rs);
                estudiante.setUsuario(usuarioDAO.findById(estudiante.getUsuarioId()));
                return estudiante;
            }
            return null;
        }
    }
    
    /**
     * Listar todos los estudiantes activos
     */
    public List<Estudiante> findAllActivos() throws SQLException {
        String sql = "SELECT e.*, u.nombres, u.apellidos, u.email " +
                    "FROM estudiantes e " +
                    "JOIN usuarios u ON e.usuario_id = u.id " +
                    "WHERE e.estado_academico = 'ACTIVO' " +
                    "ORDER BY e.grado_actual, u.apellidos, u.nombres";
        
        List<Estudiante> estudiantes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Estudiante estudiante = mapResultSetToEstudiante(rs);
                
                // Crear objeto Usuario con datos del join
                Usuario usuario = new Usuario();
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
     * Listar estudiantes por grado
     */
    public List<Estudiante> findByGrado(String grado) throws SQLException {
        String sql = "SELECT e.*, u.nombres, u.apellidos, u.email " +
                    "FROM estudiantes e " +
                    "JOIN usuarios u ON e.usuario_id = u.id " +
                    "WHERE e.grado_actual = ? AND e.estado_academico = 'ACTIVO' " +
                    "ORDER BY e.seccion, u.apellidos, u.nombres";
        
        List<Estudiante> estudiantes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, grado);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Estudiante estudiante = mapResultSetToEstudiante(rs);
                
                Usuario usuario = new Usuario();
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
     * Actualizar información del estudiante
     */
    public boolean actualizar(Estudiante estudiante) throws SQLException {
        String sql = "UPDATE estudiantes SET numero_estudiante = ?, grado_actual = ?, seccion = ?, " +
                    "promedio_general = ?, nivel_lectura = ?, nivel_matematicas = ?, " +
                    "tutor_principal_id = ?, estado_academico = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estudiante.getNumeroEstudiante());
            stmt.setString(2, estudiante.getGradoActual());
            stmt.setString(3, estudiante.getSeccion());
            
            if (estudiante.getPromedioGeneral() != null) {
                stmt.setBigDecimal(4, estudiante.getPromedioGeneral());
            } else {
                stmt.setNull(4, Types.DECIMAL);
            }
            
            stmt.setString(5, estudiante.getNivelLectura());
            stmt.setString(6, estudiante.getNivelMatematicas());
            
            if (estudiante.getTutorPrincipalId() != null) {
                stmt.setInt(7, estudiante.getTutorPrincipalId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            stmt.setString(8, estudiante.getEstadoAcademico().name());
            stmt.setInt(9, estudiante.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Generar número de estudiante único
     */
    public String generarNumeroEstudiante() throws SQLException {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(numero_estudiante, 4) AS UNSIGNED)), 0) + 1 as siguiente " +
                    "FROM estudiantes WHERE numero_estudiante LIKE 'EST%'";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int siguiente = rs.getInt("siguiente");
                return String.format("EST%04d", siguiente);
            }
            return "EST0001";
        }
    }
    
    /**
     * Verificar si existe un número de estudiante
     */
    public boolean existeNumeroEstudiante(String numeroEstudiante) throws SQLException {
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE numero_estudiante = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroEstudiante);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
    
    /**
     * Mapear ResultSet a objeto Estudiante (método público para uso en otros DAOs)
     */
    public Estudiante mapResultSetToEstudiante(ResultSet rs) throws SQLException {
        Estudiante estudiante = new Estudiante();
        
        estudiante.setId(rs.getInt("id"));
        estudiante.setUsuarioId(rs.getInt("usuario_id"));
        estudiante.setNumeroEstudiante(rs.getString("numero_estudiante"));
        estudiante.setGradoActual(rs.getString("grado_actual"));
        estudiante.setSeccion(rs.getString("seccion"));
        
        BigDecimal promedio = rs.getBigDecimal("promedio_general");
        if (!rs.wasNull()) {
            estudiante.setPromedioGeneral(promedio);
        }
        
        estudiante.setNivelLectura(rs.getString("nivel_lectura"));
        estudiante.setNivelMatematicas(rs.getString("nivel_matematicas"));
        
        Date fechaIngreso = rs.getDate("fecha_ingreso");
        if (fechaIngreso != null) {
            estudiante.setFechaIngreso(fechaIngreso.toLocalDate());
        }
        
        int tutorId = rs.getInt("tutor_principal_id");
        if (!rs.wasNull()) {
            estudiante.setTutorPrincipalId(tutorId);
        }
        
        String estado = rs.getString("estado_academico");
        if (estado != null) {
            try {
                estudiante.setEstadoAcademico(Estudiante.EstadoAcademico.valueOf(estado));
            } catch (IllegalArgumentException e) {
                estudiante.setEstadoAcademico(Estudiante.EstadoAcademico.ACTIVO);
            }
        }
        
        return estudiante;
    }
}
