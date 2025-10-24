/**
 * Paquete de utilidades del sistema SumixKids
 * Contiene clases helper y utilidades para funcionalidades
 * transversales de la aplicación
 */
package com.sumixkids.util;

// Importación para manejo de expresiones regulares
import java.util.regex.Pattern;

/**
 * ValidacionUtil - Utilidades para validación de datos de entrada
 * 
 * Funcionalidades:
 * - Validación de nombres y apellidos con caracteres especiales español
 * - Validación de usernames sin espacios y longitud controlada
 * - Validación de direcciones de email con formato estándar
 * - Validación de contraseñas con requisitos de seguridad
 * - Validación de grados académicos permitidos (3°, 4°, 5°)
 * 
 * Expresiones regulares definidas:
 * - Nombres/Apellidos: Letras, acentos, espacios, máx 30 caracteres
 * - Username: Sin espacios, 5-15 caracteres
 * - Email: Formato estándar de direcciones electrónicas
 * - Grado: Solo acepta 3°, 4° o 5° grado
 * - Contraseña: Mínimo 6 caracteres con variedad
 * 
 * @author SumixKids Team
 * @version 1.0
 * @since 2024
 */
public class ValidacionUtil {
    
    // Expresión regular para validar nombres (letras, acentos, espacios, máx 30)
    private static final String REGEX_NOMBRE = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{1,30}$";
    
    // Expresión regular para validar apellidos (igual formato que nombres)
    private static final String REGEX_APELLIDO = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{1,30}$";
    
    // Expresión regular para username (sin espacios, 5-15 caracteres)
    private static final String REGEX_USERNAME = "^[^\s]{5,15}$";
    
    // Expresión regular para validar formato de email estándar
    private static final String REGEX_CORREO = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    // Expresión regular para validar grados académicos (solo 3°, 4°, 5°)
    private static final String REGEX_GRADO = "^[345]°$";
    
    /**
     * Valida que el nombre solo contenga letras y espacios (máx. 30 caracteres)
     */
    public static boolean esNombreValido(String nombre) {
        return nombre != null && Pattern.compile(REGEX_NOMBRE).matcher(nombre.trim()).matches();
    }
    
    /**
     * Valida que el apellido solo contenga letras y espacios (máx. 30 caracteres)
     */
    public static boolean esApellidoValido(String apellido) {
        return apellido != null && Pattern.compile(REGEX_APELLIDO).matcher(apellido.trim()).matches();
    }
    
    /**
     * Valida que el username tenga entre 5 y 15 caracteres sin espacios
     */
    public static boolean esUsernameValido(String username) {
        return username != null && Pattern.compile(REGEX_USERNAME).matcher(username.trim()).matches();
    }
    
    /**
     * Valida formato de correo electrónico
     */
    public static boolean esCorreoValido(String correo) {
        return correo != null && Pattern.compile(REGEX_CORREO).matcher(correo.trim()).matches();
    }
    
    /**
     * Valida que el grado sea 3°, 4° o 5°
     */
    public static boolean esGradoValido(String grado) {
        return grado != null && Pattern.compile(REGEX_GRADO).matcher(grado.trim()).matches();
    }
    
    /**
     * Valida que la contraseña cumpla con los nuevos requerimientos:
     * - Mínimo 5 letras
     * - Mínimo 2 números  
     * - Mínimo 1 carácter especial
     * - Máximo 20 caracteres
     */
    public static boolean esPasswordValida(String password) {
        if (password == null || password.length() > 20) return false;
        
        int letras = 0, numeros = 0, especiales = 0;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                letras++;
            } else if (Character.isDigit(c)) {
                numeros++;
            } else {
                especiales++;
            }
        }
        
        return letras >= 5 && numeros >= 2 && especiales >= 1;
    }
    
    /**
     * Método legacy mantenido por compatibilidad
     */
    public static boolean esUsuarioValido(String usuario) {
        return esUsernameValido(usuario);
    }
    
    /**
     * Obtiene mensaje de error según el campo
     */
    public static String getMensajeError(String campo, String valor) {
        switch (campo) {
            case "nombres":
                return "El nombre solo puede contener letras y espacios (máximo 30 caracteres)";
            case "apellidos":
                return "El apellido solo puede contener letras y espacios (máximo 30 caracteres)";
            case "username":
                return "El usuario debe tener entre 5 y 15 caracteres sin espacios";
            case "email":
                return "El correo electrónico debe tener un formato válido";
            case "password":
                return "La contraseña debe tener mínimo 5 letras, 2 números, 1 carácter especial y máximo 20 caracteres";
            case "grado":
                return "El grado debe ser 3°, 4° o 5°";
            default:
                return "Campo no válido";
        }
    }
    
    /**
     * Obtiene mensaje detallado para contraseña con indicadores específicos
     */
    public static String getMensajePasswordDetallado(String password) {
        if (password == null) return "La contraseña es requerida";
        if (password.length() > 20) return "La contraseña no puede tener más de 20 caracteres";
        
        int letras = 0, numeros = 0, especiales = 0;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) letras++;
            else if (Character.isDigit(c)) numeros++;
            else especiales++;
        }
        
        StringBuilder mensaje = new StringBuilder("La contraseña debe tener: ");
        boolean needsSeparator = false;
        
        if (letras < 5) {
            mensaje.append("mínimo 5 letras (tienes ").append(letras).append(")");
            needsSeparator = true;
        }
        if (numeros < 2) {
            if (needsSeparator) mensaje.append(", ");
            mensaje.append("mínimo 2 números (tienes ").append(numeros).append(")");
            needsSeparator = true;
        }
        if (especiales < 1) {
            if (needsSeparator) mensaje.append(", ");
            mensaje.append("mínimo 1 carácter especial (tienes ").append(especiales).append(")");
        }
        
        return mensaje.toString();
    }
}