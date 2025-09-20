package com.sumixkids.model;

import java.time.LocalDateTime;

public class LogAuditoria {
    private Long id;
    private LocalDateTime fechaHora;
    private Integer idUsuario;
    private String nombreUsuario;
    private String ipUsuario;
    private String accion;
    private String tablaAfectada;
    private String valorAnterior;
    private String valorNuevo;
    private String descripcion;
    private String estado;
    private Integer aprobadoPorAdminId;

    // Constructor
    public LogAuditoria() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getIpUsuario() {
        return ipUsuario;
    }

    public void setIpUsuario(String ipUsuario) {
        this.ipUsuario = ipUsuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getAprobadoPorAdminId() {
        return aprobadoPorAdminId;
    }

    public void setAprobadoPorAdminId(Integer aprobadoPorAdminId) {
        this.aprobadoPorAdminId = aprobadoPorAdminId;
    }

    // Método builder para facilitar la creación
    public static LogAuditoriaBuilder builder() {
        return new LogAuditoriaBuilder();
    }

    // Clase Builder interna
    public static class LogAuditoriaBuilder {
        private LogAuditoria log;

        public LogAuditoriaBuilder() {
            log = new LogAuditoria();
        }

        public LogAuditoriaBuilder fechaHora(LocalDateTime fechaHora) {
            log.setFechaHora(fechaHora);
            return this;
        }

        public LogAuditoriaBuilder idUsuario(Integer idUsuario) {
            log.setIdUsuario(idUsuario);
            return this;
        }

        public LogAuditoriaBuilder nombreUsuario(String nombreUsuario) {
            log.setNombreUsuario(nombreUsuario);
            return this;
        }

        public LogAuditoriaBuilder ipUsuario(String ipUsuario) {
            log.setIpUsuario(ipUsuario);
            return this;
        }

        public LogAuditoriaBuilder accion(String accion) {
            log.setAccion(accion);
            return this;
        }

        public LogAuditoriaBuilder tablaAfectada(String tablaAfectada) {
            log.setTablaAfectada(tablaAfectada);
            return this;
        }

        public LogAuditoriaBuilder valorAnterior(String valorAnterior) {
            log.setValorAnterior(valorAnterior);
            return this;
        }

        public LogAuditoriaBuilder valorNuevo(String valorNuevo) {
            log.setValorNuevo(valorNuevo);
            return this;
        }

        public LogAuditoriaBuilder descripcion(String descripcion) {
            log.setDescripcion(descripcion);
            return this;
        }

        public LogAuditoriaBuilder estado(String estado) {
            log.setEstado(estado);
            return this;
        }

        public LogAuditoriaBuilder aprobadoPorAdminId(Integer aprobadoPorAdminId) {
            log.setAprobadoPorAdminId(aprobadoPorAdminId);
            return this;
        }

        public LogAuditoria build() {
            return log;
        }

        public LogAuditoriaBuilder id(long long1) { 
            throw new UnsupportedOperationException("Unimplemented method 'id'");
        }
    }
}
