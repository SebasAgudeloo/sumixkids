package com.sumixkids.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utilidad para formatear fechas de manera consistente en toda la aplicación
 */
public class FechaUtil {
    
    // Formato: 2025-10-02 10:00 PM
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.US);
    
    /**
     * Formatea una fecha LocalDateTime al formato estándar de la aplicación
     * @param fecha La fecha a formatear
     * @return String con formato "yyyy-MM-dd hh:mm AM/PM" o cadena vacía si la fecha es null
     */
    public static String formatear(LocalDateTime fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(FORMATTER);
    }
    
    /**
     * Formatea una fecha LocalDateTime al formato estándar de la aplicación
     * Si la fecha es null, retorna el texto por defecto proporcionado
     * @param fecha La fecha a formatear
     * @param textoPorDefecto Texto a mostrar si la fecha es null
     * @return String con formato "yyyy-MM-dd hh:mm AM/PM" o el texto por defecto
     */
    public static String formatear(LocalDateTime fecha, String textoPorDefecto) {
        if (fecha == null) {
            return textoPorDefecto;
        }
        return fecha.format(FORMATTER);
    }
}
