package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.util.PasswordUtil;
import com.sumixkids.util.ValidacionUtil;
import com.sumixkids.util.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet para editar usuarios existentes
 */
public class EditarUsuarioServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LogAuditoriaDAO logDAO = new LogAuditoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Verificar que sea admin
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario admin = (Usuario) session.getAttribute("usuario");
        if (admin.getRolId() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        // Obtener ID del usuario a editar
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            session.setAttribute("mensajeError", "❌ ID de usuario no especificado");
            resp.sendRedirect(req.getContextPath() + "/usuarios");
            return;
        }

        try {
            int userId = Integer.parseInt(idStr);
            Usuario usuario = usuarioDAO.findById(userId);
            
            if (usuario == null) {
                session.setAttribute("mensajeError", "❌ Usuario no encontrado");
                resp.sendRedirect(req.getContextPath() + "/usuarios");
                return;
            }

            // Enviar datos del usuario al JSP
            req.setAttribute("usuarioEditar", usuario);
            req.getRequestDispatcher("/editar_usuario.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            session.setAttribute("mensajeError", "❌ ID de usuario inválido");
            resp.sendRedirect(req.getContextPath() + "/usuarios");
        } catch (SQLException e) {
            session.setAttribute("mensajeError", "❌ Error al cargar el usuario: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/usuarios");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        // Verificar que sea admin
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Usuario admin = (Usuario) session.getAttribute("usuario");
        if (admin.getRolId() != 1) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        // Obtener parámetros
        String idStr = req.getParameter("id");
        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String email = req.getParameter("email");
        String rolIdStr = req.getParameter("rolId");
        String gradoParam = req.getParameter("grado");
        String nuevaPassword = req.getParameter("nuevaPassword");
        
        List<String> errores = new ArrayList<>();
        
        // Validar ID
        if (idStr == null || idStr.trim().isEmpty()) {
            errores.add("ID de usuario no especificado");
        }
        
        int userId = 0;
        try {
            userId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            errores.add("ID de usuario inválido");
        }
        
        // Validaciones de campos
        if (!ValidacionUtil.esNombreValido(nombres)) {
            errores.add(ValidacionUtil.getMensajeError("nombres", nombres));
            req.setAttribute("errorNombres", true);
        }
        
        if (!ValidacionUtil.esApellidoValido(apellidos)) {
            errores.add(ValidacionUtil.getMensajeError("apellidos", apellidos));
            req.setAttribute("errorApellidos", true);
        }
        
        if (!ValidacionUtil.esCorreoValido(email)) {
            errores.add(ValidacionUtil.getMensajeError("email", email));
            req.setAttribute("errorEmail", true);
        }
        
        // Validar rol
        int rolId = 3;
        if (rolIdStr != null && !rolIdStr.trim().isEmpty()) {
            try {
                rolId = Integer.parseInt(rolIdStr);
                if (rolId < 1 || rolId > 4) {
                    errores.add("Rol inválido");
                    req.setAttribute("errorRolId", true);
                }
            } catch (NumberFormatException e) {
                errores.add("Rol inválido");
                req.setAttribute("errorRolId", true);
            }
        }
        
        // Normalizar y validar grado si es estudiante
        String gradoNormalizado = null;
        if (rolId == 3) {
            if (gradoParam == null || gradoParam.trim().isEmpty()) {
                errores.add("El grado es obligatorio para estudiantes");
                req.setAttribute("errorGrado", true);
            } else {
                gradoNormalizado = gradoParam.trim();
                if (!gradoNormalizado.endsWith("°")) {
                    gradoNormalizado += "°";
                }
                if (!ValidacionUtil.esGradoValido(gradoNormalizado)) {
                    errores.add(ValidacionUtil.getMensajeError("grado", gradoNormalizado));
                    req.setAttribute("errorGrado", true);
                }
            }
        }
        
        // Validar nueva contraseña si se proporciona
        if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
            if (!ValidacionUtil.esPasswordValida(nuevaPassword)) {
                errores.add(ValidacionUtil.getMensajePasswordDetallado(nuevaPassword));
                req.setAttribute("errorPassword", true);
            }
        }
        
        // Si hay errores, volver al formulario
        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            try {
                Usuario usuario = usuarioDAO.findById(userId);
                if (usuario != null) {
                    // Actualizar con valores del formulario
                    usuario.setNombres(nombres);
                    usuario.setApellidos(apellidos);
                    usuario.setEmail(email);
                    usuario.setRolId(rolId);
                    usuario.setGrado(gradoParam);
                    req.setAttribute("usuarioEditar", usuario);
                }
            } catch (SQLException e) {
                // Ignorar error al cargar usuario
            }
            req.getRequestDispatcher("/editar_usuario.jsp").forward(req, resp);
            return;
        }
        
        try {
            // Obtener usuario actual de la BD
            Usuario usuario = usuarioDAO.findById(userId);
            if (usuario == null) {
                session.setAttribute("mensajeError", "❌ Usuario no encontrado");
                resp.sendRedirect(req.getContextPath() + "/usuarios");
                return;
            }
            
            // Verificar si el email cambió y si ya existe
            if (!usuario.getEmail().equals(email)) {
                if (usuarioDAO.existeEmail(email)) {
                    req.setAttribute("error", "❌ El correo electrónico ya está registrado por otro usuario");
                    req.setAttribute("errorEmail", true);
                    usuario.setNombres(nombres);
                    usuario.setApellidos(apellidos);
                    usuario.setEmail(email);
                    usuario.setRolId(rolId);
                    usuario.setGrado(gradoParam);
                    req.setAttribute("usuarioEditar", usuario);
                    req.getRequestDispatcher("/editar_usuario.jsp").forward(req, resp);
                    return;
                }
            }
            
            // Construir descripción de cambios para auditoría
            List<String> cambios = new ArrayList<>();
            
            if (!usuario.getNombres().equals(nombres)) {
                cambios.add(String.format("nombres: '%s' → '%s'", usuario.getNombres(), nombres));
            }
            if (!usuario.getApellidos().equals(apellidos)) {
                cambios.add(String.format("apellidos: '%s' → '%s'", usuario.getApellidos(), apellidos));
            }
            if (!usuario.getEmail().equals(email)) {
                cambios.add(String.format("email: '%s' → '%s'", usuario.getEmail(), email));
            }
            if (usuario.getRolId() != rolId) {
                String rolAntes = SecurityUtils.getNombreRol(usuario.getRolId());
                String rolDespues = SecurityUtils.getNombreRol(rolId);
                cambios.add(String.format("rol: %s → %s", rolAntes, rolDespues));
            }
            
            String gradoActual = usuario.getGrado() != null ? usuario.getGrado() : "";
            String gradoNuevo = (rolId == 3 && gradoNormalizado != null) ? gradoNormalizado : "";
            if (!gradoActual.equals(gradoNuevo)) {
                cambios.add(String.format("grado: '%s' → '%s'", gradoActual, gradoNuevo));
            }
            
            if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
                cambios.add("contraseña actualizada");
            }
            
            // Actualizar usuario
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setRolId(rolId);
            usuario.setGrado(gradoNuevo);
            
            // Actualizar contraseña si se proporcionó
            if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
                usuario.setPasswordHash(PasswordUtil.hash(nuevaPassword));
            }
            
            boolean actualizado = usuarioDAO.updateUser(usuario);
            
            if (actualizado && !cambios.isEmpty()) {
                // Registrar en auditoría
                LogAuditoria log = new LogAuditoria();
                log.setFechaHora(LocalDateTime.now());
                log.setIdUsuario(admin.getId());
                log.setNombreUsuario(admin.getUsername());
                log.setIpUsuario(req.getRemoteAddr());
                log.setAccion("EDITAR_USUARIO");
                log.setTablaAfectada("usuarios");
                log.setDescripcion(String.format("Usuario '%s' (ID: %d) editado. Cambios: %s", 
                    usuario.getUsername(), usuario.getId(), String.join(", ", cambios)));
                log.setEstado("EXITOSO");
                logDAO.registrarLog(log);
                
                // Enviar correo de notificación al usuario con los cambios
                try {
                    java.util.Properties props = new java.util.Properties();
                    try (java.io.InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                        if (in != null) props.load(in);
                    }
                    String mailUser = props.getProperty("mail.smtp.user");
                    String mailPass = props.getProperty("mail.smtp.pass");
                    com.sumixkids.service.EmailService emailService = new com.sumixkids.service.EmailService(mailUser, mailPass);
                    
                    // Usar el nuevo método sendDataChangeEmail con los cambios detectados
                    emailService.sendDataChangeEmail(usuario.getEmail(), usuario.getNombres(), 
                                                   usuario.getApellidos(), cambios, admin.getUsername());
                } catch (Exception emailEx) {
                    System.err.println("No se pudo enviar correo de notificación de cambios: " + emailEx.getMessage());
                    emailEx.printStackTrace();
                }
                
                session.setAttribute("mensajeExito", "✅ Usuario actualizado exitosamente");
            } else if (!cambios.isEmpty()) {
                session.setAttribute("mensajeError", "❌ No se pudo actualizar el usuario");
            } else {
                session.setAttribute("mensajeInfo", "ℹ️ No se realizaron cambios");
            }
            
            resp.sendRedirect(req.getContextPath() + "/usuarios");
            
        } catch (SQLException e) {
            req.setAttribute("error", "❌ Error al actualizar el usuario: " + e.getMessage());
            try {
                Usuario usuario = usuarioDAO.findById(userId);
                if (usuario != null) {
                    usuario.setNombres(nombres);
                    usuario.setApellidos(apellidos);
                    usuario.setEmail(email);
                    usuario.setRolId(rolId);
                    usuario.setGrado(gradoParam);
                    req.setAttribute("usuarioEditar", usuario);
                }
            } catch (SQLException ex) {
                // Ignorar
            }
            req.getRequestDispatcher("/editar_usuario.jsp").forward(req, resp);
        }
    }
}
