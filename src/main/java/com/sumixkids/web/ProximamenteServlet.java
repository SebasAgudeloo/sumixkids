package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

/**
 * Servlet genérico para manejar páginas "Próximamente"
 * Sirve páginas JSP estáticas sin necesidad de crear un servlet por cada una
 */
public class ProximamenteServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProximamenteServlet.class);
    
    // Lista de páginas "Próximamente" disponibles
    private static final Set<String> PAGINAS_DISPONIBLES = new HashSet<>();
    
    static {
        // Páginas que creamos con diseño "Próximamente"
        PAGINAS_DISPONIBLES.add("cambiar_password");
        PAGINAS_DISPONIBLES.add("perfil");
        PAGINAS_DISPONIBLES.add("configuracion");
        PAGINAS_DISPONIBLES.add("citas");
        PAGINAS_DISPONIBLES.add("mensajes");
        PAGINAS_DISPONIBLES.add("notificaciones");
        PAGINAS_DISPONIBLES.add("seguimiento");
        PAGINAS_DISPONIBLES.add("vincular_hijo");
        PAGINAS_DISPONIBLES.add("mis_hijos");
        PAGINAS_DISPONIBLES.add("certificados");
        PAGINAS_DISPONIBLES.add("mi_progreso");
        PAGINAS_DISPONIBLES.add("tareas");
        PAGINAS_DISPONIBLES.add("lecciones");
        PAGINAS_DISPONIBLES.add("mis_logros");
        PAGINAS_DISPONIBLES.add("juegos");
        PAGINAS_DISPONIBLES.add("estadisticas");
        PAGINAS_DISPONIBLES.add("reportes_docente");
        PAGINAS_DISPONIBLES.add("calificaciones");
        PAGINAS_DISPONIBLES.add("estudiantes");
        PAGINAS_DISPONIBLES.add("crear_clase");
        PAGINAS_DISPONIBLES.add("mis_clases");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener la página solicitada desde la URL
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        // Remover el context path para obtener solo la ruta
        String pagina = requestURI.substring(contextPath.length() + 1); // +1 para remover la barra inicial
        
        // Verificar si la página está disponible
        if (!PAGINAS_DISPONIBLES.contains(pagina)) {
            logger.warn("Página no disponible solicitada: {}", pagina);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Construir el nombre del archivo JSP
        String jspFile = pagina + ".jsp";
        
        try {
            // Servir el JSP correspondiente
            request.getRequestDispatcher("/" + jspFile).forward(request, response);
            logger.debug("Sirviendo página próximamente: {}", jspFile);
        } catch (Exception e) {
            logger.error("Error al servir página próximamente: {}", jspFile, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirigir POST a GET para páginas estáticas
        doGet(request, response);
    }
}