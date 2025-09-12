package com.sumixkids.web;

import com.sumixkids.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Página de saludo que sólo se muestra si el usuario ya inició sesión.
 */
@WebServlet(name = "BienvenidaServlet", urlPatterns = {"/bienvenida"})
public class BienvenidaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        // Ya está conectado, mostramos la vista.
        req.getRequestDispatcher("/bienvenida.jsp").forward(req, resp);
    }
}
