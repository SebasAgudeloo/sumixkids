package com.sumixkids.dao;

import com.sumixkids.config.DatabaseManager;
import com.sumixkids.model.RoleType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    /**
     * Devuelve todos los nombres de roles guardados en la base, ordenados alfabéticamente.
     * Ejemplo de uso: para mostrar opciones en un formulario.
     */
    public List<String> findAllNames() throws SQLException {
        String sql = "SELECT nombre_rol FROM roles ORDER BY nombre_rol ASC";
        List<String> out = new ArrayList<>();
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(rs.getString(1)); // Agrega el texto del rol a la lista.
            }
        }
        return out;
    }
    
    /**
     * Obtiene todos los roles como objetos RoleType
     */
    public List<RoleType> findAll() throws SQLException {
        String sql = "SELECT id, nombre_rol FROM roles ORDER BY id ASC";
        List<RoleType> roles = new ArrayList<>();
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre_rol");
                
                // Mapear el nombre del rol a RoleType
                switch (nombre.toLowerCase()) {
                    case "admin":
                    case "administrador":
                        roles.add(RoleType.ADMIN);
                        break;
                    case "docent":
                    case "docente":
                        roles.add(RoleType.DOCENT);
                        break;
                    case "student":
                    case "estudiante":
                        roles.add(RoleType.STUDENT);
                        break;
                    case "attendant":
                    case "acompañante":
                    case "tutor":
                        roles.add(RoleType.ATTENDANT);
                        break;
                }
            }
        }
        return roles;
    }
    
    /**
     * Obtiene el RoleType basándose en el ID del rol
     */
    public RoleType findRoleTypeById(Integer rolId) throws SQLException {
        if (rolId == null) {
            return null;
        }
        
        String sql = "SELECT nombre_rol FROM roles WHERE id = ?";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, rolId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre_rol");
                    
                    // Mapear el nombre del rol a RoleType
                    switch (nombre.toLowerCase()) {
                        case "admin":
                        case "administrador":
                            return RoleType.ADMIN;
                        case "docent":
                        case "docente":
                            return RoleType.DOCENT;
                        case "student":
                        case "estudiante":
                            return RoleType.STUDENT;
                        case "attendant":
                        case "acompañante":
                        case "tutor":
                            return RoleType.ATTENDANT;
                        default:
                            return null;
                    }
                }
            }
        }
        return null;
    }
}
