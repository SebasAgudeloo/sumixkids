package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumixkids.service.EmailService;
import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.dao.PasswordResetDAO;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@WebServlet("/recuperar")
public class RecuperarServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RecuperarServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("recuperar.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("error", "Debes ingresar tu correo electrónico.");
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }
        Usuario usuario = UsuarioDAO.buscarPorCorreo(correo);
        if (usuario == null) {
            request.setAttribute("error", "No existe una cuenta con ese correo.");
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }
        // Generar código seguro y guardar en BD con PasswordResetDAO
        String codigo = generarCodigo();
        LocalDateTime expiresAt = ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime().plusMinutes(15); // 15 minutos de validez
        try {
            PasswordResetDAO.guardarCodigo(usuario.getId(), codigo, expiresAt);
            PasswordResetDAO.eliminarCodigosExpiradosYUsados();
        } catch (Exception e) {
            logger.error("No se pudo guardar el código de recuperación para {}", usuario.getUsername(), e);
            request.setAttribute("error", "No se pudo guardar el código de recuperación: " + e.getMessage());
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        session.setAttribute("correo_recuperacion", correo);
        session.setAttribute("usuario_recuperacion", usuario.getUsername());
        // Enviar correo
        try {
            // Leer usuario y contraseña de Gmail desde config.properties
            Properties props = new Properties();
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) props.load(in);
            }
            String gmail = props.getProperty("mail.smtp.user");
            String pass = props.getProperty("mail.smtp.pass");
            EmailService emailService = new EmailService(gmail, pass);
            String asunto = "Recuperación de contraseña - SumixKids";
            String mensaje = "Hola " + usuario.getUsername() + ",\n\n" +
                "Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en SumixKids.\n" +
                "\n" +
                "Tu código de recuperación es: " + codigo + "\n" +
                "Este código es válido por 15 minutos y solo puede usarse una vez.\n" +
                "\n" +
                "Si tú no solicitaste este cambio, por favor ignora este mensaje o cambia tu contraseña inmediatamente desde tu perfil.\n" +
                "\n" +
                "Gracias por confiar en SumixKids.\n" +
                "\n" +
                "Atentamente,\n" +
                "El equipo de SumixKids";
            emailService.sendEmail(correo, asunto, mensaje);
            request.setAttribute("mensaje", "Se ha enviado un código a tu correo electrónico.");
        } catch (Exception e) {
            logger.error("No se pudo enviar el correo de recuperación a {}", correo, e);
            request.setAttribute("error", "No se pudo enviar el correo: " + e.getMessage());
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("restablecer.jsp");
    }

    private String generarCodigo() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[4];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
