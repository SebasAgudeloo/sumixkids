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

public class RecuperarPasswordServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RecuperarPasswordServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("recuperar_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        
        // Mantener el correo en caso de error
        request.setAttribute("correo", correo);
        
        if (correo == null || correo.trim().isEmpty()) {
            request.setAttribute("error", "❌ Debes ingresar tu correo electrónico");
            request.setAttribute("errorCorreo", true);
            request.getRequestDispatcher("recuperar_password.jsp").forward(request, response);
            return;
        }
        
        // Validar formato de correo
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!correo.matches(emailPattern)) {
            request.setAttribute("error", "❌ Por favor, ingresa un correo electrónico válido");
            request.setAttribute("errorCorreo", true);
            request.getRequestDispatcher("recuperar_password.jsp").forward(request, response);
            return;
        }
        
        Usuario usuario = UsuarioDAO.buscarPorCorreo(correo);
        if (usuario == null) {
            request.setAttribute("error", "❌ No existe una cuenta registrada con ese correo electrónico");
            request.setAttribute("errorCorreo", true);
            request.getRequestDispatcher("recuperar_password.jsp").forward(request, response);
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
            request.getRequestDispatcher("recuperar_password.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        session.setAttribute("correo_recuperacion", correo);
        session.setAttribute("usuario_recuperacion", usuario.getUsername());
        // Enviar correo usando EmailService
        try {
            // Leer usuario y contraseña de Gmail desde config.properties
            Properties props = new Properties();
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) props.load(in);
            }
            String gmail = props.getProperty("mail.smtp.user");
            String pass = props.getProperty("mail.smtp.pass");
            EmailService emailService = new EmailService(gmail, pass);
            
            // Crear enlace de recuperación con el código
            String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + 
                             request.getServerPort() + request.getContextPath() + 
                             "/restablecer_password?codigo=" + codigo;
            
            // Usar el método específico del EmailService para recuperación de contraseña
            emailService.sendPasswordRecoveryEmail(correo, usuario.getNombres(), 
                                                 usuario.getApellidos(), resetLink);
            request.setAttribute("mensaje", "✅ Se ha enviado un código de recuperación a tu correo electrónico. Por favor, revisa tu bandeja de entrada");
        } catch (Exception e) {
            logger.error("No se pudo enviar el correo de recuperación a {}", correo, e);
            request.setAttribute("error", "❌ No se pudo enviar el correo. Por favor, verifica tu conexión a internet e intenta nuevamente");
            request.getRequestDispatcher("recuperar_password.jsp").forward(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/restablecer_password");
    }

    private String generarCodigo() {
        // Generar código alfabético de 6 letras para recuperación de contraseña
        SecureRandom random = new SecureRandom();
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder codigo = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            codigo.append(letras.charAt(random.nextInt(letras.length())));
        }
        
        return codigo.toString();
    }
}
