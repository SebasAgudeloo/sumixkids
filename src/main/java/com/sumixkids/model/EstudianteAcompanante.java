package com.sumixkids.model;

import java.time.LocalDateTime;

/**
 * Vínculo entre un estudiante (rol 3) y un acompañante/tutor (rol 4).
 * Similar a la relación docente-estudiante pero para acompañantes/tutores.
 */
public class EstudianteAcompanante {
    private Integer id;
    private Integer estudianteId;
    private Integer acompananteId;
    private String tipoRelacion; // padre, madre, tutor, hermano, otro
    private LocalDateTime fechaVinculacion; // Cambiado para coincidir con BD
    private Boolean activo;
    // Campos removidos porque no existen en la tabla real:
    // private Integer vinculadoPor; 
    // private LocalDateTime fechaCreacion;
    // private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para mostrar en las vistas (JOINs)
    private String nombreEstudiante;
    private String nombreAcompanante;
    private String nombreVinculadoPor;

    // Constructores
    public EstudianteAcompanante() {}

    public EstudianteAcompanante(Integer estudianteId, Integer acompananteId, String tipoRelacion) {
        this.estudianteId = estudianteId;
        this.acompananteId = acompananteId;
        this.tipoRelacion = tipoRelacion;
        this.activo = true;
        this.fechaVinculacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }

    public Integer getAcompananteId() { return acompananteId; }
    public void setAcompananteId(Integer acompananteId) { this.acompananteId = acompananteId; }

    public String getTipoRelacion() { return tipoRelacion; }
    public void setTipoRelacion(String tipoRelacion) { this.tipoRelacion = tipoRelacion; }

    public LocalDateTime getFechaVinculacion() { return fechaVinculacion; }
    public void setFechaVinculacion(LocalDateTime fechaVinculacion) { this.fechaVinculacion = fechaVinculacion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    // Métodos removidos porque los campos no existen en la tabla real:
    // public Integer getVinculadoPor() { return vinculadoPor; }
    // public void setVinculadoPor(Integer vinculadoPor) { this.vinculadoPor = vinculadoPor; }
    // public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    // public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    // public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    // public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    // Campos adicionales (JOINs)
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }

    public String getNombreAcompanante() { return nombreAcompanante; }
    public void setNombreAcompanante(String nombreAcompanante) { this.nombreAcompanante = nombreAcompanante; }

    // Campo removido porque vinculadoPor no existe en la tabla real:
    // public String getNombreVinculadoPor() { return nombreVinculadoPor; }
    // public void setNombreVinculadoPor(String nombreVinculadoPor) { this.nombreVinculadoPor = nombreVinculadoPor; }

    // Métodos de utilidad
    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }

    @Override
    public String toString() {
        return "EstudianteAcompanante{" +
                "id=" + id +
                ", estudianteId=" + estudianteId +
                ", acompananteId=" + acompananteId +
                ", tipoRelacion='" + tipoRelacion + '\'' +
                ", activo=" + activo +
                ", nombreEstudiante='" + nombreEstudiante + '\'' +
                ", nombreAcompanante='" + nombreAcompanante + '\'' +
                '}';
    }
}
