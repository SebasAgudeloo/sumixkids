package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet para eliminar usuarios de forma segura.
 * Recibe parámetro userId por POST.
 */
@WebServlet(name = "EliminarUsuarioServlet", urlPatterns = {"/eliminarUsuario"})
public class EliminarUsuarioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdStr = req.getParameter("userId");
        if (userIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro userId");
            return;
        }
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "userId inválido");
            return;
        }
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
            boolean eliminado = usuarioDAO.deleteUser(userId);
            if (eliminado) {
                // Cerrar sesión y redirigir a login con mensaje
                req.getSession().invalidate();
                req.setAttribute("mensaje", "Usuario eliminado correctamente. ¡Hasta pronto!");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            } else {
                // Redirigir de vuelta a bienvenida con mensaje de error
                req.setAttribute("error", "No se puede eliminar el usuario porque tiene procesos o registros activos asociados.");
                req.getRequestDispatcher("/bienvenida.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException("Error al eliminar usuario", e);
        }
    }
}
