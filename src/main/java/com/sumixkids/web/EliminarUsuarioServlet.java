package com.sumixkids.web;

import com.sumixkids.dao.UsuarioDAO;
import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.Usuario;
import com.sumixkids.model.LogAuditoria;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Servlet para eliminar usuarios de forma segura.
 * Recibe parámetro userId por POST.
 */
@WebServlet(name = "EliminarUsuarioServlet", urlPatterns = {"/eliminarUsuario"})
public class EliminarUsuarioServlet extends HttpServlet {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Mostrar página de confirmación
        String userIdStr = req.getParameter("userId");
        if (userIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro userId");
            return;
        }
        
        // Verificar que sea administrador
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
        
        String logIdStr = req.getParameter("logId");
        if (logIdStr != null) {
            try {
                Long logId = Long.parseLong(logIdStr);
                req.setAttribute("logId", logId);
            } catch (NumberFormatException e) {
                // Log error but continue
                System.err.println("ID de log inválido: " + logIdStr);
            }
        }
        
        req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Usuario admin = (Usuario) session.getAttribute("usuario");
        String userIdStr = req.getParameter("userId");
        String adminPassword = req.getParameter("adminPassword");

        if (userIdStr == null || adminPassword == null || adminPassword.trim().isEmpty()) {
            req.setAttribute("error", "Parámetros inválidos");
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            
            // Verificar contraseña del admin
            Usuario adminDB = usuarioDAO.findById(admin.getId());
            if (!BCrypt.checkpw(adminPassword, adminDB.getPasswordHash())) {
                System.out.println("Debug: Contraseña incorrecta"); // Debug log
                registrarAuditoria(admin, req.getRemoteAddr(), "ELIMINACION_FALLIDA", 
                                 "usuarios", "Intento fallido", 
                                 "Contraseña incorrecta", "FALLIDO", userId);
                req.setAttribute("error", "Contraseña incorrecta");
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
                return;
            }

            boolean eliminado = usuarioDAO.deleteUser(userId);
            
            if (eliminado) {
                System.out.println("Debug: Usuario eliminado correctamente"); // Debug log
                registrarAuditoria(admin, req.getRemoteAddr(), "ELIMINAR_USUARIO", 
                                 "usuarios", "Usuario eliminado", 
                                 "Usuario eliminado correctamente", "EXITOSO", userId);
                resp.sendRedirect(req.getContextPath() + "/bienvenida?mensaje=Usuario eliminado correctamente");
            } else {
                System.out.println("Debug: No se pudo eliminar el usuario"); // Debug log
                registrarAuditoria(admin, req.getRemoteAddr(), "ELIMINACION_FALLIDA", 
                                 "usuarios", "Eliminación fallida", 
                                 "No se pudo eliminar el usuario", "FALLIDO", userId);
                req.setAttribute("error", "No se pudo eliminar el usuario");
                req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            System.err.println("Debug: Error en eliminación: " + e.getMessage()); // Debug log
            e.printStackTrace(); // Print stack trace for debugging
            registrarAuditoria(admin, req.getRemoteAddr(), "ERROR_ELIMINACION", 
                              "usuarios", "Error en eliminación", 
                              "Error: " + e.getMessage(), "ERROR", 
                              Integer.parseInt(userIdStr));
            req.setAttribute("error", "Error al procesar la eliminación: " + e.getMessage());
            req.getRequestDispatcher("/eliminar_confirmar.jsp").forward(req, resp);
        }
    }

    private void registrarAuditoria(Usuario admin, String ip, String accion, 
                                  String tabla, String valorAnterior, 
                                  String descripcion, String estado, int userId) {
        try {
            LogAuditoria log = new LogAuditoria();
            log.setFechaHora(LocalDateTime.now());
            log.setIdUsuario(admin.getId());
            log.setNombreUsuario(admin.getUsername());
            log.setIpUsuario(ip);
            log.setAccion(accion);
            log.setTablaAfectada(tabla);
            log.setValorAnterior("ID: " + userId + " - " + valorAnterior);
            log.setDescripcion(descripcion);
            log.setEstado(estado);
            log.setAprobadoPorAdminId(admin.getId());

            LogAuditoriaDAO logDAO = new LogAuditoriaDAO();
            logDAO.registrarLog(log);
            
            System.out.println("Debug: Log de auditoría registrado exitosamente"); // Debug log
            System.out.println("Debug: Detalles del log:");
            System.out.println("- Acción: " + log.getAccion());
            System.out.println("- Usuario: " + log.getNombreUsuario());
            System.out.println("- Estado: " + log.getEstado());
        } catch (SQLException e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
}
