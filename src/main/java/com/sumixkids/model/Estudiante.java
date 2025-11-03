package com.sumixkids.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa un Estudiante (subtipo de Usuario)
 */
public class Estudiante {
    private int id;
    private int usuarioId;
    private String numeroEstudiante;
    private String gradoActual;
    private String seccion;
    private BigDecimal promedioGeneral;
    private String nivelLectura;
    private String nivelMatematicas;
    private LocalDate fechaIngreso;
    private Integer tutorPrincipalId;
    private EstadoAcademico estadoAcademico;
    
    // Campos relacionados (no se almacenan en BD, se cargan por joins)
    private Usuario usuario;
    private Usuario tutorPrincipal;
    
    public enum EstadoAcademico {
        ACTIVO, INACTIVO, GRADUADO, RETIRADO
    }
    
    // Constructores
    public Estudiante() {}
    
    public Estudiante(int usuarioId, String numeroEstudiante, String gradoActual) {
        this.usuarioId = usuarioId;
        this.numeroEstudiante = numeroEstudiante;
        this.gradoActual = gradoActual;
        this.estadoAcademico = EstadoAcademico.ACTIVO;
        this.fechaIngreso = LocalDate.now();
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    
    public String getNumeroEstudiante() { return numeroEstudiante; }
    public void setNumeroEstudiante(String numeroEstudiante) { this.numeroEstudiante = numeroEstudiante; }
    
    public String getGradoActual() { return gradoActual; }
    public void setGradoActual(String gradoActual) { this.gradoActual = gradoActual; }
    
    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }
    
    public BigDecimal getPromedioGeneral() { return promedioGeneral; }
    public void setPromedioGeneral(BigDecimal promedioGeneral) { this.promedioGeneral = promedioGeneral; }
    
    public String getNivelLectura() { return nivelLectura; }
    public void setNivelLectura(String nivelLectura) { this.nivelLectura = nivelLectura; }
    
    public String getNivelMatematicas() { return nivelMatematicas; }
    public void setNivelMatematicas(String nivelMatematicas) { this.nivelMatematicas = nivelMatematicas; }
    
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    
    public Integer getTutorPrincipalId() { return tutorPrincipalId; }
    public void setTutorPrincipalId(Integer tutorPrincipalId) { this.tutorPrincipalId = tutorPrincipalId; }
    
    public EstadoAcademico getEstadoAcademico() { return estadoAcademico; }
    public void setEstadoAcademico(EstadoAcademico estadoAcademico) { this.estadoAcademico = estadoAcademico; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Usuario getTutorPrincipal() { return tutorPrincipal; }
    public void setTutorPrincipal(Usuario tutorPrincipal) { this.tutorPrincipal = tutorPrincipal; }
    
    // Métodos de utilidad
    public String getNombreCompleto() {
        return usuario != null ? usuario.getNombres() + " " + usuario.getApellidos() : "";
    }
    
    public String getGradoConSeccion() {
        if (seccion != null && !seccion.trim().isEmpty()) {
            return gradoActual + " - " + seccion;
        }
        return gradoActual;
    }
    
    public boolean tienePromedioMinimo(double minimo) {
        return promedioGeneral != null && promedioGeneral.doubleValue() >= minimo;
    }
    
    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", numeroEstudiante='" + numeroEstudiante + '\'' +
                ", gradoActual='" + gradoActual + '\'' +
                ", promedioGeneral=" + promedioGeneral +
                ", estadoAcademico=" + estadoAcademico +
                '}';
    }
}