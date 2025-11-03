<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%-- Dashboard específico para estudiantes --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Mi Espacio · SumixKids</title>
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
                    <a class="nav-link active" href="${pageContext.request.contextPath}/estudiante_dashboard">
                        🎓 Mi Espacio
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/juegos">
                        🎮 Juegos
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/lecciones">
                        📖 Lecciones
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/mi_progreso">
                        ⭐ Progreso
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center rounded-pill px-3" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-graduation-cap me-2"></i>
                        <span class="fw-semibold">${sessionScope.usuario.nombreCorto}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0">
                        <li><h6 class="dropdown-item">🎓 Estudiante</h6></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil"><i class="fas fa-user me-2"></i>Mi Perfil</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/configuracion"><i class="fas fa-cog me-2"></i>Configuración</a></li>
                        
                        <%-- Botón de eliminar cuenta solo para usuarios no-admin --%>
                        <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId != 1}">
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/eliminar_usuario?tipo=usuario">
                                    <i class="fas fa-user-times me-2"></i>Eliminar mi cuenta
                                </a>
                            </li>
                        </c:if>
                        
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
        <!-- Header personalizado para estudiante -->
        <div class="row justify-content-center mb-5">
            <div class="col-lg-10 text-center">
                <div class="page-header">
                    <h1 class="display-4 fw-bold text-white-contrast mb-3 drop-shadow">
                        🎓 ¡Hola, ${sessionScope.usuario.nombreCorto}!
                    </h1>
                    <p class="lead text-white-contrast mb-4">
                        🌟 Bienvenido a tu espacio de aprendizaje - ¡Explora y aprende jugando!
                    </p>
                </div>
            </div>
        </div>

        <!-- Módulos específicos para estudiante -->
        <div class="row g-4 mb-5">
            <div class="col-lg-4">
                <div class="card stat-card estudiante h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="stat-icon mb-3">
                            <i class="fas fa-gamepad"></i>
                        </div>
                        <h3 class="mb-2 fw-bold">🎮 Juegos Educativos</h3>
                        <p class="text-muted mb-4">Aprende jugando con matemáticas y lenguaje</p>
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/juegos" class="btn btn-success btn-lg fw-bold">
                                <i class="fas fa-play me-2"></i>Jugar Ahora
                            </a>
                            <a href="${pageContext.request.contextPath}/mis_logros" class="btn btn-outline-success">
                                <i class="fas fa-trophy me-2"></i>Mis Logros
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-lg-4">
                <div class="card stat-card docente h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="stat-icon mb-3">
                            <i class="fas fa-book-reader"></i>
                        </div>
                        <h3 class="mb-2 fw-bold">📖 Mis Lecciones</h3>
                        <p class="text-muted mb-4">Accede a tus materias y contenidos de estudio</p>
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/lecciones" class="btn btn-primary btn-lg fw-bold">
                                <i class="fas fa-book me-2"></i>Ver Lecciones
                            </a>
                            <a href="${pageContext.request.contextPath}/tareas" class="btn btn-outline-primary">
                                <i class="fas fa-tasks me-2"></i>Mis Tareas
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-lg-4">
                <div class="card stat-card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="stat-icon text-warning mb-3">
                            <i class="fas fa-star"></i>
                        </div>
                        <h3 class="mb-2 text-warning fw-bold">⭐ Mi Progreso</h3>
                        <p class="text-muted mb-4">Revisa tu avance y calificaciones</p>
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/mi_progreso" class="btn btn-warning btn-lg fw-bold text-dark">
                                <i class="fas fa-chart-line me-2"></i>Ver Progreso
                            </a>
                            <a href="${pageContext.request.contextPath}/certificados" class="btn btn-outline-warning">
                                <i class="fas fa-certificate me-2"></i>Certificados
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
</script>
</body>
</html>
