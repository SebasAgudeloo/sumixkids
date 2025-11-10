package com.sumixkids.dao;

import com.sumixkids.config.DatabaseManager;
import com.sumixkids.model.Asignacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO para gestionar las asignaciones docente-estudiante.
 * Implementa operaciones CRUD y consultas con JOIN para mostrar nombres.
 * Trabaja con la tabla 'docente_estudiante' en la base de datos.
 */
public class AsignacionDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(AsignacionDAO.class);

    /**
     * Lista todas las asignaciones con información de docente, estudiante y grado.
     */
    public List<Asignacion> listar() throws SQLException {
        List<Asignacion> lista = new ArrayList<>();
        String sql = """
            SELECT de.*, 
                   CONCAT(ud.nombres, ' ', ud.apellidos) AS nombre_docente,
                   CONCAT(ue.nombres, ' ', ue.apellidos) AS nombre_estudiante,
                   CONCAT(g.nombre_grado, g.nombre_grupo) AS nombre_grado,
                   CONCAT(ua.nombres, ' ', ua.apellidos) AS nombre_asignado_por
            FROM docente_estudiante de
            LEFT JOIN usuarios ud ON de.id_docente = ud.id
            LEFT JOIN usuarios ue ON de.id_estudiante = ue.id  
            LEFT JOIN grados_escolares g ON de.id_grado_escolar = g.id_grado
            LEFT JOIN usuarios ua ON de.asignado_por = ua.id
            ORDER BY de.fecha_creacion DESC
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearAsignacion(rs));
            }
        }
        return lista;
    }

    /**
     * Obtiene una asignación específica por ID.
     */
    public Asignacion obtenerPorId(int id) throws SQLException {
        String sql = """
            SELECT de.*, 
                   CONCAT(ud.nombres, ' ', ud.apellidos) AS nombre_docente,
                   CONCAT(ue.nombres, ' ', ue.apellidos) AS nombre_estudiante,
                   CONCAT(g.nombre_grado, g.nombre_grupo) AS nombre_grado,
                   CONCAT(ua.nombres, ' ', ua.apellidos) AS nombre_asignado_por
            FROM docente_estudiante de
            LEFT JOIN usuarios ud ON de.id_docente = ud.id
            LEFT JOIN usuarios ue ON de.id_estudiante = ue.id
            LEFT JOIN grados_escolares g ON de.id_grado_escolar = g.id_grado
            LEFT JOIN usuarios ua ON de.asignado_por = ua.id
            WHERE de.id = ?
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAsignacion(rs);
                }
            }
        }
        return null;
    }

    /**
     * Inserta una nueva asignación en la base de datos.
     */
    public int insertar(Asignacion asignacion) throws SQLException {
        String sql = """
            INSERT INTO docente_estudiante (id_docente, id_estudiante, id_grado_escolar, 
                                          fecha_asignacion, fecha_finalizacion, estado, 
                                          observaciones, asignado_por, fecha_creacion)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, asignacion.getIdDocente());
            ps.setInt(2, asignacion.getIdEstudiante());
            
            if (asignacion.getIdGradoEscolar() != null) {
                ps.setInt(3, asignacion.getIdGradoEscolar());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.setTimestamp(4, asignacion.getFechaAsignacion() != null ? 
                           Timestamp.valueOf(asignacion.getFechaAsignacion()) : 
                           Timestamp.valueOf(LocalDateTime.now()));
            
            if (asignacion.getFechaFinalizacion() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(asignacion.getFechaFinalizacion()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            
            ps.setString(6, asignacion.getEstado() != null ? asignacion.getEstado() : "ACTIVA");
            ps.setString(7, asignacion.getObservaciones());
            
            if (asignacion.getAsignadoPor() != null) {
                ps.setInt(8, asignacion.getAsignadoPor());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            
            int result = ps.executeUpdate();
            if (result > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Actualiza una asignación existente.
     */
    public boolean actualizar(Asignacion asignacion) throws SQLException {
        String sql = """
            UPDATE docente_estudiante 
            SET id_docente = ?, id_estudiante = ?, id_grado_escolar = ?, 
                fecha_asignacion = ?, fecha_finalizacion = ?, estado = ?, 
                observaciones = ?, asignado_por = ?, fecha_actualizacion = ?
            WHERE id = ?
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, asignacion.getIdDocente());
            ps.setInt(2, asignacion.getIdEstudiante());
            
            if (asignacion.getIdGradoEscolar() != null) {
                ps.setInt(3, asignacion.getIdGradoEscolar());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.setTimestamp(4, Timestamp.valueOf(asignacion.getFechaAsignacion()));
            
            if (asignacion.getFechaFinalizacion() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(asignacion.getFechaFinalizacion()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            
            ps.setString(6, asignacion.getEstado());
            ps.setString(7, asignacion.getObservaciones());
            
            if (asignacion.getAsignadoPor() != null) {
                ps.setInt(8, asignacion.getAsignadoPor());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(10, asignacion.getId());
            
            int result = ps.executeUpdate();
            return result > 0;
        }
    }

    /**
     * Elimina una asignación (eliminación lógica cambiando estado a FINALIZADA).
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE docente_estudiante SET estado = 'FINALIZADA', fecha_finalizacion = ?, fecha_actualizacion = ? WHERE id = ?";
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, id);
            
            int result = ps.executeUpdate();
            return result > 0;
        }
    }

    /**
     * Elimina físicamente una asignación de la base de datos.
     */
    public boolean eliminarFisicamente(int id) throws SQLException {
        String sql = "DELETE FROM docente_estudiante WHERE id = ?";
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            int result = ps.executeUpdate();
            return result > 0;
        }
    }

    /**
     * Obtiene todos los docentes disponibles para asignar.
     */
    public List<Map<String, Object>> obtenerDocentes() throws SQLException {
        List<Map<String, Object>> docentes = new ArrayList<>();
        String sql = "SELECT id, CONCAT(nombres, ' ', apellidos) as nombre FROM usuarios WHERE rol_id = 2 ORDER BY nombres, apellidos";
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> docente = new HashMap<>();
                docente.put("id", rs.getInt("id"));
                docente.put("nombre", rs.getString("nombre"));
                docentes.add(docente);
            }
        }
        return docentes;
    }

    /**
     * Obtiene todos los estudiantes disponibles para asignar.
     */
    public List<Map<String, Object>> obtenerEstudiantes() throws SQLException {
        List<Map<String, Object>> estudiantes = new ArrayList<>();
        String sql = "SELECT id, CONCAT(nombres, ' ', apellidos) as nombre, grado FROM usuarios WHERE rol_id = 3 ORDER BY nombres, apellidos";
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> estudiante = new HashMap<>();
                estudiante.put("id", rs.getInt("id"));
                estudiante.put("nombre", rs.getString("nombre"));
                estudiante.put("grado", rs.getString("grado"));
                estudiantes.add(estudiante);
            }
        }
        return estudiantes;
    }

    /**
     * Obtiene todos los grados escolares disponibles.
     */
    public List<Map<String, Object>> obtenerGrados() throws SQLException {
        List<Map<String, Object>> grados = new ArrayList<>();
        String sql = "SELECT id_grado, CONCAT(nombre_grado, nombre_grupo) as nombre, descripcion FROM grados_escolares WHERE activo = 1 ORDER BY nombre_grado, nombre_grupo";
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> grado = new HashMap<>();
                grado.put("id", rs.getInt("id_grado"));
                grado.put("nombre", rs.getString("nombre"));
                grado.put("descripcion", rs.getString("descripcion"));
                grados.add(grado);
            }
        }
        return grados;
    }

    /**
     * Obtiene asignaciones por docente.
     */
    public List<Asignacion> obtenerPorDocente(int idDocente) throws SQLException {
        List<Asignacion> lista = new ArrayList<>();
        String sql = """
            SELECT de.*, 
                   CONCAT(ud.nombres, ' ', ud.apellidos) AS nombre_docente,
                   CONCAT(ue.nombres, ' ', ue.apellidos) AS nombre_estudiante,
                   CONCAT(g.nombre_grado, g.nombre_grupo) AS nombre_grado,
                   CONCAT(ua.nombres, ' ', ua.apellidos) AS nombre_asignado_por
            FROM docente_estudiante de
            LEFT JOIN usuarios ud ON de.id_docente = ud.id
            LEFT JOIN usuarios ue ON de.id_estudiante = ue.id
            LEFT JOIN grados_escolares g ON de.id_grado_escolar = g.id_grado
            LEFT JOIN usuarios ua ON de.asignado_por = ua.id
            WHERE de.id_docente = ?
            ORDER BY de.fecha_creacion DESC
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsignacion(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Obtiene asignaciones por estudiante.
     */
    public List<Asignacion> obtenerPorEstudiante(int idEstudiante) throws SQLException {
        List<Asignacion> lista = new ArrayList<>();
        String sql = """
            SELECT de.*, 
                   CONCAT(ud.nombres, ' ', ud.apellidos) AS nombre_docente,
                   CONCAT(ue.nombres, ' ', ue.apellidos) AS nombre_estudiante,
                   CONCAT(g.nombre_grado, g.nombre_grupo) AS nombre_grado,
                   CONCAT(ua.nombres, ' ', ua.apellidos) AS nombre_asignado_por
            FROM docente_estudiante de
            LEFT JOIN usuarios ud ON de.id_docente = ud.id
            LEFT JOIN usuarios ue ON de.id_estudiante = ue.id
            LEFT JOIN grados_escolares g ON de.id_grado_escolar = g.id_grado
            LEFT JOIN usuarios ua ON de.asignado_por = ua.id
            WHERE de.id_estudiante = ?
            ORDER BY de.fecha_creacion DESC
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAsignacion(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Verifica si ya existe una asignación activa entre un docente y estudiante.
     */
    public boolean existeAsignacionActiva(int idDocente, int idEstudiante) throws SQLException {
        String sql = "SELECT 1 FROM docente_estudiante WHERE id_docente = ? AND id_estudiante = ? AND estado = 'ACTIVA' LIMIT 1";
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idDocente);
            ps.setInt(2, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Mapea un ResultSet a un objeto Asignacion.
     */
    private Asignacion mapearAsignacion(ResultSet rs) throws SQLException {
        Asignacion asignacion = new Asignacion();
        
        asignacion.setId(rs.getInt("id"));
        asignacion.setIdDocente(rs.getInt("id_docente"));
        asignacion.setIdEstudiante(rs.getInt("id_estudiante"));
        
        int gradoId = rs.getInt("id_grado_escolar");
        if (!rs.wasNull()) {
            asignacion.setIdGradoEscolar(gradoId);
        }
        
        Timestamp fechaAsignacion = rs.getTimestamp("fecha_asignacion");
        if (fechaAsignacion != null) {
            asignacion.setFechaAsignacion(fechaAsignacion.toLocalDateTime());
        }
        
        Timestamp fechaFinalizacion = rs.getTimestamp("fecha_finalizacion");
        if (fechaFinalizacion != null) {
            asignacion.setFechaFinalizacion(fechaFinalizacion.toLocalDateTime());
        }
        
        asignacion.setEstado(rs.getString("estado"));
        asignacion.setObservaciones(rs.getString("observaciones"));
        
        int asignadoPor = rs.getInt("asignado_por");
        if (!rs.wasNull()) {
            asignacion.setAsignadoPor(asignadoPor);
        }
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            asignacion.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        Timestamp fechaActualizacion = rs.getTimestamp("fecha_actualizacion");
        if (fechaActualizacion != null) {
            asignacion.setFechaActualizacion(fechaActualizacion.toLocalDateTime());
        }
        
        // Campos de los JOINs
        asignacion.setNombreDocente(rs.getString("nombre_docente"));
        asignacion.setNombreEstudiante(rs.getString("nombre_estudiante"));
        asignacion.setNombreGrado(rs.getString("nombre_grado"));
        asignacion.setNombreAsignadoPor(rs.getString("nombre_asignado_por"));
        
        return asignacion;
    }
}