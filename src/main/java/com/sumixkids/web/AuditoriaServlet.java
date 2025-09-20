package com.sumixkids.web;

import com.sumixkids.dao.LogAuditoriaDAO;
import com.sumixkids.model.LogAuditoria;
import com.sumixkids.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/auditoria")
public class AuditoriaServlet extends HttpServlet {
    
    private final LogAuditoriaDAO logDAO = new LogAuditoriaDAO();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        try {
            // Obtener parámetros de filtro de fecha
            String fechaDesde = req.getParameter("desde");
            String fechaHasta = req.getParameter("hasta");

            // Convertir fechas usando DATE_FORMAT
            LocalDateTime desde = null;
            LocalDateTime hasta = null;

            if (fechaDesde != null && !fechaDesde.trim().isEmpty()) {
                desde = LocalDateTime.parse(fechaDesde + " 00:00:00", DATE_FORMAT);
            }
            if (fechaHasta != null && !fechaHasta.trim().isEmpty()) {
                hasta = LocalDateTime.parse(fechaHasta + " 23:59:59", DATE_FORMAT);
            }

            // Buscar logs con el filtro de fechas
            List<LogAuditoria> logs = logDAO.buscarLogs(null, desde, hasta, null);
            System.out.println("Logs encontrados: " + logs.size());
            
            req.setAttribute("logs", logs);
            req.setAttribute("desdeActual", fechaDesde);
            req.setAttribute("hastaActual", fechaHasta);
            
            req.getRequestDispatcher("/auditoria.jsp").forward(req, resp);
            
        } catch (SQLException e) {
            System.err.println("Error al cargar logs: " + e.getMessage());
            throw new ServletException("Error al cargar logs", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
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

        String accion = req.getParameter("accion");
        String logIdStr = req.getParameter("logId");

        if ("eliminar".equals(accion) && logIdStr != null) {
            try {
                Long logId = Long.parseLong(logIdStr);
                // Redirigir a EliminarUsuarioServlet con el logId
                resp.sendRedirect(req.getContextPath() + 
                    "/eliminarUsuario?logId=" + logId);
                return;
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID de log inválido");
                doGet(req, resp);
                return;
            }
        }

        req.setAttribute("error", "Acción no válida");
        doGet(req, resp);
    }
}
