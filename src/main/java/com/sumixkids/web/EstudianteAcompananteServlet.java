package com.sumixkids.web;

import com.sumixkids.dao.EstudianteAcompananteDAO;
import com.sumixkids.model.EstudianteAcompanante;
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
import java.util.List;
import java.util.Map;

/**
 * Servlet para gestionar vínculos Estudiante-Acompañante.
 * Acciones: listar, agregar, guardar, editar, actualizar, eliminar, activar, delete.
 */
@WebServlet("/vinculos")
public class EstudianteAcompananteServlet extends HttpServlet {

    private final EstudianteAcompananteDAO dao = new EstudianteAcompananteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdminODocente(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "listar";

        try {
            switch (action) {
                case "listar":
                    listar(req, resp);
                    break;
                case "agregar":
                    mostrarFormularioAgregar(req, resp);
                    break;
                case "editar":
                    mostrarFormularioEditar(req, resp);
                    break;
                case "eliminar":
                    eliminar(req, resp);
                    break;
                case "activar":
                    activar(req, resp);
                    break;
                case "delete":
                    delete(req, resp);
                    break;
                default:
                    listar(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Error al acceder a la base de datos: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdminODocente(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        
        try {
            if ("guardar".equals(action)) {
                guardar(req, resp);
            } else if ("actualizar".equals(action)) {
                actualizar(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/vinculos");
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        System.out.println("DEBUG SERVLET - Iniciando método listar()");
        
        try {
            List<EstudianteAcompanante> vinculos = dao.listar();
            System.out.println("DEBUG SERVLET - Número de vínculos encontrados: " + vinculos.size());
            
            for (EstudianteAcompanante v : vinculos) {
                System.out.println("DEBUG SERVLET - Vínculo: " + v.getId() + 
                                 " - Estudiante: " + v.getNombreEstudiante() + 
                                 " - Acompañante: " + v.getNombreAcompanante() +
                                 " - Tipo: " + v.getTipoRelacion() +
                                 " - Activo: " + v.getActivo());
            }
            
            req.setAttribute("vinculos", vinculos);
            System.out.println("DEBUG SERVLET - Attribute 'vinculos' establecido con " + vinculos.size() + " elementos");
            
            req.getRequestDispatcher("/asig_estu_acom.jsp").forward(req, resp);
            System.out.println("DEBUG SERVLET - Forward a JSP completado");
            
        } catch (SQLException e) {
            System.out.println("DEBUG SERVLET - Error SQL: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void mostrarFormularioAgregar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        cargarDatosFormulario(req);
        req.getRequestDispatcher("/asig_estu_acom_add.jsp").forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/vinculos");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            EstudianteAcompanante vinculo = dao.obtenerPorId(id);
            
            if (vinculo == null) {
                req.setAttribute("error", "Vínculo no encontrado");
                listar(req, resp);
                return;
            }
            
            cargarDatosFormulario(req);
            req.setAttribute("vinculo", vinculo);
            req.getRequestDispatcher("/asig_estu_acom_edit.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/vinculos");
        }
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String estudianteIdStr = req.getParameter("estudianteId");
        String acompananteIdStr = req.getParameter("acompananteId");
        String tipoRelacion = req.getParameter("tipoRelacion");
        
        // Debug temporal
        System.out.println("DEBUG - estudianteIdStr: '" + estudianteIdStr + "'");
        System.out.println("DEBUG - acompananteIdStr: '" + acompananteIdStr + "'");
        System.out.println("DEBUG - tipoRelacion: '" + tipoRelacion + "'");
        
        if (estudianteIdStr == null || acompananteIdStr == null || estudianteIdStr.trim().isEmpty() || acompananteIdStr.trim().isEmpty()) {
            req.setAttribute("error", "Debe seleccionar estudiante y acompañante (estudianteId='" + estudianteIdStr + "', acompananteId='" + acompananteIdStr + "')");
            mostrarFormularioAgregar(req, resp);
            return;
        }
        
        try {
            int estudianteId = Integer.parseInt(estudianteIdStr);
            int acompananteId = Integer.parseInt(acompananteIdStr);
            
            // Validar que no exista vínculo activo
            if (dao.existeVinculoActivo(estudianteId, acompananteId)) {
                req.setAttribute("error", "Ya existe un vínculo activo entre este estudiante y acompañante");
                cargarDatosFormulario(req);
                req.getRequestDispatcher("/asig_estu_acom_add.jsp").forward(req, resp);
                return;
            }
            
            EstudianteAcompanante vinculo = new EstudianteAcompanante();
            vinculo.setEstudianteId(estudianteId);
            vinculo.setAcompananteId(acompananteId);
            vinculo.setTipoRelacion(tipoRelacion != null && !tipoRelacion.trim().isEmpty() ? tipoRelacion : "PRINCIPAL");
            vinculo.setActivo(true);
            vinculo.setFechaVinculacion(LocalDateTime.now());
            
            // El modelo correcto no tiene campo vinculadoPor
            
            int id = dao.insertar(vinculo);
            
            if (id > 0) {
                resp.sendRedirect(req.getContextPath() + "/vinculos?success=" + 
                    java.net.URLEncoder.encode("Vínculo creado exitosamente", "UTF-8"));
            } else {
                req.setAttribute("error", "No se pudo crear el vínculo");
                mostrarFormularioAgregar(req, resp);
            }
            
        } catch (NumberFormatException e) {
            req.setAttribute("error", "IDs inválidos");
            mostrarFormularioAgregar(req, resp);
        }
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String idStr = req.getParameter("id");
        String tipoRelacion = req.getParameter("tipoRelacion");
        String activoStr = req.getParameter("activo");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/vinculos");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            EstudianteAcompanante vinculo = dao.obtenerPorId(id);
            
            if (vinculo == null) {
                req.setAttribute("error", "Vínculo no encontrado");
                resp.sendRedirect(req.getContextPath() + "/vinculos");
                return;
            }
            
            vinculo.setTipoRelacion(tipoRelacion);
            vinculo.setActivo("on".equals(activoStr) || "true".equals(activoStr));
            
            boolean actualizado = dao.actualizar(vinculo);
            
            if (actualizado) {
                resp.sendRedirect(req.getContextPath() + "/vinculos?success=" + 
                    java.net.URLEncoder.encode("Vínculo actualizado exitosamente", "UTF-8"));
            } else {
                req.setAttribute("error", "No se pudo actualizar el vínculo");
                mostrarFormularioEditar(req, resp);
            }
            
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/vinculos");
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/vinculos");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean ok = dao.desactivar(id);
            
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/vinculos?success=" + 
                    java.net.URLEncoder.encode("Vínculo desactivado exitosamente", "UTF-8"));
            } else {
                resp.sendRedirect(req.getContextPath() + "/vinculos?error=" + 
                    java.net.URLEncoder.encode("No se pudo desactivar el vínculo", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/vinculos?error=" + 
                java.net.URLEncoder.encode("ID inválido", "UTF-8"));
        }
    }

    private void activar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/vinculos");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean ok = dao.activar(id);
            
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/vinculos?success=" + 
                    java.net.URLEncoder.encode("Vínculo activado exitosamente", "UTF-8"));
            } else {
                resp.sendRedirect(req.getContextPath() + "/vinculos?error=" + 
                    java.net.URLEncoder.encode("No se pudo activar el vínculo", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/vinculos?error=" + 
                java.net.URLEncoder.encode("ID inválido", "UTF-8"));
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        if (!esAdministrador(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/vinculos");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean ok = dao.eliminar(id);
            
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/vinculos?success=" + 
                    java.net.URLEncoder.encode("Vínculo eliminado permanentemente", "UTF-8"));
            } else {
                resp.sendRedirect(req.getContextPath() + "/vinculos?error=" + 
                    java.net.URLEncoder.encode("No se pudo eliminar el vínculo", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/vinculos?error=" + 
                java.net.URLEncoder.encode("ID inválido", "UTF-8"));
        }
    }

    private void cargarDatosFormulario(HttpServletRequest req) throws SQLException {
        List<Map<String, Object>> estudiantes = dao.obtenerEstudiantes();
        List<Map<String, Object>> acompanantes = dao.obtenerAcompanantes();
        
        // Debug temporal
        System.out.println("DEBUG - Estudiantes encontrados: " + estudiantes.size());
        System.out.println("DEBUG - Acompañantes encontrados: " + acompanantes.size());
        if (!estudiantes.isEmpty()) {
            System.out.println("DEBUG - Primer estudiante: " + estudiantes.get(0));
        }
        if (!acompanantes.isEmpty()) {
            System.out.println("DEBUG - Primer acompañante: " + acompanantes.get(0));
        }
        
        req.setAttribute("estudiantes", estudiantes);
        req.setAttribute("acompanantes", acompanantes);
    }

    // Permisos
    private boolean esAdministrador(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && u.getRolId() == 1;
    }

    private boolean esAdminODocente(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && (u.getRolId() == 1 || u.getRolId() == 2);
    }
}
