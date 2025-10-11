package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.util.PasswordUtil;
import com.sumixkids.service.EmailService;
import com.sumixkids.util.ValidacionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Atiende el formulario de registro de nuevos usuarios.
 * GET: muestra el formulario.
 * POST: valida datos y crea el usuario.
 */
public class RegistroServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LogAuditoriaDAO logDAO = new LogAuditoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Sólo mostrar el formulario de registro.
        req.getRequestDispatcher("/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        // Verificar si es un admin quien está creando el usuario
        HttpSession session = req.getSession(false);
        Usuario admin = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        boolean esAdmin = (admin != null && admin.getRolId() == 1);
        
        String username = req.getParameter("username");
        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String gradoParam = req.getParameter("grado");
        String rolIdStr = req.getParameter("rolId"); // Solo para admin
        
        // Variable para grado normalizado
        String gradoNormalizado = null;
        
        List<String> errores = new ArrayList<>();

        // Validaciones estrictas usando ValidacionUtil
        if (!ValidacionUtil.esNombreValido(nombres)) {
            errores.add(ValidacionUtil.getMensajeError("nombres", nombres));
            req.setAttribute("errorNombres", true);
        }
        
        if (!ValidacionUtil.esApellidoValido(apellidos)) {
            errores.add(ValidacionUtil.getMensajeError("apellidos", apellidos));
            req.setAttribute("errorApellidos", true);
        }
        
        if (!ValidacionUtil.esUsernameValido(username)) {
            errores.add(ValidacionUtil.getMensajeError("username", username));
            req.setAttribute("errorUsername", true);
        }
        
        if (!ValidacionUtil.esCorreoValido(email)) {
            errores.add(ValidacionUtil.getMensajeError("email", email));
            req.setAttribute("errorEmail", true);
        }
        
        // Validación de contraseña con mensaje detallado
        if (!ValidacionUtil.esPasswordValida(password)) {
            errores.add(ValidacionUtil.getMensajePasswordDetallado(password));
            req.setAttribute("errorPassword", true);
        }
        
        // Validar rol si es admin
        int rolId = 3; // Por defecto estudiante para registro público
        if (esAdmin && rolIdStr != null && !rolIdStr.trim().isEmpty()) {
            try {
                rolId = Integer.parseInt(rolIdStr);
                if (rolId < 1 || rolId > 4) {
                    errores.add("Rol inválido. Debe seleccionar un rol válido.");
                    req.setAttribute("errorRolId", true);
                }
            } catch (NumberFormatException e) {
                errores.add("Rol inválido. Debe seleccionar un rol válido.");
                req.setAttribute("errorRolId", true);
            }
        }
        
        // Validación de grado (obligatorio solo para estudiantes)
        if (rolId == 3) {
            if (gradoParam == null || gradoParam.trim().isEmpty()) {
                errores.add("El grado es obligatorio para estudiantes");
                req.setAttribute("errorGrado", true);
            } else {
                // Normalizar grado: agregar símbolo ° si no lo tiene
                gradoNormalizado = gradoParam.trim();
                if (!gradoNormalizado.endsWith("°")) {
                    gradoNormalizado += "°";
                }
                // Validar con el grado normalizado
                if (!ValidacionUtil.esGradoValido(gradoNormalizado)) {
                    errores.add(ValidacionUtil.getMensajeError("grado", gradoNormalizado));
                    req.setAttribute("errorGrado", true);
                }
            }
        }

        // Si hay errores, volver al formulario
        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            setFormValues(req, username, nombres, apellidos, email, gradoParam, rolIdStr);
            req.getRequestDispatcher("/registro.jsp").forward(req, resp);
            return;
        }

        try {
            if (usuarioDAO.existsByUsernameOrEmail(username, email)) {
                req.setAttribute("error", "Usuario o correo ya existe");
                setFormValues(req, username, nombres, apellidos, email, gradoParam, rolIdStr);
                req.getRequestDispatcher("/registro.jsp").forward(req, resp);
                return;
            }

            Usuario u = new Usuario();
            u.setUsername(username);
            u.setNombres(nombres);
            u.setApellidos(apellidos);
            u.setEmail(email);
            u.setPasswordHash(PasswordUtil.hash(password));
            u.setRolId(rolId); // Usar el rol determinado (3 por defecto o el seleccionado por admin)
            // Asignar grado normalizado (con °) solo si es estudiante
            if (rolId == 3 && gradoNormalizado != null && !gradoNormalizado.trim().isEmpty()) {
                u.setGrado(gradoNormalizado.trim());
            } else {
                u.setGrado("");
            }
            u.setFechaRegistro(ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime());
            int id = usuarioDAO.createUser(u);
            if (id > 0) {
                // Registrar en auditoría
                LogAuditoria log = new LogAuditoria();
                log.setFechaHora(LocalDateTime.now());
                log.setIpUsuario(req.getRemoteAddr());
                
                if (esAdmin) {
                    // Registro creado por admin
                    log.setIdUsuario(admin.getId());
                    log.setNombreUsuario(admin.getUsername());
                    log.setAccion("REGISTRO_ADMIN");
                    log.setDescripcion(String.format("Admin creó usuario '%s' con rol ID %d", username, rolId));
                } else {
                    // Registro público
                    log.setIdUsuario(id);
                    log.setNombreUsuario(username);
                    log.setAccion("REGISTRO");
                    log.setDescripcion(String.format("Usuario '%s' se registró públicamente", username));
                }
                
                log.setTablaAfectada("usuarios");
                log.setEstado("EXITOSO");
                logDAO.registrarLog(log);
                // Enviar correo de bienvenida
                try {
                    ResourceBundle config = ResourceBundle.getBundle("config");
                    String mailUser = config.getString("mail.smtp.user");
                    String mailPass = config.getString("mail.smtp.pass");
                    EmailService emailService = new EmailService(mailUser, mailPass);
                    String asunto = "🎉 ¡Bienvenido a SumixKids!";
                    
                    String fechaHora = EmailService.getCurrentFormattedDateTime();
                    String mensaje = EmailService.getEmailHeader() +
                        "<h2 style='color: #4CAF50; margin-bottom: 20px;'>¡Hola " + nombres + " " + apellidos + "! 👋</h2>" +
                        "<div style='background-color: white; padding: 25px; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>" +
                        "<p style='font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 15px;'>¡Bienvenido a <strong>SumixKids</strong>! 🌟</p>" +
                        "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Tu cuenta ha sido creada exitosamente el <strong>" + fechaHora + "</strong>.</p>" +
                        "<p style='font-size: 14px; line-height: 1.6; color: #555; margin-bottom: 15px;'>Ahora puedes iniciar sesión y disfrutar de todos los recursos de aprendizaje que tenemos preparados para ti. ¡Comienza tu aventura educativa! 🚀</p>" +
                        "<div style='background-color: #E8F5E8; padding: 15px; border-radius: 8px; border-left: 4px solid #4CAF50; margin: 20px 0;'>" +
                        "<p style='margin: 0; color: #2E7D32; font-weight: bold;'>💡 Consejos para comenzar:</p>" +
                        "<ul style='margin: 10px 0 0 0; color: #2E7D32;'>" +
                        "<li>Explora las diferentes secciones educativas</li>" +
                        "<li>Personaliza tu perfil de usuario</li>" +
                        "<li>¡Diviértete aprendiendo!</li>" +
                        "</ul>" +
                        "</div>" +
                        "</div>" +
                        EmailService.getEmailFooter() +
                        EmailService.getEmailCloser();
                    
                    emailService.sendHtmlEmail(email, asunto, mensaje);
                } catch (Exception e) {
                    // No interrumpir el registro si falla el correo
                    e.printStackTrace();
                }
                
                // Redirigir según quien creó el usuario
                if (esAdmin) {
                    req.setAttribute("success", "✅ Usuario registrado exitosamente");
                    req.getRequestDispatcher("/registro.jsp").forward(req, resp);
                } else {
                    req.setAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                }
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
            String email, String grado, String rolId) {
        req.setAttribute("username", username);
        req.setAttribute("nombres", nombres);
        req.setAttribute("apellidos", apellidos);
        req.setAttribute("email", email);
        req.setAttribute("grado", grado);
        req.setAttribute("rolId", rolId);
    }
    
}
