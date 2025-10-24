package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.RoleType;
import com.sumixkids.dao.RoleDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet que maneja la página de inicio con redirección inteligente
 * - Si no hay sesión: muestra la página pública (index.jsp)
 * - Si hay sesión: redirige al dashboard correspondiente según el rol
 */
public class IndexServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(IndexServlet.class);
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Si no hay sesión activa, mostrar la página pública
        if (session == null) {
            logger.debug("No hay sesión activa, mostrando página pública");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        
        // Verificar si hay un usuario logueado
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            logger.debug("Sesión existe pero no hay usuario, mostrando página pública");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        
        // Usuario logueado, redirigir según su rol
        try {
            RoleDAO roleDAO = new RoleDAO();
            RoleType rol = roleDAO.findRoleTypeById(usuario.getRolId());
            String redirectUrl = determinarDashboardPorRol(rol);
            
            if (redirectUrl != null) {
                logger.debug("Usuario {} con rol {} redirigido a {}", 
                            usuario.getUsername(), rol, redirectUrl);
                response.sendRedirect(request.getContextPath() + redirectUrl);
            } else {
                // Rol desconocido, redirigir a página genérica
                logger.warn("Rol desconocido para usuario {}: {}", usuario.getUsername(), rol);
                response.sendRedirect(request.getContextPath() + "/bienvenida");
            }
        } catch (Exception e) {
            logger.error("Error al obtener rol del usuario {}", usuario.getUsername(), e);
            response.sendRedirect(request.getContextPath() + "/bienvenida");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirigir POST a GET
        doGet(request, response);
    }
    
    /**
     * Determina a qué dashboard redirigir según el rol del usuario
     */
    private String determinarDashboardPorRol(RoleType rol) {
        if (rol == null) {
            return "/bienvenida";
        }
        
        switch (rol) {
            case ADMIN:
                return "/bienvenida";  // Los admins van al dashboard general
            case DOCENT:
                return "/bienvenida";  // Los docentes van al dashboard general
            case STUDENT:
                return "/bienvenida";  // Los estudiantes van al dashboard general
            case PARENTS:
                return "/bienvenida";  // Los padres van al dashboard general
            default:
                return "/bienvenida";  // Fallback por seguridad
        }
    }
}