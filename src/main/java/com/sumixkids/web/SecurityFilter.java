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
        "/usuarios", "/admin/", "/auditoria", "/carga_masiva"
    };

    // Rutas que requieren autenticación pero no rol específico
    private static final String[] AUTHENTICATED_PATHS = {
        "/bienvenida", "/registros_asociados", "/eliminar_usuario", "/eliminar_confirmar.jsp",
        "/usuario_eliminar", "/admin_eliminar", "/logout", "/editar_usuario", "/cambiar_estado"
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

        System.out.println("[SecurityFilter] Procesando path: " + path);

        // Permitir recursos estáticos y rutas públicas
        for (String pub : PUBLIC_PATHS) {
            if (path.startsWith(pub)) {
                System.out.println("[SecurityFilter] Ruta pública permitida: " + path);
                chain.doFilter(request, response);
                return;
            }
        }

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        // Si no está autenticado, redirigir a login
        if (usuario == null) {
            System.out.println("[SecurityFilter] Usuario no autenticado, redirigiendo");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        System.out.println("[SecurityFilter] Usuario: " + usuario.getNombres() + ", Rol ID: " + usuario.getRolId());

        // Verificar rutas autenticadas (cualquier usuario logueado puede acceder)
        for (String auth : AUTHENTICATED_PATHS) {
            if (path.startsWith(auth)) {
                System.out.println("[SecurityFilter] Ruta autenticada permitida: " + path);
                chain.doFilter(request, response);
                return;
            }
        }

        // Si es ruta de admin, verificar rol
        for (String admin : ADMIN_PATHS) {
            if (path.startsWith(admin)) {
                if (usuario.getRolId() == null || usuario.getRolId() != 1) {
                    System.out.println("[SecurityFilter] Acceso denegado a admin para usuario: " + usuario.getNombres());
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: solo para administradores");
                    return;
                }
                System.out.println("[SecurityFilter] Acceso admin permitido");
                break;
            }
        }

        // Si pasa todos los filtros, continuar
        System.out.println("[SecurityFilter] Permitiendo acceso a: " + path);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
