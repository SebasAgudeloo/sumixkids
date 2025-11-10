package com.sumixkids.model;

import java.time.LocalDateTime;

/**
 * Entidad que representa un Grado Escolar con su grupo/sección.
 * Corresponde a la tabla grados_escolares en la base de datos.
 * Permite gestionar los diferentes grados y secciones disponibles en la institución.
 */
public class GradoEscolar {
    
    private int idGrado;
    private String nombreGrado;
    private String nombreGrupo;
    private String descripcion;
    private NivelEducativo nivelEducativo;
    private Integer capacidadMaxima;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    
    /**
     * Enum para los niveles educativos disponibles
     */
    public enum NivelEducativo {
        PREESCOLAR("Preescolar"),
        PRIMARIA("Primaria"),
        SECUNDARIA("Secundaria");
        
        private final String displayName;
        
        NivelEducativo(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructores
    public GradoEscolar() {
        this.activo = true;
        this.nivelEducativo = NivelEducativo.PRIMARIA;
        this.capacidadMaxima = 30;
    }
    
    public GradoEscolar(String nombreGrado, String nombreGrupo) {
        this();
        this.nombreGrado = nombreGrado;
        this.nombreGrupo = nombreGrupo;
    }
    
    public GradoEscolar(String nombreGrado, String nombreGrupo, String descripcion) {
        this(nombreGrado, nombreGrupo);
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public int getIdGrado() {
        return idGrado;
    }
    
    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }
    
    public String getNombreGrado() {
        return nombreGrado;
    }
    
    public void setNombreGrado(String nombreGrado) {
        this.nombreGrado = nombreGrado;
    }
    
    public String getNombreGrupo() {
        return nombreGrupo;
    }
    
    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public NivelEducativo getNivelEducativo() {
        return nivelEducativo;
    }
    
    public void setNivelEducativo(NivelEducativo nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }
    
    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
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
    
    // Métodos de utilidad
    
    /**
     * Obtiene el nombre completo del grado con su grupo.
     * Ejemplo: "3° - A" o "4° - B"
     */
    public String getNombreCompleto() {
        if (nombreGrado != null && nombreGrupo != null) {
            return nombreGrado + " - " + nombreGrupo;
        }
        return nombreGrado != null ? nombreGrado : "";
    }
    
    /**
     * Obtiene una descripción detallada del grado.
     * Si no hay descripción personalizada, genera una automática.
     */
    public String getDescripcionCompleta() {
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            return descripcion;
        }
        return generarDescripcionAutomatica();
    }
    
    /**
     * Genera una descripción automática basada en los datos del grado.
     */
    private String generarDescripcionAutomatica() {
        StringBuilder desc = new StringBuilder();
        
        if (nombreGrado != null && nombreGrupo != null) {
            desc.append(getNombreGradoTexto()).append(" - Grupo ").append(nombreGrupo);
        }
        
        if (nivelEducativo != null) {
            desc.append(" (").append(nivelEducativo.getDisplayName()).append(")");
        }
        
        return desc.toString();
    }
    
    /**
     * Convierte el nombre del grado a texto completo.
     * Ejemplo: "3°" -> "Tercer grado"
     */
    private String getNombreGradoTexto() {
        if (nombreGrado == null) return "";
        
        String grado = nombreGrado.replace("°", "").trim();
        switch (grado) {
            case "1": return "Primer grado";
            case "2": return "Segundo grado";
            case "3": return "Tercer grado";
            case "4": return "Cuarto grado";
            case "5": return "Quinto grado";
            case "6": return "Sexto grado";
            case "7": return "Séptimo grado";
            case "8": return "Octavo grado";
            case "9": return "Noveno grado";
            case "10": return "Décimo grado";
            case "11": return "Undécimo grado";
            default: return nombreGrado;
        }
    }
    
    /**
     * Verifica si el grado está disponible para asignaciones.
     */
    public boolean estaDisponible() {
        return activo;
    }
    
    /**
     * Verifica si el grado pertenece a primaria.
     */
    public boolean esPrimaria() {
        return nivelEducativo == NivelEducativo.PRIMARIA;
    }
    
    /**
     * Verifica si el grado pertenece a secundaria.
     */
    public boolean esSecundaria() {
        return nivelEducativo == NivelEducativo.SECUNDARIA;
    }
    
    /**
     * Verifica si el grado pertenece a preescolar.
     */
    public boolean esPreescolar() {
        return nivelEducativo == NivelEducativo.PREESCOLAR;
    }
    
    @Override
    public String toString() {
        return "GradoEscolar{" +
                "idGrado=" + idGrado +
                ", nombreGrado='" + nombreGrado + '\'' +
                ", nombreGrupo='" + nombreGrupo + '\'' +
                ", nivelEducativo=" + nivelEducativo +
                ", capacidadMaxima=" + capacidadMaxima +
                ", activo=" + activo +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        GradoEscolar that = (GradoEscolar) o;
        
        if (idGrado != 0 && that.idGrado != 0) {
            return idGrado == that.idGrado;
        }
        
        // Si no tienen ID, comparar por grado y grupo
        if (nombreGrado != null ? !nombreGrado.equals(that.nombreGrado) : that.nombreGrado != null)
            return false;
        return nombreGrupo != null ? nombreGrupo.equals(that.nombreGrupo) : that.nombreGrupo == null;
    }
    
    @Override
    public int hashCode() {
        int result = idGrado;
        result = 31 * result + (nombreGrado != null ? nombreGrado.hashCode() : 0);
        result = 31 * result + (nombreGrupo != null ? nombreGrupo.hashCode() : 0);
        return result;
    }
}
