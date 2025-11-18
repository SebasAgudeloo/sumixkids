<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Mis Estudiantes · SumixKids</title>
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
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
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

<main class="flex-fill py-5">
    <div class="container">
        <!-- Header -->
        <div class="mb-4">
            <h1 class="display-6 fw-bold text-white-contrast drop-shadow">
                <i class="fas fa-user-graduate me-2"></i>
                Mis Estudiantes
            </h1>
            <p class="text-white-contrast">Estudiantes asignados a tu cargo</p>
        </div>

        <!-- Mensajes -->
        <c:if test="${not empty mensaje}">
            <div class="alert alert-success alert-dismissible fade show shadow-sm">
                <i class="fas fa-check-circle me-2"></i>${mensaje}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Filtros -->
        <div class="card shadow-lg border-0 mb-4">
            <div class="card-body">
                <form method="get" action="${pageContext.request.contextPath}/mis-estudiantes" class="row g-3">
                    <div class="col-md-4">
                        <label for="filtroGrado" class="form-label fw-semibold">Grado/Grupo</label>
                        <select class="form-select" id="filtroGrado" name="grado" onchange="this.form.submit()">
                            <option value="">Todos</option>
                            <c:forEach var="grado" items="${gradosDocente}">
                                <option value="${grado.idGrado}" ${param.grado == grado.idGrado ? 'selected' : ''}>
                                    ${grado.nombreGrado} ${grado.nombreGrupo}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label for="filtroEstado" class="form-label fw-semibold">Estado</label>
                        <select class="form-select" id="filtroEstado" name="estado" onchange="this.form.submit()">
                            <option value="">Todos</option>
                            <option value="ACTIVA" ${param.estado == 'ACTIVA' ? 'selected' : ''}>Activos</option>
                            <option value="SUSPENDIDA" ${param.estado == 'SUSPENDIDA' ? 'selected' : ''}>Suspendidos</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label for="filtroBusqueda" class="form-label fw-semibold">Buscar</label>
                        <input type="text" class="form-control" id="filtroBusqueda" name="busqueda" 
                               value="${param.busqueda}" placeholder="Nombre del estudiante...">
                    </div>
                </form>
            </div>
        </div>

        <!-- Estadísticas rápidas -->
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card shadow border-0 bg-success text-white">
                    <div class="card-body">
                        <h5 class="card-title">
                            <i class="fas fa-users me-2"></i>Total Estudiantes
                        </h5>
                        <h2 class="mb-0">${totalEstudiantes != null ? totalEstudiantes : 0}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow border-0 bg-info text-white">
                    <div class="card-body">
                        <h5 class="card-title">
                            <i class="fas fa-graduation-cap me-2"></i>Grados/Grupos
                        </h5>
                        <h2 class="mb-0">${totalGrados != null ? totalGrados : 0}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow border-0 bg-warning text-dark">
                    <div class="card-body">
                        <h5 class="card-title">
                            <i class="fas fa-check-circle me-2"></i>Asignaciones Activas
                        </h5>
                        <h2 class="mb-0">${totalActivas != null ? totalActivas : 0}</h2>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabla de estudiantes -->
        <div class="card shadow-lg border-0">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Estudiante</th>
                                <th>Correo</th>
                                <th>Grado/Grupo</th>
                                <th>Nivel</th>
                                <th>Fecha Asignación</th>
                                <th>Estado</th>
                                <th class="text-end">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty misEstudiantes}">
                                    <tr>
                                        <td colspan="7" class="text-center text-muted py-4">
                                            <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                            No tienes estudiantes asignados
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="asignacion" items="${misEstudiantes}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-3"
                                                         style="width: 40px; height: 40px;">
                                                        <i class="fas fa-user"></i>
                                                    </div>
                                                    <div>
                                                        <strong>
                                                            <c:choose>
                                                                <c:when test="${not empty asignacion.estudianteObj}">
                                                                    ${asignacion.estudianteObj.nombreCompleto}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    Estudiante #${asignacion.idEstudiante}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </strong>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <c:if test="${not empty asignacion.estudianteObj}">
                                                    <small>${asignacion.estudianteObj.correo}</small>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty asignacion.gradoEscolarObj}">
                                                        <span class="badge bg-primary">
                                                            ${asignacion.gradoEscolarObj.nombreGrado} ${asignacion.gradoEscolarObj.nombreGrupo}
                                                        </span>
                                                        <br>
                                                        <small class="text-muted">${asignacion.gradoEscolarObj.nivelEducativo}</small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">Grado #${asignacion.idGradoEscolar}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${not empty asignacion.gradoEscolarObj}">
                                                    <c:choose>
                                                        <c:when test="${asignacion.gradoEscolarObj.nivelEducativo == 'PREESCOLAR'}">
                                                            <span class="badge bg-warning">Preescolar</span>
                                                        </c:when>
                                                        <c:when test="${asignacion.gradoEscolarObj.nivelEducativo == 'PRIMARIA'}">
                                                            <span class="badge bg-primary">Primaria</span>
                                                        </c:when>
                                                        <c:when test="${asignacion.gradoEscolarObj.nivelEducativo == 'SECUNDARIA'}">
                                                            <span class="badge bg-success">Secundaria</span>
                                                        </c:when>
                                                    </c:choose>
                                                </c:if>
                                            </td>
                                            <td>
                                                <small>
                                                    <c:choose>
                                                        <c:when test="${not empty asignacion.fechaAsignacion}">
                                                            ${util:formatearFecha(asignacion.fechaAsignacion)}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </small>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${asignacion.estado == 'ACTIVA'}">
                                                        <span class="badge bg-success">
                                                            <i class="fas fa-check-circle me-1"></i>Activo
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${asignacion.estado == 'SUSPENDIDA'}">
                                                        <span class="badge bg-warning text-dark">
                                                            <i class="fas fa-pause-circle me-1"></i>Suspendido
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">
                                                            ${asignacion.estado}
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-end">
                                                <div class="btn-group" role="group">
                                                    <a href="${pageContext.request.contextPath}/estudiante/perfil?id=${asignacion.idEstudiante}" 
                                                       class="btn btn-sm btn-outline-primary"
                                                       title="Ver perfil">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/estudiante/progreso?id=${asignacion.idEstudiante}" 
                                                       class="btn btn-sm btn-outline-info"
                                                       title="Ver progreso">
                                                        <i class="fas fa-chart-line"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/estudiante/tareas?id=${asignacion.idEstudiante}" 
                                                       class="btn btn-sm btn-outline-success"
                                                       title="Asignar tarea">
                                                        <i class="fas fa-tasks"></i>
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <!-- Resumen -->
                <c:if test="${not empty misEstudiantes}">
                    <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                        <span class="text-muted">
                            <i class="fas fa-users me-2"></i>
                            Mostrando <strong>${misEstudiantes.size()}</strong> estudiantes
                        </span>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
