package com.sumixkids.model;

import java.time.LocalDateTime;

/**
 * Representa a un usuario tal como está guardado en la base de datos.
 * Sólo guarda datos; no contiene lógica complicada.
 */
public class Usuario {
	private Integer id;
	private String username;
	private String nombres;
	private String apellidos;
	private String passwordHash;
	private String email;
	private String grado;
	private Integer rolId;
	private LocalDateTime fechaRegistro;
	private LocalDateTime ultimaConexion;
	private Integer intentosFallidos;
	private Boolean bloqueado;
	private String autenticacion2fa;

	public Usuario() {}

	public Usuario(Integer id, String username, String nombres, String apellidos, String passwordHash, String email, String grado, Integer rolId,
				   LocalDateTime fechaRegistro, LocalDateTime ultimaConexion,
				   Integer intentosFallidos, Boolean bloqueado, String autenticacion2fa) {
		this.id = id;
		this.username = username;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.passwordHash = passwordHash;
		this.email = email;
		this.grado = grado;
		this.rolId = rolId;
		this.fechaRegistro = fechaRegistro;
		this.ultimaConexion = ultimaConexion;
		this.intentosFallidos = intentosFallidos;
		this.bloqueado = bloqueado;
		this.autenticacion2fa = autenticacion2fa;
	}
	/** Nombres reales del usuario. */
	public String getNombres() { return nombres; }
	public void setNombres(String nombres) { this.nombres = nombres; }

	/** Apellidos reales del usuario. */
	public String getApellidos() { return apellidos; }
	public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    /** Identificador interno (número). */
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

    /** Nombre que la persona usa para entrar (login). */
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

    /** Contraseña en forma segura (encriptada). */
	public String getPasswordHash() { return passwordHash; }
	public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    /** Correo de contacto y también puede servir para iniciar sesión. */
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

    /** Grado escolar del estudiante (3°, 4°, 5°). */
	public String getGrado() { return grado; }
	public void setGrado(String grado) { this.grado = grado; }

    /** Número que señala el rol (se conecta con la tabla roles). */
	public Integer getRolId() { return rolId; }
	public void setRolId(Integer rolId) { this.rolId = rolId; }

    /** Momento en que se registró. */
	public LocalDateTime getFechaRegistro() { return fechaRegistro; }
	public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    /** Última vez que inició sesión correctamente. */
	public LocalDateTime getUltimaConexion() { return ultimaConexion; }
	public void setUltimaConexion(LocalDateTime ultimaConexion) { this.ultimaConexion = ultimaConexion; }

    /** Cuántos intentos incorrectos lleva (para bloqueo). */
	public Integer getIntentosFallidos() { return intentosFallidos; }
	public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    /** Indica si la cuenta está temporalmente bloqueada. */
	public Boolean getBloqueado() { return bloqueado; }
	public void setBloqueado(Boolean bloqueado) { this.bloqueado = bloqueado; }

    /** Espacio reservado para un posible código de verificación adicional (2FA). */
	public String getAutenticacion2fa() { return autenticacion2fa; }
	public void setAutenticacion2fa(String autenticacion2fa) { this.autenticacion2fa = autenticacion2fa; }
}
