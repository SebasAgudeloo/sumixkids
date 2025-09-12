package com.sumixkids.config;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase que consigue una conexión (puerta de entrada) a la base de datos.
 * Forma de trabajar (en palabras simples):
 * 1) Primero intenta usar la configuración del servidor (JNDI) si el admin de Tomcat la puso.
 * 2) Si no existe esa configuración, usa los datos guardados en el archivo config.properties.
 */
public class DatabaseManager {

	private static final String JNDI_NAME = "java:comp/env/jdbc/sumixkidsDS";
	private static DataSource dataSource;
	private static final Properties props = new Properties();

	static {
		// Carga DataSource JNDI si existe
		try {
			InitialContext ic = new InitialContext();
			dataSource = (DataSource) ic.lookup(JNDI_NAME);
		} catch (NamingException e) {
			dataSource = null; // Fallback a DriverManager
		}

		// Carga propiedades desde resources/config.properties
		try (InputStream in = DatabaseManager.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (in != null) {
				props.load(in);
			}
		} catch (Exception ignored) {
		}
	}

	/**
	 * Devuelve una conexión lista para usar.
	 * Si falla el método principal (JNDI), intenta con los datos del archivo de configuración.
	 */
	public static Connection getConnection() throws SQLException {
		// 1) Preferir DataSource JNDI si existe
		if (dataSource != null) {
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				// Si el JNDI está mal configurado, hacemos fallback a DriverManager usando config.properties
				System.err.println("[DatabaseManager] Error obteniendo conexión JNDI (jdbc/sumixkidsDS): " + e.getMessage());
				System.err.println("[DatabaseManager] Usando fallback DriverManager con config.properties");
			}
		}

		// 2) Fallback DriverManager
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.username");
		String pass = props.getProperty("db.password");
		String driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");

		if (url == null || url.trim().isEmpty()) {
			throw new SQLException("db.url no definido y JNDI falló. Configura el DataSource JNDI 'jdbc/sumixkidsDS' en Tomcat o define db.url en src/main/resources/config.properties");
		}

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new SQLException("No se encontró el driver JDBC: " + driver + ". Asegúrate que mysql-connector-j esté en el classpath.", e);
		}
		return DriverManager.getConnection(url, user, pass);
	}
}
