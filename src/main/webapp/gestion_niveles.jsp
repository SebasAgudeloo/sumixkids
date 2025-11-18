<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Niveles de Dificultad · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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

<main class="flex-fill py-5">
    <div class="container">
        <!-- Header con botón de acción -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="display-6 fw-bold text-white-contrast drop-shadow">
                <i class="fas fa-signal me-2"></i>
                Niveles de Dificultad
            </h1>
            <a href="${pageContext.request.contextPath}/niveles?action=nuevo" class="btn btn-light btn-lg shadow">
                <i class="fas fa-plus me-2"></i>Nuevo Nivel
            </a>
        </div>



        <!-- Filtros -->
        <div class="card shadow-lg border-0 mb-4">
            <div class="card-body">
                <form method="get" action="${pageContext.request.contextPath}/niveles" class="row g-3">
                    <div class="col-md-4">
                        <label for="filtroEstado" class="form-label fw-semibold">Estado</label>
                        <select class="form-select" id="filtroEstado" name="estado" onchange="this.form.submit()">
                            <option value="">Todos</option>
                            <option value="activo" ${param.estado == 'activo' ? 'selected' : ''}>Activos</option>
                            <option value="inactivo" ${param.estado == 'inactivo' ? 'selected' : ''}>Inactivos</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label for="filtroBusqueda" class="form-label fw-semibold">Buscar</label>
                        <input type="text" class="form-control" id="filtroBusqueda" name="busqueda" 
                               value="${param.busqueda}" placeholder="Nombre del nivel...">
                    </div>
                    <div class="col-md-4 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary me-2">
                            <i class="fas fa-search me-2"></i>Buscar
                        </button>
                        <a href="${pageContext.request.contextPath}/niveles" class="btn btn-outline-secondary">
                            <i class="fas fa-redo me-2"></i>Limpiar
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Tabla de niveles -->
        <div class="card shadow-lg border-0">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Orden</th>
                                <th>Icono</th>
                                <th>Nombre</th>
                                <th>Descripción</th>
                                <th>Color</th>
                                <th>Estado</th>
                                <th class="text-end">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty niveles}">
                                    <tr>
                                        <td colspan="7" class="text-center text-muted py-4">
                                            <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                            No se encontraron niveles de dificultad
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="nivel" items="${niveles}">
                                        <tr>
                                            <td>
                                                <span class="badge bg-secondary">#${nivel.orden}</span>
                                            </td>
                                            <td>
                                                <span style="font-size: 1.5rem;">${nivel.icono}</span>
                                            </td>
                                            <td>
                                                <strong>${nivel.nombreNivel}</strong>
                                                <br>
                                                <small class="text-muted">${nivel.estrellas}</small>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty nivel.descripcionLegible}">
                                                        ${util:truncate(nivel.descripcionLegible, 50)}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted fst-italic">Sin descripción</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="color-preview mx-auto" 
                                                     style="width: 30px; height: 30px; background-color: ${nivel.colorHex}; border-radius: 50%; border: 2px solid #ddd; cursor: help;"
                                                     title="Color: ${nivel.colorHex}">
                                                </div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${nivel.activo}">
                                                        <span class="badge bg-success">
                                                            <i class="fas fa-check-circle me-1"></i>Activo
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger">
                                                            <i class="fas fa-times-circle me-1"></i>Inactivo
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-end">
                                                <div class="btn-group" role="group">
                                                    <a href="${pageContext.request.contextPath}/niveles?action=editar&id=${nivel.idNivelDificultad}" 
                                                       class="btn btn-sm btn-outline-primary"
                                                       title="Editar">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <c:choose>
                                                        <c:when test="${nivel.activo}">
                                                            <button class="btn btn-sm btn-outline-danger btn-desactivar"
                                                                    title="Desactivar"
                                                                    data-id="${nivel.idNivelDificultad}"
                                                                    data-nombre="${nivel.nombreNivel}">
                                                                <i class="fas fa-ban"></i>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button class="btn btn-sm btn-outline-success btn-activar"
                                                                    title="Activar"
                                                                    data-id="${nivel.idNivelDificultad}"
                                                                    data-nombre="${nivel.nombreNivel}">
                                                                <i class="fas fa-check"></i>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <button class="btn btn-sm btn-outline-dark btn-eliminar"
                                                            title="Eliminar permanentemente"
                                                            data-id="${nivel.idNivelDificultad}"
                                                            data-nombre="${nivel.nombreNivel}">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
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
                <c:if test="${not empty niveles}">
                    <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                        <span class="text-muted">
                            <i class="fas fa-layer-group me-2"></i>
                            Total: <strong>${niveles.size()}</strong> niveles
                        </span>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<c:if test="${not empty mensaje}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            Swal.fire({
                title: '¡Éxito!',
                text: '${mensaje}',
                icon: 'success',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#28a745'
            });
        });
    </script>
    <c:remove var="mensaje" scope="session"/>
</c:if>

<c:if test="${not empty error}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            Swal.fire({
                title: '¡Oops!',
                text: '${error}',
                icon: 'error',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#dc3545'
            });
        });
    </script>
    <c:remove var="error" scope="session"/>
</c:if>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Eventos para botones de desactivar
        document.querySelectorAll('.btn-desactivar').forEach(function(btn) {
            btn.addEventListener('click', function() {
                var id = this.getAttribute('data-id');
                var nombre = this.getAttribute('data-nombre');
                
                Swal.fire({
                    title: '¿Desactivar nivel?',
                    text: '¿Estás seguro de desactivar el nivel "' + nombre + '"?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#dc3545',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, desactivar',
                    cancelButtonText: 'Cancelar'
                }).then(function(result) {
                    if (result.isConfirmed) {
                        window.location.href = '${pageContext.request.contextPath}/niveles?action=eliminar&id=' + id;
                    }
                });
            });
        });
        
        // Eventos para botones de activar
        document.querySelectorAll('.btn-activar').forEach(function(btn) {
            btn.addEventListener('click', function() {
                var id = this.getAttribute('data-id');
                var nombre = this.getAttribute('data-nombre');
                
                Swal.fire({
                    title: '¿Activar nivel?',
                    text: '¿Estás seguro de activar el nivel "' + nombre + '"?',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#28a745',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, activar',
                    cancelButtonText: 'Cancelar'
                }).then(function(result) {
                    if (result.isConfirmed) {
                        window.location.href = '${pageContext.request.contextPath}/niveles?action=activar&id=' + id;
                    }
                });
            });
        });
        
        // Eventos para botones de eliminar permanentemente
        document.querySelectorAll('.btn-eliminar').forEach(function(btn) {
            btn.addEventListener('click', function() {
                var id = this.getAttribute('data-id');
                var nombre = this.getAttribute('data-nombre');
                
                Swal.fire({
                    title: '¿Eliminar permanentemente?',
                    html: '¿Estás seguro de <strong>eliminar permanentemente</strong> el nivel "' + nombre + '"?<br><br>' +
                          '<span class="text-danger"><i class="fas fa-exclamation-triangle"></i> Esta acción no se puede deshacer</span>',
                    icon: 'error',
                    showCancelButton: true,
                    confirmButtonColor: '#dc3545',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, eliminar',
                    cancelButtonText: 'Cancelar'
                }).then(function(result) {
                    if (result.isConfirmed) {
                        window.location.href = '${pageContext.request.contextPath}/niveles?action=delete&id=' + id;
                    }
                });
            });
        });
    });
</script>
</body>
</html>
