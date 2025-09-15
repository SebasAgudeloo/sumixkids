package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.sumixkids.dao.TwoFactorCodeDAO;
import com.sumixkids.model.Usuario;

@WebServlet(name = "TwoFactorServlet", urlPatterns = {"/2fa"})
public class TwoFactorServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(TwoFactorServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/2fa.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String code = req.getParameter("codigo");
        boolean valido = false;
        try {
            valido = TwoFactorCodeDAO.validarCodigo(usuario.getId(), code);
        } catch (Exception e) {
            logger.error("Error al validar el código 2FA para usuario {}", usuario.getUsername(), e);
            req.setAttribute("error", "Error al validar el código: " + e.getMessage());
            req.getRequestDispatcher("/2fa.jsp").forward(req, resp);
            return;
        }
        if (valido) {
            try {
                TwoFactorCodeDAO.marcarComoUsado(usuario.getId(), code);
                TwoFactorCodeDAO.eliminarCodigosExpiradosYUsados();
            } catch (Exception e) {
                logger.warn("Error limpiando códigos 2FA expirados/usados para usuario {}", usuario.getUsername(), e);
                // Si falla la limpieza, no bloquea el login
            }
            session.setAttribute("2fa_passed", true);
            session.removeAttribute("2fa_code");
            resp.sendRedirect(req.getContextPath() + "/bienvenida");
        } else {
            req.setAttribute("error", "Código incorrecto, expirado o ya usado. Intenta de nuevo.");
            req.getRequestDispatcher("/2fa.jsp").forward(req, resp);
        }
    }
}
