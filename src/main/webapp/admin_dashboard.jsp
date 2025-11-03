<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%-- Dashboard específico para administradores --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Panel Administrador · SumixKids</title>
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
                    <a class="nav-link active" href="${pageContext.request.contextPath}/bienvenida">
                        🏠 Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">
                        👥 Usuarios
                    </a>
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

<main class="flex-fill py-5">
    <div class="container">
        <!-- Header de bienvenida personalizado -->
        <div class="row justify-content-center mb-5">
            <div class="col-lg-10 text-center">
                <div class="page-header">
                    <h1 class="display-4 fw-bold text-white-contrast mb-3 drop-shadow">
                        👑 ¡Hola, ${sessionScope.usuario.nombreCorto}!
                    </h1>
                    <p class="lead text-white-contrast mb-4">
                        🚀 Bienvenido al Panel de Administración - Gestiona toda la plataforma educativa
                    </p>
                </div>
            </div>
        </div>

        <!-- MÓDULOS PARA ADMINISTRADOR -->
        <div class="row g-4 mb-5">
            <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                <div class="card stat-card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="stat-icon text-success mb-3">
                            <i class="fas fa-users"></i>
                        </div>
                        <h3 class="mb-2 text-success fw-bold">Gestionar Usuarios</h3>
                        <p class="text-muted mb-4">
                            Administra usuarios, roles y permisos del sistema de manera integral
                        </p>
                        <div class="row g-2 mb-4">
                            <div class="col-6">
                                <div class="bg-light rounded-3 p-2">
                                    <small class="text-muted d-block">👑 Admins</small>
                                    <strong class="text-warning">${totalAdmins != null ? totalAdmins : 0}</strong>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="bg-light rounded-3 p-2">
                                    <small class="text-muted d-block">👨‍🏫 Docentes</small>
                                    <strong class="text-primary">${totalDocentes != null ? totalDocentes : 0}</strong>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="bg-light rounded-3 p-2">
                                    <small class="text-muted d-block">🎓 Estudiantes</small>
                                    <strong class="text-success">${totalEstudiantes != null ? totalEstudiantes : 0}</strong>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="bg-light rounded-3 p-2">
                                    <small class="text-muted d-block">👥 Acompañantes</small>
                                    <strong class="text-info">${totalAttendants != null ? totalAttendants : 0}</strong>
                                </div>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-success btn-lg w-100 fw-bold">
                            <i class="fas fa-users-cog me-2"></i>Ir a Usuarios
                        </a>
                    </div>
                </div>
            </div>
            
            <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                <div class="card stat-card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="stat-icon text-info mb-3">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <h3 class="mb-2 text-info fw-bold">Ver Auditoría</h3>
                        <p class="text-muted mb-4">
                            Monitorea actividades, eventos y estadísticas del sistema en tiempo real
                        </p>
                        <div class="mb-4">
                            <div class="d-flex justify-content-between align-items-center bg-light rounded-3 p-3 mb-2">
                                <span class="text-muted small">📊 Eventos registrados</span>
                                <strong class="text-info">${totalEventos != null ? totalEventos : 0}</strong>
                            </div>
                            <div class="d-flex justify-content-between align-items-center bg-light rounded-3 p-3">
                                <span class="text-muted small">⚠️ Eventos fallidos</span>
                                <strong class="text-warning">${eventosFallidos != null ? eventosFallidos : 0}</strong>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/auditoria" class="btn btn-info btn-lg w-100 fw-bold text-white">
                            <i class="fas fa-eye me-2"></i>Ver Auditoría
                        </a>
                    </div>
                </div>
            </div>
            
            <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                <div class="card stat-card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="stat-icon text-warning mb-3">
                            <i class="fas fa-upload"></i>
                        </div>
                        <h3 class="mb-2 text-warning fw-bold">Cargar Archivo</h3>
                        <p class="text-muted mb-4">
                            Importa múltiples usuarios desde archivos Excel de manera eficiente y segura
                        </p>
                        <div class="mb-4">
                            <div class="bg-light rounded-3 p-3 mb-2">
                                <div class="d-flex align-items-center justify-content-center">
                                    <i class="fas fa-file-excel text-success me-2"></i>
                                    <span class="text-muted small">Formatos: .xlsx, .xls</span>
                                </div>
                            </div>
                            <div class="bg-light rounded-3 p-3">
                                <div class="d-flex align-items-center justify-content-center">
                                    <i class="fas fa-shield-alt text-primary me-2"></i>
                                    <span class="text-muted small">Validación automática</span>
                                </div>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/carga_masiva" class="btn btn-warning btn-lg w-100 fw-bold text-dark">
                            <i class="fas fa-cloud-upload-alt me-2"></i>Cargar Usuarios
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- HERRAMIENTAS ADMINISTRATIVAS ADICIONALES -->
        <div class="row g-4 mb-5">
            <div class="col-12">
                <h2 class="text-white drop-shadow mb-4">🛠️ Herramientas Administrativas</h2>
            </div>
            
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="mb-3">
                            <i class="fas fa-database text-primary" style="font-size: 3rem;"></i>
                        </div>
                        <h5 class="mb-2 fw-bold">Base de Datos</h5>
                        <p class="text-muted mb-4">Gestiona respaldos y mantenimiento de la base de datos</p>
                        <div class="d-grid gap-2">
                            <button class="btn btn-outline-primary" onclick="mostrarMantenimientoBD()">
                                <i class="fas fa-tools me-2"></i>Mantenimiento
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="mb-3">
                            <i class="fas fa-cogs text-secondary" style="font-size: 3rem;"></i>
                        </div>
                        <h5 class="mb-2 fw-bold">Configuración Sistema</h5>
                        <p class="text-muted mb-4">Administra configuraciones globales del sistema</p>
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/configuracion" class="btn btn-outline-secondary">
                                <i class="fas fa-sliders-h me-2"></i>Configuraciones
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="mb-3">
                            <i class="fas fa-chart-pie text-success" style="font-size: 3rem;"></i>
                        </div>
                        <h5 class="mb-2 fw-bold">Estadísticas Globales</h5>
                        <p class="text-muted mb-4">Visualiza métricas y estadísticas del sistema</p>
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/estadisticas" class="btn btn-outline-success">
                                <i class="fas fa-chart-line me-2"></i>Ver Estadísticas
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- SECCIÓN DE SEGURIDAD -->
        <div class="row g-4 mb-5">
            <div class="col-12">
                <h2 class="text-white drop-shadow mb-4">🔐 Seguridad y Configuración</h2>
            </div>
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="mb-3">
                            <i class="bi bi-shield-check text-success" style="font-size: 3rem;"></i>
                        </div>
                        <h5 class="mb-2 fw-bold">Dispositivos 2FA</h5>
                        <p class="text-muted mb-4">Gestiona los dispositivos reconocidos para autenticación</p>
                        <div class="d-grid">
                            <a href="${pageContext.request.contextPath}/dispositivos_reconocidos" class="btn btn-outline-success">
                                <i class="bi bi-devices me-2"></i>Ver Dispositivos
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="mb-3">
                            <i class="bi bi-key text-warning" style="font-size: 3rem;"></i>
                        </div>
                        <h5 class="mb-2 fw-bold">Cambiar Contraseña</h5>
                        <p class="text-muted mb-4">Actualiza tu contraseña por seguridad</p>
                        <div class="d-grid">
                            <a href="${pageContext.request.contextPath}/cambiar_password" class="btn btn-outline-warning">
                                <i class="bi bi-lock me-2"></i>Cambiar Contraseña
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="mb-3">
                            <i class="bi bi-person-gear text-info" style="font-size: 3rem;"></i>
                        </div>
                        <h5 class="mb-2 fw-bold">Mi Perfil</h5>
                        <p class="text-muted mb-4">Edita tu información personal</p>
                        <div class="d-grid">
                            <a href="${pageContext.request.contextPath}/perfil" class="btn btn-outline-info">
                                <i class="bi bi-person me-2"></i>Ver Perfil
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Alertas de notificación -->
        <c:if test="${not empty error || not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show shadow-lg" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <strong>❌ Error:</strong> ${not empty error ? error : param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty mensaje || not empty param.mensaje}">
            <div class="alert alert-success alert-dismissible fade show shadow-lg" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                <strong>✅ ¡Éxito!</strong> ${not empty mensaje ? mensaje : param.mensaje}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
    </div>
</main>

<%-- Footer --%>
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
<!-- SweetAlert2 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>
<script>
document.getElementById('year').textContent = new Date().getFullYear();

// Auto-hide alerts after 10 seconds
setTimeout(function() {
    document.querySelectorAll('.alert-dismissible').forEach(alert => {
        if (alert.classList.contains('show')) {
            bootstrap.Alert.getOrCreateInstance(alert).close();
        }
    });
}, 10000);

function mostrarMantenimientoBD() {
    Swal.fire({
        title: '🛠️ Mantenimiento de Base de Datos',
        html: `
            <div class="text-start">
                <p><strong>Funciones disponibles:</strong></p>
                <ul>
                    <li>Optimizar tablas</li>
                    <li>Verificar integridad</li>
                    <li>Crear respaldo</li>
                    <li>Limpiar logs antiguos</li>
                </ul>
            </div>
        `,
        icon: 'info',
        showCancelButton: true,
        confirmButtonText: 'Ejecutar Mantenimiento',
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#0d6efd'
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('🔧 Mantenimiento iniciado', 'Se ejecutará en segundo plano', 'success');
        }
    });
}
</script>
</body>
</html>