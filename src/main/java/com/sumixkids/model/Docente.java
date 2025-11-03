package com.sumixkids.model;

import java.time.LocalDate;

/**
 * Entidad que representa un Docente (subtipo de Usuario)
 */
public class Docente {
    private int id;
    private int usuarioId;
    private String numeroEmpleado;
    private String especialidad;
    private String gradosAsignados;
    private Integer anosExperiencia;
    private LocalDate fechaContratacion;
    private String tituloAcademico;
    private String institucionTitulo;
    private String telefonoTrabajo;
    private String horarioDisponible;
    private EstadoLaboral estadoLaboral;
    
    // Campo relacionado (no se almacena en BD, se carga por join)
    private Usuario usuario;
    
    public enum EstadoLaboral {
        ACTIVO, LICENCIA, VACACIONES, INACTIVO
    }
    
    // Constructores
    public Docente() {}
    
    public Docente(int usuarioId, String numeroEmpleado) {
        this.usuarioId = usuarioId;
        this.numeroEmpleado = numeroEmpleado;
        this.estadoLaboral = EstadoLaboral.ACTIVO;
        this.fechaContratacion = LocalDate.now();
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    
    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }
    
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    
    public String getGradosAsignados() { return gradosAsignados; }
    public void setGradosAsignados(String gradosAsignados) { this.gradosAsignados = gradosAsignados; }
    
    public Integer getAnosExperiencia() { return anosExperiencia; }
    public void setAnosExperiencia(Integer anosExperiencia) { this.anosExperiencia = anosExperiencia; }
    
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(LocalDate fechaContratacion) { this.fechaContratacion = fechaContratacion; }
    
    public String getTituloAcademico() { return tituloAcademico; }
    public void setTituloAcademico(String tituloAcademico) { this.tituloAcademico = tituloAcademico; }
    
    public String getInstitucionTitulo() { return institucionTitulo; }
    public void setInstitucionTitulo(String institucionTitulo) { this.institucionTitulo = institucionTitulo; }
    
    public String getTelefonoTrabajo() { return telefonoTrabajo; }
    public void setTelefonoTrabajo(String telefonoTrabajo) { this.telefonoTrabajo = telefonoTrabajo; }
    
    public String getHorarioDisponible() { return horarioDisponible; }
    public void setHorarioDisponible(String horarioDisponible) { this.horarioDisponible = horarioDisponible; }
    
    public EstadoLaboral getEstadoLaboral() { return estadoLaboral; }
    public void setEstadoLaboral(EstadoLaboral estadoLaboral) { this.estadoLaboral = estadoLaboral; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    // Métodos de utilidad
    public String getNombreCompleto() {
        return usuario != null ? usuario.getNombres() + " " + usuario.getApellidos() : "";
    }
    
    public String getInfoProfesional() {
        StringBuilder info = new StringBuilder();
        if (especialidad != null && !especialidad.trim().isEmpty()) {
            info.append(especialidad);
        }
        if (gradosAsignados != null && !gradosAsignados.trim().isEmpty()) {
            if (info.length() > 0) info.append(" - ");
            info.append("Grados: ").append(gradosAsignados);
        }
        return info.toString();
    }
    
    public boolean esExperimentado() {
        return anosExperiencia != null && anosExperiencia >= 5;
    }
    
    public boolean estaActivo() {
        return estadoLaboral == EstadoLaboral.ACTIVO;
    }
    
    @Override
    public String toString() {
        return "Docente{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", numeroEmpleado='" + numeroEmpleado + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", gradosAsignados='" + gradosAsignados + '\'' +
                ", estadoLaboral=" + estadoLaboral +
                '}';
    }
}