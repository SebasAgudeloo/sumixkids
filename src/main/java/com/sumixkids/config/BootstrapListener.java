package com.sumixkids.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.sumixkids.model.RoleType;
import com.sumixkids.dao.DispositivoReconocidoDAO;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Se ejecuta automáticamente cuando la aplicación web inicia en el servidor.
 * Aquí nos aseguramos de que los nombres de roles básicos existan en la base de datos.
 * Si ya existen, no pasa nada; si faltan, se insertan.
 */
@WebListener
public class BootstrapListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Forzar zona horaria global desde config.properties
        try {
            Properties props = new Properties();
            props.load(BootstrapListener.class.getClassLoader().getResourceAsStream("config.properties"));
            String tz = props.getProperty("app.timezone", "America/Bogota");
            TimeZone.setDefault(TimeZone.getTimeZone(tz));
            logger.info("Zona horaria global configurada: {}", tz);
        } catch (Exception e) {
            logger.error("No se pudo configurar zona horaria", e);
        }
        try {
            // Recorremos todos los roles definidos en el enum y los creamos si faltan.
            for (RoleType rt : RoleType.values()) {
                ensureRole(rt.dbName());
            }
            
            // Limpiar dispositivos antiguos al inicio
            DispositivoReconocidoDAO dispositivoDAO = new DispositivoReconocidoDAO();
            dispositivoDAO.limpiarDispositivosAntiguos();
            logger.info("Limpieza de dispositivos antiguos completada");
            
        } catch (Exception e) {
            logger.error("Problema en inicialización del sistema", e);
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

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No cleanup needed
    }
}
