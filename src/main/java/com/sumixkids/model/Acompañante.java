package com.sumixkids.model;

/**
 * Entidad que representa un Acompañante/Tutor (subtipo de Usuario)
 */
public class Acompañante {
    private int id;
    private int usuarioId;
    private RelacionEstudiante relacionEstudiante;
    private String telefonoPrincipal;
    private String telefonoSecundario;
    private String direccion;
    private String ocupacion;
    private String empresaTrabajo;
    private String nivelEducativo;
    private boolean recibirNotificaciones;
    private String horarioContacto;
    private boolean autorizacionRecoger;
    
    // Campo relacionado (no se almacena en BD, se carga por join)
    private Usuario usuario;
    
    public enum RelacionEstudiante {
        PADRE("Padre"),
        MADRE("Madre"),
        ABUELO("Abuelo"),
        ABUELA("Abuela"),
        TIO("Tío"),
        TIA("Tía"),
        HERMANO("Hermano"),
        TUTOR_LEGAL("Tutor Legal"),
        OTRO("Otro");
        
        private final String displayName;
        
        RelacionEstudiante(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructores
    public Acompañante() {
        this.recibirNotificaciones = true;
        this.autorizacionRecoger = true;
    }
    
    public Acompañante(int usuarioId, RelacionEstudiante relacionEstudiante) {
        this();
        this.usuarioId = usuarioId;
        this.relacionEstudiante = relacionEstudiante;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    
    public RelacionEstudiante getRelacionEstudiante() { return relacionEstudiante; }
    public void setRelacionEstudiante(RelacionEstudiante relacionEstudiante) { this.relacionEstudiante = relacionEstudiante; }
    
    public String getTelefonoPrincipal() { return telefonoPrincipal; }
    public void setTelefonoPrincipal(String telefonoPrincipal) { this.telefonoPrincipal = telefonoPrincipal; }
    
    public String getTelefonoSecundario() { return telefonoSecundario; }
    public void setTelefonoSecundario(String telefonoSecundario) { this.telefonoSecundario = telefonoSecundario; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    
    public String getEmpresaTrabajo() { return empresaTrabajo; }
    public void setEmpresaTrabajo(String empresaTrabajo) { this.empresaTrabajo = empresaTrabajo; }
    
    public String getNivelEducativo() { return nivelEducativo; }
    public void setNivelEducativo(String nivelEducativo) { this.nivelEducativo = nivelEducativo; }
    
    public boolean isRecibirNotificaciones() { return recibirNotificaciones; }
    public void setRecibirNotificaciones(boolean recibirNotificaciones) { this.recibirNotificaciones = recibirNotificaciones; }
    
    public String getHorarioContacto() { return horarioContacto; }
    public void setHorarioContacto(String horarioContacto) { this.horarioContacto = horarioContacto; }
    
    public boolean isAutorizacionRecoger() { return autorizacionRecoger; }
    public void setAutorizacionRecoger(boolean autorizacionRecoger) { this.autorizacionRecoger = autorizacionRecoger; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    // Métodos de utilidad
    public String getNombreCompleto() {
        return usuario != null ? usuario.getNombres() + " " + usuario.getApellidos() : "";
    }
    
    public String getNombreConRelacion() {
        String nombre = getNombreCompleto();
        if (relacionEstudiante != null) {
            return nombre + " (" + relacionEstudiante.getDisplayName() + ")";
        }
        return nombre;
    }
    
    public String getTelefonoPreferido() {
        if (telefonoPrincipal != null && !telefonoPrincipal.trim().isEmpty()) {
            return telefonoPrincipal;
        }
        return telefonoSecundario;
    }
    
    public boolean tieneTelefono() {
        return (telefonoPrincipal != null && !telefonoPrincipal.trim().isEmpty()) ||
               (telefonoSecundario != null && !telefonoSecundario.trim().isEmpty());
    }
    
    public String getInfoContacto() {
        StringBuilder info = new StringBuilder();
        
        if (telefonoPrincipal != null && !telefonoPrincipal.trim().isEmpty()) {
            info.append("Tel: ").append(telefonoPrincipal);
        }
        
        if (telefonoSecundario != null && !telefonoSecundario.trim().isEmpty()) {
            if (info.length() > 0) info.append(" / ");
            info.append("Sec: ").append(telefonoSecundario);
        }
        
        if (horarioContacto != null && !horarioContacto.trim().isEmpty()) {
            if (info.length() > 0) info.append(" - ");
            info.append(horarioContacto);
        }
        
        return info.toString();
    }
    
    @Override
    public String toString() {
        return "Acompañante{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", relacionEstudiante=" + relacionEstudiante +
                ", telefonoPrincipal='" + telefonoPrincipal + '\'' +
                ", recibirNotificaciones=" + recibirNotificaciones +
                '}';
    }
}