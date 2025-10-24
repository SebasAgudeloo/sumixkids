package com.sumixkids.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.util.PasswordUtil;
import com.sumixkids.dao.PasswordResetDAO;
import com.sumixkids.service.EmailService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RestablecerPasswordServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RestablecerPasswordServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Si viene un código en la URL, pasarlo al JSP
        String codigo = request.getParameter("codigo");
        if (codigo != null && !codigo.trim().isEmpty()) {
            request.setAttribute("codigo", codigo);
        }
        request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuarioCorreo = request.getParameter("usuario_correo");
        String codigo = request.getParameter("codigo");
        String nueva1 = request.getParameter("nueva1");
        String nueva2 = request.getParameter("nueva2");
        HttpSession session = request.getSession();

        // Mantener valores en caso de error
        request.setAttribute("usuario_correo", usuarioCorreo);
        request.setAttribute("codigo", codigo);

        if (usuarioCorreo == null || usuarioCorreo.trim().isEmpty() || codigo == null || nueva1 == null || nueva2 == null ||
            codigo.trim().isEmpty() || nueva1.trim().isEmpty() || nueva2.trim().isEmpty()) {
            request.setAttribute("error", "❌ Todos los campos son obligatorios");
            request.setAttribute("errorUsuarioCorreo", true);
            request.setAttribute("errorCodigo", true);
            request.setAttribute("errorPassword", true);
            request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
            return;
        }
        
        // Validar formato de código alfabético para recuperación de contraseña (6 letras)
        if (!codigo.matches("^[A-Za-z]{6}$")) {
            request.setAttribute("error", "❌ El código debe tener 6 letras (solo letras, sin números)");
            request.setAttribute("errorCodigo", true);
            request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
            return;
        }
        
        // Validar contraseña (mínimo 5 letras, 2 números, 1 especial, máximo 20)
        String passwordPattern = "^(?=.*[A-Za-záéíóúÁÉÍÓÚñÑ]{5,})(?=.*\\d.*\\d)(?=.*[!@#$%^&*])[A-Za-záéíóúÁÉÍÓÚñÑ\\d!@#$%^&*]{8,20}$";
        if (!nueva1.matches(passwordPattern)) {
            request.setAttribute("error", "❌ La contraseña debe tener mínimo 5 letras, 2 números, 1 carácter especial y máximo 20 caracteres");
            request.setAttribute("errorPassword", true);
            request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
            return;
        }
        // Buscar usuario por correo o usuario
        Usuario user = null;
        try {
            user = new UsuarioDAO().findByUsernameOrEmail(usuarioCorreo);
        } catch (Exception e) {
            logger.error("Error al buscar el usuario {}", usuarioCorreo, e);
            request.setAttribute("error", "Error al buscar el usuario: " + e.getMessage());
            request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
            return;
        }
        if (user == null) {
            request.setAttribute("error", "❌ No se encontró una cuenta con ese usuario o correo");
            request.setAttribute("errorUsuarioCorreo", true);
            request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
            return;
        }
        try {
            boolean valido = PasswordResetDAO.validarCodigo(user.getId(), codigo);
            if (!valido) {
                request.setAttribute("error", "❌ El código ingresado es incorrecto, expiró o ya fue usado");
                request.setAttribute("errorCodigo", true);
                request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            logger.error("Error al validar el código de recuperación para usuario {}", user.getUsername(), e);
            request.setAttribute("error", "❌ Error al validar el código. Por favor, intenta nuevamente");
            request.setAttribute("errorCodigo", true);
            request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
            return;
        }
        if (!nueva1.equals(nueva2)) {
            request.setAttribute("error", "❌ Las contraseñas no coinciden");
            request.setAttribute("errorPassword", true);
            request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
            return;
        }
        try {
            String hash = PasswordUtil.hash(nueva1);
            new UsuarioDAO().updatePassword(user.getId(), hash);
            // Marcar código como usado en la base de datos
            PasswordResetDAO.marcarComoUsado(user.getId(), codigo);
            // Eliminar códigos expirados o usados
            PasswordResetDAO.eliminarCodigosExpiradosYUsados();
            
            // Enviar correo de confirmación
            enviarCorreoConfirmacion(user);
            
            // Limpiar sesión de recuperación
            session.removeAttribute("correo_recuperacion");
            session.removeAttribute("usuario_recuperacion");
            request.setAttribute("mensaje", "✅ Contraseña actualizada correctamente. Ahora puedes iniciar sesión con tu nueva contraseña");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error al actualizar la contraseña para usuario {}", user.getUsername(), e);
            request.setAttribute("error", "❌ Error al actualizar la contraseña. Por favor, intenta nuevamente o contacta al administrador");
            request.getRequestDispatcher("restablecer_password.jsp").forward(request, response);
        }
    }
    
    /**
     * Envía un correo de confirmación al usuario informando que su contraseña fue restablecida exitosamente
     */
    private void enviarCorreoConfirmacion(Usuario usuario) {
        try {
            // Leer credenciales de Gmail desde config.properties
            Properties props = new Properties();
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) props.load(in);
            }
            String gmail = props.getProperty("mail.smtp.user");
            String pass = props.getProperty("mail.smtp.pass");
            
            EmailService emailService = new EmailService(gmail, pass);
            String asunto = "✅ Contraseña restablecida exitosamente - SumixKids";
            
            String fechaHora = EmailService.getCurrentFormattedDateTime();
            String mensaje = EmailService.getEmailHeader() +
                "<h2 style='color: #4CAF50; margin-bottom: 20px;'>¡Hola " + usuario.getNombres() + " " + usuario.getApellidos() + "! 👋</h2>" +
                "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
                "<p style='font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 15px;'>Te confirmamos que tu contraseña en <strong>SumixKids</strong> ha sido <strong style='color: #4CAF50;'>restablecida exitosamente</strong>.</p>" +
                "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 20px;'>Fecha y hora del cambio: <strong>" + fechaHora + "</strong></p>" +
                "<div style='background-color: #E8F5E9; padding: 20px; border-radius: 8px; text-align: center; border: 2px solid #4CAF50; margin: 20px 0;'>" +
                "<p style='font-size: 18px; font-weight: bold; color: #2E7D32; margin: 0;'>✅ Tu cuenta está segura</p>" +
                "<p style='margin: 10px 0 0 0; color: #388E3C; font-size: 14px;'>Ya puedes iniciar sesión con tu nueva contraseña</p>" +
                "</div>" +
                "<div style='background-color: #FFF3E0; padding: 15px; border-radius: 8px; margin: 20px 0;'>" +
                "<p style='margin: 0 0 10px 0; color: #E65100; font-weight: bold;'>💡 Recomendaciones de seguridad:</p>" +
                "<ul style='margin: 0; padding-left: 20px; color: #555;'>" +
                "<li style='margin-bottom: 8px;'>No compartas tu contraseña con nadie</li>" +
                "<li style='margin-bottom: 8px;'>Usa contraseñas únicas para cada servicio</li>" +
                "<li style='margin-bottom: 8px;'>Cambia tu contraseña periódicamente</li>" +
                "<li>Cierra sesión cuando uses computadores públicos</li>" +
                "</ul>" +
                "</div>" +
                "<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #C62828; font-weight: bold;'>⚠️ ¿No fuiste tú?</p>" +
                "<p style='margin: 10px 0 0 0; color: #C62828; font-size: 14px;'>Si tú no realizaste este cambio, por favor contacta inmediatamente al administrador del sistema para proteger tu cuenta.</p>" +
                "</div>" +
                "</div>" +
                EmailService.getEmailFooter() +
                EmailService.getEmailCloser();
            
            emailService.sendHtmlEmail(usuario.getEmail(), asunto, mensaje);
            logger.info("Correo de confirmación enviado exitosamente a {}", usuario.getEmail());
            
        } catch (Exception e) {
            logger.error("Error al enviar correo de confirmación a {}", usuario.getEmail(), e);
            // No lanzamos la excepción para que el proceso continúe aunque falle el envío del correo
        }
    }
}
