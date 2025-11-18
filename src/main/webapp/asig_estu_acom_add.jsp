<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Nuevo Vínculo · SumixKids</title>
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
                        <li>
                            <h6 class="dropdown-item">
                                <c:choose>
                                    <c:when test="${sessionScope.usuario.rolId == 1}">👑 Administrador</c:when>
                                    <c:when test="${sessionScope.usuario.rolId == 2}">👨‍🏫 Docente</c:when>
                                    <c:otherwise>👤 Usuario</c:otherwise>
                                </c:choose>
                            </h6>
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
                            <i class="fas fa-plus-circle me-2"></i>Nuevo Vínculo Estudiante-Acompañante
                        </h1>
                        
                        <!-- Mensajes de error -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <!-- Formulario -->
                        <form id="vinculoForm" method="post" action="${pageContext.request.contextPath}/vinculos">
                            <input type="hidden" name="action" value="guardar">
                            
                            <!-- Selección de estudiante -->
                            <div class="mb-3">
                                <label for="estudianteId" class="form-label fw-semibold">
                                    <i class="fas fa-user-graduate me-1"></i>Estudiante <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="estudianteId" name="estudianteId" required>
                                    <option value="">Seleccionar estudiante...</option>
                                    <c:forEach var="estudiante" items="${estudiantes}">
                                        <option value="${estudiante.id}">
                                            ${estudiante.nombre}
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Selecciona el estudiante a vincular
                                </small>
                            </div>
                            
                            <!-- Selección de acompañante -->
                            <div class="mb-3">
                                <label for="acompananteId" class="form-label fw-semibold">
                                    <i class="fas fa-user-shield me-1"></i>Acompañante (Padre/Tutor) <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="acompananteId" name="acompananteId" required>
                                    <option value="">Seleccionar acompañante...</option>
                                    <c:forEach var="acompanante" items="${acompanantes}">
                                        <option value="${acompanante.id}">
                                            ${acompanante.nombre}
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Selecciona el padre, madre o tutor responsable
                                </small>
                            </div>
                            
                            <!-- Tipo de relación -->
                            <div class="mb-4">
                                <label for="tipoRelacion" class="form-label fw-semibold">
                                    <i class="fas fa-heart me-1"></i>Tipo de Relación <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="tipoRelacion" name="tipoRelacion" required>
                                    <option value="">Seleccionar tipo de relación...</option>
                                    <option value="PRINCIPAL" selected>👨 Principal (Padre/Madre/Tutor)</option>
                                    <option value="SECUNDARIO">👥 Secundario (Familiar cercano)</option>
                                    <option value="EMERGENCIA">🚨 Emergencia (Contacto de emergencia)</option>
                                </select>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Especifica el parentesco o tipo de relación
                                </small>
                            </div>
                            
                            <!-- Información adicional -->
                            <div class="alert alert-info" role="alert">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>Nota:</strong> La fecha de vínculo se registrará automáticamente al momento de crear el vínculo.
                                El vínculo se creará en estado <strong>Activo</strong> por defecto.
                            </div>
                            
                            <!-- Botones -->
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="${pageContext.request.contextPath}/vinculos" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>Crear Vínculo
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

// Validación del formulario
document.getElementById('vinculoForm').addEventListener('submit', function(e) {
    const estudiante = document.getElementById('estudianteId').value;
    const acompanante = document.getElementById('acompananteId').value;
    const tipoRelacion = document.getElementById('tipoRelacion').value;
    
    if (!estudiante) {
        e.preventDefault();
        alert('Por favor selecciona un estudiante');
        document.getElementById('estudianteId').focus();
        return;
    }
    
    if (!acompanante) {
        e.preventDefault();
        alert('Por favor selecciona un acompañante');
        document.getElementById('acompananteId').focus();
        return;
    }
    
    if (!tipoRelacion) {
        e.preventDefault();
        alert('Por favor selecciona el tipo de relación');
        document.getElementById('tipoRelacion').focus();
        return;
    }
    
    // Validar que estudiante y acompañante no sean la misma persona
    if (estudiante === acompanante) {
        e.preventDefault();
        alert('El estudiante y el acompañante no pueden ser la misma persona');
        document.getElementById('acompananteId').focus();
        return;
    }
});

// Auto-ocultar alertas después de 5 segundos
setTimeout(() => {
    document.querySelectorAll('.alert:not(.alert-info)').forEach(alert => {
        const bsAlert = new bootstrap.Alert(alert);
        bsAlert.close();
    });
}, 5000);
</script>
</body>
</html>
