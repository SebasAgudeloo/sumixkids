package com.sumixkids.dao;

import com.sumixkids.model.Acompañante;
import com.sumixkids.model.Usuario;
import com.sumixkids.config.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejar operaciones de la entidad Acompañante
 */
public class AcompañanteDAO {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    /**
     * Crear un nuevo acompañante
     */
    public int crear(Acompañante acompañante) throws SQLException {
        String sql = "INSERT INTO acompañantes (usuario_id, relacion_estudiante, telefono_principal, " +
                    "telefono_secundario, direccion, ocupacion, empresa_trabajo, nivel_educativo, " +
                    "recibir_notificaciones, horario_contacto, autorizacion_recoger) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, acompañante.getUsuarioId());
            stmt.setString(2, acompañante.getRelacionEstudiante() != null ? 
                          acompañante.getRelacionEstudiante().name() : "OTRO");
            stmt.setString(3, acompañante.getTelefonoPrincipal());
            stmt.setString(4, acompañante.getTelefonoSecundario());
            stmt.setString(5, acompañante.getDireccion());
            stmt.setString(6, acompañante.getOcupacion());
            stmt.setString(7, acompañante.getEmpresaTrabajo());
            stmt.setString(8, acompañante.getNivelEducativo());
            stmt.setBoolean(9, acompañante.isRecibirNotificaciones());
            stmt.setString(10, acompañante.getHorarioContacto());
            stmt.setBoolean(11, acompañante.isAutorizacionRecoger());
            
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
     * Buscar acompañante por ID de usuario
     */
    public Acompañante findByUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM acompañantes WHERE usuario_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Acompañante acompañante = mapResultSetToAcompañante(rs);
                // Cargar información del usuario
                acompañante.setUsuario(usuarioDAO.findById(usuarioId));
                return acompañante;
            }
            return null;
        }
    }
    
    /**
     * Buscar acompañante por ID
     */
    public Acompañante findById(int id) throws SQLException {
        String sql = "SELECT * FROM acompañantes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Acompañante acompañante = mapResultSetToAcompañante(rs);
                // Cargar información del usuario
                acompañante.setUsuario(usuarioDAO.findById(acompañante.getUsuarioId()));
                return acompañante;
            }
            return null;
        }
    }
    
    /**
     * Listar todos los acompañantes
     */
    public List<Acompañante> findAll() throws SQLException {
        String sql = "SELECT a.*, u.nombres, u.apellidos, u.email " +
                    "FROM acompañantes a " +
                    "JOIN usuarios u ON a.usuario_id = u.id " +
                    "ORDER BY u.apellidos, u.nombres";
        
        List<Acompañante> acompañantes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Acompañante acompañante = mapResultSetToAcompañante(rs);
                
                // Crear objeto Usuario con datos del join
                Usuario usuario = new Usuario();
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
     * Listar acompañantes por tipo de relación
     */
    public List<Acompañante> findByRelacion(Acompañante.RelacionEstudiante relacion) throws SQLException {
        String sql = "SELECT a.*, u.nombres, u.apellidos, u.email " +
                    "FROM acompañantes a " +
                    "JOIN usuarios u ON a.usuario_id = u.id " +
                    "WHERE a.relacion_estudiante = ? " +
                    "ORDER BY u.apellidos, u.nombres";
        
        List<Acompañante> acompañantes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, relacion.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Acompañante acompañante = mapResultSetToAcompañante(rs);
                
                Usuario usuario = new Usuario();
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
     * Actualizar información del acompañante
     */
    public boolean actualizar(Acompañante acompañante) throws SQLException {
        String sql = "UPDATE acompañantes SET relacion_estudiante = ?, telefono_principal = ?, " +
                    "telefono_secundario = ?, direccion = ?, ocupacion = ?, empresa_trabajo = ?, " +
                    "nivel_educativo = ?, recibir_notificaciones = ?, horario_contacto = ?, " +
                    "autorizacion_recoger = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, acompañante.getRelacionEstudiante().name());
            stmt.setString(2, acompañante.getTelefonoPrincipal());
            stmt.setString(3, acompañante.getTelefonoSecundario());
            stmt.setString(4, acompañante.getDireccion());
            stmt.setString(5, acompañante.getOcupacion());
            stmt.setString(6, acompañante.getEmpresaTrabajo());
            stmt.setString(7, acompañante.getNivelEducativo());
            stmt.setBoolean(8, acompañante.isRecibirNotificaciones());
            stmt.setString(9, acompañante.getHorarioContacto());
            stmt.setBoolean(10, acompañante.isAutorizacionRecoger());
            stmt.setInt(11, acompañante.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Buscar acompañantes que reciben notificaciones
     */
    public List<Acompañante> findByRecibeNotificaciones(boolean recibe) throws SQLException {
        String sql = "SELECT a.*, u.nombres, u.apellidos, u.email " +
                    "FROM acompañantes a " +
                    "JOIN usuarios u ON a.usuario_id = u.id " +
                    "WHERE a.recibir_notificaciones = ? " +
                    "ORDER BY u.apellidos, u.nombres";
        
        List<Acompañante> acompañantes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, recibe);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Acompañante acompañante = mapResultSetToAcompañante(rs);
                
                Usuario usuario = new Usuario();
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
     * Mapear ResultSet a objeto Acompañante (método público para uso en otros DAOs)
     */
    public Acompañante mapResultSetToAcompañante(ResultSet rs) throws SQLException {
        Acompañante acompañante = new Acompañante();
        
        acompañante.setId(rs.getInt("id"));
        acompañante.setUsuarioId(rs.getInt("usuario_id"));
        
        String relacion = rs.getString("relacion_estudiante");
        if (relacion != null) {
            try {
                acompañante.setRelacionEstudiante(Acompañante.RelacionEstudiante.valueOf(relacion));
            } catch (IllegalArgumentException e) {
                acompañante.setRelacionEstudiante(Acompañante.RelacionEstudiante.OTRO);
            }
        }
        
        acompañante.setTelefonoPrincipal(rs.getString("telefono_principal"));
        acompañante.setTelefonoSecundario(rs.getString("telefono_secundario"));
        acompañante.setDireccion(rs.getString("direccion"));
        acompañante.setOcupacion(rs.getString("ocupacion"));
        acompañante.setEmpresaTrabajo(rs.getString("empresa_trabajo"));
        acompañante.setNivelEducativo(rs.getString("nivel_educativo"));
        acompañante.setRecibirNotificaciones(rs.getBoolean("recibir_notificaciones"));
        acompañante.setHorarioContacto(rs.getString("horario_contacto"));
        acompañante.setAutorizacionRecoger(rs.getBoolean("autorizacion_recoger"));
        
        return acompañante;
    }
}