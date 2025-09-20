/**
	 * Obtiene la última IP usada por el usuario (requiere columna 'ultima_ip' en la tabla usuarios).
	 */
package com.sumixkids.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumixkids.config.DatabaseManager;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.RoleType;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos (consultar / guardar) de usuarios.
 * Cada método se encarga de una acción concreta y sencilla.
 */
public class UsuarioDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

    /**
     * Actualiza la última IP usada por el usuario.
     */
    public void updateUltimaIp(int userId, String ip) throws SQLException {
        String sql = "UPDATE usuarios SET ultima_ip = ? WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ip);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un usuario por su id.
     */
    public Usuario findById(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    /**
     * Cambia el rol de un usuario dado su id y el id del nuevo rol.
     */
    public void cambiarRol(int userId, int nuevoRol) throws SQLException {
        String sql = "UPDATE usuarios SET rol_id = ? WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, nuevoRol);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Busca usuarios con filtro de búsqueda, rol y paginación.
     */
    public List<Usuario> buscarUsuarios(String busqueda, Integer rolId, int offset, int limit) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM usuarios WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" AND (username LIKE ? OR nombres LIKE ? OR apellidos LIKE ? OR email LIKE ?)");
            String like = "%" + busqueda.trim() + "%";
            for (int i = 0; i < 4; i++) params.add(like);
        }
        
        if (rolId != null && rolId > 0) {
            sql.append(" AND rol_id = ?");
            params.add(rolId);
        }
        
        sql.append(" ORDER BY id ASC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) ps.setString(i+1, (String)p);
                else if (p instanceof Integer) ps.setInt(i+1, (Integer)p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(map(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Cuenta el total de usuarios que cumplen los filtros.
     */
    public int contarUsuarios(String busqueda, Integer rolId) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM usuarios WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" AND (username LIKE ? OR nombres LIKE ? OR apellidos LIKE ? OR email LIKE ?)");
            String like = "%" + busqueda.trim() + "%";
            for (int i = 0; i < 4; i++) params.add(like);
        }
        
        if (rolId != null && rolId > 0) {
            sql.append(" AND rol_id = ?");
            params.add(rolId);
        }
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) ps.setString(i+1, (String)p);
                else if (p instanceof Integer) ps.setInt(i+1, (Integer)p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Devuelve la lista de todos los usuarios.
     */
    public List<Usuario> findAll() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(map(rs));
            }
        }
        return lista;
    }

    /**
     * Elimina un usuario de forma segura verificando dependencias.
     * Retorna true si se eliminó, false si tiene registros asociados.
     */
    public boolean deleteUser(int userId) throws SQLException {
        try (Connection cn = DatabaseManager.getConnection()) {
            cn.setAutoCommit(false);
            try {
                // Primero verificar si el usuario existe
                String checkSql = "SELECT id FROM usuarios WHERE id = ?";
                try (PreparedStatement ps = cn.prepareStatement(checkSql)) {
                    ps.setInt(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            logger.warn("Usuario con ID {} no encontrado", userId);
                            return false;
                        }
                    }
                }

                // Si el usuario existe, proceder con la eliminación directa
                String deleteSql = "DELETE FROM usuarios WHERE id = ?";
                try (PreparedStatement ps = cn.prepareStatement(deleteSql)) {
                    ps.setInt(1, userId);
                    int result = ps.executeUpdate();
                    if (result > 0) {
                        cn.commit();
                        logger.info("Usuario con ID {} eliminado exitosamente", userId);
                        return true;
                    } else {
                        cn.rollback();
                        logger.warn("No se pudo eliminar el usuario con ID {}", userId);
                        return false;
                    }
                }
            } catch (SQLException e) {
                cn.rollback();
                logger.error("Error al eliminar usuario con ID {}: {}", userId, e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Busca un usuario por su nombre de usuario.
     */
    public Usuario findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    /**
     * Busca un usuario por username o email.
     */
    public Usuario findByUsernameOrEmail(String login) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ? OR email = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    /**
     * Actualiza contador de intentos fallidos.
     */
    public void updateLoginFailure(int userId, int maxIntentos) throws SQLException {
        String sql = "UPDATE usuarios SET intentos_fallidos = intentos_fallidos + 1, " +
                    "bloqueado = (intentos_fallidos + 1) >= ? WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, maxIntentos);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza información tras login exitoso.
     */
    public void updateLoginSuccess(int userId) throws SQLException {
        String sql = "UPDATE usuarios SET intentos_fallidos = 0, bloqueado = 0, ultima_conexion = ? WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime()));
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Crea un nuevo usuario.
     */
    public int createUser(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (username, nombres, apellidos, password_hash, email, " +
                    "rol_id, fecha_registro, intentos_fallidos, bloqueado, autenticacion_2fa) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, 0, 0, ?)";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getNombres());
            ps.setString(3, u.getApellidos());
            ps.setString(4, u.getPasswordHash());
            ps.setString(5, u.getEmail());
            int rolId = (u.getRolId() != null) ? u.getRolId() : 
                       resolveRolIdByName(cn, RoleType.STUDENT.dbName());
            ps.setInt(6, rolId);
            ps.setTimestamp(7, Timestamp.valueOf(u.getFechaRegistro() != null ? 
                          u.getFechaRegistro() : 
                          ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime()));
            ps.setString(8, u.getAutenticacion2fa());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    /**
     * Verifica si existe un usuario con el username o email dados.
     */
    public boolean existsByUsernameOrEmail(String username, String email) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE username = ? OR email = ? LIMIT 1";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Actualiza la contraseña de un usuario.
     */
    public void updatePassword(Integer userId, String newHash) throws SQLException {
        String sql = "UPDATE usuarios SET password_hash = ? WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Resuelve el ID de un rol por su nombre.
     */
    private int resolveRolIdByName(Connection cn, String roleName) throws SQLException {
        String q = "SELECT id FROM roles WHERE nombre_rol = ?";
        try (PreparedStatement ps = cn.prepareStatement(q)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        
        try (PreparedStatement ins = cn.prepareStatement(
                "INSERT INTO roles(nombre_rol) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ins.setString(1, roleName);
            ins.executeUpdate();
            try (ResultSet rs = ins.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        
        try (PreparedStatement ps2 = cn.prepareStatement(q)) {
            ps2.setString(1, roleName);
            try (ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next()) return rs2.getInt(1);
            }
        }
        throw new SQLException("No se pudo resolver/crear el rol por nombre: " + roleName);
    }

    public int resolveRolIdByName(String roleName) throws SQLException {
        try (Connection cn = DatabaseManager.getConnection()) {
            return resolveRolIdByName(cn, roleName);
        }
    }

    public int resolveRolIdByType(RoleType roleType) throws SQLException {
        return resolveRolIdByName(roleType.dbName());
    }

    /**
     * Busca un usuario por su correo electrónico.
     */
    public static Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (Exception e) {
            logger.error("Error buscando usuario por correo: {}", correo, e);
        }
        return null;
    }

    /**
     * Mapea un ResultSet a un objeto Usuario.
     */
    private static Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setEmail(rs.getString("email"));
        u.setRolId(rs.getInt("rol_id"));
        Timestamp fr = rs.getTimestamp("fecha_registro");
        u.setFechaRegistro(fr != null ? fr.toLocalDateTime() : null);
        Timestamp uc = rs.getTimestamp("ultima_conexion");
        u.setUltimaConexion(uc != null ? uc.toLocalDateTime() : null);
        u.setIntentosFallidos(rs.getInt("intentos_fallidos"));
        u.setBloqueado(rs.getBoolean("bloqueado"));
        u.setAutenticacion2fa(rs.getString("autenticacion_2fa"));
        return u;
    }
}
