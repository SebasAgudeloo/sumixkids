package com.sumixkids.util;

/**
 * Utilidad para manipular y formatear strings en la aplicación
 */
public class StringUtil {
    
    /**
     * Trunca un texto a una longitud máxima y agrega "..." al final
     * @param texto El texto a truncar
     * @param longitudMaxima Longitud máxima del texto
     * @return Texto truncado con "..." o el texto original si es más corto
     */
    public static String truncate(String texto, int longitudMaxima) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        
        if (texto.length() <= longitudMaxima) {
            return texto;
        }
        
        return texto.substring(0, longitudMaxima - 3) + "...";
    }
    
    /**
     * Trunca un texto a una longitud máxima sin agregar puntos suspensivos
     * @param texto El texto a truncar
     * @param longitudMaxima Longitud máxima del texto
     * @return Texto truncado
     */
    public static String truncateSimple(String texto, int longitudMaxima) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        
        if (texto.length() <= longitudMaxima) {
            return texto;
        }
        
        return texto.substring(0, longitudMaxima);
    }
    
    /**
     * Capitaliza la primera letra de un texto
     * @param texto El texto a capitalizar
     * @return Texto con la primera letra en mayúscula
     */
    public static String capitalize(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        
        if (texto.length() == 1) {
            return texto.toUpperCase();
        }
        
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
    
    /**
     * Escapa caracteres HTML para prevenir XSS
     * @param texto El texto a escapar
     * @return Texto con caracteres HTML escapados
     */
    public static String escapeHtml(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        
        return texto
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
    
    /**
     * Escapa caracteres especiales de JavaScript para prevenir XSS
     * @param texto El texto a escapar
     * @return Texto con caracteres JavaScript escapados
     */
    public static String escapeJs(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        
        return texto
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("'", "\\'")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
            .replace("/", "\\/");
    }
    
    /**
     * Verifica si un string está vacío o solo contiene espacios
     * @param texto El texto a verificar
     * @return true si está vacío o solo contiene espacios
     */
    public static boolean isEmpty(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
    
    /**
     * Verifica si un string no está vacío
     * @param texto El texto a verificar
     * @return true si no está vacío
     */
    public static boolean isNotEmpty(String texto) {
        return !isEmpty(texto);
    }
}
