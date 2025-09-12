package com.sumixkids.util;

import org.mindrot.jbcrypt.BCrypt;

import java.io.InputStream;
import java.util.Properties;

/**
 * Herramientas para guardar contraseñas de manera segura (no en texto plano).
 * Usa la librería BCrypt que añade "sal" y repite el proceso varias veces.
 */
public class PasswordUtil {

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
			return DEFAULT_COST;
		}
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
			// Si el valor guardado está dañado o tiene formato raro devolvemos false.
			return false;
		}
	}
}
