
package com.sumixkids.service;

import java.util.Properties;
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
}
