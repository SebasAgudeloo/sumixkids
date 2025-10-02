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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

public class RecuperarServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RecuperarServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("recuperar.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        
        // Mantener el correo en caso de error
        request.setAttribute("correo", correo);
        
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("error", "❌ Debes ingresar tu correo electrónico");
            request.setAttribute("errorCorreo", true);
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }
        
        // Validar formato de correo
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!correo.matches(emailPattern)) {
            request.setAttribute("error", "❌ Por favor, ingresa un correo electrónico válido");
            request.setAttribute("errorCorreo", true);
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }
        
        Usuario usuario = UsuarioDAO.buscarPorCorreo(correo);
        if (usuario == null) {
            request.setAttribute("error", "❌ No existe una cuenta registrada con ese correo electrónico");
            request.setAttribute("errorCorreo", true);
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
            String asunto = "🔐 Recuperación de contraseña - SumixKids";
            
            String fechaHora = EmailService.getCurrentFormattedDateTime();
            String mensaje = EmailService.getEmailHeader() +
                "<h2 style='color: #FF9800; margin-bottom: 20px;'>¡Hola " + usuario.getNombres() + " " + usuario.getApellidos() + "! 👋</h2>" +
                "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
                "<p style='font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 15px;'>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en <strong>SumixKids</strong>.</p>" +
                "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 20px;'>Fecha y hora de la solicitud: <strong>" + fechaHora + "</strong></p>" +
                "<div style='background-color: #FFF3E0; padding: 20px; border-radius: 8px; text-align: center; border: 2px solid #FF9800; margin: 20px 0;'>" +
                "<p style='margin: 0 0 10px 0; color: #E65100; font-weight: bold; font-size: 14px;'>Tu código de recuperación es:</p>" +
                "<p style='font-size: 32px; font-weight: bold; color: #FF9800; margin: 10px 0; letter-spacing: 3px; font-family: monospace;'>" + codigo + "</p>" +
                "<p style='margin: 10px 0 0 0; color: #E65100; font-size: 12px;'>⏰ Válido por 15 minutos - Solo se puede usar una vez</p>" +
                "</div>" +
                "<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #C62828; font-weight: bold;'>⚠️ Importante:</p>" +
                "<p style='margin: 10px 0 0 0; color: #C62828; font-size: 14px;'>Si tú no solicitaste este cambio, por favor ignora este mensaje o cambia tu contraseña inmediatamente desde tu perfil.</p>" +
                "</div>" +
                "</div>" +
                EmailService.getEmailFooter() +
                EmailService.getEmailCloser();
            
            emailService.sendHtmlEmail(correo, asunto, mensaje);
            request.setAttribute("mensaje", "✅ Se ha enviado un código de recuperación a tu correo electrónico. Por favor, revisa tu bandeja de entrada");
        } catch (Exception e) {
            logger.error("No se pudo enviar el correo de recuperación a {}", correo, e);
            request.setAttribute("error", "❌ No se pudo enviar el correo. Por favor, verifica tu conexión a internet e intenta nuevamente");
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
