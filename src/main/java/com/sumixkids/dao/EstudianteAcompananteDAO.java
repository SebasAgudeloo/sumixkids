package com.sumixkids.dao;

import com.sumixkids.config.DatabaseManager;
import com.sumixkids.model.EstudianteAcompanante;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO para gestionar vínculos Estudiante-acompañante.
 * ESTRUCTURA REAL DE LA DB:
 * - Tabla: estudiante_acompañante (con ñ en DB)
 * - Campos: id, estudiante_id, acompañante_id (con ñ), tipo_relacion, fecha_vinculacion, activo
 * - FKs: estudiante_id → estudiantes.id, acompañante_id → acompañante.id
 * - ENUM tipo_relacion: 'PRINCIPAL','SECUNDARIO','EMERGENCIA'
 */
public class EstudianteAcompananteDAO {

    /**
     * Lista todos los vínculos con información completa usando la estructura real de DB.
     * TABLA: estudiante_acompañante
     * CAMPOS: id, estudiante_id, acompañante_id (CON TILDE), tipo_relacion, fecha_vinculacion, activo
     */
    public List<EstudianteAcompanante> listar() throws SQLException {
        List<EstudianteAcompanante> lista = new ArrayList<>();
        
        // Consulta con JOIN usando la estructura REAL de la base de datos
        String sql = """
            SELECT 
                ea.id, 
                ea.estudiante_id, 
                ea.acompañante_id, 
                ea.tipo_relacion, 
                ea.fecha_vinculacion, 
                ea.activo,
                CONCAT(ue.nombres, ' ', ue.apellidos) as nombre_estudiante,
                CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_acompanante
            FROM estudiante_acompañante ea
            INNER JOIN estudiantes e ON ea.estudiante_id = e.id
            INNER JOIN usuarios ue ON e.usuario_id = ue.id
            INNER JOIN acompañante a ON ea.acompañante_id = a.id
            INNER JOIN usuarios ua ON a.usuario_id = ua.id
            ORDER BY ea.fecha_vinculacion DESC
            """;
        
        System.out.println("DEBUG LISTAR - Ejecutando consulta con estructura real de DB:");
        System.out.println(sql);
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            int contador = 0;
            while (rs.next()) {
                contador++;
                int id = rs.getInt("id");
                int estudianteId = rs.getInt("estudiante_id");
                int acompananteId = rs.getInt("acompañante_id"); // CON TILDE en DB
                String tipoRelacion = rs.getString("tipo_relacion");
                boolean activo = rs.getBoolean("activo");
                Timestamp fechaVinculacion = rs.getTimestamp("fecha_vinculacion");
                String nombreEstudiante = rs.getString("nombre_estudiante");
                String nombreAcompanante = rs.getString("nombre_acompanante");
                
                System.out.println("DEBUG LISTAR - Fila " + contador + ": ID=" + id + 
                                 ", EstudianteID=" + estudianteId + 
                                 ", AcompañanteID=" + acompananteId +
                                 ", Tipo=" + tipoRelacion +
                                 ", Activo=" + activo +
                                 ", NombreEst=" + nombreEstudiante +
                                 ", NombreAcomp=" + nombreAcompanante);
                
                EstudianteAcompanante v = new EstudianteAcompanante();
                v.setId(id);
                v.setEstudianteId(estudianteId);
                v.setAcompananteId(acompananteId);
                v.setTipoRelacion(tipoRelacion);
                v.setActivo(activo);
                
                if (fechaVinculacion != null) v.setFechaVinculacion(fechaVinculacion.toLocalDateTime());
                
                // Nombres reales desde JOIN
                v.setNombreEstudiante(nombreEstudiante);
                v.setNombreAcompanante(nombreAcompanante);
                
                lista.add(v);
            }
            System.out.println("DEBUG LISTAR - Total vínculos procesados con nombres reales: " + contador);
        } catch (SQLException e) {
            System.out.println("DEBUG LISTAR - Error en consulta JOIN: " + e.getMessage());
            e.printStackTrace();
            // Fallback a consulta simple
            return listarSinJoins();
        }
        return lista;
    }
    
    /**
     * Método de respaldo sin JOINs si hay problemas con las tablas relacionadas
     */
    private List<EstudianteAcompanante> listarSinJoins() throws SQLException {
        List<EstudianteAcompanante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiante_acompañante ORDER BY fecha_vinculacion DESC";
        
        System.out.println("DEBUG - Usando consulta de respaldo sin JOINs");
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                EstudianteAcompanante v = new EstudianteAcompanante();
                v.setId(rs.getInt("id"));
                v.setEstudianteId(rs.getInt("estudiante_id"));
                v.setAcompananteId(rs.getInt("acompañante_id")); // CON TILDE
                v.setTipoRelacion(rs.getString("tipo_relacion"));
                v.setActivo(rs.getBoolean("activo"));
                
                Timestamp fv = rs.getTimestamp("fecha_vinculacion");
                if (fv != null) v.setFechaVinculacion(fv.toLocalDateTime());
                
                // Nombres temporales
                v.setNombreEstudiante("Estudiante ID " + rs.getInt("estudiante_id"));
                v.setNombreAcompanante("Acompañante ID " + rs.getInt("acompañante_id"));
                
                lista.add(v);
            }
        }
        return lista;
    }

    /**
     * Obtiene un vínculo específico por ID.
     */
    public EstudianteAcompanante obtenerPorId(int id) throws SQLException {
        String sql = """
            SELECT 
                ea.id, ea.estudiante_id, ea.acompañante_id, ea.tipo_relacion, 
                ea.fecha_vinculacion, ea.activo,
                CONCAT(ue.nombres, ' ', ue.apellidos) as nombre_estudiante,
                CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_acompanante
            FROM estudiante_acompañante ea
            INNER JOIN estudiantes e ON ea.estudiante_id = e.id
            INNER JOIN usuarios ue ON e.usuario_id = ue.id
            INNER JOIN acompañante a ON ea.acompañante_id = a.id  
            INNER JOIN usuarios ua ON a.usuario_id = ua.id
            WHERE ea.id = ?
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    /**
     * Inserta un nuevo vínculo.
     * NOTA: En DB es acompañante_id (con ñ)
     */
    public int insertar(EstudianteAcompanante vinculo) throws SQLException {
        // Validaciones - PATRÓN COMO AsignacionDAO: validar usuarios con rol correcto
        if (!existeUsuarioConRol(vinculo.getEstudianteId(), 3)) {
            throw new SQLException("El ID no corresponde a un usuario con rol estudiante");
        }
        if (!existeUsuarioConRol(vinculo.getAcompananteId(), 4)) {
            throw new SQLException("El ID no corresponde a un usuario con rol acompañante");
        }
        
        // Validar si ya existe un vínculo PRINCIPAL para este estudiante
        if ("PRINCIPAL".equals(vinculo.getTipoRelacion())) {
            Integer estudianteDbId = obtenerEstudianteIdPorUsuario(vinculo.getEstudianteId());
            if (estudianteDbId != null && existeVinculoPrincipal(estudianteDbId)) {
                throw new SQLException("Este estudiante ya tiene un acompañante PRINCIPAL asignado. Solo puede tener un acompañante principal. Puede asignar acompañantes SECUNDARIOS o de EMERGENCIA.");
            }
        }

        String sql = """
            INSERT INTO estudiante_acompañante 
            (estudiante_id, acompañante_id, tipo_relacion, fecha_vinculacion, activo) 
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Convertir usuarios.id a estudiantes.id y acompañante.id
            Integer estudianteDbId = obtenerEstudianteIdPorUsuario(vinculo.getEstudianteId());
            Integer acompananteDbId = obtenerAcompananteIdPorUsuario(vinculo.getAcompananteId());
            
            if (estudianteDbId == null) {
                throw new SQLException("No se encontró registro de estudiante para usuario ID: " + vinculo.getEstudianteId());
            }
            if (acompananteDbId == null) {
                throw new SQLException("No se encontró registro de acompañante para usuario ID: " + vinculo.getAcompananteId());
            }
            
            stmt.setInt(1, estudianteDbId);
            stmt.setInt(2, acompananteDbId);
            stmt.setString(3, vinculo.getTipoRelacion());
            stmt.setObject(4, vinculo.getFechaVinculacion() != null ? 
                          vinculo.getFechaVinculacion() : LocalDateTime.now());
            stmt.setBoolean(5, vinculo.getActivo() != null ? vinculo.getActivo() : true);

            int filasAfectadas = stmt.executeUpdate();
            System.out.println("DEBUG INSERTAR - Filas afectadas: " + filasAfectadas);
            System.out.println("DEBUG INSERTAR - Estudiante DB ID: " + estudianteDbId + " - Acompañante DB ID: " + acompananteDbId);
            System.out.println("DEBUG INSERTAR - Tipo relación: " + vinculo.getTipoRelacion());
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        System.out.println("DEBUG INSERTAR - Nuevo vínculo creado con ID: " + newId);
                        return newId;
                    }
                }
            }
        }
        System.out.println("DEBUG INSERTAR - Error: No se pudo crear el vínculo");
        return -1;
    }

    /**
     * Actualiza un vínculo existente.
     * Solo se puede cambiar tipo_relacion y activo según estructura real de DB
     */
    public boolean actualizar(EstudianteAcompanante vinculo) throws SQLException {
        String sql = """
            UPDATE estudiante_acompañante 
            SET tipo_relacion = ?, activo = ?
            WHERE id = ?
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, vinculo.getTipoRelacion());
            ps.setBoolean(2, vinculo.getActivo());
            ps.setInt(3, vinculo.getId());
            
            return ps.executeUpdate() > 0;
        }
    }

    public boolean desactivar(int id) throws SQLException {
        String sql = "UPDATE estudiante_acompañante SET activo = false WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean activar(int id) throws SQLException {
        String sql = "UPDATE estudiante_acompañante SET activo = true WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM estudiante_acompañante WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Obtiene lista de estudiantes para el select.
     * PATRÓN COMO AsignacionDAO: usa usuarios.id directamente, sin requerir tabla estudiantes
     */
    public List<Map<String, Object>> obtenerEstudiantes() throws SQLException {
        List<Map<String, Object>> estudiantes = new ArrayList<>();
        String sql = """
            SELECT u.id, CONCAT(u.nombres, ' ', u.apellidos) as nombre, u.grado 
            FROM usuarios u 
            WHERE u.rol_id = 3 
            ORDER BY u.nombres, u.apellidos
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> est = new HashMap<>();
                est.put("id", rs.getInt("id"));
                est.put("nombre", rs.getString("nombre"));
                est.put("grado", rs.getString("grado"));
                estudiantes.add(est);
            }
        }
        return estudiantes;
    }

    /**
     * Obtiene lista de acompañantes para el select.
     * PATRÓN COMO AsignacionDAO: usa usuarios.id directamente, sin requerir tabla acompañante
     */
    public List<Map<String, Object>> obtenerAcompanantes() throws SQLException {
        List<Map<String, Object>> acompanantes = new ArrayList<>();
        String sql = """
            SELECT u.id, CONCAT(u.nombres, ' ', u.apellidos) as nombre, u.email 
            FROM usuarios u 
            WHERE u.rol_id = 4 
            ORDER BY u.nombres, u.apellidos
            """;
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> aco = new HashMap<>();
                aco.put("id", rs.getInt("id"));
                aco.put("nombre", rs.getString("nombre"));
                aco.put("email", rs.getString("email"));
                acompanantes.add(aco);
            }
        }
        return acompanantes;
    }

    /**
     * Obtiene vínculos por estudiante.
     */
    public List<EstudianteAcompanante> obtenerPorEstudiante(int estudianteId, boolean soloActivos) throws SQLException {
        List<EstudianteAcompanante> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT 
                ea.id, ea.estudiante_id, ea.acompañante_id, ea.tipo_relacion, 
                ea.fecha_vinculacion, ea.activo,
                CONCAT(ue.nombres, ' ', ue.apellidos) as nombre_estudiante,
                CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_acompanante
            FROM estudiante_acompañante ea
            INNER JOIN estudiantes e ON ea.estudiante_id = e.id
            INNER JOIN usuarios ue ON e.usuario_id = ue.id
            INNER JOIN acompañantes a ON ea.acompañante_id = a.id  
            INNER JOIN usuarios ua ON a.usuario_id = ua.id
            WHERE ea.estudiante_id = ?
            """);
        
        if (soloActivos) sql.append(" AND ea.activo = 1");
        sql.append(" ORDER BY ea.fecha_vinculacion DESC");

        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Obtiene vínculos por acompañante.
     */
    public List<EstudianteAcompanante> obtenerPorAcompanante(int acompananteId, boolean soloActivos) throws SQLException {
        List<EstudianteAcompanante> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT 
                ea.id, ea.estudiante_id, ea.acompañante_id, ea.tipo_relacion, 
                ea.fecha_vinculacion, ea.activo,
                CONCAT(ue.nombres, ' ', ue.apellidos) as nombre_estudiante,
                CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_acompanante
            FROM estudiante_acompañante ea
            INNER JOIN estudiantes e ON ea.estudiante_id = e.id
            INNER JOIN usuarios ue ON e.usuario_id = ue.id
            INNER JOIN acompañante a ON ea.acompañante_id = a.id  
            INNER JOIN usuarios ua ON a.usuario_id = ua.id
            WHERE ea.acompañante_id = ?
            """);
        
        if (soloActivos) sql.append(" AND ea.activo = 1");
        sql.append(" ORDER BY ea.fecha_vinculacion DESC");

        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            ps.setInt(1, acompananteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public int contarPorAcompanante(int acompananteId, boolean soloActivos) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM estudiante_acompañante WHERE acompañante_id = ?");
        if (soloActivos) sb.append(" AND activo = 1");
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sb.toString())) {
            ps.setInt(1, acompananteId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    public int contarPorEstudiante(int estudianteId, boolean soloActivos) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM estudiante_acompañante WHERE estudiante_id = ?");
        if (soloActivos) sb.append(" AND activo = 1");
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sb.toString())) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    public boolean existeVinculoActivo(int estudianteId, int acompananteId) throws SQLException {
        String sql = "SELECT 1 FROM estudiante_acompañante WHERE estudiante_id = ? AND acompañante_id = ? AND activo = 1 LIMIT 1";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ps.setInt(2, acompananteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Helpers
    private boolean existeVinculoTx(Connection cn, int estudianteId, int acompananteId) throws SQLException {
        String sql = "SELECT 1 FROM estudiante_acompañante WHERE estudiante_id = ? AND acompañante_id = ? LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ps.setInt(2, acompananteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * PATRÓN COMO AsignacionDAO: Verifica que exista un usuario con el rol específico
     */
    private boolean existeUsuarioConRol(Integer usuarioId, int rolId) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE id = ? AND rol_id = ? LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    /**
     * Verifica si ya existe un vínculo PRINCIPAL activo para el estudiante
     */
    private boolean existeVinculoPrincipal(Integer estudianteId) throws SQLException {
        String sql = "SELECT 1 FROM estudiante_acompañante WHERE estudiante_id = ? AND tipo_relacion = 'PRINCIPAL' AND activo = 1 LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    /**
     * Obtiene el ID de la tabla estudiantes basado en usuarios.id
     * Si no existe, lo crea automáticamente
     */
    private Integer obtenerEstudianteIdPorUsuario(Integer usuarioId) throws SQLException {
        // Primero intentar obtener existente
        String sqlSelect = "SELECT id FROM estudiantes WHERE usuario_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        
        // Si no existe, crear uno básico
        String sqlInsert = "INSERT INTO estudiantes (usuario_id, grado_actual, estado_academico) SELECT ?, grado, 'ACTIVO' FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }
    
    /**
     * Obtiene el ID de la tabla acompañante basado en usuarios.id
     * Si no existe, lo crea automáticamente
     */
    private Integer obtenerAcompananteIdPorUsuario(Integer usuarioId) throws SQLException {
        // Primero intentar obtener existente
        String sqlSelect = "SELECT id FROM acompañante WHERE usuario_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        
        // Si no existe, crear uno básico
        String sqlInsert = "INSERT INTO acompañante (usuario_id, relacion_estudiante, recibir_notificaciones, autorizacion_recoger) VALUES (?, 'OTRO', 1, 1)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    private Integer getRolId(Connection cn, int userId) throws SQLException {
        String sql = "SELECT rol_id FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return null;
    }

    private EstudianteAcompanante mapear(ResultSet rs) throws SQLException {
        EstudianteAcompanante v = new EstudianteAcompanante();
        v.setId(rs.getInt("id"));
        v.setEstudianteId(rs.getInt("estudiante_id"));
        v.setAcompananteId(rs.getInt("acompañante_id")); // NOTA: columna con ñ en DB
        v.setTipoRelacion(rs.getString("tipo_relacion"));
        
        Timestamp fv = rs.getTimestamp("fecha_vinculacion");
        if (fv != null) v.setFechaVinculacion(fv.toLocalDateTime());
        
        v.setActivo(rs.getBoolean("activo"));
        
        // Campos adicionales de JOIN (si existen)
        try {
            v.setNombreEstudiante(rs.getString("nombre_estudiante"));
            v.setNombreAcompanante(rs.getString("nombre_acompanante"));
        } catch (SQLException e) {
            // Estos campos pueden no existir en todas las consultas
        }
        
        return v;
    }


}
