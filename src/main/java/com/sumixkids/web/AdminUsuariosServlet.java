package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminUsuariosServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(AdminUsuariosServlet.class.getName());
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        if (usuarioSesion.getRolId() != 1) { // Solo administradores
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        
        try {
            // Obtener todos los usuarios
            logger.info("Obteniendo lista de usuarios...");
            List<Usuario> usuarios = usuarioDAO.findAll();
            
            if (usuarios == null) {
                logger.severe("La lista de usuarios es NULL");
                usuarios = new java.util.ArrayList<>();
            } else {
                logger.info("Usuarios obtenidos: " + usuarios.size());
                for (Usuario u : usuarios) {
                    logger.info("Usuario: ID=" + u.getId() + ", Username=" + u.getUsername() + ", RolId=" + u.getRolId() + ", Bloqueado=" + u.getBloqueado());
                }
            }
            
            request.setAttribute("usuarios", usuarios);
            
            // Obtener estadísticas por rol
            logger.info("Obteniendo estadísticas por rol...");
            Map<String, Object> estadisticas = usuarioDAO.getEstadisticasPorRol();
            request.setAttribute("totalUsuarios", usuarios.size());
            request.setAttribute("totalAdmins", estadisticas.getOrDefault("administradores", 0));
            request.setAttribute("totalDocentes", estadisticas.getOrDefault("docentes", 0));
            request.setAttribute("totalEstudiantes", estadisticas.getOrDefault("estudiantes", 0));
            request.setAttribute("totalPadres", estadisticas.getOrDefault("padres", 0));
            logger.info("Estadísticas obtenidas correctamente");
            
            logger.info("Cargando gestión de usuarios para administrador: " + usuarioSesion.getUsername());
            
            // Redirigir al JSP
            logger.info("Enviando a JSP: /gestion_usuarios.jsp");
            request.getRequestDispatcher("/gestion_usuarios.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar gestión de usuarios", e);
            e.printStackTrace(); // Para ver el stacktrace completo
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error al cargar la página de gestión de usuarios: " + e.getMessage());
        }
    }
}
