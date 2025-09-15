package com.sumixkids.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.sumixkids.model.Usuario;

/**
 * Filtro global para proteger rutas según el rol del usuario.
 * Solo permite acceso a rutas protegidas si el usuario tiene el rol adecuado.
 */
@WebFilter("/*")
public class SecurityFilter implements Filter {
    // Rutas públicas (no requieren autenticación)
    private static final String[] PUBLIC_PATHS = {
        "/login", "/registro", "/recuperar", "/restablecer", "/2fa", "/css/", "/images/", "/js/", "/favicon.ico", "/"
    };

    // Rutas solo para admin
    private static final String[] ADMIN_PATHS = {
        "/bienvenida", "/CambiarRolUsuarioServlet", "/EliminarUsuarioServlet"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Permitir recursos estáticos y rutas públicas
        for (String pub : PUBLIC_PATHS) {
            if (path.startsWith(pub)) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        // Si no está autenticado, redirigir a login
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Si es ruta de admin, verificar rol
        for (String admin : ADMIN_PATHS) {
            if (path.startsWith(admin)) {
                if (usuario.getRolId() == null || usuario.getRolId() != 1) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: solo para administradores");
                    return;
                }
            }
        }

        // Si pasa todos los filtros, continuar
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
