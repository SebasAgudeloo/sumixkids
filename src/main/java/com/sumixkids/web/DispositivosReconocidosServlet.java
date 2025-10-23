package com.sumixkids.web;

import com.sumixkids.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sumixkids.config.DatabaseManager;

/**
 * Servlet para gestionar dispositivos reconocidos del usuario
 */
public class DispositivosReconocidosServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(DispositivosReconocidosServlet.class);
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        try {
            List<Map<String, Object>> dispositivos = obtenerDispositivosUsuario(usuario.getId());
            req.setAttribute("dispositivos", dispositivos);
            req.setAttribute("usuario", usuario);
            
        } catch (SQLException e) {
            logger.error("Error obteniendo dispositivos del usuario {}", usuario.getUsername(), e);
            req.setAttribute("error", "Error al cargar la información de dispositivos");
        }
        
        req.getRequestDispatcher("/dispositivos_reconocidos.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String action = req.getParameter("action");
        
        if ("eliminar".equals(action)) {
            String deviceId = req.getParameter("deviceId");
            if (deviceId != null && !deviceId.trim().isEmpty()) {
                try {
                    eliminarDispositivo(usuario.getId(), deviceId);
                    req.setAttribute("mensaje", "Dispositivo eliminado correctamente");
                    logger.info("Dispositivo eliminado - Usuario: {}, DeviceID: {}", 
                               usuario.getUsername(), deviceId);
                } catch (SQLException e) {
                    logger.error("Error eliminando dispositivo del usuario {}", usuario.getUsername(), e);
                    req.setAttribute("error", "Error al eliminar el dispositivo");
                }
            }
        }
        
        // Recargar la página
        doGet(req, resp);
    }
    
    /**
     * Obtiene todos los dispositivos reconocidos de un usuario
     */
    private List<Map<String, Object>> obtenerDispositivosUsuario(int usuarioId) throws SQLException {
        List<Map<String, Object>> dispositivos = new ArrayList<>();
        
        String sql = "SELECT device_id, user_agent, fecha_ultimo_2fa, contador_2fa, fecha_registro " +
                     "FROM dispositivos_reconocidos " +
                     "WHERE usuario_id = ? " +
                     "ORDER BY fecha_ultimo_2fa DESC";
            
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, usuarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> dispositivo = new HashMap<>();
                    dispositivo.put("deviceId", rs.getString("device_id"));
                    dispositivo.put("userAgent", rs.getString("user_agent"));
                    dispositivo.put("fechaUltimo2FA", rs.getTimestamp("fecha_ultimo_2fa"));
                    dispositivo.put("contador2FA", rs.getInt("contador_2fa"));
                    dispositivo.put("fechaRegistro", rs.getTimestamp("fecha_registro"));
                    
                    // Extraer información del navegador del User-Agent
                    String navegador = extraerNombreNavegador(rs.getString("user_agent"));
                    dispositivo.put("navegador", navegador);
                    
                    // Calcular estado
                    java.time.LocalDateTime ultimo = rs.getTimestamp("fecha_ultimo_2fa").toLocalDateTime();
                    java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
                    boolean activo = ultimo.plusDays(15).isAfter(ahora);
                    dispositivo.put("activo", activo);
                    
                    dispositivos.add(dispositivo);
                }
            }
        }
        
        return dispositivos;
    }
    
    /**
     * Elimina un dispositivo específico del usuario
     */
    private void eliminarDispositivo(int usuarioId, String deviceId) throws SQLException {
        String sql = "DELETE FROM dispositivos_reconocidos WHERE usuario_id = ? AND device_id = ?";
        
        try (Connection cn = DatabaseManager.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, usuarioId);
            ps.setString(2, deviceId);
            ps.executeUpdate();
        }
    }
    
    /**
     * Extrae el nombre del navegador del User-Agent
     */
    private String extraerNombreNavegador(String userAgent) {
        if (userAgent == null) return "Desconocido";
        
        userAgent = userAgent.toLowerCase();
        
        if (userAgent.contains("chrome") && !userAgent.contains("edge")) {
            return "Chrome";
        } else if (userAgent.contains("firefox")) {
            return "Firefox";
        } else if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
            return "Safari";
        } else if (userAgent.contains("edge")) {
            return "Edge";
        } else if (userAgent.contains("opera")) {
            return "Opera";
        } else {
            return "Otro";
        }
    }
}