
package com.sumixkids.service;

import java.util.Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio para enviar correos electrónicos usando SMTP de Gmail.
 */
public class EmailService {

    private final String username; // Correo Gmail
    private final String password; // Contraseña o App Password

    /**
     * Constructor del servicio de correo.
     * @param username Correo de Gmail
     * @param password Contraseña de Gmail o App Password
     */
    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Envía un correo electrónico simple (texto plano) usando SMTP de Gmail.
     * @param to Correo de destino
     * @param subject Asunto
     * @param text Cuerpo del mensaje
     * @throws MessagingException Si ocurre un error al enviar
     */
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        message.setText(text);

        Transport.send(message);
    }

    /**
     * Envía un correo electrónico con formato HTML.
     * @param to Correo de destino
     * @param subject Asunto
     * @param htmlContent Contenido HTML del mensaje
     * @throws MessagingException Si ocurre un error al enviar
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
    }

    /**
     * Obtiene la fecha y hora actual formateada para Colombia.
     * @return Fecha formateada como "2025-09-28 9:47 PM"
     */
    public static String getCurrentFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a", Locale.ENGLISH);
        return now.format(formatter);
    }

    /**
     * Genera el footer estándar para todos los correos de SumixKids.
     * @return HTML del footer
     */
    public static String getEmailFooter() {
        return "<div style='margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0e0e0; color: #666; font-size: 12px;'>" +
               "<p><strong>Gracias por confiar en SumixKids.</strong></p>" +
               "<p>El equipo de SumixKids</p>" +
               "<p>© 2025 SumixKids - Plataforma Educativa</p>" +
               "<p style='color: #999; font-style: italic;'>Este correo fue enviado automáticamente, por favor no responder.</p>" +
               "</div>";
    }

    /**
     * Genera el footer con mensaje de error para correos de alerta.
     * @return HTML del footer con mensaje de error
     */
    public static String getErrorEmailFooter() {
        return "<div style='margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0e0e0; color: #666; font-size: 12px;'>" +
               "<p style='color: #d32f2f; font-weight: bold;'>Si crees que esto fue un error, contacta con un docente encargado o un administrador.</p>" +
               "<p><strong>Gracias por confiar en SumixKids.</strong></p>" +
               "<p>El equipo de SumixKids</p>" +
               "<p>© 2025 SumixKids - Plataforma Educativa</p>" +
               "<p style='color: #999; font-style: italic;'>Este correo fue enviado automáticamente, por favor no responder.</p>" +
               "</div>";
    }

    /**
     * Genera el encabezado estándar para correos HTML.
     * @return HTML del encabezado
     */
    public static String getEmailHeader() {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; background-color: #ffffff;'>" +
               "<div style='background: linear-gradient(135deg, #4CAF50, #81C784); padding: 20px; text-align: center;'>" +
               "<h1 style='color: white; margin: 0; font-size: 28px;'>📚 SumixKids</h1>" +
               "<p style='color: #E8F5E8; margin: 5px 0 0 0; font-size: 14px;'>Plataforma Educativa</p>" +
               "</div>" +
               "<div style='padding: 30px; background-color: #fafafa;'>";
    }

    /**
     * Cierra el contenedor HTML del correo.
     * @return HTML de cierre
     */
    public static String getEmailCloser() {
        return "</div></div>";
    }

    /**
     * Genera un email de confirmación para auto-eliminación de cuenta.
     * @param usuario Usuario que elimina su cuenta
     * @return HTML del email
     */
    public static String generateSelfDeletionEmail(String nombres, String apellidos, String username) {
        String fechaHora = getCurrentFormattedDateTime();
        
        return getEmailHeader() +
            "<h2 style='color: #FF9800; margin-bottom: 20px;'>¡Hasta pronto " + nombres + " " + apellidos + "! 👋</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: #FFF3E0; padding: 20px; border-radius: 8px; border: 2px solid #FF9800; margin-bottom: 20px; text-align: center;'>" +
            "<h3 style='color: #E65100; margin: 0 0 15px 0;'>✅ CONFIRMACIÓN DE ELIMINACIÓN</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #E65100; margin: 0; font-weight: bold;'>Tu cuenta ha sido eliminada exitosamente</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>Hemos procesado tu solicitud de eliminación de cuenta en <strong>SumixKids</strong>.</p>" +
            "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #0D47A1; font-weight: bold;'>📊 Detalles del proceso:</p>" +
            "<ul style='margin: 0; color: #0D47A1; list-style: none; padding: 0;'>" +
            "<li style='margin-bottom: 8px;'><strong>👤 Usuario eliminado:</strong> " + username + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>📅 Fecha y hora:</strong> " + fechaHora + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>🔄 Eliminado por:</strong> El propio usuario</li>" +
            "</ul>" +
            "</div>" +
            "<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #C62828; font-weight: bold;'>⚠️ Información importante:</p>" +
            "<p style='margin: 10px 0 0 0; color: #C62828; font-size: 14px;'>Tu cuenta y todos los datos asociados han sido eliminados permanentemente. Esta acción no se puede deshacer.</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>¡Esperamos que hayas disfrutado tu tiempo en SumixKids! Si en el futuro deseas volver, siempre serás bienvenido para crear una nueva cuenta.</p>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si tienes algún comentario o sugerencia sobre tu experiencia, nos encantaría escucharte.</p>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
    }

    /**
     * Genera un email de notificación cuando un administrador bloquea una cuenta.
     * @param nombres Nombres del usuario
     * @param apellidos Apellidos del usuario
     * @param username Username del usuario
     * @param adminUsername Username del administrador que bloqueó la cuenta
     * @return HTML del email
     */
    public static String generateAccountBlockedEmail(String nombres, String apellidos, String username, String adminUsername) {
        String fechaHora = getCurrentFormattedDateTime();
        
        return getEmailHeader() +
            "<h2 style='color: #D32F2F; margin-bottom: 20px;'>⚠️ Cuenta bloqueada - " + nombres + " " + apellidos + "</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: #FFEBEE; padding: 20px; border-radius: 8px; border: 2px solid #D32F2F; margin-bottom: 20px; text-align: center;'>" +
            "<h3 style='color: #C62828; margin: 0 0 15px 0;'>🔒 TU CUENTA HA SIDO BLOQUEADA</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #C62828; margin: 0; font-weight: bold;'>Ya no puedes acceder a SumixKids</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>Te informamos que tu cuenta en <strong>SumixKids</strong> ha sido <strong style='color: #D32F2F;'>bloqueada por un administrador</strong>.</p>" +
            "<div style='background-color: #FFF3E0; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #E65100; font-weight: bold;'>📊 Detalles del bloqueo:</p>" +
            "<ul style='margin: 0; color: #E65100; list-style: none; padding: 0;'>" +
            "<li style='margin-bottom: 8px;'><strong>👤 Usuario bloqueado:</strong> " + username + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>📅 Fecha y hora:</strong> " + fechaHora + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>👨‍💼 Bloqueado por:</strong> " + adminUsername + " (Administrador)</li>" +
            "</ul>" +
            "</div>" +
            "<div style='background-color: #E3F2FD; padding: 15px; border-radius: 8px; border-left: 4px solid #1976D2; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #0D47A1; font-weight: bold;'>ℹ️ ¿Qué significa esto?</p>" +
            "<p style='margin: 10px 0 0 0; color: #0D47A1; font-size: 14px;'>No podrás iniciar sesión en la plataforma hasta que un administrador desbloquee tu cuenta.</p>" +
            "</div>" +
            "<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #C62828; font-weight: bold;'>📞 ¿Necesitas ayuda?</p>" +
            "<p style='margin: 10px 0 0 0; color: #C62828; font-size: 14px;'>Si crees que esto es un error o necesitas más información, contacta con tu docente o con el administrador de la plataforma.</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si tienes preguntas sobre esta decisión, te recomendamos comunicarte directamente con un administrador del sistema.</p>" +
            "</div>" +
            getErrorEmailFooter() +
            getEmailCloser();
    }

    /**
     * Genera un email de notificación cuando un administrador desbloquea una cuenta.
     * @param nombres Nombres del usuario
     * @param apellidos Apellidos del usuario
     * @param username Username del usuario
     * @param adminUsername Username del administrador que desbloqueó la cuenta
     * @return HTML del email
     */
    public static String generateAccountUnblockedEmail(String nombres, String apellidos, String username, String adminUsername) {
        String fechaHora = getCurrentFormattedDateTime();
        
        return getEmailHeader() +
            "<h2 style='color: #4CAF50; margin-bottom: 20px;'>✅ ¡Bienvenido de nuevo " + nombres + " " + apellidos + "! 🎉</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: #E8F5E9; padding: 20px; border-radius: 8px; border: 2px solid #4CAF50; margin-bottom: 20px; text-align: center;'>" +
            "<h3 style='color: #2E7D32; margin: 0 0 15px 0;'>🔓 TU CUENTA HA SIDO DESBLOQUEADA</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #2E7D32; margin: 0; font-weight: bold;'>Ya puedes acceder nuevamente a SumixKids</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>¡Buenas noticias! Tu cuenta en <strong>SumixKids</strong> ha sido <strong style='color: #4CAF50;'>desbloqueada por un administrador</strong>.</p>" +
            "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #0D47A1; font-weight: bold;'>📊 Detalles del desbloqueo:</p>" +
            "<ul style='margin: 0; color: #0D47A1; list-style: none; padding: 0;'>" +
            "<li style='margin-bottom: 8px;'><strong>👤 Usuario:</strong> " + username + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>📅 Fecha y hora:</strong> " + fechaHora + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>👨‍💼 Desbloqueado por:</strong> " + adminUsername + " (Administrador)</li>" +
            "</ul>" +
            "</div>" +
            "<div style='background-color: #F1F8E9; padding: 15px; border-radius: 8px; border-left: 4px solid #8BC34A; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #558B2F; font-weight: bold;'>✨ ¿Qué puedes hacer ahora?</p>" +
            "<ul style='margin: 10px 0 0 0; color: #558B2F; font-size: 14px; padding-left: 20px;'>" +
            "<li style='margin-bottom: 5px;'>Iniciar sesión normalmente con tu usuario y contraseña</li>" +
            "<li style='margin-bottom: 5px;'>Acceder a todas las funcionalidades de la plataforma</li>" +
            "<li style='margin-bottom: 5px;'>Continuar con tus actividades educativas</li>" +
            "</ul>" +
            "</div>" +
            "<div style='background-color: #FFF3E0; padding: 15px; border-radius: 8px; border-left: 4px solid #FF9800; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #E65100; font-weight: bold;'>💡 Recomendaciones:</p>" +
            "<ul style='margin: 10px 0 0 0; color: #E65100; font-size: 14px; padding-left: 20px;'>" +
            "<li style='margin-bottom: 5px;'>Asegúrate de seguir las normas de uso de la plataforma</li>" +
            "<li style='margin-bottom: 5px;'>Mantén tu cuenta segura con una contraseña fuerte</li>" +
            "<li style='margin-bottom: 5px;'>Si tienes dudas, contacta con tu docente o administrador</li>" +
            "</ul>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>¡Estamos felices de tenerte de vuelta! Si tienes alguna pregunta, no dudes en contactarnos.</p>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
    }
}
