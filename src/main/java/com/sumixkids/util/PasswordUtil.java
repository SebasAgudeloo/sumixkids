package com.sumixkids.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mindrot.jbcrypt.BCrypt;

import java.io.InputStream;
import java.util.Properties;

/**
 * Herramientas para guardar contraseñas de manera segura (no en texto plano).
 * Usa la librería BCrypt que añade "sal" y repite el proceso varias veces.
 */
public class PasswordUtil {

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
	 * - Mínimo 8 caracteres
	 * - Al menos una mayúscula, una minúscula, un número y un carácter especial
	 */
	public static boolean isStrong(String password) {
		if (password == null || password.length() < 8) return false;
		boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) hasUpper = true;
			else if (Character.isLowerCase(c)) hasLower = true;
			else if (Character.isDigit(c)) hasDigit = true;
			else hasSpecial = true;
		}
		return hasUpper && hasLower && hasDigit && hasSpecial;
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
