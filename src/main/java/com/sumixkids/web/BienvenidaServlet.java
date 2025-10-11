package com.sumixkids.web;

import com.sumixkids.model.Usuario;
import com.sumixkids.dao.UsuarioDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Página de saludo que sólo se muestra si el usuario ya inició sesión.
 */
public class BienvenidaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        // Si es admin, cargar lista de usuarios con paginación, filtro y búsqueda
        if (u != null && u.getRolId() != null && u.getRolId() == 1) {
            String busqueda = req.getParameter("busqueda");
            String rolParam = req.getParameter("rol");
            Integer rolId = null;
            if (rolParam != null && !rolParam.isEmpty() && !rolParam.equals("todos")) {
                try { rolId = Integer.parseInt(rolParam); } catch (Exception ignored) {}
            }
            int page = 1;
            int pageSize = 10;
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                try { page = Integer.parseInt(pageParam); } catch (Exception ignored) {}
                if (page < 1) page = 1;
            }
            int offset = (page - 1) * pageSize;
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            try {
                java.util.List<Usuario> usuarios = usuarioDAO.buscarUsuarios(busqueda, rolId, offset, pageSize);
                int totalUsuarios = usuarioDAO.contarUsuarios(busqueda, rolId);
                int totalPages = (int) Math.ceil((double) totalUsuarios / pageSize);
                
                // Obtener conteos por rol para el dashboard
                int totalAdmins = usuarioDAO.getCountByRol(1);
                int totalDocentes = usuarioDAO.getCountByRol(2);
                int totalEstudiantes = usuarioDAO.getCountByRol(3);
                int totalPadres = usuarioDAO.getCountByRol(4);
                int totalTodosUsuarios = usuarioDAO.getTotalUsers();
                
                req.setAttribute("usuarios", usuarios);
                req.setAttribute("totalPages", totalPages);
                req.setAttribute("currentPage", page);
                req.setAttribute("busqueda", busqueda);
                req.setAttribute("rolSeleccionado", rolId);
                req.setAttribute("totalUsuarios", totalUsuarios);
                
                // Conteos para dashboard
                req.setAttribute("totalAdmins", totalAdmins);
                req.setAttribute("totalDocentes", totalDocentes);
                req.setAttribute("totalEstudiantes", totalEstudiantes);
                req.setAttribute("totalPadres", totalPadres);
                req.setAttribute("totalTodosUsuarios", totalTodosUsuarios);
            } catch (Exception e) {
                req.setAttribute("error", "No se pudo cargar la lista de usuarios: " + e.getMessage());
            }
        }
        req.getRequestDispatcher("/bienvenida.jsp").forward(req, resp);
    }
}
