package com.sumixkids.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class SessionTimeoutFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
            FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // No verificar timeout para páginas públicas
        String path = req.getRequestURI();
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        if (session != null) {
            Long lastActivity = (Long) session.getAttribute("lastActivity");
            long currentTime = System.currentTimeMillis();
            
            // Solo verificar timeout si existe lastActivity (30 minutos = 1,800,000 ms)
            if (lastActivity != null && currentTime - lastActivity > 1800000) {
                session.invalidate();
                res.sendRedirect(req.getContextPath() + "/login?timeout=true");
                return;
            }
            
            session.setAttribute("lastActivity", currentTime);
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.endsWith("/login") || 
               path.endsWith("/registro") ||
               path.contains("/css/") ||
               path.contains("/js/") ||
               path.contains("/images/");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
