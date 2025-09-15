package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.util.PasswordUtil;
import com.sumixkids.dao.PasswordResetDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/restablecer")
public class RestablecerServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RestablecerServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("restablecer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String usuarioCorreo = request.getParameter("usuario_correo");
        String codigo = request.getParameter("codigo");
        String nueva1 = request.getParameter("nueva1");
        String nueva2 = request.getParameter("nueva2");
    HttpSession session = request.getSession();

        if (usuarioCorreo == null || usuarioCorreo.trim().isEmpty() || codigo == null || nueva1 == null || nueva2 == null ||
            codigo.trim().isEmpty() || nueva1.trim().isEmpty() || nueva2.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("restablecer.jsp").forward(request, response);
            return;
        }
        // Buscar usuario por correo o usuario
        Usuario user = null;
        try {
            user = new UsuarioDAO().findByUsernameOrEmail(usuarioCorreo);
        } catch (Exception e) {
            logger.error("Error al buscar el usuario {}", usuarioCorreo, e);
            request.setAttribute("error", "Error al buscar el usuario: " + e.getMessage());
            request.getRequestDispatcher("restablecer.jsp").forward(request, response);
            return;
        }
        if (user == null) {
            request.setAttribute("error", "No se encontró una cuenta con ese usuario o correo.");
            request.getRequestDispatcher("restablecer.jsp").forward(request, response);
            return;
        }
        try {
            boolean valido = PasswordResetDAO.validarCodigo(user.getId(), codigo);
            if (!valido) {
                request.setAttribute("error", "El código ingresado es incorrecto, expiró o ya fue usado.");
                request.getRequestDispatcher("restablecer.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            logger.error("Error al validar el código de recuperación para usuario {}", user.getUsername(), e);
            request.setAttribute("error", "Error al validar el código: " + e.getMessage());
            request.getRequestDispatcher("restablecer.jsp").forward(request, response);
            return;
        }
        if (!nueva1.equals(nueva2)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("restablecer.jsp").forward(request, response);
            return;
        }
        try {
            String hash = PasswordUtil.hash(nueva1);
            new UsuarioDAO().updatePassword(user.getId(), hash);
            // Marcar código como usado en la base de datos
            PasswordResetDAO.marcarComoUsado(user.getId(), codigo);
            // Eliminar códigos expirados o usados
            PasswordResetDAO.eliminarCodigosExpiradosYUsados();
            // Limpiar sesión de recuperación
            session.removeAttribute("correo_recuperacion");
            session.removeAttribute("usuario_recuperacion");
            request.setAttribute("mensaje", "Contraseña actualizada correctamente. Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error al actualizar la contraseña para usuario {}", user.getUsername(), e);
            request.setAttribute("error", "Error al actualizar la contraseña: " + e.getMessage());
            request.getRequestDispatcher("restablecer.jsp").forward(request, response);
        }
    }
}
