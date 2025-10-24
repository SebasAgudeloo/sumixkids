/**
 * Paquete de utilidades del sistema SumixKids
 * Contiene clases helper y utilidades para funcionalidades
 * transversales de la aplicación
 */
package com.sumixkids.util;

// Importación para modelo de usuario
import com.sumixkids.model.Usuario;
// Importación para objetos de petición HTTP
import javax.servlet.http.HttpServletRequest;
// Importación para gestión de sesiones HTTP
import javax.servlet.http.HttpSession;

/**
 * SecurityUtils - Utilidades para manejo de seguridad y permisos
 * 
 * Funcionalidades:
 * - Verificación de roles y permisos de usuario
 * - Constantes de roles del sistema
 * - Validación de autenticación de sesiones
 * - Métodos helper para control de acceso
 * - Verificación de privilegios administrativos
 * 
 * Roles del sistema:
 * - ROL_ADMIN (1): Administrador con todos los permisos
 * - ROL_DOCENTE (2): Docente con permisos de gestión académica
 * - ROL_ESTUDIANTE (3): Estudiante con acceso limitado
 * - ROL_PADRE (4): Padre/tutor con acceso a información del hijo
 * 
 * Uso típico:
 * - Validar permisos antes de ejecutar acciones
 * - Controlar acceso a funcionalidades específicas
 * - Verificar autenticación en filtros de seguridad
 * 
 * @author SumixKids Team
 * @version 1.0
 * @since 2024
 */
public class SecurityUtils {
    
    // Constante para identificar rol de administrador del sistema
    public static final int ROL_ADMIN = 1;
    
    // Constante para identificar rol de docente
    public static final int ROL_DOCENTE = 2;
    
    // Constante para identificar rol de estudiante
    public static final int ROL_ESTUDIANTE = 3;
    
    // Constante para identificar rol de padre/tutor
    public static final int ROL_PADRE = 4;
    
    /**
     * Verifica si el usuario actual es administrador
     */
    public static boolean esAdministrador(Usuario usuario) {
        return usuario != null && usuario.getRolId() != null && usuario.getRolId() == ROL_ADMIN;
    }
    
    /**
     * Verifica si el usuario actual es docente
     */
    public static boolean esDocente(Usuario usuario) {
        return usuario != null && usuario.getRolId() != null && usuario.getRolId() == ROL_DOCENTE;
    }
    
    /**
     * Verifica si el usuario actual es estudiante
     */
    public static boolean esEstudiante(Usuario usuario) {
        return usuario != null && usuario.getRolId() != null && usuario.getRolId() == ROL_ESTUDIANTE;
    }
    
    /**
     * Verifica si el usuario actual es padre
     */
    public static boolean esPadre(Usuario usuario) {
        return usuario != null && usuario.getRolId() != null && usuario.getRolId() == ROL_PADRE;
    }
    
    /**
     * Obtiene el usuario de la sesión actual
     */
    public static Usuario getUsuarioSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Usuario) session.getAttribute("usuario");
        }
        return null;
    }
    
    /**
     * Verifica si el usuario puede eliminar a otro usuario
     */
    public static boolean puedeEliminarOtroUsuario(Usuario usuario) {
        return esAdministrador(usuario);
    }
    
    /**
     * Verifica si el usuario puede eliminar su propia cuenta
     */
    public static boolean puedeEliminarPropiaCuenta(Usuario usuario) {
        // Los admins no pueden eliminar su propia cuenta por seguridad
        return usuario != null && !esAdministrador(usuario);
    }
    
    /**
     * Obtiene el nombre del rol basado en el ID
     */
    public static String getNombreRol(Integer rolId) {
        if (rolId == null) return "Sin rol";
        
        switch (rolId) {
            case ROL_ADMIN: return "Administrador";
            case ROL_DOCENTE: return "Docente";
            case ROL_ESTUDIANTE: return "Estudiante";
            case ROL_PADRE: return "Padre";
            default: return "Rol desconocido";
        }
    }
    
    /**
     * Valida permisos para eliminación de usuario
     * @param usuarioSesion Usuario que realiza la acción
     * @param targetUserId ID del usuario a eliminar
     * @return true si tiene permisos, false si no
     */
    public static boolean validarPermisosEliminacion(Usuario usuarioSesion, Integer targetUserId) {
        if (usuarioSesion == null || targetUserId == null) {
            return false;
        }
        
        // Si está eliminando su propia cuenta
        if (usuarioSesion.getId().equals(targetUserId)) {
            return puedeEliminarPropiaCuenta(usuarioSesion);
        }
        
        // Si está eliminando a otro usuario
        return puedeEliminarOtroUsuario(usuarioSesion);
    }
    
    /**
     * Logs de seguridad con información del usuario
     */
    public static void logAccesoSeguridad(String accion, Usuario usuario, String detalles) {
        String mensaje = String.format("[SEGURIDAD] Acción: %s | Usuario: %s (ID: %d, Rol: %s) | Detalles: %s",
            accion,
            usuario != null ? usuario.getUsername() : "null",
            usuario != null ? usuario.getId() : 0,
            getNombreRol(usuario != null ? usuario.getRolId() : null),
            detalles != null ? detalles : "N/A"
        );
        System.out.println(mensaje);
    }
}