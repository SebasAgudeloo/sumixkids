<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Nueva Asignación · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
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
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
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
                    <a class="nav-link active dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                        📚 Académico
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/grados"><i class="fas fa-graduation-cap me-2"></i>Grados Escolares</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/niveles"><i class="fas fa-signal me-2"></i>Niveles de Dificultad</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/asignaciones"><i class="fas fa-users me-2"></i>Asignaciones</a></li>
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
                    <a class="nav-link dropdown-toggle d-flex align-items-center rounded-pill px-3" href="#" role="button" data-bs-toggle="dropdown">
                        <c:choose>
                            <c:when test="${sessionScope.usuario.rolId == 1}">
                                <i class="fas fa-crown me-2"></i>
                            </c:when>
                            <c:when test="${sessionScope.usuario.rolId == 2}">
                                <i class="fas fa-chalkboard-teacher me-2"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-user me-2"></i>
                            </c:otherwise>
                        </c:choose>
                        <span class="fw-semibold">${sessionScope.usuario.nombreCorto}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil"><i class="fas fa-user me-2"></i>Mi Perfil</a></li>
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
                            <i class="fas fa-plus-circle me-2"></i>Nueva Asignación
                        </h1>
                        
                        <!-- Mensajes de error -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <!-- Formulario -->
                        <form id="asignacionForm" method="post" action="${pageContext.request.contextPath}/asignaciones">
                            <input type="hidden" name="action" value="guardar">
                            
                            <!-- Selección de docente -->
                            <div class="mb-3">
                                <label for="idDocente" class="form-label fw-semibold">
                                    <i class="fas fa-chalkboard-teacher me-1"></i>Docente <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="idDocente" name="idDocente" required>
                                    <option value="">Seleccionar docente...</option>
                                    <c:forEach var="docente" items="${docentes}">
                                        <option value="${docente.id}" ${asignacion != null && asignacion.idDocente == docente.id ? 'selected' : ''}>
                                            ${docente.nombre}
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Selecciona el docente que será responsable del estudiante
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
                                        <option value="${estudiante.id}" ${asignacion != null && asignacion.idEstudiante == estudiante.id ? 'selected' : ''}
                                                data-grado="${estudiante.grado}">
                                            ${estudiante.nombre}
                                            <c:if test="${not empty estudiante.grado}"> (${estudiante.grado}°)</c:if>
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Selecciona el estudiante a asignar al docente
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
                                        <option value="${grado.id}" ${asignacion != null && asignacion.idGradoEscolar == grado.id ? 'selected' : ''}>
                                            ${grado.nombre}
                                            <c:if test="${not empty grado.descripcion}"> - ${grado.descripcion}</c:if>
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Opcional: Especifica el grado escolar para esta asignación
                                </small>
                            </div>
                            
                            <!-- Fecha de asignación -->
                            <div class="mb-3">
                                <label for="fechaAsignacion" class="form-label fw-semibold">
                                    <i class="fas fa-calendar-alt me-1"></i>Fecha de Asignación <span class="text-danger">*</span>
                                </label>
                                <input type="datetime-local" class="form-control" id="fechaAsignacion" name="fechaAsignacion" 
                                       value="${asignacion != null ? asignacion.fechaAsignacion : ''}" required>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Solo se permite la fecha actual o fechas pasadas
                                </small>
                            </div>
                            
                            <!-- Estado -->
                            <div class="mb-3">
                                <label for="estado" class="form-label fw-semibold">
                                    <i class="fas fa-info-circle me-1"></i>Estado <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="estado" name="estado" required>
                                    <option value="ACTIVA" ${asignacion == null || asignacion.estado == 'ACTIVA' ? 'selected' : ''}>
                                        <i class="fas fa-check-circle"></i> Activa
                                    </option>
                                    <option value="SUSPENDIDA" ${asignacion != null && asignacion.estado == 'SUSPENDIDA' ? 'selected' : ''}>
                                        <i class="fas fa-pause-circle"></i> Suspendida
                                    </option>
                                    <option value="FINALIZADA" ${asignacion != null && asignacion.estado == 'FINALIZADA' ? 'selected' : ''}>
                                        <i class="fas fa-times-circle"></i> Finalizada
                                    </option>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Estado inicial de la asignación
                                </small>
                            </div>
                            
                            <!-- Observaciones -->
                            <div class="mb-4">
                                <label for="observaciones" class="form-label fw-semibold">
                                    <i class="fas fa-sticky-note me-1"></i>Observaciones
                                </label>
                                <textarea class="form-control" id="observaciones" name="observaciones" rows="4" 
                                          placeholder="Ingrese observaciones adicionales sobre esta asignación...">${asignacion != null ? asignacion.observaciones : ''}</textarea>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Notas o comentarios adicionales sobre la asignación
                                </small>
                            </div>
                            
                            <!-- Botones -->
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${pageContext.request.contextPath}/asignaciones" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>Guardar Asignación
                                </button>
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
<script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>
<script>
document.getElementById('year').textContent = new Date().getFullYear();

// Establecer fecha actual por defecto y restricciones
document.addEventListener('DOMContentLoaded', function() {
    const fechaAsignacion = document.getElementById('fechaAsignacion');
    
    // Obtener fecha y hora actual
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    const nowString = now.toISOString().slice(0, 16);
    
    // Establecer fecha actual como valor por defecto si no hay valor
    if (!fechaAsignacion.value) {
        fechaAsignacion.value = nowString;
    }
    
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