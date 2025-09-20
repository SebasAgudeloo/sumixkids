package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.util.PasswordUtil;
import com.sumixkids.service.EmailService;
import com.sumixkids.util.ValidacionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Atiende el formulario de registro de nuevos usuarios.
 * GET: muestra el formulario.
 * POST: valida datos y crea el usuario.
 */
@WebServlet(name = "RegistroServlet", urlPatterns = { "/registro" })
public class RegistroServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Sólo mostrar el formulario de registro.
        req.getRequestDispatcher("/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        String username = req.getParameter("username");
        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        
        List<String> errores = new ArrayList<>();

        // Validaciones estrictas usando ValidacionUtil
        if (!ValidacionUtil.esNombreValido(nombres)) {
            errores.add("El nombre solo puede contener letras");
            req.setAttribute("errorNombres", true);
        }
        
        if (!ValidacionUtil.esNombreValido(apellidos)) {
            errores.add("El apellido solo puede contener letras");
            req.setAttribute("errorApellidos", true);
        }
        
        if (!ValidacionUtil.esUsuarioValido(username)) {
            errores.add("El usuario debe contener al menos 2 números");
            req.setAttribute("errorUsername", true);
        }
        
        if (!ValidacionUtil.esCorreoValido(email)) {
            errores.add("Formato de correo electrónico inválido");
            req.setAttribute("errorEmail", true);
        }

        // Si hay errores, volver al formulario
        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            setFormValues(req, username, nombres, apellidos, email);
            req.getRequestDispatcher("/registro.jsp").forward(req, resp);
            return;
        }

        try {
            if (usuarioDAO.existsByUsernameOrEmail(username, email)) {
                req.setAttribute("error", "Usuario o correo ya existe");
                setFormValues(req, username, nombres, apellidos, email);
                req.getRequestDispatcher("/registro.jsp").forward(req, resp);
                return;
            }

            Usuario u = new Usuario(); // Creamos el objeto y llenamos sus campos.
            u.setUsername(username);
            u.setNombres(nombres);
            u.setApellidos(apellidos);
            u.setEmail(email);
            u.setPasswordHash(PasswordUtil.hash(password));
            // Asignar rol automáticamente como estudiante
            u.setRolId(usuarioDAO.resolveRolIdByName("student"));
            u.setFechaRegistro(ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime());
            int id = usuarioDAO.createUser(u);
            if (id > 0) {
                // Enviar correo de bienvenida
                try {
                    ResourceBundle config = ResourceBundle.getBundle("config");
                    String mailUser = config.getString("mail.smtp.user");
                    String mailPass = config.getString("mail.smtp.pass");
                    EmailService emailService = new EmailService(mailUser, mailPass);
                    String asunto = "¡Bienvenido a SumixKids!";
                    String mensaje = "Hola " + nombres + ",\n\n" +
                        "¡Bienvenido a SumixKids! Tu cuenta ha sido creada exitosamente.\n" +
                        "Ahora puedes iniciar sesión y disfrutar de todos los recursos de la plataforma.\n\n" +
                        "Si tienes alguna duda o necesitas ayuda, contáctanos.\n\n" +
                        "Saludos,\nEl equipo de SumixKids";
                    emailService.sendEmail(email, asunto, mensaje);
                } catch (Exception e) {
                    // No interrumpir el registro si falla el correo
                    e.printStackTrace();
                }
                req.setAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "No se pudo registrar el usuario");
                req.getRequestDispatcher("/registro.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException("Error en registro", e);
        }
    }

    /**
     * Pone los valores del formulario como atributos para que el JSP los conserve
     * tras error.
     */
    private void setFormValues(HttpServletRequest req, String username, String nombres, String apellidos,
            String email) {
        req.setAttribute("username", username);
        req.setAttribute("nombres", nombres);
        req.setAttribute("apellidos", apellidos);
        req.setAttribute("email", email);
    }
}
