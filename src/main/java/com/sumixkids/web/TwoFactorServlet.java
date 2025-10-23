package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.sumixkids.dao.TwoFactorCodeDAO;
import com.sumixkids.dao.DispositivoReconocidoDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.util.ClienteUtil;

public class TwoFactorServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(TwoFactorServlet.class);
    private final DispositivoReconocidoDAO dispositivoDAO = new DispositivoReconocidoDAO();
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
            logger.info("Código 2FA válido para usuario: {}", usuario.getUsername());
            try {
                // Marcar código como usado y limpiar códigos expirados
                TwoFactorCodeDAO.marcarComoUsado(usuario.getId(), code);
                TwoFactorCodeDAO.eliminarCodigosExpiradosYUsados();
                logger.info("Código 2FA marcado como usado para usuario: {}", usuario.getUsername());
                
                // Registrar 2FA exitoso para el dispositivo (de forma asíncrona para no bloquear)
                try {
                    String clienteIP = ClienteUtil.getClienteIP(req);
                    String userAgent = ClienteUtil.getUserAgent(req);
                    logger.info("Intentando registrar dispositivo - Usuario: {}, IP: {}", usuario.getUsername(), clienteIP);
                    
                    dispositivoDAO.registrar2FAExitoso(usuario.getId(), clienteIP, userAgent);
                } catch (Exception deviceException) {
                    // Si falla el registro del dispositivo, no debe bloquear el login
                    logger.warn("Error registrando dispositivo para usuario {}: {}", 
                               usuario.getUsername(), deviceException.getMessage());
                }
                
                logger.info("2FA exitoso registrado - Usuario: {}", usuario.getUsername());
                           
            } catch (Exception e) {
                logger.warn("Error en procesamiento post-2FA para usuario {}: {}", 
                           usuario.getUsername(), e.getMessage());
                // Los errores en limpieza o registro no deben bloquear el login
            }
            
            session.setAttribute("2fa_passed", true);
            session.removeAttribute("2fa_code");
            logger.info("Redirigiendo a bienvenida para usuario: {}", usuario.getUsername());
            resp.sendRedirect(req.getContextPath() + "/bienvenida");
        } else {
            req.setAttribute("error", "Código incorrecto, expirado o ya usado. Intenta de nuevo.");
            req.getRequestDispatcher("/2fa.jsp").forward(req, resp);
        }
    }
}
