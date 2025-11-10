package com.sumixkids.web;

import com.sumixkids.dao.NivelDificultadDAO;
import com.sumixkids.model.NivelDificultad;
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
 * Servlet para gestionar niveles de dificultad.
 * Solo accesible para administradores y docentes.
 * 
 * Operaciones:
 * - listar: Muestra todos los niveles de dificultad
 * - crear: Formulario y creación de nuevo nivel
 * - editar: Formulario y actualización de nivel existente
 * - eliminar: Desactivar un nivel
 * - activar: Activar un nivel
 */
@WebServlet("/niveles")
public class NivelDificultadServlet extends HttpServlet {
    
    private final NivelDificultadDAO nivelDAO = new NivelDificultadDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // Verificar que el usuario sea administrador o docente
        if (!esAdminODocente(req)) {
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
                    listarNiveles(req, resp);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(req, resp);
                    break;
                case "editar":
                    mostrarFormularioEditar(req, resp);
                    break;
                case "eliminar":
                    eliminarNivel(req, resp);
                    break;
                case "activar":
                    activarNivel(req, resp);
                    break;
                case "delete":
                    eliminarPermanentemente(req, resp);
                    break;
                default:
                    listarNiveles(req, resp);
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
        
        // Verificar que el usuario sea administrador (solo admin puede crear/editar)
        if (!esAdministrador(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String action = req.getParameter("action");
        
        try {
            if ("crear".equals(action)) {
                crearNivel(req, resp);
            } else if ("actualizar".equals(action)) {
                actualizarNivel(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/niveles");
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
    
    /**
     * Listar todos los niveles de dificultad
     */
    private void listarNiveles(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        String estado = req.getParameter("estado");
        String busqueda = req.getParameter("busqueda");
        List<NivelDificultad> niveles;
        
        // Filtrar por estado
        if ("activo".equals(estado)) {
            niveles = nivelDAO.findActivos();
        } else if ("inactivo".equals(estado)) {
            niveles = nivelDAO.findAll().stream()
                    .filter(n -> !n.isActivo())
                    .collect(java.util.stream.Collectors.toList());
        } else {
            niveles = nivelDAO.findAll();
        }
        
        // Filtrar por búsqueda si se especifica
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            String busquedaLower = busqueda.toLowerCase().trim();
            niveles = niveles.stream()
                    .filter(n -> n.getNombreNivel() != null && 
                                n.getNombreNivel().toLowerCase().contains(busquedaLower))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        req.setAttribute("niveles", niveles);
        req.setAttribute("estadoActual", estado);
        req.setAttribute("busquedaActual", busqueda);
        req.getRequestDispatcher("/gestion_niveles.jsp").forward(req, resp);
    }
    
    /**
     * Mostrar formulario para crear nuevo nivel
     */
    private void mostrarFormularioNuevo(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setAttribute("accion", "crear");
        req.getRequestDispatcher("/nivel_form.jsp").forward(req, resp);
    }
    
    /**
     * Mostrar formulario para editar nivel existente
     */
    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, ServletException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/niveles");
            return;
        }
        
        try {
            int idNivel = Integer.parseInt(idStr);
            NivelDificultad nivel = nivelDAO.findById(idNivel);
            
            if (nivel == null) {
                req.setAttribute("error", "Nivel de dificultad no encontrado");
                listarNiveles(req, resp);
                return;
            }
            
            req.setAttribute("nivel", nivel);
            req.setAttribute("accion", "actualizar");
            req.getRequestDispatcher("/nivel_form.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/niveles");
        }
    }
    
    /**
     * Crear un nuevo nivel de dificultad
     */
    private void crearNivel(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException, ServletException {
        
        String nombreNivel = req.getParameter("nombreNivel");
        String ordenStr = req.getParameter("orden");
        String descripcionTexto = req.getParameter("descripcionTexto");
        String colorHex = req.getParameter("colorHex");
        String icono = req.getParameter("icono");
        
        // Validaciones
        if (nombreNivel == null || nombreNivel.trim().isEmpty()) {
            req.setAttribute("error", "El nombre del nivel es obligatorio");
            mostrarFormularioNuevo(req, resp);
            return;
        }
        
        if (ordenStr == null || ordenStr.trim().isEmpty()) {
            req.setAttribute("error", "El orden de dificultad es obligatorio");
            mostrarFormularioNuevo(req, resp);
            return;
        }
        
        int orden;
        try {
            orden = Integer.parseInt(ordenStr);
            if (orden < 1) {
                req.setAttribute("error", "El orden debe ser un número positivo");
                mostrarFormularioNuevo(req, resp);
                return;
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "El orden debe ser un número válido");
            mostrarFormularioNuevo(req, resp);
            return;
        }
        
        // Verificar si ya existe el nombre
        NivelDificultad existente = nivelDAO.findByNombre(nombreNivel.trim());
        if (existente != null) {
            req.setAttribute("error", "Ya existe un nivel con ese nombre");
            mostrarFormularioNuevo(req, resp);
            return;
        }
        
        // Verificar si ya existe el orden
        if (nivelDAO.existeOrden(orden, 0)) {
            req.setAttribute("error", "Ya existe un nivel con ese orden de dificultad");
            mostrarFormularioNuevo(req, resp);
            return;
        }
        
        // Crear el objeto NivelDificultad
        NivelDificultad nivel = new NivelDificultad();
        nivel.setNombreNivel(nombreNivel.trim());
        nivel.setOrden(orden);
        
        // Convertir descripción simple a JSON para almacenamiento
        if (descripcionTexto != null && !descripcionTexto.trim().isEmpty()) {
            int rangoInicio = Math.max(1, orden * 5 - 4);  // Básico: 1-5, Intermedio: 6-10, etc.
            int rangoFin = orden * 10;  // Básico: 10, Intermedio: 20, etc.
            String descripcionJSON = NivelDificultad.crearDescripcionJSON(descripcionTexto.trim(), rangoInicio, rangoFin);
            nivel.setDescripcion(descripcionJSON);
        } else {
            nivel.setDescripcion("");
        }
        
        nivel.setColorHex(colorHex != null && !colorHex.trim().isEmpty() ? colorHex.trim() : "#4CAF50");
        nivel.setIcono(icono != null && !icono.trim().isEmpty() ? icono.trim() : "⭐");
        nivel.setActivo(true);
        
        // Guardar en la base de datos
        int idGenerado = nivelDAO.crear(nivel);
        
        if (idGenerado > 0) {
            req.getSession().setAttribute("mensaje", "Nivel de dificultad creado exitosamente");
            resp.sendRedirect(req.getContextPath() + "/niveles");
        } else {
            req.setAttribute("error", "Error al crear el nivel de dificultad");
            mostrarFormularioNuevo(req, resp);
        }
    }
    
    /**
     * Actualizar un nivel de dificultad existente
     */
    private void actualizarNivel(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException, ServletException {
        
        String idStr = req.getParameter("idNivelDificultad");
        String nombreNivel = req.getParameter("nombreNivel");
        String ordenStr = req.getParameter("orden");
        String descripcionTexto = req.getParameter("descripcionTexto");
        String colorHex = req.getParameter("colorHex");
        String icono = req.getParameter("icono");
        String activoStr = req.getParameter("activo");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/niveles");
            return;
        }
        
        try {
            int idNivel = Integer.parseInt(idStr);
            NivelDificultad nivel = nivelDAO.findById(idNivel);
            
            if (nivel == null) {
                req.setAttribute("error", "Nivel de dificultad no encontrado");
                resp.sendRedirect(req.getContextPath() + "/niveles");
                return;
            }
            
            int orden = Integer.parseInt(ordenStr);
            
            // Verificar si el nuevo nombre ya existe (excluyendo el actual)
            if (nivelDAO.existeNombre(nombreNivel.trim(), idNivel)) {
                req.setAttribute("error", "Ya existe otro nivel con ese nombre");
                req.setAttribute("nivel", nivel);
                req.setAttribute("accion", "actualizar");
                req.getRequestDispatcher("/nivel_form.jsp").forward(req, resp);
                return;
            }
            
            // Verificar si el nuevo orden ya existe (excluyendo el actual)
            if (nivelDAO.existeOrden(orden, idNivel)) {
                req.setAttribute("error", "Ya existe otro nivel con ese orden de dificultad");
                req.setAttribute("nivel", nivel);
                req.setAttribute("accion", "actualizar");
                req.getRequestDispatcher("/nivel_form.jsp").forward(req, resp);
                return;
            }
            
            // Actualizar campos
            nivel.setNombreNivel(nombreNivel.trim());
            nivel.setOrden(orden);
            
            // Convertir descripción simple a JSON para almacenamiento
            if (descripcionTexto != null && !descripcionTexto.trim().isEmpty()) {
                int rangoInicio = Math.max(1, orden * 5 - 4);
                int rangoFin = orden * 10;
                String descripcionJSON = NivelDificultad.crearDescripcionJSON(descripcionTexto.trim(), rangoInicio, rangoFin);
                nivel.setDescripcion(descripcionJSON);
            } else {
                nivel.setDescripcion("");
            }
            
            nivel.setColorHex(colorHex != null && !colorHex.trim().isEmpty() ? colorHex.trim() : "#4CAF50");
            nivel.setIcono(icono != null && !icono.trim().isEmpty() ? icono.trim() : "⭐");
            nivel.setActivo("true".equals(activoStr) || "on".equals(activoStr));
            
            // Actualizar en la base de datos
            boolean actualizado = nivelDAO.actualizar(nivel);
            
            if (actualizado) {
                req.getSession().setAttribute("mensaje", "Nivel de dificultad actualizado exitosamente");
                resp.sendRedirect(req.getContextPath() + "/niveles");
            } else {
                req.setAttribute("error", "Error al actualizar el nivel de dificultad");
                req.setAttribute("nivel", nivel);
                req.setAttribute("accion", "actualizar");
                req.getRequestDispatcher("/nivel_form.jsp").forward(req, resp);
            }
            
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/niveles");
        }
    }
    
    /**
     * Desactivar un nivel de dificultad
     */
    private void eliminarNivel(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/niveles");
            return;
        }
        
        try {
            int idNivel = Integer.parseInt(idStr);
            
            // Verificar si tiene escenarios asignados
            int numEscenarios = nivelDAO.contarEscenarios(idNivel);
            if (numEscenarios > 0) {
                req.getSession().setAttribute("error", 
                    "No se puede desactivar el nivel porque tiene " + numEscenarios + " escenarios asignados");
            } else {
                boolean eliminado = nivelDAO.eliminar(idNivel);
                if (eliminado) {
                    req.getSession().setAttribute("mensaje", "Nivel de dificultad desactivado exitosamente");
                } else {
                    req.getSession().setAttribute("error", "Error al desactivar el nivel de dificultad");
                }
            }
            
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID de nivel inválido");
        }
        
        resp.sendRedirect(req.getContextPath() + "/niveles");
    }
    
    /**
     * Eliminar permanentemente un nivel de dificultad
     */
    private void eliminarPermanentemente(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/niveles");
            return;
        }
        
        try {
            int idNivel = Integer.parseInt(idStr);
            boolean eliminado = nivelDAO.delete(idNivel);
            
            if (eliminado) {
                req.getSession().setAttribute("mensaje", "Nivel de dificultad eliminado permanentemente");
            } else {
                req.getSession().setAttribute("error", "Error al eliminar el nivel de dificultad");
            }
            
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID de nivel inválido");
        }
        
        resp.sendRedirect(req.getContextPath() + "/niveles");
    }
    
    /**
     * Activar un nivel de dificultad
     */
    private void activarNivel(HttpServletRequest req, HttpServletResponse resp) 
            throws SQLException, IOException {
        
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/niveles");
            return;
        }
        
        try {
            int idNivel = Integer.parseInt(idStr);
            NivelDificultad nivel = nivelDAO.findById(idNivel);
            
            if (nivel != null) {
                nivel.setActivo(true);
                boolean actualizado = nivelDAO.actualizar(nivel);
                
                if (actualizado) {
                    req.getSession().setAttribute("mensaje", "Nivel de dificultad activado exitosamente");
                } else {
                    req.getSession().setAttribute("error", "Error al activar el nivel de dificultad");
                }
            } else {
                req.getSession().setAttribute("error", "Nivel de dificultad no encontrado");
            }
            
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID de nivel inválido");
        }
        
        resp.sendRedirect(req.getContextPath() + "/niveles");
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
    
    /**
     * Verificar si el usuario actual es administrador o docente
     */
    private boolean esAdminODocente(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return false;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && (usuario.getRolId() == 1 || usuario.getRolId() == 2);
    }
}
