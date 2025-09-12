package com.sumixkids.dao;

import com.sumixkids.config.DatabaseManager;

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
}
