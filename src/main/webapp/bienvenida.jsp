<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Página mostrada después de iniciar sesión correctamente --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Bienvenido · SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<%-- Barra de navegación con opción de salir --%>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" style="height: 36px; width: auto; margin-right: 8px;"/>
            SumixKids
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarsExample" aria-controls="navbarsExample" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarsExample">
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/logout">Cerrar sesión</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="flex-fill py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8 text-center">
                <h1 class="display-6 fw-bold text-white mb-3">¡Bienvenido!</h1>
                <p class="lead text-white-50 mb-4">Has iniciado sesión correctamente.</p>
                <!-- Botón para eliminar usuario actual (solo para pruebas o admins) -->
                <!-- Si es admin, puede ver y eliminar cualquier usuario -->
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 1}">
                    <h2 class="text-white mt-5">Gestión de usuarios</h2>
                    <!-- Formulario de búsqueda y filtro de rol -->
                    <form method="get" class="row g-3 mb-3" style="max-width:800px; margin:auto;">
                        <div class="col-md-5">
                            <input type="text" name="busqueda" class="form-control" placeholder="Buscar por usuario, nombre, correo o apellidos" value="${busqueda != null ? busqueda : ''}" />
                        </div>
                        <div class="col-md-4">
                            <select name="rol" class="form-select">
                                <option value="todos" ${rolSeleccionado == null ? 'selected' : ''}>Todos los roles</option>
                                <option value="1" ${rolSeleccionado == 1 ? 'selected' : ''}>Admin</option>
                                <option value="2" ${rolSeleccionado == 2 ? 'selected' : ''}>Docente</option>
                                <option value="3" ${rolSeleccionado == 3 ? 'selected' : ''}>Estudiante</option>
                                <option value="4" ${rolSeleccionado == 4 ? 'selected' : ''}>Padre</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <button type="submit" class="btn btn-primary w-100">Buscar / Filtrar</button>
                        </div>
                    </form>
                    <table class="table table-striped table-bordered bg-white mt-3">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Nombre</th>
                                <th>Correo</th>
                                <th>Rol</th>
                                <th>Cambiar rol</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${usuarios}">
                                <tr>
                                    <td>${u.id}</td>
                                    <td>${u.username}</td>
                                    <td>${u.nombres} ${u.apellidos}</td>
                                    <td>${u.email}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.rolId == 1}">Admin</c:when>
                                            <c:when test="${u.rolId == 2}">Docente</c:when>
                                            <c:when test="${u.rolId == 3}">Estudiante</c:when>
                                            <c:when test="${u.rolId == 4}">Padre</c:when>
                                            <c:otherwise>Desconocido</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/cambiarRolUsuario" style="display:inline;">
                                            <input type="hidden" name="userId" value="${u.id}" />
                                            <select name="nuevoRol" class="form-select form-select-sm d-inline w-auto" style="min-width:110px;">
                                                <option value="1" ${u.rolId == 1 ? 'selected' : ''}>Admin</option>
                                                <option value="2" ${u.rolId == 2 ? 'selected' : ''}>Docente</option>
                                                <option value="3" ${u.rolId == 3 ? 'selected' : ''}>Estudiante</option>
                                                <option value="4" ${u.rolId == 4 ? 'selected' : ''}>Padre</option>
                                            </select>
                                            <button type="submit" class="btn btn-warning btn-sm ms-1">Cambiar</button>
                                        </form>
                                    </td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/eliminarUsuario" style="display:inline;">
                                            <input type="hidden" name="userId" value="${u.id}" />
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Seguro que deseas eliminar al usuario ${u.username}? Esta acción es irreversible.');">Eliminar</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                    <!-- Controles de paginación -->
                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Paginación de usuarios">
                            <ul class="pagination justify-content-center mt-3">
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link" href="?busqueda=${busqueda}&rol=${rolSeleccionado}&page=${currentPage - 1}">Anterior</a>
                                    </li>
                                </c:if>
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="?busqueda=${busqueda}&rol=${rolSeleccionado}&page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                                <c:if test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link" href="?busqueda=${busqueda}&rol=${rolSeleccionado}&page=${currentPage + 1}">Siguiente</a>
                                    </li>
                                </c:if>
                            </ul>
                        </nav>
                    </c:if>
                        </tbody>
                    </table>
                </c:if>
                <!-- Si es usuario normal, solo puede eliminarse a sí mismo -->
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId != 1}">
                    <form id="formEliminarUsuarioNormal" method="post" action="${pageContext.request.contextPath}/eliminarUsuario" style="display:inline;">
                        <input type="hidden" name="userId" value="${sessionScope.usuario.id}" />
                        <button type="submit" class="btn btn-danger px-4" onclick="return confirm('¿Seguro que deseas eliminar tu cuenta? Esta acción es irreversible.');">Eliminar mi cuenta</button>
                    </form>
                </c:if>
                <c:if test="${not empty error || not empty param.error}">
                    <div id="alertaError" class="alert alert-danger mt-3 shadow rounded-3 p-3" style="font-size:1.1em;max-width:500px;margin:auto;">
                        <i class="bi bi-x-circle-fill me-2"></i>
                        <strong>Error:</strong> ${not empty error ? error : param.error}
                    </div>
                </c:if>
                <c:if test="${not empty mensaje || not empty param.mensaje}">
                    <div id="alertaExito" class="alert alert-success mt-3 shadow rounded-3 p-3" style="font-size:1.1em;max-width:500px;margin:auto;">
                        <i class="bi bi-check-circle-fill me-2"></i>
                        <strong>¡Éxito!</strong> ${not empty mensaje ? mensaje : param.mensaje}
                    </div>
                </c:if>
                <!-- Iconos Bootstrap -->
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
                <a class="btn btn-outline-light px-4" href="${pageContext.request.contextPath}/logout">Cerrar sesión</a>
            </div>
        </div>
    </div>
</main>

<footer class="py-3 bg-dark mt-auto text-center text-white-50 small">
    © <span id="year"></span> SumixKids
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script>
document.getElementById('year').textContent = new Date().getFullYear();
// Ocultar notificaciones después de 3 segundos
setTimeout(function() {
    var alertaExito = document.getElementById('alertaExito');
    if (alertaExito) alertaExito.style.display = 'none';
    var alertaError = document.getElementById('alertaError');
    if (alertaError) alertaError.style.display = 'none';
}, 3000);
</script>
<script src="${pageContext.request.contextPath}/js/session-timeout.js"></script>
</body>
</html>
