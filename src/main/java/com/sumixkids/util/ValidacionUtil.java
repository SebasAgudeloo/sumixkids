package com.sumixkids.util;

import java.util.regex.Pattern;

public class ValidacionUtil {
    
    // Expresiones regulares para validación
    private static final String REGEX_NOMBRE = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$";
    private static final String REGEX_USUARIO = "^(?=.*[0-9].*[0-9])[a-zA-Z0-9]{4,20}$";
    private static final String REGEX_CORREO = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    /**
     * Valida que el nombre solo contenga letras y espacios
     */
    public static boolean esNombreValido(String nombre) {
        return nombre != null && Pattern.compile(REGEX_NOMBRE).matcher(nombre).matches();
    }
    
    /**
     * Valida que el usuario contenga al menos 2 números
     */
    public static boolean esUsuarioValido(String usuario) {
        return usuario != null && Pattern.compile(REGEX_USUARIO).matcher(usuario).matches();
    }
    
    /**
     * Valida formato de correo electrónico
     */
    public static boolean esCorreoValido(String correo) {
        return correo != null && Pattern.compile(REGEX_CORREO).matcher(correo).matches();
    }
    
    /**
     * Obtiene mensaje de error según el campo
     */
    public static String getMensajeError(String campo, String valor) {
        switch (campo) {
            case "nombres":
                return "El nombre solo puede contener letras y espacios";
            case "apellidos":
                return "El apellido solo puede contener letras y espacios";
            case "username":
                return "El usuario debe contener al menos 2 números y entre 4-20 caracteres";
            case "email":
                return "El correo electrónico debe tener un formato válido";
            default:
                return "Campo no válido";
        }
    }
}