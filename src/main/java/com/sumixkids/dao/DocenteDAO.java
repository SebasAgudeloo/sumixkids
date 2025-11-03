package com.sumixkids.dao;

import com.sumixkids.model.Docente;
import com.sumixkids.model.Usuario;
import com.sumixkids.config.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar operaciones de la entidad Docente
 */
public class DocenteDAO {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    /**
     * Crear un nuevo docente
     */
    public int crear(Docente docente) throws SQLException {
        String sql = "INSERT INTO docentes (usuario_id, numero_empleado, especialidad, grados_asignados, " +
                    "anos_experiencia, fecha_contratacion, titulo_academico, institucion_titulo, " +
                    "telefono_trabajo, horario_disponible, estado_laboral) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, docente.getUsuarioId());
            stmt.setString(2, docente.getNumeroEmpleado());
            stmt.setString(3, docente.getEspecialidad());
            stmt.setString(4, docente.getGradosAsignados());
            
            if (docente.getAnosExperiencia() != null) {
                stmt.setInt(5, docente.getAnosExperiencia());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            if (docente.getFechaContratacion() != null) {
                stmt.setDate(6, Date.valueOf(docente.getFechaContratacion()));
            } else {
                stmt.setDate(6, Date.valueOf(LocalDate.now()));
            }
            
            stmt.setString(7, docente.getTituloAcademico());
            stmt.setString(8, docente.getInstitucionTitulo());
            stmt.setString(9, docente.getTelefonoTrabajo());
            stmt.setString(10, docente.getHorarioDisponible());
            stmt.setString(11, docente.getEstadoLaboral() != null ? 
                          docente.getEstadoLaboral().name() : "ACTIVO");
            
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
     * Buscar docente por ID de usuario
     */
    public Docente findByUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM docentes WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Docente docente = mapResultSetToDocente(rs);
                // Cargar información del usuario
                docente.setUsuario(usuarioDAO.findById(usuarioId));
                return docente;
            }
            return null;
        }
    }
    
    /**
     * Buscar docente por ID
     */
    public Docente findById(int id) throws SQLException {
        String sql = "SELECT * FROM docentes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Docente docente = mapResultSetToDocente(rs);
                // Cargar información del usuario
                docente.setUsuario(usuarioDAO.findById(docente.getUsuarioId()));
                return docente;
            }
            return null;
        }
    }
    
    /**
     * Buscar docente por número de empleado
     */
    public Docente findByNumeroEmpleado(String numeroEmpleado) throws SQLException {
        String sql = "SELECT * FROM docentes WHERE numero_empleado = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroEmpleado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Docente docente = mapResultSetToDocente(rs);
                docente.setUsuario(usuarioDAO.findById(docente.getUsuarioId()));
                return docente;
            }
            return null;
        }
    }
    
    /**
     * Listar todos los docentes activos
     */
    public List<Docente> findAllActivos() throws SQLException {
        String sql = "SELECT d.*, u.nombres, u.apellidos, u.email " +
                    "FROM docentes d " +
                    "JOIN usuarios u ON d.usuario_id = u.id " +
                    "WHERE d.estado_laboral = 'ACTIVO' " +
                    "ORDER BY u.apellidos, u.nombres";
        
        List<Docente> docentes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Docente docente = mapResultSetToDocente(rs);
                
                // Crear objeto Usuario con datos del join
                Usuario usuario = new Usuario();
                usuario.setId(docente.getUsuarioId());
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setEmail(rs.getString("email"));
                
                docente.setUsuario(usuario);
                docentes.add(docente);
            }
        }
        
        return docentes;
    }
    
    /**
     * Listar docentes por especialidad
     */
    public List<Docente> findByEspecialidad(String especialidad) throws SQLException {
        String sql = "SELECT d.*, u.nombres, u.apellidos, u.email " +
                    "FROM docentes d " +
                    "JOIN usuarios u ON d.usuario_id = u.id " +
                    "WHERE d.especialidad = ? AND d.estado_laboral = 'ACTIVO' " +
                    "ORDER BY u.apellidos, u.nombres";
        
        List<Docente> docentes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, especialidad);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Docente docente = mapResultSetToDocente(rs);
                
                Usuario usuario = new Usuario();
                usuario.setId(docente.getUsuarioId());
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setEmail(rs.getString("email"));
                
                docente.setUsuario(usuario);
                docentes.add(docente);
            }
        }
        
        return docentes;
    }
    
    /**
     * Actualizar información del docente
     */
    public boolean actualizar(Docente docente) throws SQLException {
        String sql = "UPDATE docentes SET numero_empleado = ?, especialidad = ?, grados_asignados = ?, " +
                    "anos_experiencia = ?, titulo_academico = ?, institucion_titulo = ?, " +
                    "telefono_trabajo = ?, horario_disponible = ?, estado_laboral = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, docente.getNumeroEmpleado());
            stmt.setString(2, docente.getEspecialidad());
            stmt.setString(3, docente.getGradosAsignados());
            
            if (docente.getAnosExperiencia() != null) {
                stmt.setInt(4, docente.getAnosExperiencia());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, docente.getTituloAcademico());
            stmt.setString(6, docente.getInstitucionTitulo());
            stmt.setString(7, docente.getTelefonoTrabajo());
            stmt.setString(8, docente.getHorarioDisponible());
            stmt.setString(9, docente.getEstadoLaboral().name());
            stmt.setInt(10, docente.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Generar número de empleado único
     */
    public String generarNumeroEmpleado() throws SQLException {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(numero_empleado, 4) AS UNSIGNED)), 0) + 1 as siguiente " +
                    "FROM docentes WHERE numero_empleado LIKE 'DOC%'";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int siguiente = rs.getInt("siguiente");
                return String.format("DOC%04d", siguiente);
            }
            return "DOC0001";
        }
    }
    
    /**
     * Verificar si existe un número de empleado
     */
    public boolean existeNumeroEmpleado(String numeroEmpleado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM docentes WHERE numero_empleado = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroEmpleado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
    
    /**
     * Mapear ResultSet a objeto Docente
     */
    private Docente mapResultSetToDocente(ResultSet rs) throws SQLException {
        Docente docente = new Docente();
        
        docente.setId(rs.getInt("id"));
        docente.setUsuarioId(rs.getInt("usuario_id"));
        docente.setNumeroEmpleado(rs.getString("numero_empleado"));
        docente.setEspecialidad(rs.getString("especialidad"));
        docente.setGradosAsignados(rs.getString("grados_asignados"));
        
        int anos = rs.getInt("anos_experiencia");
        if (!rs.wasNull()) {
            docente.setAnosExperiencia(anos);
        }
        
        Date fechaContratacion = rs.getDate("fecha_contratacion");
        if (fechaContratacion != null) {
            docente.setFechaContratacion(fechaContratacion.toLocalDate());
        }
        
        docente.setTituloAcademico(rs.getString("titulo_academico"));
        docente.setInstitucionTitulo(rs.getString("institucion_titulo"));
        docente.setTelefonoTrabajo(rs.getString("telefono_trabajo"));
        docente.setHorarioDisponible(rs.getString("horario_disponible"));
        
        String estado = rs.getString("estado_laboral");
        if (estado != null) {
            try {
                docente.setEstadoLaboral(Docente.EstadoLaboral.valueOf(estado));
            } catch (IllegalArgumentException e) {
                docente.setEstadoLaboral(Docente.EstadoLaboral.ACTIVO);
            }
        }
        
        return docente;
    }
}