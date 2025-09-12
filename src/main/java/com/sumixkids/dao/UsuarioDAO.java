package com.sumixkids.dao;

import com.sumixkids.config.DatabaseManager;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.RoleType;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Acceso a datos (consultar / guardar) de usuarios.
 * Cada método se encarga de una acción concreta y sencilla.
 */
public class UsuarioDAO {

	/**
	 * Convierte una fila obtenida de la base de datos en un objeto Usuario.
	 */
	private static Usuario map(ResultSet rs) throws SQLException {
		Usuario u = new Usuario();
		u.setId(rs.getInt("id"));
		u.setUsername(rs.getString("username"));
		u.setPasswordHash(rs.getString("password_hash"));
		u.setEmail(rs.getString("email"));
		u.setRolId(rs.getInt("rol_id"));
		Timestamp fr = rs.getTimestamp("fecha_registro");
		u.setFechaRegistro(fr != null ? fr.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null);
		Timestamp uc = rs.getTimestamp("ultima_conexion");
		u.setUltimaConexion(uc != null ? uc.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null);
		u.setIntentosFallidos(rs.getInt("intentos_fallidos"));
		u.setBloqueado(rs.getBoolean("bloqueado"));
		u.setAutenticacion2fa(rs.getString("autenticacion_2fa"));
		return u;
	}

	/** Busca un usuario por su nombre (username). */
	public Usuario findByUsername(String username) throws SQLException {
		String sql = "SELECT * FROM usuarios WHERE username = ?";
		try (Connection cn = DatabaseManager.getConnection();
			 PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return map(rs);
				return null;
			}
		}
	}

	/** Busca un usuario usando su nombre o su correo (sirve para el login flexible). */
	public Usuario findByUsernameOrEmail(String login) throws SQLException {
		String sql = "SELECT * FROM usuarios WHERE username = ? OR email = ?";
		try (Connection cn = DatabaseManager.getConnection();
		     PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, login);
			ps.setString(2, login);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return map(rs);
				return null;
			}
		}
	}

	/** Suma 1 intento de login fallido y bloquea si se pasó el límite permitido. */
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

	/** Limpia el conteo de intentos fallidos y guarda la hora de último acceso. */
	public void updateLoginSuccess(int userId) throws SQLException {
		String sql = "UPDATE usuarios SET intentos_fallidos = 0, bloqueado = 0, ultima_conexion = ? WHERE id = ?";
		try (Connection cn = DatabaseManager.getConnection();
			 PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
			ps.setInt(2, userId);
			ps.executeUpdate();
		}
	}

	/** Crea (guarda) un usuario nuevo en la base de datos. */
	public int createUser(Usuario u) throws SQLException {
		String sql = "INSERT INTO usuarios (username, password_hash, email, rol_id, fecha_registro, intentos_fallidos, bloqueado, autenticacion_2fa) " +
					 "VALUES (?, ?, ?, ?, ?, 0, 0, ?)";
		try (Connection cn = DatabaseManager.getConnection();
			 PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPasswordHash());
			ps.setString(3, u.getEmail());
			int rolId = (u.getRolId() != null) ? u.getRolId() : resolveRolIdByName(cn, RoleType.STUDENT.dbName());
			ps.setInt(4, rolId);
			ps.setTimestamp(5, Timestamp.valueOf(u.getFechaRegistro() != null ? u.getFechaRegistro() : LocalDateTime.now()));
			ps.setString(6, u.getAutenticacion2fa());
			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					return keys.getInt(1);
				}
			}
		}
		return -1;
	}

	/** Revisa si ya existe un usuario con ese nombre o correo (para evitar duplicados). */
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

	/** Cambia la contraseña (ya en forma encriptada) de un usuario. */
	public void updatePassword(Integer userId, String newHash) throws SQLException {
		String sql = "UPDATE usuarios SET password_hash = ? WHERE id = ?";
		try (Connection cn = DatabaseManager.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
			ps.setString(1, newHash);
			ps.setInt(2, userId);
			ps.executeUpdate();
		}
	}

	/** Obtiene el número (id) de un rol por su nombre; si no está, lo crea en la tabla. */
	private int resolveRolIdByName(Connection cn, String roleName) throws SQLException {
		String q = "SELECT id FROM roles WHERE nombre_rol = ?";
		try (PreparedStatement ps = cn.prepareStatement(q)) {
			ps.setString(1, roleName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return rs.getInt(1);
			}
		}
		// Si no existe, crearlo y devolver id
		try (PreparedStatement ins = cn.prepareStatement("INSERT INTO roles(nombre_rol) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
			ins.setString(1, roleName);
			ins.executeUpdate();
			try (ResultSet rs = ins.getGeneratedKeys()) {
				if (rs.next()) return rs.getInt(1);
			}
		}
		// Último recurso: intentar consultar de nuevo (por si hubo condición de carrera)
		try (PreparedStatement ps2 = cn.prepareStatement(q)) {
			ps2.setString(1, roleName);
			try (ResultSet rs2 = ps2.executeQuery()) {
				if (rs2.next()) return rs2.getInt(1);
			}
		}
		throw new SQLException("No se pudo resolver/crear el rol por nombre: " + roleName);
	}

	/** Versión pública que abre y cierra la conexión automáticamente. */
	public int resolveRolIdByName(String roleName) throws SQLException {
		try (Connection cn = DatabaseManager.getConnection()) {
			return resolveRolIdByName(cn, roleName);
		}
	}

	/** Variante usando el enum de roles para mayor comodidad. */
    public int resolveRolIdByType(RoleType roleType) throws SQLException {
        return resolveRolIdByName(roleType.dbName());
    }
}
