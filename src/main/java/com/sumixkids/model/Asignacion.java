package com.sumixkids.model;

import java.time.LocalDateTime;

/**
 * Representa una asignación docente-estudiante tal como está guardada en la base de datos.
 * Una asignación establece la relación entre un docente y un estudiante para un grado específico.
 * Mapea la tabla 'docente_estudiante' en la base de datos.
 */
public class Asignacion {
    private Integer id;
    private Integer idDocente;
    private Integer idEstudiante;
    private Integer idGradoEscolar; // Referencia a grados_escolares.id_grado
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaFinalizacion;
    private String estado; // ACTIVA, FINALIZADA, SUSPENDIDA
    private String observaciones;
    private Integer asignadoPor;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Campos adicionales para mostrar en las vistas (JOINs)
    private String nombreDocente;
    private String nombreEstudiante;
    private String nombreGrado;
    private String nombreAsignadoPor;

    // Constructores
    public Asignacion() {}

    public Asignacion(Integer idDocente, Integer idEstudiante, Integer idGradoEscolar, 
                     LocalDateTime fechaAsignacion, String estado, String observaciones, 
                     Integer asignadoPor) {
        this.idDocente = idDocente;
        this.idEstudiante = idEstudiante;
        this.idGradoEscolar = idGradoEscolar;
        this.fechaAsignacion = fechaAsignacion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.asignadoPor = asignadoPor;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdGradoEscolar() {
        return idGradoEscolar;
    }

    public void setIdGradoEscolar(Integer idGradoEscolar) {
        this.idGradoEscolar = idGradoEscolar;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getAsignadoPor() {
        return asignadoPor;
    }

    public void setAsignadoPor(Integer asignadoPor) {
        this.asignadoPor = asignadoPor;
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

    // Getters y Setters para campos adicionales (JOINs)
    public String getNombreDocente() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreGrado() {
        return nombreGrado;
    }

    public void setNombreGrado(String nombreGrado) {
        this.nombreGrado = nombreGrado;
    }

    public String getNombreAsignadoPor() {
        return nombreAsignadoPor;
    }

    public void setNombreAsignadoPor(String nombreAsignadoPor) {
        this.nombreAsignadoPor = nombreAsignadoPor;
    }

    // Métodos de utilidad
    
    /**
     * Verifica si la asignación está activa
     */
    public boolean isActiva() {
        return "ACTIVA".equals(estado);
    }
    
    /**
     * Verifica si la asignación está finalizada
     */
    public boolean isFinalizada() {
        return "FINALIZADA".equals(estado);
    }
    
    /**
     * Verifica si la asignación está suspendida
     */
    public boolean isSuspendida() {
        return "SUSPENDIDA".equals(estado);
    }

    @Override
    public String toString() {
        return "Asignacion{" +
                "id=" + id +
                ", idDocente=" + idDocente +
                ", idEstudiante=" + idEstudiante +
                ", idGradoEscolar=" + idGradoEscolar +
                ", fechaAsignacion=" + fechaAsignacion +
                ", estado='" + estado + '\'' +
                ", nombreDocente='" + nombreDocente + '\'' +
                ", nombreEstudiante='" + nombreEstudiante + '\'' +
                ", nombreGrado='" + nombreGrado + '\'' +
                '}';
    }
}