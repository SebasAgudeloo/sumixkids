package com.sumixkids.web;

import com.sumixkids.dao.AsignacionDAO;
import com.sumixkids.model.Asignacion;
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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Servlet para gestionar las asignaciones docente-estudiante.
 * Controla operaciones CRUD y navegación entre vistas.
 * 
 * Acciones disponibles:
 * - listar: Muestra todas las asignaciones (vista por defecto)
 * - agregar: Muestra formulario para nueva asignación
 * - editar: Muestra formulario para editar asignación existente
 * - guardar: Procesa creación de nueva asignación
 * - actualizar: Procesa actualización de asignación existente
 * - eliminar: Finaliza una asignación (eliminación lógica)
 * - delete: Elimina físicamente una asignación
 */
@WebServlet("/asignaciones")
public class AsignacionServlet extends HttpServlet {
    
    private final AsignacionDAO asignacionDAO = new AsignacionDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // Verificar autenticación y autorización
        if (!esUsuarioAutorizado(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String action = req.getParameter("action");
        if (action == null) {
            action = "listar";
        }
        
        try {
            switch (action) {
                case "listar":
                    listarAsignaciones(req, resp);
                    break;
                case "agregar":
                    mostrarFormularioAgregar(req, resp);
                    break;
                case "editar":
                    mostrarFormularioEditar(req, resp);
                    break;
                case "eliminar":
                    eliminarAsignacion(req, resp);
                    break;
                case "delete":
                    eliminarFisicamente(req, resp);
                    break;
                default:
                    listarAsignaciones(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Error al acceder a la base de datos: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        
        // Verificar autenticación y autorización
        if (!esUsuarioAutorizado(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String action = req.getParameter("action");
        
        try {
            if ("guardar".equals(action)) {
                guardarAsignacion(req, resp);
            } else if ("actualizar".equals(action)) {
                actualizarAsignacion(req, resp);
            } else if ("cambiarEstado".equals(action)) {
                cambiarEstadoAsignacion(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/asignaciones");
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
    
    /**
     * Muestra la lista de todas las asignaciones.
     */
    private void listarAsignaciones(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        List<Asignacion> asignaciones = asignacionDAO.listar();
        req.setAttribute("asignaciones", asignaciones);
        req.getRequestDispatcher("/asig_doc_estu.jsp").forward(req, resp);
    }
    
    /**
     * Muestra el formulario para agregar una nueva asignación.
     */
    private void mostrarFormularioAgregar(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        // Cargar datos necesarios para los select
        cargarDatosFormulario(req);
        req.getRequestDispatcher("/asig_doc_estu_add.jsp").forward(req, resp);
    }
    
    /**
     * Muestra el formulario para editar una asignación existente.
     */
    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            req.setAttribute("error", "ID de asignación no válido");
            listarAsignaciones(req, resp);
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Asignacion asignacion = asignacionDAO.obtenerPorId(id);
            
            if (asignacion == null) {
                req.setAttribute("error", "Asignación no encontrada");
                listarAsignaciones(req, resp);
                return;
            }
            
            req.setAttribute("asignacion", asignacion);
            cargarDatosFormulario(req);
            req.getRequestDispatcher("/asig_doc_estu_edit.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID de asignación no válido");
            listarAsignaciones(req, resp);
        }
    }
    
    /**
     * Guarda una nueva asignación.
     */
    private void guardarAsignacion(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        try {
            // Validar y obtener parámetros
            Asignacion asignacion = construirAsignacionDesdeRequest(req);
            
            // Validaciones de negocio
            if (asignacionDAO.existeAsignacionActiva(asignacion.getIdDocente(), asignacion.getIdEstudiante())) {
                req.setAttribute("error", "Ya existe una asignación activa entre este docente y estudiante");
                req.setAttribute("asignacion", asignacion);
                cargarDatosFormulario(req);
                req.getRequestDispatcher("/asig_doc_estu_add.jsp").forward(req, resp);
                return;
            }
            
            // Obtener el usuario actual para asignado_por
            HttpSession session = req.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario != null) {
                asignacion.setAsignadoPor(usuario.getId());
            }
            
            int id = asignacionDAO.insertar(asignacion);
            
            if (id > 0) {
                req.setAttribute("success", "Asignación creada exitosamente");
            } else {
                req.setAttribute("error", "Error al crear la asignación");
            }
            
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            cargarDatosFormulario(req);
            req.getRequestDispatcher("/asig_doc_estu_add.jsp").forward(req, resp);
            return;
        }
        
        listarAsignaciones(req, resp);
    }
    
    /**
     * Actualiza una asignación existente.
     */
    private void actualizarAsignacion(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            req.setAttribute("error", "ID de asignación no válido");
            listarAsignaciones(req, resp);
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Asignacion asignacion = construirAsignacionDesdeRequest(req);
            asignacion.setId(id);
            
            // Obtener el usuario actual para asignado_por
            HttpSession session = req.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario != null) {
                asignacion.setAsignadoPor(usuario.getId());
            }
            
            boolean actualizado = asignacionDAO.actualizar(asignacion);
            
            if (actualizado) {
                req.setAttribute("success", "Asignación actualizada exitosamente");
            } else {
                req.setAttribute("error", "Error al actualizar la asignación");
            }
            
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID de asignación no válido");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            mostrarFormularioEditar(req, resp);
            return;
        }
        
        listarAsignaciones(req, resp);
    }
    
    /**
     * Elimina una asignación (eliminación lógica).
     */
    private void eliminarAsignacion(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException {
        
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/asignaciones?error=ID no válido");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            boolean eliminado = asignacionDAO.eliminar(id);
            
            if (eliminado) {
                resp.sendRedirect(req.getContextPath() + "/asignaciones?success=Asignación finalizada exitosamente");
            } else {
                resp.sendRedirect(req.getContextPath() + "/asignaciones?error=Error al finalizar la asignación");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/asignaciones?error=ID no válido");
        }
    }
    
    /**
     * Elimina físicamente una asignación de la base de datos.
     */
    private void eliminarFisicamente(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException {
        
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/asignaciones?error=ID no válido");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            boolean eliminado = asignacionDAO.eliminarFisicamente(id);
            
            if (eliminado) {
                resp.sendRedirect(req.getContextPath() + "/asignaciones?success=Asignación eliminada permanentemente");
            } else {
                resp.sendRedirect(req.getContextPath() + "/asignaciones?error=Error al eliminar la asignación");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/asignaciones?error=ID no válido");
        }
    }
    
    /**
     * Construye un objeto Asignacion desde los parámetros del request.
     */
    private Asignacion construirAsignacionDesdeRequest(HttpServletRequest req) throws IllegalArgumentException {
        Asignacion asignacion = new Asignacion();
        
        // Validar y establecer docente
        String docenteParam = req.getParameter("idDocente");
        if (docenteParam == null || docenteParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un docente");
        }
        try {
            asignacion.setIdDocente(Integer.parseInt(docenteParam));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID de docente no válido");
        }
        
        // Validar y establecer estudiante
        String estudianteParam = req.getParameter("idEstudiante");
        if (estudianteParam == null || estudianteParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un estudiante");
        }
        try {
            asignacion.setIdEstudiante(Integer.parseInt(estudianteParam));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID de estudiante no válido");
        }
        
        // Grado escolar (puede ser null)
        String gradoParam = req.getParameter("idGradoEscolar");
        if (gradoParam != null && !gradoParam.trim().isEmpty() && !"".equals(gradoParam)) {
            try {
                asignacion.setIdGradoEscolar(Integer.parseInt(gradoParam));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID de grado escolar no válido");
            }
        }
        
        // Fecha de asignación
        String fechaAsignacionParam = req.getParameter("fechaAsignacion");
        if (fechaAsignacionParam != null && !fechaAsignacionParam.trim().isEmpty()) {
            try {
                asignacion.setFechaAsignacion(LocalDateTime.parse(fechaAsignacionParam));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de fecha de asignación no válido");
            }
        } else {
            asignacion.setFechaAsignacion(LocalDateTime.now());
        }
        
        // Fecha de finalización (opcional)
        String fechaFinalizacionParam = req.getParameter("fechaFinalizacion");
        if (fechaFinalizacionParam != null && !fechaFinalizacionParam.trim().isEmpty()) {
            try {
                asignacion.setFechaFinalizacion(LocalDateTime.parse(fechaFinalizacionParam));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de fecha de finalización no válido");
            }
        }
        
        // Estado
        String estado = req.getParameter("estado");
        if (estado == null || estado.trim().isEmpty()) {
            asignacion.setEstado("ACTIVA");
        } else {
            if (!estado.equals("ACTIVA") && !estado.equals("FINALIZADA") && !estado.equals("SUSPENDIDA")) {
                throw new IllegalArgumentException("Estado no válido");
            }
            asignacion.setEstado(estado);
        }
        
        // Observaciones
        asignacion.setObservaciones(req.getParameter("observaciones"));
        
        return asignacion;
    }
    
    /**
     * Carga los datos necesarios para los formularios (docentes, estudiantes, grados).
     */
    private void cargarDatosFormulario(HttpServletRequest req) throws SQLException {
        List<Map<String, Object>> docentes = asignacionDAO.obtenerDocentes();
        List<Map<String, Object>> estudiantes = asignacionDAO.obtenerEstudiantes();
        List<Map<String, Object>> grados = asignacionDAO.obtenerGrados();
        
        req.setAttribute("docentes", docentes);
        req.setAttribute("estudiantes", estudiantes);
        req.setAttribute("grados", grados);
    }
    
    /**
     * Verifica si el usuario tiene autorización para acceder al módulo de asignaciones.
     * Solo administradores y docentes pueden gestionar asignaciones.
     */
    private boolean esUsuarioAutorizado(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return false;
        
        // Solo administradores (rol 1) y docentes (rol 2) pueden acceder
        Integer rolId = usuario.getRolId();
        return rolId != null && (rolId == 1 || rolId == 2);
    }
    
    /**
     * Cambia el estado de una asignación.
     */
    private void cambiarEstadoAsignacion(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String nuevoEstado = req.getParameter("nuevoEstado");
            
            // Validar el estado
            if (nuevoEstado == null || 
                (!nuevoEstado.equals("ACTIVA") && !nuevoEstado.equals("SUSPENDIDA") && !nuevoEstado.equals("FINALIZADA"))) {
                req.setAttribute("error", "Estado inválido");
                listarAsignaciones(req, resp);
                return;
            }
            
            // Obtener la asignación actual
            Asignacion asignacion = asignacionDAO.obtenerPorId(id);
            if (asignacion == null) {
                req.setAttribute("error", "Asignación no encontrada");
                listarAsignaciones(req, resp);
                return;
            }
            
            // Cambiar el estado
            asignacion.setEstado(nuevoEstado);
            
            // Si se finaliza, establecer la fecha de finalización
            if ("FINALIZADA".equals(nuevoEstado)) {
                asignacion.setFechaFinalizacion(java.time.LocalDateTime.now());
            } else {
                // Si se reactiva, limpiar la fecha de finalización
                asignacion.setFechaFinalizacion(null);
            }
            
            boolean actualizado = asignacionDAO.actualizar(asignacion);
            
            if (actualizado) {
                String mensaje = switch (nuevoEstado) {
                    case "ACTIVA" -> "Asignación activada exitosamente";
                    case "SUSPENDIDA" -> "Asignación suspendida exitosamente";
                    case "FINALIZADA" -> "Asignación finalizada exitosamente";
                    default -> "Estado actualizado exitosamente";
                };
                req.setAttribute("success", mensaje);
            } else {
                req.setAttribute("error", "Error al cambiar el estado de la asignación");
            }
            
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID de asignación inválido");
        }
        
        listarAsignaciones(req, resp);
    }
}