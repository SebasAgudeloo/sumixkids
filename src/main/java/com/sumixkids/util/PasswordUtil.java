/**
 * SumixKids - Sistema Educativo para Práctica de Sumas
 * Paquete de utilidades del sistema
 */
package com.sumixkids.util;

// Sistema de logging para depuración
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Librería BCrypt para hash seguro de contraseñas
import org.mindrot.jbcrypt.BCrypt;

// Manejo de archivos y propiedades
import java.io.InputStream;
import java.util.Properties;

/**
 * PasswordUtil - Utilidades para manejo seguro de contraseñas
 * 
 * Proporciona métodos estáticos para hash, validación y verificación
 * de contraseñas utilizando el algoritmo BCrypt con salt automático.
 * 
 * FUNCIONALIDADES:
 * - Generación de hash BCrypt seguro para contraseñas
 * - Verificación de contraseñas contra hash almacenado
 * - Configuración ajustable del costo computacional
 * - Protección contra ataques de diccionario y fuerza bruta
 * 
 * SEGURIDAD:
 * - Utiliza salt aleatorio para cada contraseña
 * - Costo computacional configurable (default: 10 rounds)
 * - Resistente a ataques rainbow table
 * - No almacena contraseñas en texto plano
 * 
 * @author Equipo SumixKids
 * @version 1.0
 * @since 2024
 */
public class PasswordUtil {

	/** Logger para eventos de hash y verificación de contraseñas */
	private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

	private static final Properties props = new Properties();
	private static final int DEFAULT_COST = 10;

	static {
		try (InputStream in = PasswordUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (in != null) props.load(in);
		} catch (Exception ignored) {}
	}

	private static int cost() {
		try {
			return Integer.parseInt(props.getProperty("security.bcrypt.rounds", String.valueOf(DEFAULT_COST)));
		} catch (NumberFormatException e) {
			logger.warn("Valor inválido para security.bcrypt.rounds, usando por defecto {}", DEFAULT_COST);
			return DEFAULT_COST;
		}
	}

	/**
	 * Verifica si la contraseña cumple con las políticas de seguridad:
	 * - Mínimo 5 letras
	 * - Mínimo 2 números
	 * - Mínimo 1 carácter especial
	 * - Máximo 20 caracteres
	 */
	public static boolean isStrong(String password) {
		return ValidacionUtil.esPasswordValida(password);
	}

	/** Genera un texto cifrado seguro a partir de la contraseña original. */
	public static String hash(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(cost()));
	}

	/** Comprueba si la contraseña escrita coincide con la guardada en forma segura. */
	public static boolean verify(String plainPassword, String hashed) {
		if (plainPassword == null || hashed == null) return false;
		try {
			return BCrypt.checkpw(plainPassword, hashed);
		} catch (IllegalArgumentException e) {
			logger.warn("Hash de contraseña con formato inválido", e);
			// Si el valor guardado está dañado o tiene formato raro devolvemos false.
			return false;
		}
	}
}
