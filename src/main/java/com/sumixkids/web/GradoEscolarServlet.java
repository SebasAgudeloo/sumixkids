package com.sumixkids.web;

import com.sumixkids.dao.GradoEscolarDAO;
import com.sumixkids.model.GradoEscolar;
import com.sumixkids.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet para gestionar grados escolares.
 * Solo accesible para administradores.
 * 
 * Operaciones:
 * - listar: Muestra todos los grados escolares
 * - crear: Formulario y creación de nuevo grado
 * - editar: Formulario y actualización de grado existente
 * - eliminar: Desactivar un grado escolar
 * - activar: Activar un grado escolar
 */
@WebServlet("/grados")
public class GradoEscolarServlet extends HttpServlet {
    
    private final GradoEscolarDAO gradoDAO = new GradoEscolarDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // Verificar que el usuario sea administrador
        if (!esAdministrador(req)) {
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
                    listarGrados(req, resp);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(req, resp);
                    break;
                case "editar":
                    mostrarFormularioEditar(req, resp);
                    break;
                case "eliminar":
                    eliminarGrado(req, resp);
                    break;
                case "activar":
                    activarGrado(req, resp);
                    break;
                case "delete":
                    eliminarPermanentemente(req, resp);
                    break;
                default:
                    listarGrados(req, resp);
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
        
        // Verificar que el usuario sea administrador
        if (!esAdministrador(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String action = req.getParameter("action");
        
        try {
            if ("crear".equals(action)) {
                crearGrado(req, resp);
            } else if ("actualizar".equals(action)) {
                actualizarGrado(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/grados");
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
    
    /**
     * Listar todos los grados escolares
     */
    private void listarGrados(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        String filtro = req.getParameter("filtro");
        String nivel = req.getParameter("nivel");
        List<GradoEscolar> grados;
        
        if ("activos".equals(filtro)) {
            grados = gradoDAO.findActivos();
        } else {
            grados = gradoDAO.findAll();
        }
        
        // Filtrar por nivel educativo si se especifica
        if (nivel != null && !nivel.trim().isEmpty()) {
            grados = grados.stream()
                    .filter(g -> nivel.equals(g.getNivelEducativo() != null ? g.getNivelEducativo().name() : null))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        req.setAttribute("grados", grados);
        req.setAttribute("filtroActual", filtro);
        req.setAttribute("nivelActual", nivel);
        req.getRequestDispatcher("/gestion_grados.jsp").forward(req, resp);
    }
    
    /**
     * Mostrar formulario para crear nuevo grado
     */
    private void mostrarFormularioNuevo(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setAttribute("accion", "crear");
        req.getRequestDispatcher("/grado_form.jsp").forward(req, resp);
    }
    
    /**
     * Mostrar formulario para editar grado existente
     */
    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/grados");
            return;
        }
        
        try {
            int idGrado = Integer.parseInt(idStr);
            GradoEscolar grado = gradoDAO.findById(idGrado);
            
            if (grado == null) {
                req.setAttribute("error", "Grado escolar no encontrado");
                listarGrados(req, resp);
                return;
            }
            
            req.setAttribute("grado", grado);
            req.setAttribute("accion", "actualizar");
            req.getRequestDispatcher("/grado_form.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/grados");
        }
    }
    
    /**
     * Crear un nuevo grado escolar
     */
    private void crearGrado(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException, ServletException {
        
        String nombreGrado = req.getParameter("nombreGrado");
        String nombreGrupo = req.getParameter("nombreGrupo");
        String nivelEducativoStr = req.getParameter("nivelEducativo");
        String descripcion = req.getParameter("descripcion");
        String capacidadStr = req.getParameter("capacidadMaxima");
        
        // Validaciones
        if (nombreGrado == null || nombreGrado.trim().isEmpty()) {
            req.setAttribute("error", "El nombre del grado es obligatorio");
            mostrarFormularioNuevo(req, resp);
            return;
        }
        
        if (nombreGrupo == null || nombreGrupo.trim().isEmpty()) {
            req.setAttribute("error", "El nombre del grupo es obligatorio");
            mostrarFormularioNuevo(req, resp);
            return;
        }
        
        // Verificar si ya existe el grado y grupo
        GradoEscolar existente = gradoDAO.findByGradoYGrupo(nombreGrado.trim(), nombreGrupo.trim());
        if (existente != null) {
            req.setAttribute("error", "Ya existe un grado con ese nombre y grupo");
            mostrarFormularioNuevo(req, resp);
            return;
        }
        
        // Crear el objeto GradoEscolar
        GradoEscolar grado = new GradoEscolar();
        grado.setNombreGrado(nombreGrado.trim());
        grado.setNombreGrupo(nombreGrupo.trim());
        
        if (nivelEducativoStr != null && !nivelEducativoStr.trim().isEmpty()) {
            try {
                GradoEscolar.NivelEducativo nivel = GradoEscolar.NivelEducativo.valueOf(nivelEducativoStr);
                grado.setNivelEducativo(nivel);
            } catch (IllegalArgumentException e) {
                grado.setNivelEducativo(GradoEscolar.NivelEducativo.PRIMARIA);
            }
        }
        
        grado.setDescripcion(descripcion != null ? descripcion.trim() : "");
        
        if (capacidadStr != null && !capacidadStr.trim().isEmpty()) {
            try {
                int capacidad = Integer.parseInt(capacidadStr);
                grado.setCapacidadMaxima(capacidad);
            } catch (NumberFormatException e) {
                grado.setCapacidadMaxima(30);
            }
        }
        
        grado.setActivo(true);
        
        // Guardar en la base de datos
        int idGenerado = gradoDAO.crear(grado);
        
        if (idGenerado > 0) {
            req.getSession().setAttribute("mensaje", "Grado escolar creado exitosamente");
            resp.sendRedirect(req.getContextPath() + "/grados");
        } else {
            req.setAttribute("error", "Error al crear el grado escolar");
            mostrarFormularioNuevo(req, resp);
        }
    }
    
    /**
     * Actualizar un grado escolar existente
     */
    private void actualizarGrado(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException, ServletException {
        
        String idStr = req.getParameter("idGrado");
        String nombreGrado = req.getParameter("nombreGrado");
        String nombreGrupo = req.getParameter("nombreGrupo");
        String nivelEducativoStr = req.getParameter("nivelEducativo");
        String descripcion = req.getParameter("descripcion");
        String capacidadStr = req.getParameter("capacidadMaxima");
        String activoStr = req.getParameter("activo");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/grados");
            return;
        }
        
        try {
            int idGrado = Integer.parseInt(idStr);
            GradoEscolar grado = gradoDAO.findById(idGrado);
            
            if (grado == null) {
                req.setAttribute("error", "Grado escolar no encontrado");
                resp.sendRedirect(req.getContextPath() + "/grados");
                return;
            }
            
            // Verificar si el nuevo nombre y grupo ya existen (excluyendo el actual)
            if (gradoDAO.existeGradoYGrupo(nombreGrado.trim(), nombreGrupo.trim(), idGrado)) {
                req.setAttribute("error", "Ya existe otro grado con ese nombre y grupo");
                req.setAttribute("grado", grado);
                req.setAttribute("accion", "actualizar");
                req.getRequestDispatcher("/grado_form.jsp").forward(req, resp);
                return;
            }
            
            // Actualizar campos
            grado.setNombreGrado(nombreGrado.trim());
            grado.setNombreGrupo(nombreGrupo.trim());
            
            if (nivelEducativoStr != null && !nivelEducativoStr.trim().isEmpty()) {
                try {
                    GradoEscolar.NivelEducativo nivel = GradoEscolar.NivelEducativo.valueOf(nivelEducativoStr);
                    grado.setNivelEducativo(nivel);
                } catch (IllegalArgumentException e) {
                    // Mantener el nivel actual
                }
            }
            
            grado.setDescripcion(descripcion != null ? descripcion.trim() : "");
            
            if (capacidadStr != null && !capacidadStr.trim().isEmpty()) {
                try {
                    int capacidad = Integer.parseInt(capacidadStr);
                    grado.setCapacidadMaxima(capacidad);
                } catch (NumberFormatException e) {
                    // Mantener capacidad actual
                }
            }
            
            grado.setActivo("true".equals(activoStr) || "on".equals(activoStr));
            
            // Actualizar en la base de datos
            boolean actualizado = gradoDAO.actualizar(grado);
            
            if (actualizado) {
                req.getSession().setAttribute("mensaje", "Grado escolar actualizado exitosamente");
                resp.sendRedirect(req.getContextPath() + "/grados");
            } else {
                req.setAttribute("error", "Error al actualizar el grado escolar");
                req.setAttribute("grado", grado);
                req.setAttribute("accion", "actualizar");
                req.getRequestDispatcher("/grado_form.jsp").forward(req, resp);
            }
            
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/grados");
        }
    }
    
    /**
     * Desactivar un grado escolar
     */
    private void eliminarGrado(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/grados");
            return;
        }
        
        try {
            int idGrado = Integer.parseInt(idStr);
            
            // Verificar si tiene estudiantes asignados
            int numEstudiantes = gradoDAO.contarEstudiantes(idGrado);
            if (numEstudiantes > 0) {
                req.getSession().setAttribute("error", 
                    "No se puede desactivar el grado porque tiene " + numEstudiantes + " estudiantes asignados");
            } else {
                boolean eliminado = gradoDAO.eliminar(idGrado);
                if (eliminado) {
                    req.getSession().setAttribute("mensaje", "Grado escolar desactivado exitosamente");
                } else {
                    req.getSession().setAttribute("error", "Error al desactivar el grado escolar");
                }
            }
            
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID de grado inválido");
        }
        
        resp.sendRedirect(req.getContextPath() + "/grados");
    }
    
    /**
     * Eliminar permanentemente un grado escolar
     */
    private void eliminarPermanentemente(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/grados");
            return;
        }
        
        try {
            int idGrado = Integer.parseInt(idStr);
            boolean eliminado = gradoDAO.delete(idGrado);
            
            if (eliminado) {
                req.getSession().setAttribute("mensaje", "Grado escolar eliminado permanentemente");
            } else {
                req.getSession().setAttribute("error", "Error al eliminar el grado escolar");
            }
            
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID de grado inválido");
        }
        
        resp.sendRedirect(req.getContextPath() + "/grados");
    }
    
    /**
     * Activar un grado escolar
     */
    private void activarGrado(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/grados");
            return;
        }
        
        try {
            int idGrado = Integer.parseInt(idStr);
            GradoEscolar grado = gradoDAO.findById(idGrado);
            
            if (grado != null) {
                grado.setActivo(true);
                boolean actualizado = gradoDAO.actualizar(grado);
                
                if (actualizado) {
                    req.getSession().setAttribute("mensaje", "Grado escolar activado exitosamente");
                } else {
                    req.getSession().setAttribute("error", "Error al activar el grado escolar");
                }
            } else {
                req.getSession().setAttribute("error", "Grado escolar no encontrado");
            }
            
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID de grado inválido");
        }
        
        resp.sendRedirect(req.getContextPath() + "/grados");
    }
    
    /**
     * Verificar si el usuario actual es administrador
     */
    private boolean esAdministrador(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return false;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && usuario.getRolId() == 1;
    }
}
