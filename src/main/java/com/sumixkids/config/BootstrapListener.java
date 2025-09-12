package com.sumixkids.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.sumixkids.model.RoleType;

/**
 * Se ejecuta automáticamente cuando la aplicación web inicia en el servidor.
 * Aquí nos aseguramos de que los nombres de roles básicos existan en la base de datos.
 * Si ya existen, no pasa nada; si faltan, se insertan.
 */
@WebListener
public class BootstrapListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Recorremos todos los roles definidos en el enum y los creamos si faltan.
            for (RoleType rt : RoleType.values()) {
                ensureRole(rt.dbName());
            }
        } catch (Exception e) {
            // Si algo falla sólo lo mostramos por consola para no detener el arranque.
            System.err.println("[BootstrapListener] Problema creando roles iniciales: " + e.getMessage());
        }
    }

    /**
     * Intenta insertar el rol indicado.
     * Si el rol ya existe, la base de datos lo ignora y no genera error.
     */
    private void ensureRole(String roleName) throws SQLException {
        String sql = "INSERT IGNORE INTO roles (nombre_rol) VALUES (?)";
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            ps.executeUpdate(); // No necesitamos revisar resultado.
        }
    }
}
