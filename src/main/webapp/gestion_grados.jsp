<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Gestión de Grados · SumixKids</title>
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
        <div class="row justify-content-center mb-4">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center">
                    <h1 class="display-6 fw-bold text-white-contrast drop-shadow">
                        <i class="fas fa-graduation-cap me-2"></i>Gestión de Grados Escolares
                    </h1>
                    <a href="${pageContext.request.contextPath}/grados?action=nuevo" class="btn btn-light shadow-sm">
                        <i class="fas fa-plus me-2"></i>Nuevo Grado
                    </a>
                </div>
            </div>
        </div>



        <div class="card shadow-lg border-0 mb-4">
            <div class="card-body">
                <form method="get" action="${pageContext.request.contextPath}/grados" class="row g-3">
                    <div class="col-md-4">
                        <label for="filtro" class="form-label fw-semibold">Estado:</label>
                        <select class="form-select" id="filtro" name="filtro" onchange="this.form.submit()">
                            <option value="">Todos los grados</option>
                            <option value="activos" ${filtroActual == 'activos' ? 'selected' : ''}>Solo activos</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label for="nivel" class="form-label fw-semibold">Nivel Educativo:</label>
                        <select class="form-select" id="nivel" name="nivel" onchange="this.form.submit()">
                            <option value="">Todos los niveles</option>
                            <option value="PREESCOLAR" ${nivelActual == 'PREESCOLAR' ? 'selected' : ''}>Preescolar</option>
                            <option value="PRIMARIA" ${nivelActual == 'PRIMARIA' ? 'selected' : ''}>Primaria</option>
                            <option value="SECUNDARIA" ${nivelActual == 'SECUNDARIA' ? 'selected' : ''}>Secundaria</option>
                        </select>
                    </div>
                    <div class="col-md-4 d-flex align-items-end">
                        <a href="${pageContext.request.contextPath}/grados" class="btn btn-outline-secondary">
                            <i class="fas fa-redo me-2"></i>Limpiar
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <div class="card shadow-lg border-0">
            <div class="card-body">
                <c:choose>
                    <c:when test="${empty grados}">
                        <div class="text-center py-5">
                            <i class="fas fa-graduation-cap fa-4x text-muted mb-3"></i>
                            <h4 class="text-muted">No hay grados registrados</h4>
                            <p class="text-muted">Comienza creando tu primer grado escolar</p>
                            <a href="${pageContext.request.contextPath}/grados?action=nuevo" class="btn btn-primary">
                                <i class="fas fa-plus me-2"></i>Crear Primer Grado
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>ID</th>
                                        <th>Grado</th>
                                        <th>Grupo</th>
                                        <th>Nivel</th>
                                        <th>Capacidad</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${grados}" var="grado">
                                        <tr>
                                            <td><strong>#${grado.idGrado}</strong></td>
                                            <td><i class="fas fa-book me-2 text-primary"></i>${grado.nombreGrado}</td>
                                            <td><span class="badge bg-info">${grado.nombreGrupo}</span></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${grado.nivelEducativo == 'PREESCOLAR'}">
                                                        <span class="badge bg-warning">Preescolar</span>
                                                    </c:when>
                                                    <c:when test="${grado.nivelEducativo == 'PRIMARIA'}">
                                                        <span class="badge bg-primary">Primaria</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-success">Secundaria</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${grado.capacidadMaxima > 0 ? grado.capacidadMaxima : 'Sin límite'}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${grado.activo}">
                                                        <span class="badge bg-success"><i class="fas fa-check-circle"></i> Activo</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger"><i class="fas fa-times-circle"></i> Inactivo</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="btn-group btn-group-sm">
                                                    <a href="${pageContext.request.contextPath}/grados?action=editar&id=${grado.idGrado}" 
                                                       class="btn btn-outline-primary" title="Editar">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <c:choose>
                                                        <c:when test="${grado.activo}">
                                                            <button class="btn btn-outline-danger btn-desactivar" 
                                                                    data-id="${grado.idGrado}" 
                                                                    data-nombre="${grado.nombreGrado}"
                                                                    title="Desactivar">
                                                                <i class="fas fa-ban"></i>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button class="btn btn-outline-success btn-activar" 
                                                                    data-id="${grado.idGrado}" 
                                                                    data-nombre="${grado.nombreGrado}"
                                                                    title="Activar">
                                                                <i class="fas fa-check"></i>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <button class="btn btn-outline-dark btn-eliminar" 
                                                            data-id="${grado.idGrado}" 
                                                            data-nombre="${grado.nombreGrado}"
                                                            title="Eliminar permanentemente">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <p class="text-muted mt-3">
                            <i class="fas fa-info-circle me-2"></i>
                            Total: <strong>${grados.size()}</strong> grados
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<c:if test="${not empty sessionScope.mensaje}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            Swal.fire({
                title: '¡Éxito!',
                text: '${sessionScope.mensaje}',
                icon: 'success',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#28a745'
            });
        });
    </script>
    <c:remove var="mensaje" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.error}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            Swal.fire({
                title: '¡Oops!',
                text: '${sessionScope.error}',
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
                    title: '¿Desactivar este grado?',
                    text: '¿Estás seguro de desactivar el grado "' + nombre + '"?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#dc3545',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, desactivar',
                    cancelButtonText: 'Cancelar'
                }).then(function(result) {
                    if (result.isConfirmed) {
                        window.location.href = '${pageContext.request.contextPath}/grados?action=eliminar&id=' + id;
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
                    title: '¿Activar este grado?',
                    text: '¿Estás seguro de activar el grado "' + nombre + '"?',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#28a745',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, activar',
                    cancelButtonText: 'Cancelar'
                }).then(function(result) {
                    if (result.isConfirmed) {
                        window.location.href = '${pageContext.request.contextPath}/grados?action=activar&id=' + id;
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
                    html: '¿Estás seguro de <strong>eliminar permanentemente</strong> el grado "' + nombre + '"?<br><br>' +
                          '<span class="text-danger"><i class="fas fa-exclamation-triangle"></i> Esta acción no se puede deshacer</span>',
                    icon: 'error',
                    showCancelButton: true,
                    confirmButtonColor: '#dc3545',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Sí, eliminar',
                    cancelButtonText: 'Cancelar'
                }).then(function(result) {
                    if (result.isConfirmed) {
                        window.location.href = '${pageContext.request.contextPath}/grados?action=delete&id=' + id;
                    }
                });
            });
        });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
