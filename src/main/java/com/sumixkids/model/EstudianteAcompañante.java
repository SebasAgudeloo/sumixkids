package com.sumixkids.model;

import java.time.LocalDateTime;

/**
 * Entidad que representa la relación muchos-a-muchos entre Estudiante y Acompañante
 */
public class EstudianteAcompañante {
    private int id;
    private int estudianteId;
    private int acompañanteId;
    private TipoRelacion tipoRelacion;
    private LocalDateTime fechaVinculacion;
    private boolean activo;
    
    // Campos relacionados (no se almacenan en BD, se cargan por joins)
    private Estudiante estudiante;
    private Acompañante acompañante;
    
    public enum TipoRelacion {
        PRINCIPAL("Principal"),
        SECUNDARIO("Secundario"),
        EMERGENCIA("Emergencia");
        
        private final String displayName;
        
        TipoRelacion(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructores
    public EstudianteAcompañante() {
        this.fechaVinculacion = LocalDateTime.now();
        this.activo = true;
    }
    
    public EstudianteAcompañante(int estudianteId, int acompañanteId, TipoRelacion tipoRelacion) {
        this();
        this.estudianteId = estudianteId;
        this.acompañanteId = acompañanteId;
        this.tipoRelacion = tipoRelacion;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEstudianteId() { return estudianteId; }
    public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }
    
    public int getAcompañanteId() { return acompañanteId; }
    public void setAcompañanteId(int acompañanteId) { this.acompañanteId = acompañanteId; }
    
    public TipoRelacion getTipoRelacion() { return tipoRelacion; }
    public void setTipoRelacion(TipoRelacion tipoRelacion) { this.tipoRelacion = tipoRelacion; }
    
    public LocalDateTime getFechaVinculacion() { return fechaVinculacion; }
    public void setFechaVinculacion(LocalDateTime fechaVinculacion) { this.fechaVinculacion = fechaVinculacion; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    
    public Acompañante getAcompañante() { return acompañante; }
    public void setAcompañante(Acompañante acompañante) { this.acompañante = acompañante; }
    
    // Métodos de utilidad
    public boolean esPrincipal() {
        return tipoRelacion == TipoRelacion.PRINCIPAL;
    }
    
    public boolean esSecundario() {
        return tipoRelacion == TipoRelacion.SECUNDARIO;
    }
    
    public boolean esEmergencia() {
        return tipoRelacion == TipoRelacion.EMERGENCIA;
    }
    
    public String getDescripcionRelacion() {
        if (estudiante != null && acompañante != null) {
            return acompañante.getNombreCompleto() + " es " + 
                   (acompañante.getRelacionEstudiante() != null ? 
                    acompañante.getRelacionEstudiante().getDisplayName().toLowerCase() : "tutor") + 
                   " de " + estudiante.getNombreCompleto() + 
                   " (" + tipoRelacion.getDisplayName() + ")";
        }
        return "Relación " + tipoRelacion.getDisplayName();
    }
    
    @Override
    public String toString() {
        return "EstudianteAcompañante{" +
                "id=" + id +
                ", estudianteId=" + estudianteId +
                ", acompañanteId=" + acompañanteId +
                ", tipoRelacion=" + tipoRelacion +
                ", activo=" + activo +
                ", fechaVinculacion=" + fechaVinculacion +
                '}';
    }
}