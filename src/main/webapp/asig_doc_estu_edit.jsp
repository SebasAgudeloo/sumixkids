<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Editar Asignación · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <style>
        /* Estilos adicionales para botones más visibles */
        .btn-lg {
            padding: 12px 24px;
            font-size: 1.1rem;
            font-weight: 600;
            min-width: 180px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }
        
        .btn-lg:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        }
        
        .btn-primary.btn-lg {
            background: linear-gradient(135deg, #007bff, #0056b3);
            border: none;
            box-shadow: 0 2px 8px rgba(0,123,255,0.3);
        }
        
        .btn-primary.btn-lg:hover {
            background: linear-gradient(135deg, #0056b3, #004085);
            box-shadow: 0 6px 16px rgba(0,123,255,0.4);
        }
        
        @media (max-width: 576px) {
            .btn-lg {
                width: 100%;
                margin-bottom: 10px;
            }
        }
    </style>
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">

<!-- Navbar principal -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/bienvenida">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" 
                 style="height: 40px; width: auto; margin-right: 12px; border-radius: 8px;"/>
            SumixKids
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/bienvenida">
                        🏠 Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">
                        👥 Usuarios
                    </a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link active dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        📚 Académico
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/grados"><i class="fas fa-graduation-cap me-2"></i>Grados Escolares</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/niveles"><i class="fas fa-signal me-2"></i>Niveles de Dificultad</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/asignaciones"><i class="fas fa-users me-2"></i>Asignaciones</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/vinculos"><i class="fas fa-user-friends me-2"></i>Vínculos</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/auditoria">
                        📊 Auditoría
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/carga_masiva">
                        📁 Carga Masiva
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center rounded-pill px-3" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-crown me-2"></i>
                        <span class="fw-semibold">${sessionScope.usuario.nombreCorto}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0">
                        <li>
                            <h6 class="dropdown-item">👑 Administrador</h6>
                        </li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil"><i class="fas fa-user me-2"></i>Mi Perfil</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/configuracion"><i class="fas fa-cog me-2"></i>Configuración</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/dispositivos_reconocidos"><i class="fa-solid fa-shield-halved me-2"></i>Dispositivos Reconocidos</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-2 text-danger"></i>Cerrar Sesión</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Contenido principal -->
<main class="flex-fill py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-md-9 col-lg-8 col-xl-7">
                <div class="card shadow-lg border-0 rounded-4">
                    <div class="card-body p-4 p-md-5">
                        <h1 class="h3 mb-4 text-center fw-bold text-primary">
                            <i class="fas fa-edit me-2"></i>Editar Asignación
                        </h1>
                        
                        <!-- Información de la asignación -->
                        <c:if test="${not empty asignacion}">
                            <div class="alert alert-info mb-4">
                                <h6 class="alert-heading">
                                    <i class="fas fa-info-circle me-2"></i>Información actual
                                </h6>
                                <p class="mb-1"><strong>ID:</strong> #${asignacion.id}</p>
                                <p class="mb-1"><strong>Creada:</strong> 
                                    <c:if test="${not empty asignacion.fechaCreacion}">
                                        ${util:formatearFecha(asignacion.fechaCreacion)}
                                    </c:if>
                                </p>
                                <c:if test="${not empty asignacion.fechaActualizacion}">
                                    <p class="mb-0"><strong>Última actualización:</strong> 
                                        ${util:formatearFecha(asignacion.fechaActualizacion)}
                                    </p>
                                </c:if>
                            </div>
                        </c:if>
                        
                        <!-- Mensajes de error -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <!-- Formulario -->
                        <form id="asignacionForm" method="post" action="${pageContext.request.contextPath}/asignaciones">
                            <input type="hidden" name="action" value="actualizar">
                            <input type="hidden" name="id" value="${asignacion.id}">
                            
                            <!-- Selección de docente -->
                            <div class="mb-3">
                                <label for="idDocente" class="form-label fw-semibold">
                                    <i class="fas fa-chalkboard-teacher me-1"></i>Docente <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="idDocente" name="idDocente" required>
                                    <option value="">Seleccionar docente...</option>
                                    <c:forEach var="docente" items="${docentes}">
                                        <option value="${docente.id}" ${asignacion.idDocente == docente.id ? 'selected' : ''}>
                                            ${docente.nombre}
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Docente responsable del estudiante
                                </small>
                            </div>
                            
                            <!-- Selección de estudiante -->
                            <div class="mb-3">
                                <label for="idEstudiante" class="form-label fw-semibold">
                                    <i class="fas fa-graduation-cap me-1"></i>Estudiante <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="idEstudiante" name="idEstudiante" required>
                                    <option value="">Seleccionar estudiante...</option>
                                    <c:forEach var="estudiante" items="${estudiantes}">
                                        <option value="${estudiante.id}" ${asignacion.idEstudiante == estudiante.id ? 'selected' : ''}
                                                data-grado="${estudiante.grado}">
                                            ${estudiante.nombre}
                                            <c:if test="${not empty estudiante.grado}"> (${estudiante.grado}°)</c:if>
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Estudiante asignado al docente
                                </small>
                            </div>
                            
                            <!-- Selección de grado escolar -->
                            <div class="mb-3">
                                <label for="idGradoEscolar" class="form-label fw-semibold">
                                    <i class="fas fa-school me-1"></i>Grado Escolar
                                </label>
                                <select class="form-select" id="idGradoEscolar" name="idGradoEscolar">
                                    <option value="">Sin grado específico</option>
                                    <c:forEach var="grado" items="${grados}">
                                        <option value="${grado.id}" ${asignacion.idGradoEscolar == grado.id ? 'selected' : ''}>
                                            ${grado.nombre}
                                            <c:if test="${not empty grado.descripcion}"> - ${grado.descripcion}</c:if>
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Grado escolar específico para esta asignación
                                </small>
                            </div>
                            
                            <!-- Fecha de asignación -->
                            <div class="mb-3">
                                <label for="fechaAsignacion" class="form-label fw-semibold">
                                    <i class="fas fa-calendar-alt me-1"></i>Fecha de Asignación <span class="text-danger">*</span>
                                </label>
                                <input type="datetime-local" class="form-control" id="fechaAsignacion" name="fechaAsignacion" 
                                       value="${util:formatearFechaHTML(asignacion.fechaAsignacion)}" required>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Solo se permite la fecha actual o fechas pasadas
                                </small>
                            </div>
                            
                            <!-- Información de fecha de finalización (solo lectura) -->
                            <c:if test="${not empty asignacion.fechaFinalizacion}">
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fas fa-calendar-times me-1"></i>Fecha de Finalización
                                    </label>
                                    <div class="form-control-plaintext bg-light rounded p-2">
                                        <i class="fas fa-calendar me-1"></i>${util:formatearFecha(asignacion.fechaFinalizacion)}
                                    </div>
                                    <small class="form-text text-muted">
                                        <i class="fas fa-info-circle me-1"></i>Fecha automática cuando se finaliza la asignación
                                    </small>
                                </div>
                            </c:if>
                            
                            <!-- Estado -->
                            <div class="mb-3">
                                <label for="estado" class="form-label fw-semibold">
                                    <i class="fas fa-info-circle me-1"></i>Estado <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="estado" name="estado" required>
                                    <option value="ACTIVA" ${asignacion.estado == 'ACTIVA' ? 'selected' : ''}>
                                        ✅ Activa
                                    </option>
                                    <option value="SUSPENDIDA" ${asignacion.estado == 'SUSPENDIDA' ? 'selected' : ''}>
                                        ⏸️ Suspendida
                                    </option>
                                    <option value="FINALIZADA" ${asignacion.estado == 'FINALIZADA' ? 'selected' : ''}>
                                        ❌ Finalizada
                                    </option>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Estado actual de la asignación
                                </small>
                            </div>
                            
                            <!-- Observaciones -->
                            <div class="mb-4">
                                <label for="observaciones" class="form-label fw-semibold">
                                    <i class="fas fa-sticky-note me-1"></i>Observaciones
                                </label>
                                <textarea class="form-control" id="observaciones" name="observaciones" rows="4" 
                                          placeholder="Ingrese observaciones adicionales sobre esta asignación...">${asignacion.observaciones}</textarea>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Notas o comentarios adicionales sobre la asignación
                                </small>
                            </div>
                            
                            <!-- Información adicional -->
                            <c:if test="${not empty asignacion.nombreAsignadoPor}">
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fas fa-user-check me-1"></i>Asignado por
                                    </label>
                                    <div class="form-control-plaintext bg-light rounded p-2">
                                        <i class="fas fa-user me-1"></i>${asignacion.nombreAsignadoPor}
                                    </div>
                                    <small class="form-text text-muted">
                                        <i class="fas fa-info-circle me-1"></i>Usuario que creó esta asignación
                                    </small>
                                </div>
                            </c:if>
                            
                            <!-- Separador visual -->
                            <hr class="my-4">
                            
                            <!-- Botones de acción -->
                            <div class="row">
                                <div class="col-12">
                                    <div class="d-flex flex-column flex-sm-row gap-3 justify-content-between align-items-center">
                                        <!-- Botón Cancelar/Volver -->
                                        <a href="${pageContext.request.contextPath}/asignaciones" 
                                           class="btn btn-outline-secondary btn-lg d-flex align-items-center">
                                            <i class="fas fa-times me-2"></i>Cancelar
                                        </a>
                                        
                                        <!-- Botones principales -->
                                        <div class="d-flex gap-3">
                                            <button type="button" 
                                                    class="btn btn-outline-danger btn-lg d-flex align-items-center" 
                                                    onclick="confirmarEliminacion()">
                                                <i class="fas fa-ban me-2"></i>Finalizar Asignación
                                            </button>
                                            <button type="submit" 
                                                    class="btn btn-primary btn-lg d-flex align-items-center px-4">
                                                <i class="fas fa-save me-2"></i>Guardar Cambios
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Footer -->
<footer class="py-4 mt-auto" style="background: rgba(0,0,0,0.8); backdrop-filter: blur(10px);">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-6 text-center text-md-start">
                <div class="d-flex align-items-center justify-content-center justify-content-md-start mb-2 mb-md-0">
                    <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="SumixKids" 
                         style="height: 24px; margin-right: 8px;">
                    <span class="text-white fw-semibold">SumixKids</span>
                </div>
                <p class="text-white-50 small mb-0">Software educativo para la práctica de sumas</p>
            </div>
            <div class="col-md-6 text-center text-md-end">
                <p class="text-white-50 small mb-1">
                    © <span id="year"></span> SumixKids · Todos los derechos reservados
                </p>
            </div>
        </div>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>
<script>
document.getElementById('year').textContent = new Date().getFullYear();

// Establecer restricciones de fecha
document.addEventListener('DOMContentLoaded', function() {
    const fechaAsignacion = document.getElementById('fechaAsignacion');
    
    // Obtener fecha y hora actual
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    const nowString = now.toISOString().slice(0, 16);
    
    // Restringir fechas: solo permitir fecha actual o anteriores
    fechaAsignacion.max = nowString;
    
    // Establecer fecha mínima (1 año hacia atrás)
    const oneYearAgo = new Date();
    oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);
    oneYearAgo.setMinutes(oneYearAgo.getMinutes() - oneYearAgo.getTimezoneOffset());
    const minDate = oneYearAgo.toISOString().slice(0, 16);
    
    fechaAsignacion.min = minDate;
});

// Validación del formulario
document.getElementById('asignacionForm').addEventListener('submit', function(e) {
    const docente = document.getElementById('idDocente').value;
    const estudiante = document.getElementById('idEstudiante').value;
    const fechaAsignacion = document.getElementById('fechaAsignacion').value;
    const estado = document.getElementById('estado').value;
    
    if (!docente) {
        e.preventDefault();
        alert('Por favor selecciona un docente');
        document.getElementById('idDocente').focus();
        return;
    }
    
    if (!estudiante) {
        e.preventDefault();
        alert('Por favor selecciona un estudiante');
        document.getElementById('idEstudiante').focus();
        return;
    }
    
    if (!fechaAsignacion) {
        e.preventDefault();
        alert('Por favor ingresa la fecha de asignación');
        document.getElementById('fechaAsignacion').focus();
        return;
    }
    
    if (!estado) {
        e.preventDefault();
        alert('Por favor selecciona un estado');
        document.getElementById('estado').focus();
        return;
    }
    
    // Validar que no se seleccionen fechas futuras
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    const nowString = now.toISOString().slice(0, 16);
    
    if (fechaAsignacion > nowString) {
        e.preventDefault();
        alert('No se pueden seleccionar fechas futuras para la asignación');
        document.getElementById('fechaAsignacion').focus();
        return;
    }
});

// Función para confirmar finalización
function confirmarEliminacion() {
    Swal.fire({
        title: '¿Finalizar asignación?',
        text: 'Esta acción cambiará el estado a "FINALIZADA" y establecerá la fecha de finalización.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, finalizar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '${pageContext.request.contextPath}/asignaciones?action=eliminar&id=${asignacion.id}';
        }
    });
}

// Auto-ocultar alertas después de 5 segundos
setTimeout(() => {
    document.querySelectorAll('.alert').forEach(alert => {
        const bsAlert = new bootstrap.Alert(alert);
        bsAlert.close();
    });
}, 5000);
</script>
</body>
</html>