package com.sumixkids.filter;

import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.Usuario;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter("/admin/*")
public class AdminSecurityFilter implements Filter {
    private LogAuditoriaDAO logDAO = new LogAuditoriaDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String ipAddress = req.getRemoteAddr();

        if (session == null || session.getAttribute("usuario") == null) {
            try {
                logDAO.registrarIntentoAcceso(
                    ipAddress,
                    "DESCONOCIDO",
                    "ACCESO_DENEGADO",
                    "Intento de acceso a área administrativa sin autenticación: " + req.getRequestURI()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Verificar si el rol del usuario NO es administrador (rolId = 1)
        if (usuario.getRolId() != 1) {
            try {
                logDAO.registrarIntentoAcceso(
                    ipAddress,
                    usuario.getUsername(),
                    "ACCESO_DENEGADO",
                    "Intento de acceso a área administrativa sin privilegios: " + req.getRequestURI()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }

            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
