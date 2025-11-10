package com.sumixkids.model;

import java.time.LocalDateTime;

/**
 * Entidad que representa un Nivel de Dificultad para ejercicios y juegos educativos.
 * Corresponde a la tabla niveles_dificultad en la base de datos.
 * 
 * La descripción se almacena como JSON para permitir configuraciones flexibles
 * como: rango de números, tiempo límite, puntos base, ayudas disponibles, etc.
 */
public class NivelDificultad {
    
    private int idNivelDificultad;
    private String nombreNivel;
    private int orden;
    private String descripcion; // JSON con configuración completa
    private String colorHex;
    private String icono;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Constructores
    public NivelDificultad() {
        this.activo = true;
        this.colorHex = "#4CAF50"; // Verde por defecto
        this.icono = "⭐";
        this.orden = 0;
    }
    
    public NivelDificultad(String nombreNivel, int orden) {
        this();
        this.nombreNivel = nombreNivel;
        this.orden = orden;
    }
    
    public NivelDificultad(String nombreNivel, int orden, String descripcion) {
        this(nombreNivel, orden);
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public int getIdNivelDificultad() {
        return idNivelDificultad;
    }
    
    public void setIdNivelDificultad(int idNivelDificultad) {
        this.idNivelDificultad = idNivelDificultad;
    }
    
    public String getNombreNivel() {
        return nombreNivel;
    }
    
    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }
    
    public int getOrden() {
        return orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getColorHex() {
        return colorHex;
    }
    
    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
    
    public String getIcono() {
        return icono;
    }
    
    public void setIcono(String icono) {
        this.icono = icono;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    // Métodos de utilidad
    
    /**
     * Obtiene un nombre completo del nivel con su icono.
     * Ejemplo: "⭐ Básico"
     */
    public String getNombreConIcono() {
        if (icono != null && !icono.trim().isEmpty()) {
            return icono + " " + (nombreNivel != null ? nombreNivel : "");
        }
        return nombreNivel != null ? nombreNivel : "";
    }
    
    /**
     * Verifica si este nivel es más fácil que otro.
     * @param otro El nivel a comparar
     * @return true si este nivel tiene un orden menor (es más fácil)
     */
    public boolean esMasFacilQue(NivelDificultad otro) {
        return this.orden < otro.orden;
    }
    
    /**
     * Verifica si este nivel es más difícil que otro.
     * @param otro El nivel a comparar
     * @return true si este nivel tiene un orden mayor (es más difícil)
     */
    public boolean esMasDificilQue(NivelDificultad otro) {
        return this.orden > otro.orden;
    }
    
    /**
     * Verifica si el nivel está disponible para uso.
     */
    public boolean estaDisponible() {
        return activo;
    }
    
    /**
     * Obtiene el estilo CSS para el color del nivel.
     * Ejemplo: "color: #4CAF50;"
     */
    public String getEstiloColor() {
        return colorHex != null ? "color: " + colorHex + ";" : "";
    }
    
    /**
     * Obtiene el estilo CSS para el fondo del nivel con transparencia.
     * Ejemplo: "background-color: #4CAF5020;"
     */
    public String getEstiloFondo() {
        if (colorHex != null && colorHex.length() == 7) {
            return "background-color: " + colorHex + "20;"; // 20 = 12% transparencia
        }
        return "";
    }
    
    /**
     * Obtiene el nombre del nivel en minúsculas para clases CSS.
     * Ejemplo: "básico" -> "basico"
     */
    public String getClaseCSS() {
        if (nombreNivel == null) return "";
        return nombreNivel.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replaceAll("\\s+", "-");
    }
    
    /**
     * Determina si el nivel es para principiantes (orden 1).
     */
    public boolean esPrincipiante() {
        return orden == 1;
    }
    
    /**
     * Determina si el nivel es avanzado (orden >= 3).
     */
    public boolean esAvanzado() {
        return orden >= 3;
    }
    
    /**
     * Obtiene una representación visual del nivel con estrellas.
     * Ejemplo: orden 1 = "⭐", orden 2 = "⭐⭐", etc.
     */
    public String getEstrellas() {
        StringBuilder estrellas = new StringBuilder();
        for (int i = 0; i < Math.min(orden, 5); i++) {
            estrellas.append("⭐");
        }
        return estrellas.toString();
    }
    
    /**
     * Obtiene la descripción legible extrayendo del JSON si es necesario.
     * Si no es JSON válido, devuelve el texto tal como está.
     */
    public String getDescripcionLegible() {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "";
        }
        
        // Si parece ser JSON, intentar extraer el campo "descripcion"
        if (descripcion.trim().startsWith("{") && descripcion.trim().endsWith("}")) {
            try {
                // Buscar el campo descripcion usando regex simple
                if (descripcion.contains("\"descripcion\"")) {
                    String patron = "\"descripcion\"\\s*:\\s*\"([^\"]+)\"";
                    java.util.regex.Pattern p = java.util.regex.Pattern.compile(patron);
                    java.util.regex.Matcher m = p.matcher(descripcion);
                    if (m.find()) {
                        return m.group(1);
                    }
                }
                
                // Si no encuentra "descripcion", buscar "nombre"
                if (descripcion.contains("\"nombre\"")) {
                    String patron = "\"nombre\"\\s*:\\s*\"([^\"]+)\"";
                    java.util.regex.Pattern p = java.util.regex.Pattern.compile(patron);
                    java.util.regex.Matcher m = p.matcher(descripcion);
                    if (m.find()) {
                        return m.group(1);
                    }
                }
                
            } catch (Exception e) {
                // Si hay error en parsing, devolver el texto original
            }
        }
        
        // Si no es JSON o no se puede parsear, devolver tal como está
        return descripcion;
    }
    
    /**
     * Convierte una descripción simple de texto a formato JSON almacenable.
     * Mantiene compatibilidad con el sistema existente.
     */
    public static String crearDescripcionJSON(String textoSimple, int rangoInicio, int rangoFin) {
        if (textoSimple == null || textoSimple.trim().isEmpty()) {
            textoSimple = "Nivel de dificultad";
        }
        
        return String.format(
            "{\"descripcion\":\"%s\",\"rangoNumeros\":[%d,%d],\"tiempoLimite\":60,\"puntosBase\":10,\"ayudas\":true}",
            textoSimple.replace("\"", "\\\""), rangoInicio, rangoFin
        );
    }
    
    @Override
    public String toString() {
        return "NivelDificultad{" +
                "idNivelDificultad=" + idNivelDificultad +
                ", nombreNivel='" + nombreNivel + '\'' +
                ", orden=" + orden +
                ", colorHex='" + colorHex + '\'' +
                ", icono='" + icono + '\'' +
                ", activo=" + activo +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        NivelDificultad that = (NivelDificultad) o;
        
        if (idNivelDificultad != 0 && that.idNivelDificultad != 0) {
            return idNivelDificultad == that.idNivelDificultad;
        }
        
        // Si no tienen ID, comparar por nombre
        return nombreNivel != null ? nombreNivel.equals(that.nombreNivel) : that.nombreNivel == null;
    }
    
    @Override
    public int hashCode() {
        int result = idNivelDificultad;
        result = 31 * result + (nombreNivel != null ? nombreNivel.hashCode() : 0);
        result = 31 * result + orden;
        return result;
    }
}
