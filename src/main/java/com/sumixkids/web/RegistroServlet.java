package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.model.RoleType;
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
        RoleType tipoRol = RoleType.STUDENT; // Por defecto estudiante para registro público
        if (esAdmin && rolIdStr != null && !rolIdStr.trim().isEmpty()) {
            try {
                int rolIdNum = Integer.parseInt(rolIdStr);
                switch (rolIdNum) {
                    case 1: tipoRol = RoleType.ADMIN; break;
                    case 2: tipoRol = RoleType.DOCENT; break;
                    case 3: tipoRol = RoleType.STUDENT; break;
                    case 4: tipoRol = RoleType.ATTENDANT; break;
                    default:
                        errores.add("Rol inválido. Debe seleccionar un rol válido.");
                        req.setAttribute("errorRolId", true);
                }
            } catch (NumberFormatException e) {
                errores.add("Rol inválido. Debe seleccionar un rol válido.");
                req.setAttribute("errorRolId", true);
            }
        }
        
        // Validación de grado (obligatorio solo para estudiantes)
        if (tipoRol == RoleType.STUDENT) {
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
            // Resolver rol_id por nombre (crea el rol si no existe)
            int rolId = usuarioDAO.resolveRolIdByType(tipoRol);
            u.setRolId(rolId);
            // Asignar grado normalizado (con °) solo si es estudiante
            if (tipoRol == RoleType.STUDENT && gradoNormalizado != null && !gradoNormalizado.trim().isEmpty()) {
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
                    log.setAccion("Registro Admin");
                    log.setDescripcion(String.format("Admin creó usuario '%s' con rol %s (ID %d)", username, tipoRol.displayName(), rolId));
                } else {
                    // Registro público
                    log.setIdUsuario(id);
                    log.setNombreUsuario(username);
                    log.setAccion("Registro");
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
                    
                    // Usar el método específico del EmailService para bienvenida
                    emailService.sendWelcomeEmail(email, nombres, apellidos, username);
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
