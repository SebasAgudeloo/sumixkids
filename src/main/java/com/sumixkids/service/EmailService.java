
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
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            // Configuraciones adicionales para debugging
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.debug", "false"); // Cambiar a true para debug
            
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
            System.out.println("✅ Email enviado exitosamente a: " + to);
            
        } catch (jakarta.mail.AuthenticationFailedException e) {
            System.err.println("❌ Error de autenticación Gmail:");
            System.err.println("   - Usuario: " + username);
            System.err.println("   - Verificar que la contraseña de aplicación esté correcta");
            System.err.println("   - Verificar que 2FA esté habilitado en Google");
            throw new MessagingException("Error de autenticación Gmail. Verificar contraseña de aplicación.", e);
        } catch (MessagingException e) {
            System.err.println("❌ Error general de correo: " + e.getMessage());
            throw e;
        }
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

    // ========================= MÉTODOS DE ENVÍO DE EMAILS ESPECÍFICOS =========================
    
    /**
     * Envía email con código de verificación 2FA.
     */
    public void send2FAEmail(String toEmail, String nombres, String apellidos, String code, 
                            String ipAddress, String infoDispositivo) throws MessagingException {
        String fechaHora = getCurrentFormattedDateTime();
        
        String mensaje = getEmailHeader() +
            "<h2 style='color: #2196F3; margin-bottom: 20px;'>¡Hola " + nombres + " " + apellidos + "! 👋</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 15px;'>Hemos detectado un intento de inicio de sesión en tu cuenta de <strong>SumixKids</strong>. 🔐</p>" +
            "<div style='background-color: #F5F5F5; padding: 15px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 10px 0; color: #666; font-weight: bold;'>📊 Información del acceso:</p>" +
            "<ul style='margin: 0; color: #666;'>" +
            "<li><strong>Fecha y hora:</strong> " + fechaHora + "</li>" +
            "<li><strong>IP de acceso:</strong> " + ipAddress + "</li>" +
            "<li><strong>Dispositivo:</strong> " + infoDispositivo + "</li>" +
            "</ul>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 20px;'>Para continuar, por favor ingresa el siguiente código de verificación en la pantalla de autenticación de dos factores (2FA):</p>" +
            "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; text-align: center; border: 2px solid #2196F3; margin: 20px 0;'>" +
            "<p style='margin: 0 0 10px 0; color: #0D47A1; font-weight: bold; font-size: 14px;'>Código de verificación 2FA:</p>" +
            "<p style='font-size: 32px; font-weight: bold; color: #2196F3; margin: 10px 0; letter-spacing: 3px; font-family: monospace;'>" + code + "</p>" +
            "<p style='margin: 10px 0 0 0; color: #0D47A1; font-size: 12px;'>⏰ Válido por 15 minutos - Solo se puede usar una vez</p>" +
            "</div>" +
            "<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #C62828; font-weight: bold;'>⚠️ Importante:</p>" +
            "<p style='margin: 10px 0 0 0; color: #C62828; font-size: 14px;'>Si tú no solicitaste este acceso, te recomendamos cambiar tu contraseña inmediatamente o contactar al soporte.</p>" +
            "</div>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
            
        sendHtmlEmail(toEmail, "Código de verificación 2FA - SumixKids", mensaje);
    }

    /**
     * Envía email de alerta por cuenta bloqueada por múltiples intentos fallidos.
     */
    public void sendAccountLockedEmail(String toEmail, String nombres, String apellidos, 
                                      String ipAddress) throws MessagingException {
        String fechaHora = getCurrentFormattedDateTime();
        String asunto = "🚨 Alerta de seguridad: Cuenta bloqueada - SumixKids";
        
        String mensaje = getEmailHeader() +
            "<h2 style='color: #F44336; margin-bottom: 20px;'>¡Hola " + nombres + " " + apellidos + "! 👋</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: #FFEBEE; padding: 20px; border-radius: 8px; border: 2px solid #F44336; margin-bottom: 20px;'>" +
            "<h3 style='color: #C62828; margin: 0 0 15px 0; text-align: center;'>🚨 ALERTA DE SEGURIDAD</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #C62828; margin-bottom: 15px; text-align: center; font-weight: bold;'>Tu cuenta ha sido bloqueada automáticamente</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 15px;'>Tu cuenta de <strong>SumixKids</strong> ha sido bloqueada por superar el número máximo de intentos fallidos de inicio de sesión permitidos.</p>" +
            "<div style='background-color: #FFF3E0; padding: 15px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 10px 0; color: #E65100; font-weight: bold;'>📊 Detalles del bloqueo:</p>" +
            "<ul style='margin: 0; color: #E65100;'>" +
            "<li><strong>Fecha y hora:</strong> " + fechaHora + "</li>" +
            "<li><strong>IP de último intento:</strong> " + ipAddress + "</li>" +
            "<li><strong>Motivo:</strong> Múltiples intentos fallidos consecutivos</li>" +
            "</ul>" +
            "</div>" +
            "<div style='background-color: #E8F5E8; padding: 15px; border-radius: 8px; border-left: 4px solid #4CAF50; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #2E7D32; font-weight: bold;'>💡 ¿Qué puedes hacer?</p>" +
            "<ul style='margin: 10px 0 0 0; color: #2E7D32;'>" +
            "<li>Utiliza la opción de 'Recuperar contraseña' en la página de login</li>" +
            "<li>Contacta con un docente encargado o administrador</li>" +
            "<li>Espera un tiempo antes de intentar nuevamente</li>" +
            "</ul>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si no reconoces estos intentos, te recomendamos restablecer tu contraseña inmediatamente.</p>" +
            "</div>" +
            getErrorEmailFooter() +
            getEmailCloser();
        
        sendHtmlEmail(toEmail, asunto, mensaje);
    }

    /**
     * Envía email de recuperación de contraseña con enlace.
     */
    public void sendPasswordRecoveryEmail(String toEmail, String nombres, String apellidos, 
                                         String resetLink) throws MessagingException {
        String fechaHora = getCurrentFormattedDateTime();
        String asunto = "Recuperación de contraseña - SumixKids";
        
        String mensaje = getEmailHeader() +
            "<h2 style='color: #2196F3; margin-bottom: 20px;'>¡Hola " + nombres + " " + apellidos + "! 👋</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 15px;'>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en <strong>SumixKids</strong>.</p>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 20px;'>Fecha de solicitud: <strong>" + fechaHora + "</strong></p>" +
            "<div style='text-align: center; margin: 30px 0;'>" +
            "<a href='" + resetLink + "' style='background-color: #2196F3; color: white; padding: 15px 30px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;'>🔑 Restablecer mi contraseña</a>" +
            "</div>" +
            "<div style='background-color: #FFF3E0; padding: 15px; border-radius: 8px; border-left: 4px solid #FF9800; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #E65100; font-weight: bold;'>⚠️ Importante:</p>" +
            "<ul style='margin: 10px 0 0 0; color: #E65100; font-size: 14px;'>" +
            "<li>Este enlace es válido solo por 24 horas</li>" +
            "<li>Solo se puede usar una vez</li>" +
            "<li>Si no solicitaste este cambio, ignora este correo</li>" +
            "</ul>" +
            "</div>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
            
        sendHtmlEmail(toEmail, asunto, mensaje);
    }

    /**
     * Envía email de bienvenida para nuevos registros.
     */
    public void sendWelcomeEmail(String toEmail, String nombres, String apellidos, 
                                String username) throws MessagingException {
        String fechaHora = getCurrentFormattedDateTime();
        String asunto = "¡Bienvenido a SumixKids!";
        
        String mensaje = getEmailHeader() +
            "<h2 style='color: #4CAF50; margin-bottom: 20px;'>¡Bienvenido " + nombres + " " + apellidos + "! 🎉</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: #E8F5E9; padding: 20px; border-radius: 8px; border: 2px solid #4CAF50; margin-bottom: 20px; text-align: center;'>" +
            "<h3 style='color: #2E7D32; margin: 0 0 15px 0;'>✅ ¡REGISTRO EXITOSO!</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #2E7D32; margin: 0; font-weight: bold;'>Tu cuenta ha sido creada correctamente</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>¡Te damos la más cordial bienvenida a <strong>SumixKids</strong>! Tu cuenta ha sido registrada exitosamente.</p>" +
            "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #0D47A1; font-weight: bold;'>📊 Detalles de tu cuenta:</p>" +
            "<ul style='margin: 0; color: #0D47A1; list-style: none; padding: 0;'>" +
            "<li style='margin-bottom: 8px;'><strong>👤 Usuario:</strong> " + username + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>📧 Email:</strong> " + toEmail + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>📅 Fecha de registro:</strong> " + fechaHora + "</li>" +
            "</ul>" +
            "</div>" +
            "<div style='background-color: #F1F8E9; padding: 15px; border-radius: 8px; border-left: 4px solid #8BC34A; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #558B2F; font-weight: bold;'>🚀 ¿Qué sigue?</p>" +
            "<ul style='margin: 10px 0 0 0; color: #558B2F; font-size: 14px; padding-left: 20px;'>" +
            "<li style='margin-bottom: 5px;'>Inicia sesión con tu usuario y contraseña</li>" +
            "<li style='margin-bottom: 5px;'>Explora las funcionalidades de la plataforma</li>" +
            "<li style='margin-bottom: 5px;'>Comienza tu aventura educativa</li>" +
            "</ul>" +
            "</div>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
            
        sendHtmlEmail(toEmail, asunto, mensaje);
    }

    /**
     * Envía email de notificación por cambio de datos del usuario.
     * @param toEmail Correo del usuario
     * @param nombres Nombres del usuario
     * @param apellidos Apellidos del usuario
     * @param cambios Lista de cambios realizados (formato: "campo: valor_anterior → valor_nuevo")
     * @param adminUsername Usuario administrador que realizó el cambio
     */
    public void sendDataChangeEmail(String toEmail, String nombres, String apellidos, 
                                   java.util.List<String> cambios, String adminUsername) throws MessagingException {
        String fechaHora = getCurrentFormattedDateTime();
        String asunto = "Datos de cuenta actualizados - SumixKids";
        
        // Determinar icono y color según el tipo de cambios
        String icono = "🔄";
        String color = "#FF9800";
        boolean hayContrasena = false;
        boolean hayRol = false;
        
        for (String cambio : cambios) {
            if (cambio.toLowerCase().contains("contraseña")) {
                hayContrasena = true;
            }
            if (cambio.toLowerCase().contains("rol")) {
                hayRol = true;
            }
        }
        
        if (hayContrasena && hayRol) {
            icono = "🔐🔄";
            color = "#D32F2F";
        } else if (hayContrasena) {
            icono = "🔐";
            color = "#D32F2F";
        } else if (hayRol) {
            icono = "🎭";
            color = "#9C27B0";
        }
        
        String mensaje = getEmailHeader() +
            "<h2 style='color: " + color + "; margin-bottom: 20px;'>¡Hola " + nombres + " " + apellidos + "! 👋</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: #FFF3E0; padding: 20px; border-radius: 8px; border: 2px solid " + color + "; margin-bottom: 20px; text-align: center;'>" +
            "<h3 style='color: #E65100; margin: 0 0 15px 0;'>" + icono + " DATOS ACTUALIZADOS</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #E65100; margin: 0; font-weight: bold;'>Un administrador ha modificado tu información</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>Te informamos que algunos datos de tu cuenta en <strong>SumixKids</strong> han sido modificados por un administrador.</p>" +
            "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #0D47A1; font-weight: bold;'>📊 Cambios realizados:</p>" +
            "<ul style='margin: 0; color: #0D47A1; list-style: none; padding: 0;'>";
        
        // Procesar cambios: si vienen cambios de rol en formato numérico (ej: "rol: 3 → 1")
        // los convertimos a nombres usando SecurityUtils para mostrar "Estudiante → Administrador".
        for (String cambio : cambios) {
            String texto = cambio;
            try {
                // Detectar patrón "Rol: <num> → <num>" (espacios opcionales)
                java.util.regex.Pattern p = java.util.regex.Pattern.compile("rol:\\s*(\\d+)\\s*→\\s*(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
                java.util.regex.Matcher m = p.matcher(cambio);
                if (m.find()) {
                    int antes = Integer.parseInt(m.group(1));
                    int despues = Integer.parseInt(m.group(2));
                    String nombreAntes = com.sumixkids.util.SecurityUtils.getNombreRol(antes);
                    String nombreDespues = com.sumixkids.util.SecurityUtils.getNombreRol(despues);
                    texto = String.format("rol: %s → %s", nombreAntes, nombreDespues);
                }
            } catch (Exception ex) {
                // Si falla la conversión, dejamos el texto original
                texto = cambio;
            }
            mensaje += "<li style='margin-bottom: 8px; padding: 8px; background-color: #F5F5F5; border-radius: 4px;'><strong>🔹 " + texto + "</strong></li>";
        }
        
        mensaje += "</ul>" +
            "</div>" +
            "<div style='background-color: #F1F8E9; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #2E7D32; font-weight: bold;'>📋 Información adicional:</p>" +
            "<ul style='margin: 0; color: #2E7D32; list-style: none; padding: 0;'>" +
            "<li style='margin-bottom: 8px;'><strong>📅 Fecha del cambio:</strong> " + fechaHora + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>👨‍💼 Modificado por:</strong> " + adminUsername + " (Administrador)</li>" +
            "</ul>" +
            "</div>";
            
        if (hayContrasena) {
            mensaje += "<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #C62828; font-weight: bold;'>🔐 Seguridad importante:</p>" +
                "<p style='margin: 10px 0 0 0; color: #C62828; font-size: 14px;'>Tu contraseña ha sido modificada. Te recomendamos cambiarla por una personal en tu próximo inicio de sesión.</p>" +
                "</div>";
        }
        
        if (hayRol) {
            mensaje += "<div style='background-color: #F3E5F5; padding: 15px; border-radius: 8px; border-left: 4px solid #9C27B0; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #7B1FA2; font-weight: bold;'>🎭 Cambio de rol:</p>" +
                "<p style='margin: 10px 0 0 0; color: #7B1FA2; font-size: 14px;'>Tu rol ha cambiado. Esto puede afectar las funcionalidades disponibles cuando inicies sesión.</p>" +
                "</div>";
        }
        
        mensaje += "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si tienes preguntas sobre estos cambios, no dudes en contactar con un administrador.</p>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
            
        sendHtmlEmail(toEmail, asunto, mensaje);
    }
    
    /**
     * Envía email de notificación por cambio de rol.
     * @deprecated Usar sendDataChangeEmail() en su lugar para mejor funcionalidad
     */
    @Deprecated
    public void sendRoleChangeEmail(String toEmail, String nombres, String apellidos, 
                                   String nuevoRol, String adminUsername) throws MessagingException {
        java.util.List<String> cambios = new java.util.ArrayList<>();
        cambios.add("Rol actualizado a: " + nuevoRol);
        sendDataChangeEmail(toEmail, nombres, apellidos, cambios, adminUsername);
    }

    /**
     * Envía email de notificación por cambio de estado (habilitado/deshabilitado).
     */
    public void sendStatusChangeEmail(String toEmail, String nombres, String apellidos, 
                                     boolean habilitado, String adminUsername) throws MessagingException {
        String fechaHora = getCurrentFormattedDateTime();
        String estado = habilitado ? "habilitada" : "deshabilitada";
        String color = habilitado ? "#4CAF50" : "#F44336";
        String backgroundColor = habilitado ? "#E8F5E9" : "#FFEBEE";
        String icon = habilitado ? "" : "";
        String asunto = (habilitado ? "" : "") + "Estado de cuenta actualizado - SumixKids";
        
        String mensaje = getEmailHeader() +
            "<h2 style='color: " + color + "; margin-bottom: 20px;'>¡Hola " + nombres + " " + apellidos + "! 👋</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: " + backgroundColor + "; padding: 20px; border-radius: 8px; border: 2px solid " + color + "; margin-bottom: 20px; text-align: center;'>" +
            "<h3 style='color: " + color + "; margin: 0 0 15px 0;'>" + icon + " CUENTA " + estado.toUpperCase() + "</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: " + color + "; margin: 0; font-weight: bold;'>El estado de tu cuenta ha cambiado</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>Te informamos que el estado de tu cuenta en <strong>SumixKids</strong> ha sido <strong style='color: " + color + ";'>" + estado + "</strong> por un administrador.</p>" +
            "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #0D47A1; font-weight: bold;'>📊 Detalles del cambio:</p>" +
            "<ul style='margin: 0; color: #0D47A1; list-style: none; padding: 0;'>" +
            "<li style='margin-bottom: 8px;'><strong>📊 Nuevo estado:</strong> " + (habilitado ? "Cuenta habilitada" : "Cuenta deshabilitada") + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>📅 Fecha del cambio:</strong> " + fechaHora + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>👨‍💼 Modificado por:</strong> " + adminUsername + " (Administrador)</li>" +
            "</ul>" +
            "</div>";
            
        if (habilitado) {
            mensaje += "<div style='background-color: #F1F8E9; padding: 15px; border-radius: 8px; border-left: 4px solid #8BC34A; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #558B2F; font-weight: bold;'>✨ ¿Qué significa esto?</p>" +
                "<ul style='margin: 10px 0 0 0; color: #558B2F; font-size: 14px; padding-left: 20px;'>" +
                "<li style='margin-bottom: 5px;'>Puedes acceder normalmente a la plataforma</li>" +
                "<li style='margin-bottom: 5px;'>Todas las funcionalidades están disponibles</li>" +
                "<li style='margin-bottom: 5px;'>Tu cuenta está completamente activa</li>" +
                "</ul>" +
                "</div>";
        } else {
            mensaje += "<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #C62828; font-weight: bold;'>⚠️ ¿Qué significa esto?</p>" +
                "<ul style='margin: 10px 0 0 0; color: #C62828; font-size: 14px; padding-left: 20px;'>" +
                "<li style='margin-bottom: 5px;'>No podrás acceder a la plataforma temporalmente</li>" +
                "<li style='margin-bottom: 5px;'>El acceso se restablecerá cuando sea habilitada nuevamente</li>" +
                "<li style='margin-bottom: 5px;'>Para más información, contacta con un administrador</li>" +
                "</ul>" +
                "</div>";
        }
        
        mensaje += "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si tienes preguntas sobre este cambio, no dudes en contactar con un administrador.</p>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
            
        sendHtmlEmail(toEmail, asunto, mensaje);
    }

    /**
     * Envía email de notificación por eliminación de cuenta.
     */
    public void sendAccountDeletionEmail(String toEmail, String nombres, String apellidos, 
                                        String adminUsername) throws MessagingException {
        String fechaHora = getCurrentFormattedDateTime();
        String asunto = "Cuenta eliminada - SumixKids";
        
        String mensaje = getEmailHeader() +
            "<h2 style='color: #F44336; margin-bottom: 20px;'>¡Hola " + nombres + " " + apellidos + "! 👋</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: #FFEBEE; padding: 20px; border-radius: 8px; border: 2px solid #F44336; margin-bottom: 20px; text-align: center;'>" +
            "<h3 style='color: #C62828; margin: 0 0 15px 0;'>🗑️ CUENTA ELIMINADA</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #C62828; margin: 0; font-weight: bold;'>Tu cuenta ha sido eliminada del sistema</p>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #333; margin-bottom: 20px;'>Te informamos que tu cuenta en <strong>SumixKids</strong> ha sido <strong style='color: #F44336;'>eliminada permanentemente</strong> por un administrador.</p>" +
            "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #0D47A1; font-weight: bold;'>📊 Detalles de la eliminación:</p>" +
            "<ul style='margin: 0; color: #0D47A1; list-style: none; padding: 0;'>" +
            "<li style='margin-bottom: 8px;'><strong>📅 Fecha de eliminación:</strong> " + fechaHora + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>👨‍💼 Eliminado por:</strong> " + adminUsername + " (Administrador)</li>" +
            "<li style='margin-bottom: 8px;'><strong>⚠️ Estado:</strong> Eliminación permanente</li>" +
            "</ul>" +
            "</div>" +
            "<div style='background-color: #FFEBEE; padding: 15px; border-radius: 8px; border-left: 4px solid #F44336; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #C62828; font-weight: bold;'>📝 Información importante:</p>" +
            "<ul style='margin: 10px 0 0 0; color: #C62828; font-size: 14px; padding-left: 20px;'>" +
            "<li style='margin-bottom: 5px;'>Ya no podrás acceder con tus credenciales anteriores</li>" +
            "<li style='margin-bottom: 5px;'>Todos tus datos han sido eliminados del sistema</li>" +
            "<li style='margin-bottom: 5px;'>Si necesitas una nueva cuenta, debes registrarte nuevamente</li>" +
            "</ul>" +
            "</div>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Si consideras que esta eliminación fue un error, contacta inmediatamente con el soporte técnico.</p>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
            
        sendHtmlEmail(toEmail, asunto, mensaje);
    }

    /**
     * Envía email de notificación de desbloqueo de cuenta.
     */
    public void sendAccountUnblockedNotification(String toEmail, String nombres, String apellidos, 
                                               String username, String adminUsername) throws MessagingException {
        String asunto = "Cuenta desbloqueada - SumixKids";
        String mensaje = generateAccountUnblockedEmail(nombres, apellidos, username, adminUsername);
        sendHtmlEmail(toEmail, asunto, mensaje);
    }

    /**
     * Envía email de bienvenida para usuario creado por administrador (incluye contraseña temporal).
     */
    public void sendAdminCreatedUserEmail(String toEmail, String nombres, String apellidos, 
                                         String username, String password, int rolId, 
                                         String grado, String adminNombres, String adminApellidos) throws MessagingException {
        String fechaHora = getCurrentFormattedDateTime();
        String asunto = "¡Bienvenido a SumixKids! - Cuenta creada por administrador";
        
        String rolNombre = "";
        String icono = "";
        String color = "";
        switch (rolId) {
            case 1: rolNombre = "Administrador"; icono = "👑"; color = "#9C27B0"; break;
            case 2: rolNombre = "Docente"; icono = "👨‍🏫"; color = "#2196F3"; break;
            case 3: rolNombre = "Estudiante"; icono = "🎓"; color = "#4CAF50"; break;
            case 4: rolNombre = "Padre/Madre"; icono = "👨‍👩‍👧‍👦"; color = "#FF9800"; break;
            default: rolNombre = "Usuario"; icono = "👤"; color = "#757575";
        }
        
        String mensaje = getEmailHeader() +
            "<h2 style='color: " + color + "; margin-bottom: 20px;'>¡Hola " + nombres + " " + apellidos + "! 👋</h2>" +
            "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
            "<div style='background-color: #E8F5E9; padding: 20px; border-radius: 8px; border: 2px solid #4CAF50; margin-bottom: 20px; text-align: center;'>" +
            "<h3 style='color: #2E7D32; margin: 0 0 15px 0;'>🎉 ¡CUENTA CREADA!</h3>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #2E7D32; margin: 0; font-weight: bold;'>Un administrador ha creado tu cuenta</p>" +
            "</div>" +
            "<p style='font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 15px;'>¡Bienvenido a <strong>SumixKids</strong>! 🌟</p>" +
            "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Un administrador ha creado una cuenta para ti el <strong>" + fechaHora + "</strong>.</p>" +
            "<div style='background-color: #E3F2FD; padding: 20px; border-radius: 8px; margin: 20px 0;'>" +
            "<p style='margin: 0 0 15px 0; color: #0D47A1; font-weight: bold;'>📊 Detalles de tu cuenta:</p>" +
            "<ul style='margin: 0; color: #0D47A1; list-style: none; padding: 0;'>" +
            "<li style='margin-bottom: 8px;'><strong>👤 Usuario:</strong> " + username + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>📧 Email:</strong> " + toEmail + "</li>" +
            "<li style='margin-bottom: 8px;'><strong>🎯 Rol asignado:</strong> " + icono + " " + rolNombre + "</li>" +
            (grado != null && !grado.isEmpty() ? 
            "<li style='margin-bottom: 8px;'><strong>📚 Grado:</strong> " + grado + "</li>" : "") +
            "<li style='margin-bottom: 8px;'><strong>🛡️ Creado por:</strong> " + adminNombres + " " + adminApellidos + "</li>" +
            "</ul>" +
            "</div>" +
            "<div style='background-color: #FFEBEE; padding: 20px; border-radius: 8px; border: 2px solid #F44336; margin: 20px 0;'>" +
            "<p style='margin: 0 0 10px 0; color: #C62828; font-weight: bold;'>🔐 Credenciales de acceso:</p>" +
            "<p style='margin: 0; color: #C62828; font-size: 14px;'><strong>Contraseña temporal:</strong> " + password + "</p>" +
            "<p style='margin: 10px 0 0 0; color: #C62828; font-size: 12px;'>⚠️ Te recomendamos cambiar esta contraseña en tu primer inicio de sesión</p>" +
            "</div>" +
            "<div style='background-color: #F1F8E9; padding: 15px; border-radius: 8px; border-left: 4px solid #8BC34A; margin: 20px 0;'>" +
            "<p style='margin: 0; color: #558B2F; font-weight: bold;'>🚀 Primeros pasos:</p>" +
            "<ul style='margin: 10px 0 0 0; color: #558B2F; font-size: 14px; padding-left: 20px;'>" +
            "<li style='margin-bottom: 5px;'>Inicia sesión con tus credenciales</li>" +
            "<li style='margin-bottom: 5px;'>Explora las funcionalidades de tu rol</li>" +
            "<li style='margin-bottom: 5px;'>Cambia tu contraseña por una personal</li>" +
            "<li style='margin-bottom: 5px;'>¡Comienza a disfrutar la plataforma!</li>" +
            "</ul>" +
            "</div>" +
            "</div>" +
            getEmailFooter() +
            getEmailCloser();
            
        sendHtmlEmail(toEmail, asunto, mensaje);
    }
}
