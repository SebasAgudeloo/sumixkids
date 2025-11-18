<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Gestión de Vínculos Estudiante-Acompañante · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        /* Asegurar que el dropdown aparezca por encima de los cards y otros elementos */
        .dropdown-menu {
            z-index: 1060 !important;
            position: absolute !important;
            will-change: transform;
        }
        
        .table .btn-group {
            position: static;
        }
        
        .dropdown-toggle::after {
            margin-left: 0.5em;
        }
        
        .dropdown-menu {
            transition: all 0.2s ease-in-out;
            opacity: 0;
            transform: translateY(-10px);
        }
        
        .dropdown-menu.show {
            opacity: 1;
            transform: translateY(0);
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

<main class="flex-fill py-5">
    <div class="container">
        <div class="row justify-content-center mb-4">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center">
                    <h1 class="display-6 fw-bold text-white-contrast drop-shadow">
                        <i class="fas fa-user-friends me-2"></i>Gestión de Vínculos
                    </h1>
                    <a href="${pageContext.request.contextPath}/vinculos?action=agregar" class="btn btn-light shadow-sm">
                        <i class="fas fa-plus me-2"></i>Nuevo Vínculo
                    </a>
                </div>
            </div>
        </div>

        <!-- Mensajes de estado -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${param.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Tabla de vínculos -->
        <div class="card shadow-lg border-0 mb-4">
            <div class="card-body">
                <!-- Debug: Verificar condiciones -->
                <c:choose>
                    <c:when test="${vinculos != null && fn:length(vinculos) > 0}">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-dark">
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col"><i class="fas fa-user-graduate me-1"></i>Estudiante</th>
                                        <th scope="col"><i class="fas fa-user-shield me-1"></i>Acompañante</th>
                                        <th scope="col"><i class="fas fa-heart me-1"></i>Tipo Relación</th>
                                        <th scope="col"><i class="fas fa-toggle-on me-1"></i>Estado</th>
                                        <th scope="col"><i class="fas fa-calendar me-1"></i>F. Vinculación</th>
                                        <th scope="col"><i class="fas fa-cogs me-1"></i>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="vinculo" items="${vinculos}">
                                        <tr>
                                            <td class="fw-semibold">${vinculo.id}</td>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <i class="fas fa-graduation-cap text-success me-2"></i>
                                                    <span>${vinculo.nombreEstudiante}</span>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <i class="fas fa-user-shield text-primary me-2"></i>
                                                    <span>${vinculo.nombreAcompanante}</span>
                                                </div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${vinculo.tipoRelacion == 'PRINCIPAL'}">
                                                        <span class="badge bg-primary">
                                                            <i class="fas fa-star me-1"></i>Principal
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${vinculo.tipoRelacion == 'SECUNDARIO'}">
                                                        <span class="badge bg-info">
                                                            <i class="fas fa-user me-1"></i>Secundario
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${vinculo.tipoRelacion == 'EMERGENCIA'}">
                                                        <span class="badge bg-warning">
                                                            <i class="fas fa-exclamation-triangle me-1"></i>Emergencia
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-light text-dark">
                                                            <i class="fas fa-user me-1"></i>${vinculo.tipoRelacion}
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${vinculo.activo}">
                                                        <span class="badge bg-success">
                                                            <i class="fas fa-check-circle me-1"></i>Activo
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">
                                                            <i class="fas fa-times-circle me-1"></i>Inactivo
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${not empty vinculo.fechaVinculacion}">
                                                    <small class="text-muted">
                                                        <i class="fas fa-calendar-alt me-1"></i>
                                                        ${util:formatearFecha(vinculo.fechaVinculacion)}
                                                    </small>
                                                </c:if>
                                            </td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <a href="${pageContext.request.contextPath}/vinculos?action=editar&id=${vinculo.id}" 
                                                       class="btn btn-sm btn-outline-primary" title="Editar vínculo">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    
                                                    <!-- Dropdown para activar/desactivar -->
                                                    <div class="btn-group" role="group">
                                                        <button type="button" class="btn btn-sm btn-outline-secondary dropdown-toggle" 
                                                                data-bs-toggle="dropdown" data-bs-boundary="viewport" 
                                                                data-bs-placement="bottom-start" aria-expanded="false" title="Cambiar estado">
                                                            <i class="fas fa-exchange-alt"></i>
                                                        </button>
                                                        <ul class="dropdown-menu">
                                                            <c:choose>
                                                                <c:when test="${vinculo.activo}">
                                                                    <li>
                                                                        <a class="dropdown-item" href="javascript:void(0)" 
                                                                           data-id="${vinculo.id}" 
                                                                           onclick="desactivarVinculo(this)">
                                                                            <i class="fas fa-times-circle text-warning me-2"></i>Desactivar
                                                                        </a>
                                                                    </li>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <li>
                                                                        <a class="dropdown-item" href="javascript:void(0)" 
                                                                           data-id="${vinculo.id}" 
                                                                           onclick="activarVinculo(this)">
                                                                            <i class="fas fa-check-circle text-success me-2"></i>Activar
                                                                        </a>
                                                                    </li>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </ul>
                                                    </div>
                                                    
                                                    <c:if test="${sessionScope.usuario.rolId == 1}">
                                                        <button type="button" class="btn btn-sm btn-outline-danger" 
                                                                title="Eliminar permanentemente"
                                                                data-id="${vinculo.id}"
                                                                onclick="confirmarEliminacion(this)">
                                                            <i class="fas fa-trash"></i>
                                                        </button>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="fas fa-user-friends fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">No hay vínculos registrados</h5>
                            <p class="text-muted">Comienza creando un nuevo vínculo entre estudiante y acompañante.</p>
                            <a href="${pageContext.request.contextPath}/vinculos?action=agregar" class="btn btn-primary">
                                <i class="fas fa-plus me-2"></i>Crear primer vínculo
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
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

// Función para activar vínculo
function activarVinculo(element) {
    const id = element.getAttribute('data-id');
    
    Swal.fire({
        title: 'Activar Vínculo',
        html: `<p>¿Estás seguro de que deseas <strong>activar</strong> este vínculo?</p>
               <div class="alert alert-success mt-3">
                   <i class="fas fa-info-circle me-2"></i>
                   El vínculo estará activo y funcional.
               </div>`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, activar',
        cancelButtonText: 'Cancelar',
        width: '400px'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '${pageContext.request.contextPath}/vinculos?action=activar&id=' + id;
        }
    });
}

// Función para desactivar vínculo
function desactivarVinculo(element) {
    const id = element.getAttribute('data-id');
    
    Swal.fire({
        title: 'Desactivar Vínculo',
        html: `<p>¿Estás seguro de que deseas <strong>desactivar</strong> este vínculo?</p>
               <div class="alert alert-warning mt-3">
                   <i class="fas fa-info-circle me-2"></i>
                   El vínculo se marcará como inactivo.
               </div>`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ffc107',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, desactivar',
        cancelButtonText: 'Cancelar',
        width: '400px'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '${pageContext.request.contextPath}/vinculos?action=eliminar&id=' + id;
        }
    });
}

// Función para confirmar eliminación permanente
function confirmarEliminacion(button) {
    const id = button.getAttribute('data-id');
    
    Swal.fire({
        title: '¡Atención!',
        html: `<p>¿Estás seguro de que deseas <strong>eliminar permanentemente</strong> este vínculo?</p>
               <div class="alert alert-warning mt-3">
                   <i class="fas fa-exclamation-triangle me-2"></i>
                   <strong>Esta acción NO se puede deshacer.</strong>
               </div>`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar permanentemente',
        cancelButtonText: 'Cancelar',
        width: '450px'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = '${pageContext.request.contextPath}/vinculos?action=delete&id=' + id;
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
