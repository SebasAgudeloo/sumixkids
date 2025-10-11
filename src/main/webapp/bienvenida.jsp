<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%-- Dashboard principal después de iniciar sesión --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Dashboard · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">

<!-- Navbar principal -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/">
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
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 1}">
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
                </c:if>
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 2}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/mis_clases">
                            📚 Mis Clases
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/estudiantes">
                            🎓 Estudiantes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/reportes_docente">
                            📈 Reportes
                        </a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 3}">
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
                </c:if>
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 4}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/mis_hijos">
                            👶 Mis Hijos
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/seguimiento">
                            📊 Seguimiento
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/mensajes">
                            💬 Mensajes
                        </a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center rounded-pill px-3" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <c:choose>
                            <c:when test="${sessionScope.usuario.rolId == 1}"><i class="fas fa-crown me-2"></i></c:when>
                            <c:when test="${sessionScope.usuario.rolId == 2}"><i class="fas fa-chalkboard-teacher me-2"></i></c:when>
                            <c:when test="${sessionScope.usuario.rolId == 3}"><i class="fas fa-graduation-cap me-2"></i></c:when>
                            <c:when test="${sessionScope.usuario.rolId == 4}"><i class="fas fa-users me-2"></i></c:when>
                            <c:otherwise><i class="fas fa-user-circle me-2"></i></c:otherwise>
                        </c:choose>
                        <span class="fw-semibold">${sessionScope.usuario.nombres}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0">
                        <li>
                            <h6 class="dropdown-item">
                                <c:choose>
                                    <c:when test="${sessionScope.usuario.rolId == 1}">👑 Administrador</c:when>
                                    <c:when test="${sessionScope.usuario.rolId == 2}">👨‍🏫 Docente</c:when>
                                    <c:when test="${sessionScope.usuario.rolId == 3}">🎓 Estudiante</c:when>
                                    <c:when test="${sessionScope.usuario.rolId == 4}">👨‍👩‍👧‍👦 Padre/Madre</c:when>
                                    <c:otherwise>👤 Usuario</c:otherwise>
                                </c:choose>
                            </h6>
                        </li>
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
        <!-- Header de bienvenida personalizado -->
        <div class="row justify-content-center mb-5">
            <div class="col-lg-10 text-center">
                <div class="page-header">
                    <h1 class="display-4 fw-bold text-white-contrast mb-3 drop-shadow">
                        <c:choose>
                            <c:when test="${sessionScope.usuario.rolId == 1}">👑 ¡Hola, ${sessionScope.usuario.nombres}!</c:when>
                            <c:when test="${sessionScope.usuario.rolId == 2}">👨‍🏫 ¡Hola, ${sessionScope.usuario.nombres}!</c:when>
                            <c:when test="${sessionScope.usuario.rolId == 3}">🎓 ¡Hola, ${sessionScope.usuario.nombres}!</c:when>
                            <c:when test="${sessionScope.usuario.rolId == 4}">👨‍👩‍👧‍👦 ¡Hola, ${sessionScope.usuario.nombres}!</c:when>
                            <c:otherwise>👋 ¡Hola, ${sessionScope.usuario.nombres}!</c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="lead text-white-contrast mb-4">
                        <c:choose>
                            <c:when test="${sessionScope.usuario.rolId == 1}">🚀 Bienvenido al Panel de Administración - Gestiona toda la plataforma educativa</c:when>
                            <c:when test="${sessionScope.usuario.rolId == 2}">📚 Bienvenido al Panel de Docente - Gestiona tus clases y estudiantes</c:when>
                            <c:when test="${sessionScope.usuario.rolId == 3}">🌟 Bienvenido a tu espacio de aprendizaje - ¡Explora y aprende jugando!</c:when>
                            <c:when test="${sessionScope.usuario.rolId == 4}">👀 Bienvenido al Panel de Padres - Supervisa el progreso de tus hijos</c:when>
                            <c:otherwise>🎉 Bienvenido a SumixKids - Tu plataforma educativa</c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
        </div>

        <!-- MÓDULOS PARA ADMINISTRADOR -->
        <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 1}">
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
                                        <small class="text-muted d-block">👨‍👩‍👧‍👦 Padres</small>
                                        <strong class="text-info">${totalPadres != null ? totalPadres : 0}</strong>
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
        </c:if>

        <!-- MÓDULOS PARA DOCENTE -->
        <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 2}">
            <div class="row g-4 mb-5">
                <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                    <div class="card stat-card docente h-100 border-0 shadow-lg">
                        <div class="card-body text-center p-4">
                            <div class="stat-icon mb-3">
                                <i class="fas fa-chalkboard-teacher"></i>
                            </div>
                            <h3 class="mb-2 fw-bold">📚 Mis Clases</h3>
                            <p class="text-muted mb-4">Gestiona tus materias y contenidos educativos</p>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/mis-clases" class="btn btn-primary btn-lg fw-bold">
                                    <i class="fas fa-book me-2"></i>Ver Clases
                                </a>
                                <a href="${pageContext.request.contextPath}/crear-clase" class="btn btn-outline-primary">
                                    <i class="fas fa-plus me-2"></i>Nueva Clase
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                    <div class="card stat-card estudiante h-100 border-0 shadow-lg">
                        <div class="card-body text-center p-4">
                            <div class="stat-icon mb-3">
                                <i class="fas fa-user-graduate"></i>
                            </div>
                            <h3 class="mb-2 fw-bold">🎓 Estudiantes</h3>
                            <p class="text-muted mb-4">Administra y evalúa el progreso de tus estudiantes</p>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/estudiantes" class="btn btn-success btn-lg fw-bold">
                                    <i class="fas fa-users me-2"></i>Ver Estudiantes
                                </a>
                                <a href="${pageContext.request.contextPath}/calificaciones" class="btn btn-outline-success">
                                    <i class="fas fa-star me-2"></i>Calificaciones
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                    <div class="card stat-card h-100 border-0 shadow-lg">
                        <div class="card-body text-center p-4">
                            <div class="stat-icon text-info mb-3">
                                <i class="fas fa-chart-bar"></i>
                            </div>
                            <h3 class="mb-2 text-info fw-bold">📈 Reportes</h3>
                            <p class="text-muted mb-4">Genera reportes de progreso y estadísticas</p>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/reportes-docente" class="btn btn-info btn-lg fw-bold text-white">
                                    <i class="fas fa-file-alt me-2"></i>Ver Reportes
                                </a>
                                <a href="${pageContext.request.contextPath}/estadisticas" class="btn btn-outline-info">
                                    <i class="fas fa-chart-pie me-2"></i>Estadísticas
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- MÓDULOS PARA ESTUDIANTE -->
        <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 3}">
            <div class="row g-4 mb-5">
                <div class="col-12 col-md-6 col-lg-4 col-xl-4">
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
                                <a href="${pageContext.request.contextPath}/mis-logros" class="btn btn-outline-success">
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
                
                <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                    <div class="card stat-card h-100 border-0 shadow-lg">
                        <div class="card-body text-center p-4">
                            <div class="stat-icon text-warning mb-3">
                                <i class="fas fa-star"></i>
                            </div>
                            <h3 class="mb-2 text-warning fw-bold">⭐ Mi Progreso</h3>
                            <p class="text-muted mb-4">Revisa tu avance y calificaciones</p>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/mi-progreso" class="btn btn-warning btn-lg fw-bold text-dark">
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
        </c:if>

        <!-- MÓDULOS PARA PADRE -->
        <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 4}">
            <div class="row g-4 mb-5">
                <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                    <div class="card stat-card h-100 border-0 shadow-lg">
                        <div class="card-body text-center p-4">
                            <div class="stat-icon text-info mb-3">
                                <i class="fas fa-child"></i>
                            </div>
                            <h3 class="mb-2 text-info fw-bold">👶 Mis Hijos</h3>
                            <p class="text-muted mb-4">Supervisa el progreso académico de tus hijos</p>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/mis-hijos" class="btn btn-info btn-lg fw-bold text-white">
                                    <i class="fas fa-users me-2"></i>Ver Hijos
                                </a>
                                <a href="${pageContext.request.contextPath}/vincular-hijo" class="btn btn-outline-info">
                                    <i class="fas fa-link me-2"></i>Vincular Hijo
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                    <div class="card stat-card estudiante h-100 border-0 shadow-lg">
                        <div class="card-body text-center p-4">
                            <div class="stat-icon mb-3">
                                <i class="fas fa-chart-line"></i>
                            </div>
                            <h3 class="mb-2 fw-bold">📊 Seguimiento</h3>
                            <p class="text-muted mb-4">Monitorea calificaciones y actividades</p>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/seguimiento" class="btn btn-success btn-lg fw-bold">
                                    <i class="fas fa-eye me-2"></i>Ver Seguimiento
                                </a>
                                <a href="${pageContext.request.contextPath}/notificaciones" class="btn btn-outline-success">
                                    <i class="fas fa-bell me-2"></i>Notificaciones
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-12 col-md-6 col-lg-4 col-xl-4">
                    <div class="card stat-card padre h-100 border-0 shadow-lg">
                        <div class="card-body text-center p-4">
                            <div class="stat-icon mb-3">
                                <i class="fas fa-comments"></i>
                            </div>
                            <h3 class="mb-2 fw-bold">💬 Comunicación</h3>
                            <p class="text-muted mb-4">Mantente en contacto con docentes</p>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/mensajes" class="btn btn-lg fw-bold text-white" style="background-color: #8b5cf6;">
                                    <i class="fas fa-envelope me-2"></i>Mensajes
                                </a>
                                <a href="${pageContext.request.contextPath}/citas" class="btn btn-outline-secondary">
                                    <i class="fas fa-calendar me-2"></i>Agendar Cita
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

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
<script src="${pageContext.request.contextPath}/js/session-timeout.js"></script>
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