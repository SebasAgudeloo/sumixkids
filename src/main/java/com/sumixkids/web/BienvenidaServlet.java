package com.sumixkids.web;

import com.sumixkids.model.Usuario;
import com.sumixkids.dao.*;
import com.sumixkids.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Página de saludo que sólo se muestra si el usuario ya inició sesión.
 */
public class BienvenidaServlet extends HttpServlet {
    
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final DocenteDAO docenteDAO = new DocenteDAO();
    private final AcompañanteDAO acompañanteDAO = new AcompañanteDAO();
    private final EstudianteAcompananteDAO estudianteAcompananteDAO = new EstudianteAcompananteDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        // Cargar información específica del subtipo según el rol
        cargarInformacionSubtipo(req, u);
        
        // Redireccionar a dashboard específico según el rol
        String dashboardPath = getDashboardPath(u.getRolId());
        
        // Si es admin, cargar estadísticas específicas
        if (u.getRolId() == 1) {
            cargarEstadisticasAdmin(req);
        }
        
        req.getRequestDispatcher(dashboardPath).forward(req, resp);
    }
    
    /**
     * Obtener la ruta del dashboard específico según el rol
     */
    private String getDashboardPath(Integer rolId) {
        switch (rolId) {
            case 1: return "/admin_dashboard.jsp";      // Administrador
            case 2: return "/docente_dashboard.jsp";    // Docente
            case 3: return "/estudiante_dashboard.jsp"; // Estudiante
            case 4: return "/padre_dashboard.jsp";      // Acompañante/Tutor
            default: return "/bienvenida.jsp";          // Fallback al dashboard genérico
        }
    }
    
    /**
     * Cargar estadísticas específicas para el dashboard de administrador
     */
    private void cargarEstadisticasAdmin(HttpServletRequest req) {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            // Obtener conteos por rol
            int totalAdmins = usuarioDAO.getCountByRol(1);
            int totalDocentes = usuarioDAO.getCountByRol(2);
            int totalEstudiantes = usuarioDAO.getCountByRol(3);
            int totalAttendants = usuarioDAO.getCountByRol(4);
            int totalTodosUsuarios = usuarioDAO.getTotalUsers();
            
            // Conteos para dashboard admin
            req.setAttribute("totalAdmins", totalAdmins);
            req.setAttribute("totalDocentes", totalDocentes);
            req.setAttribute("totalEstudiantes", totalEstudiantes);
            req.setAttribute("totalAttendants", totalAttendants);
            req.setAttribute("totalTodosUsuarios", totalTodosUsuarios);
            
            // TODO: Agregar estadísticas de auditoría cuando esté implementado
            req.setAttribute("totalEventos", 0);
            req.setAttribute("eventosFallidos", 0);
            
        } catch (Exception e) {
            System.err.println("Error al cargar estadísticas de admin: " + e.getMessage());
            req.setAttribute("error", "No se pudieron cargar las estadísticas del sistema");
        }
    }
    
    /**
     * Cargar información específica del subtipo según el rol del usuario
     */
    private void cargarInformacionSubtipo(HttpServletRequest req, Usuario usuario) {
        try {
            switch (usuario.getRolId()) {
                case 3: // Estudiante
                    cargarInfoEstudiante(req, usuario);
                    break;
                case 2: // Docente
                    cargarInfoDocente(req, usuario);
                    break;
                case 4: // Acompañante
                    cargarInfoAcompañante(req, usuario);
                    break;
                default:
                    // Admin no necesita información adicional específica
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar información del subtipo para usuario " + usuario.getId() + ": " + e.getMessage());
            e.printStackTrace();
            // No interrumpir el flujo, mostrar dashboard básico
        }
    }
    
    /**
     * Cargar información específica del estudiante
     */
    private void cargarInfoEstudiante(HttpServletRequest req, Usuario usuario) throws Exception {
        Estudiante estudiante = estudianteDAO.findByUsuarioId(usuario.getId());
        if (estudiante != null) {
            req.setAttribute("estudiante", estudiante);
            
            // TODO: Implementar carga de acompañantes cuando los modelos estén listos
            // List<Acompañante> acompañantes = estudianteAcompananteDAO.findAcompañantesByEstudianteId(estudiante.getId());
            // req.setAttribute("acompañantes", acompañantes);
            // Acompañante acompañantePrincipal = estudianteAcompananteDAO.findAcompañantePrincipal(estudiante.getId());
            // req.setAttribute("acompañantePrincipal", acompañantePrincipal);
            
            System.out.println("✅ Info estudiante cargada: " + estudiante.getNumeroEstudiante() + 
                             " - Grado: " + estudiante.getGradoActual());
        } else {
            System.out.println("⚠️ No se encontró información de estudiante para usuario: " + usuario.getId());
        }
    }
    
    /**
     * Cargar información específica del docente
     */
    private void cargarInfoDocente(HttpServletRequest req, Usuario usuario) throws Exception {
        Docente docente = docenteDAO.findByUsuarioId(usuario.getId());
        if (docente != null) {
            req.setAttribute("docente", docente);
            
            // TODO: En el futuro, cargar estudiantes asignados al docente
            // List<Estudiante> estudiantesAsignados = estudianteDAO.findByDocenteId(docente.getId());
            // req.setAttribute("estudiantesAsignados", estudiantesAsignados);
            
            System.out.println("✅ Info docente cargada: " + docente.getNumeroEmpleado() + 
                             " - Especialidad: " + docente.getEspecialidad());
        } else {
            System.out.println("⚠️ No se encontró información de docente para usuario: " + usuario.getId());
        }
    }
    
    /**
     * Cargar información específica del acompañante
     */
    private void cargarInfoAcompañante(HttpServletRequest req, Usuario usuario) throws Exception {
        Acompañante acompañante = acompañanteDAO.findByUsuarioId(usuario.getId());
        if (acompañante != null) {
            req.setAttribute("acompañante", acompañante);
            
            // TODO: Implementar carga de estudiantes a cargo cuando los modelos estén listos
            // List<Estudiante> estudiantesACargo = estudianteAcompananteDAO.findEstudiantesByAcompañanteId(acompañante.getId());
            // req.setAttribute("estudiantesACargo", estudiantesACargo);
            
            System.out.println("✅ Info acompañante cargada - Relación: " + acompañante.getRelacionEstudiante());
        } else {
            System.out.println("⚠️ No se encontró información de acompañante para usuario: " + usuario.getId());
        }
    }
}
