package com.sumixkids.web;

import com.sumixkids.dao.*;
import com.sumixkids.model.*;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Servlet para que los administradores creen nuevos usuarios desde el panel admin.
 * Mantiene la navegación del panel administrativo.
 * GET: muestra el formulario de creación.
 * POST: valida datos y crea el usuario.
 */
@WebServlet("/admin_crear_usuario")
public class AdminCrearUsuarioServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LogAuditoriaDAO logDAO = new LogAuditoriaDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final DocenteDAO docenteDAO = new DocenteDAO();
    private final AcompañanteDAO acompañanteDAO = new AcompañanteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Verificar que el usuario sea administrador
        HttpSession session = req.getSession(false);
        Usuario admin = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        
        if (admin == null || admin.getRolId() != 1) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        // Mostrar el formulario de creación de usuario para admin
        req.getRequestDispatcher("/admin_crear_usuario.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        // Verificar que el usuario sea administrador
        HttpSession session = req.getSession(false);
        Usuario admin = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        
        if (admin == null || admin.getRolId() != 1) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String username = req.getParameter("username");
        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String gradoParam = req.getParameter("grado");
        String rolIdStr = req.getParameter("rolId");
        
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
        
        // Validar rol (obligatorio para admin)
        int rolId = 0;
        if (rolIdStr == null || rolIdStr.trim().isEmpty()) {
            errores.add("Debe seleccionar un rol para el usuario.");
            req.setAttribute("errorRolId", true);
        } else {
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
            req.getRequestDispatcher("/admin_crear_usuario.jsp").forward(req, resp);
            return;
        }

        try {
            // Verificar si el usuario o email ya existen
            if (usuarioDAO.existsByUsernameOrEmail(username, email)) {
                req.setAttribute("error", "El nombre de usuario o correo electrónico ya están registrados");
                setFormValues(req, username, nombres, apellidos, email, gradoParam, rolIdStr);
                req.getRequestDispatcher("/admin_crear_usuario.jsp").forward(req, resp);
                return;
            }

            // Crear nuevo usuario
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setNombres(nombres);
            u.setApellidos(apellidos);
            u.setEmail(email);
            u.setPasswordHash(PasswordUtil.hash(password));
            u.setRolId(rolId);
            
            // Asignar grado normalizado (con °) solo si es estudiante
            if (rolId == 3 && gradoNormalizado != null && !gradoNormalizado.trim().isEmpty()) {
                u.setGrado(gradoNormalizado.trim());
            } else {
                u.setGrado("");
            }
            
            u.setFechaRegistro(ZonedDateTime.now(ZoneId.of("America/Bogota")).toLocalDateTime());
            
            int id = usuarioDAO.createUser(u);
            if (id > 0) {
                // Crear registro en subtipo según el rol
                crearSubtipo(id, rolId, gradoNormalizado);
                
                // Registrar en auditoría
                LogAuditoria log = new LogAuditoria();
                log.setFechaHora(LocalDateTime.now());
                log.setIpUsuario(req.getRemoteAddr());
                log.setIdUsuario(admin.getId());
                log.setNombreUsuario(admin.getUsername());
                log.setAccion("Registro Admin");
                log.setDescripcion(String.format("Admin creó usuario '%s' (%s %s) con rol ID %d", 
                    username, nombres, apellidos, rolId));
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
                    System.err.println("Error al enviar correo de bienvenida: " + e.getMessage());
                    e.printStackTrace();
                }
                
                // Mostrar mensaje de éxito y limpiar formulario
                req.setAttribute("success", "✅ Usuario registrado exitosamente. Se ha enviado un correo de bienvenida.");
                // Limpiar valores del formulario para permitir crear otro usuario
                clearFormValues(req);
                req.getRequestDispatcher("/admin_crear_usuario.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "❌ No se pudo registrar el usuario. Inténtelo nuevamente.");
                setFormValues(req, username, nombres, apellidos, email, gradoParam, rolIdStr);
                req.getRequestDispatcher("/admin_crear_usuario.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            // Log del error
            LogAuditoria log = new LogAuditoria();
            log.setFechaHora(LocalDateTime.now());
            log.setIpUsuario(req.getRemoteAddr());
            log.setIdUsuario(admin.getId());
            log.setNombreUsuario(admin.getUsername());
            log.setAccion("Error Registro Admin");
            log.setDescripcion(String.format("Error al crear usuario '%s': %s", username, e.getMessage()));
            log.setTablaAfectada("usuarios");
            log.setEstado("ERROR");
            
            try {
                logDAO.registrarLog(log);
            } catch (Exception logException) {
                System.err.println("Error al registrar log de auditoría: " + logException.getMessage());
            }
            
            req.setAttribute("error", "❌ Error interno del sistema. Por favor, contacte al administrador.");
            setFormValues(req, username, nombres, apellidos, email, gradoParam, rolIdStr);
            req.getRequestDispatcher("/admin_crear_usuario.jsp").forward(req, resp);
        }
    }

    /**
     * Pone los valores del formulario como atributos para que el JSP los conserve
     * tras error.
     */
    private void setFormValues(HttpServletRequest req, String username, String nombres, String apellidos,
            String email, String grado, String rolId) {
        req.setAttribute("username", username != null ? username : "");
        req.setAttribute("nombres", nombres != null ? nombres : "");
        req.setAttribute("apellidos", apellidos != null ? apellidos : "");
        req.setAttribute("email", email != null ? email : "");
        req.setAttribute("grado", grado != null ? grado : "");
        req.setAttribute("rolId", rolId != null ? rolId : "");
    }
    
    /**
     * Limpia los valores del formulario tras registro exitoso
     */
    private void clearFormValues(HttpServletRequest req) {
        req.setAttribute("username", "");
        req.setAttribute("nombres", "");
        req.setAttribute("apellidos", "");
        req.setAttribute("email", "");
        req.setAttribute("grado", "");
        req.setAttribute("rolId", "");
    }
    
    /**
     * Crear registro en tabla de subtipo según el rol del usuario
     */
    private void crearSubtipo(int usuarioId, int rolId, String grado) {
        try {
            switch (rolId) {
                case 3: // Estudiante
                    crearEstudiante(usuarioId, grado);
                    break;
                case 2: // Docente
                    crearDocente(usuarioId);
                    break;
                case 4: // Acompañante
                    crearAcompañante(usuarioId);
                    break;
                default:
                    // Admin (rol 1) no necesita subtipo específico
                    System.out.println("Usuario con rol " + rolId + " no requiere subtipo específico");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error al crear subtipo para usuario " + usuarioId + " con rol " + rolId + ": " + e.getMessage());
            e.printStackTrace();
            // No interrumpir el flujo principal, el usuario base ya fue creado
        }
    }
    
    /**
     * Crear registro de estudiante
     */
    private void crearEstudiante(int usuarioId, String grado) throws SQLException {
        Estudiante estudiante = new Estudiante();
        estudiante.setUsuarioId(usuarioId);
        estudiante.setNumeroEstudiante(estudianteDAO.generarNumeroEstudiante());
        estudiante.setGradoActual(grado != null && !grado.trim().isEmpty() ? grado : "1°");
        estudiante.setEstadoAcademico(Estudiante.EstadoAcademico.ACTIVO);
        
        int estudianteId = estudianteDAO.crear(estudiante);
        if (estudianteId > 0) {
            System.out.println("✅ Estudiante creado con ID: " + estudianteId + " y número: " + estudiante.getNumeroEstudiante());
        } else {
            System.err.println("❌ No se pudo crear el registro de estudiante para usuario: " + usuarioId);
        }
    }
    
    /**
     * Crear registro de docente
     */
    private void crearDocente(int usuarioId) throws SQLException {
        Docente docente = new Docente();
        docente.setUsuarioId(usuarioId);
        docente.setNumeroEmpleado(docenteDAO.generarNumeroEmpleado());
        docente.setEstadoLaboral(Docente.EstadoLaboral.ACTIVO);
        
        int docenteId = docenteDAO.crear(docente);
        if (docenteId > 0) {
            System.out.println("✅ Docente creado con ID: " + docenteId + " y número: " + docente.getNumeroEmpleado());
        } else {
            System.err.println("❌ No se pudo crear el registro de docente para usuario: " + usuarioId);
        }
    }
    
    /**
     * Crear registro de acompañante
     */
    private void crearAcompañante(int usuarioId) throws SQLException {
        Acompañante acompañante = new Acompañante();
        acompañante.setUsuarioId(usuarioId);
        acompañante.setRelacionEstudiante(Acompañante.RelacionEstudiante.OTRO); // Por defecto, se puede cambiar después
        acompañante.setRecibirNotificaciones(true);
        acompañante.setAutorizacionRecoger(true);
        
        int acompañanteId = acompañanteDAO.crear(acompañante);
        if (acompañanteId > 0) {
            System.out.println("✅ Acompañante creado con ID: " + acompañanteId);
        } else {
            System.err.println("❌ No se pudo crear el registro de acompañante para usuario: " + usuarioId);
        }
    }
}