package com.sumixkids.web;

import com.sumixkids.service.EmailService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * Servlet temporal para probar el envío de correos
 */
@WebServlet("/test_email")
public class TestEmailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        out.println("<html><head><title>Test Email</title></head><body>");
        out.println("<h1>Test de Correo SumixKids</h1>");
        
        try {
            // Cargar configuración
            Properties props = new Properties();
            try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) {
                    props.load(in);
                    out.println("<p>✅ Configuración cargada correctamente</p>");
                } else {
                    out.println("<p>❌ No se pudo cargar config.properties</p>");
                    return;
                }
            }
            
            String mailUser = props.getProperty("mail.smtp.user");
            String mailPass = props.getProperty("mail.smtp.pass");
            
            out.println("<p><strong>Usuario de correo:</strong> " + mailUser + "</p>");
            out.println("<p><strong>Contraseña configurada:</strong> " + (mailPass != null && !mailPass.isEmpty() ? "✅ SÍ" : "❌ NO") + "</p>");
            
            if (mailUser == null || mailPass == null) {
                out.println("<p>❌ Credenciales no configuradas</p>");
                return;
            }
            
            // Crear EmailService
            EmailService emailService = new EmailService(mailUser, mailPass);
            out.println("<p>✅ EmailService creado</p>");
            
            // Probar envío de correo de cuenta bloqueada
            String testEmail = "sebastianagudeloo@gmail.com"; // Cambia por tu email de prueba
            String testIP = req.getRemoteAddr();
            
            out.println("<p>📧 Enviando correo de prueba a: " + testEmail + "</p>");
            out.println("<p>🌐 IP del request: " + testIP + "</p>");
            
            emailService.sendAccountLockedEmail(testEmail, "Usuario", "Prueba", testIP);
            
            out.println("<p>✅ <strong>¡Correo enviado exitosamente!</strong></p>");
            out.println("<p>Revisa tu bandeja de entrada y spam.</p>");
            
        } catch (jakarta.mail.MessagingException me) {
            out.println("<p>❌ <strong>Error de mensajería:</strong> " + me.getMessage() + "</p>");
            me.printStackTrace();
        } catch (Exception e) {
            out.println("<p>❌ <strong>Error general:</strong> " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("</body></html>");
    }
}