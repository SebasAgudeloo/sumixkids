<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
            <!DOCTYPE html>
            <html lang="es">

            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <title>Gestión de Usuarios · SumixKids</title>
                <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
                    crossorigin="anonymous">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"
                    crossorigin="anonymous">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
            </head>

            <body class="bg-gradient-primary d-flex flex-column min-vh-100">

                <!-- Navbar principal -->
                <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
                    <div class="container">
                        <a class="navbar-brand fw-bold d-flex align-items-center"
                            href="${pageContext.request.contextPath}/bienvenida">
                            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids"
                                style="height: 36px; width: auto; margin-right: 8px;" />
                            SumixKids
                        </a>
                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
                            aria-label="Toggle navigation">
                            <span class="navbar-toggler-icon"></span>
                        </button>
                        <div class="collapse navbar-collapse" id="navbarNav">
                            <ul class="navbar-nav me-auto">
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/bienvenida">
                                        🏠 Dashboard
                                    </a>
                                </li>
                                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 1}">
                                    <li class="nav-item">
                                        <a class="nav-link active" href="${pageContext.request.contextPath}/usuarios">
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
                            </ul>
                            <ul class="navbar-nav">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle d-flex align-items-center rounded-pill px-3"
                                        href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
                                        aria-expanded="false">
                                        <i class="fas fa-crown me-2"></i>
                                        <span class="fw-semibold">${sessionScope.usuario.nombres}</span>
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0">
                                        <li class="dropdown-item">👑 Administrador</li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil"><i
                                                    class="fas fa-user me-2"></i>Mi Perfil</a></li>
                                        <li><a class="dropdown-item"
                                                href="${pageContext.request.contextPath}/configuracion">
                                                <i class="fas fa-cog me-2"></i>Configuración</a></li>
                                        <li><a class="dropdown-item"
                                                href="${pageContext.request.contextPath}/dispositivos_reconocidos"><i
                                                    class="fa-solid fa-shield-halved me-2"></i>Dispositivos
                                                Reconocidos</a></li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><i
                                                    class="fas fa-sign-out-alt me-2 text-danger"></i>Cerrar Sesión</a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <main class="flex-fill py-4">
                    <div class="container">
                        <!-- Header de la página -->
                        <div class="row mb-4">
                            <div class="col-12">
                                <div class="page-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h1 class="display-6 fw-bold text-white mb-2">👥 Gestión de Usuarios</h1>
                                            <p class="lead text-white mb-0">Administra todos los usuarios del sistema
                                            </p>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/admin_crear_usuario"
                                            class="btn btn-outline-light btn-lg shadow-sm">
                                            <i class="fas fa-plus-circle me-2"></i>Crear Usuario
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Tarjetas de estadísticas -->
                        <div class="row g-4 mb-4">
                            <div class="col-lg-3 col-md-6">
                                <div class="card stat-card admin h-100 border-0 shadow-lg">
                                    <div class="card-body text-center">
                                        <div class="stat-icon">
                                            <i class="fas fa-crown"></i>
                                        </div>
                                        <h3 class="mb-1">${totalAdmins}</h3>
                                        <p class="mb-0 fw-semibold">👑 Administradores</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6">
                                <div class="card stat-card docente h-100 border-0 shadow-lg">
                                    <div class="card-body text-center">
                                        <div class="stat-icon">
                                            <i class="fas fa-chalkboard-teacher"></i>
                                        </div>
                                        <h3 class="mb-1">${totalDocentes}</h3>
                                        <p class="mb-0 fw-semibold">👨‍🏫 Docentes</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6">
                                <div class="card stat-card estudiante h-100 border-0 shadow-lg">
                                    <div class="card-body text-center">
                                        <div class="stat-icon">
                                            <i class="fas fa-graduation-cap"></i>
                                        </div>
                                        <h3 class="mb-1">${totalEstudiantes}</h3>
                                        <p class="mb-0 fw-semibold">🎓 Estudiantes</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6">
                                <div class="card stat-card padre h-100 border-0 shadow-lg">
                                    <div class="card-body text-center">
                                        <div class="stat-icon">
                                            <i class="fas fa-users"></i>
                                        </div>
                                        <h3 class="mb-1">${totalPadres}</h3>
                                        <p class="mb-0 fw-semibold">👨‍👩‍👧‍👦 Padres</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Panel de búsqueda y filtros -->
                        <div class="row mb-4">
                            <div class="col-12">
                                <div class="filter-panel">
                                    <h5 class="mb-3">
                                        🔍 Búsqueda y Filtros
                                    </h5>
                                    <div class="row g-3">
                                        <div class="col-md-4">
                                            <label for="buscarUsuario" class="form-label">
                                                <i class="fas fa-user me-1"></i>Buscar usuario
                                            </label>
                                            <input type="text" class="form-control" id="buscarUsuario"
                                                placeholder="Nombre, apellido o username..."
                                                onkeyup="filtrarUsuarios()">
                                        </div>
                                        <div class="col-md-3">
                                            <label for="filtroRol" class="form-label">
                                                <i class="fas fa-filter me-1"></i>Filtrar por rol
                                            </label>
                                            <select class="form-select" id="filtroRol" onchange="filtrarUsuarios()">
                                                <option value="">Todos los roles</option>
                                                <option value="1">👑 Administrador</option>
                                                <option value="2">👨‍🏫 Docente</option>
                                                <option value="3">🎓 Estudiante</option>
                                                <option value="4">👨‍👩‍👧‍👦 Padre</option>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <label for="filtroEstado" class="form-label">
                                                <i class="fas fa-toggle-on me-1"></i>Estado
                                            </label>
                                            <select class="form-select" id="filtroEstado" onchange="filtrarUsuarios()">
                                                <option value="">Todos</option>
                                                <option value="activo">✅ Activos</option>
                                                <option value="inactivo">❌ Inactivos</option>
                                            </select>
                                        </div>
                                        <div class="col-md-2">
                                            <label class="form-label">&nbsp;</label>
                                            <button type="button" class="btn btn-outline-secondary w-100"
                                                onclick="limpiarFiltros()">
                                                🧹 Limpiar
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Tabla de usuarios -->
                        <div class="row">
                            <div class="col-12">
                                <div class="card border-0 shadow-lg">
                                    <div
                                        class="card-header bg-gradient-primary d-flex justify-content-between align-items-center">
                                        <h5 class="mb-0">
                                            📋 Lista de Usuarios
                                        </h5>
                                        <span class="badge bg-white text-dark fs-6" id="contadorUsuarios">
                                            ${totalUsuarios} usuarios registrados
                                        </span>
                                    </div>
                                    <div class="card-body p-0">
                                        <div class="table-responsive">
                                            <table class="table table-hover mb-0" id="tablaUsuarios">
                                                <thead class="table-dark">
                                                    <tr>
                                                        <th scope="col">🔑 ID</th>
                                                        <th scope="col">👤 Usuario</th>
                                                        <th scope="col">📋 Información</th>
                                                        <th scope="col">📧 Email</th>
                                                        <th scope="col">🏷️ Rol</th>
                                                        <th scope="col">🎓 Grado</th>
                                                        <th scope="col">✅ Estado</th>
                                                        <th scope="col" class="text-center">🔧 Acciones</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:choose>
                                                        <c:when test="${not empty usuarios}">
                                                            <c:forEach var="usuario" items="${usuarios}"
                                                                varStatus="status">
                                                                <tr class="usuario-row" data-rol="${usuario.rolId}"
                                                                    data-estado="${!usuario.bloqueado}">
                                                                    <td>
                                                                        <span
                                                                            class="badge bg-secondary">${usuario.id}</span>
                                                                    </td>
                                                                    <td>
                                                                        <div class="d-flex align-items-center">
                                                                            <div class="me-2">
                                                                                <c:choose>
                                                                                    <c:when
                                                                                        test="${usuario.rolId == 1}"><i
                                                                                            class="fas fa-crown text-warning"></i>
                                                                                    </c:when>
                                                                                    <c:when
                                                                                        test="${usuario.rolId == 2}"><i
                                                                                            class="fas fa-chalkboard-teacher text-primary"></i>
                                                                                    </c:when>
                                                                                    <c:when
                                                                                        test="${usuario.rolId == 3}"><i
                                                                                            class="fas fa-graduation-cap text-success"></i>
                                                                                    </c:when>
                                                                                    <c:when
                                                                                        test="${usuario.rolId == 4}"><i
                                                                                            class="fas fa-users text-info"></i>
                                                                                    </c:when>
                                                                                    <c:otherwise><i
                                                                                            class="fas fa-user text-secondary"></i>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </div>
                                                                            <div>
                                                                                <div class="fw-bold">${usuario.username}
                                                                                </div>
                                                                                <small class="text-muted">Usuario
                                                                                    #${usuario.id}</small>
                                                                            </div>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <div>
                                                                            <div class="fw-semibold">${usuario.nombres}
                                                                            </div>
                                                                            <div class="text-muted">${usuario.apellidos}
                                                                            </div>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <span
                                                                            class="font-monospace">${usuario.email}</span>
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${usuario.rolId == 1}">
                                                                                <span class="badge bg-warning">👑
                                                                                    Admin</span>
                                                                            </c:when>
                                                                            <c:when test="${usuario.rolId == 2}">
                                                                                <span class="badge bg-primary">👨‍🏫
                                                                                    Docente</span>
                                                                            </c:when>
                                                                            <c:when test="${usuario.rolId == 3}">
                                                                                <span class="badge bg-success">🎓
                                                                                    Estudiante</span>
                                                                            </c:when>
                                                                            <c:when test="${usuario.rolId == 4}">
                                                                                <span class="badge"
                                                                                    style="background-color: #8b5cf6; color: white;">👨‍👩‍👧‍👦
                                                                                    Padre</span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="badge bg-secondary">❓
                                                                                    Desconocido</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when
                                                                                test="${usuario.rolId == 3 and not empty usuario.grado}">
                                                                                <span class="badge bg-info">🎓
                                                                                    ${usuario.grado}</span>
                                                                            </c:when>
                                                                            <c:when test="${usuario.rolId == 3}">
                                                                                <span class="badge bg-secondary">Sin
                                                                                    grado</span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span
                                                                                    class="text-muted small">N/A</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${!usuario.bloqueado}">
                                                                                <span class="badge bg-success">✅
                                                                                    Activo</span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="badge bg-danger">❌
                                                                                    Bloqueado</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <div class="btn-group" role="group">
                                                                            <button type="button"
                                                                                class="btn btn-sm btn-outline-primary"
                                                                                data-bs-toggle="tooltip"
                                                                                title="Editar usuario"
                                                                                onclick="editarUsuario('${usuario.id}')">
                                                                                <i class="fas fa-edit"></i>
                                                                            </button>
                                                                            <button type="button"
                                                                                class="btn btn-sm btn-outline-warning"
                                                                                data-bs-toggle="tooltip"
                                                                                title="Cambiar estado"
                                                                                onclick="cambiarEstado('${usuario.id}', '${!usuario.bloqueado}')">
                                                                                <c:choose>
                                                                                    <c:when
                                                                                        test="${!usuario.bloqueado}">
                                                                                        <i class="fas fa-lock"></i>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <i class="fas fa-unlock"></i>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </button>
                                                                            <button type="button"
                                                                                class="btn btn-sm btn-outline-danger"
                                                                                data-bs-toggle="tooltip"
                                                                                title="Eliminar usuario"
                                                                                data-user-id="${usuario.id}"
                                                                                onclick="eliminarUsuarioSeguro(this)">
                                                                                <i class="fas fa-trash"></i>
                                                                            </button>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tr>
                                                                <td colspan="8" class="text-center py-5">
                                                                    <div class="text-muted">
                                                                        <i class="fas fa-users fa-3x mb-3"></i>
                                                                        <h5>👥 No hay usuarios</h5>
                                                                        <p>No se encontraron usuarios que coincidan con
                                                                            los
                                                                            filtros aplicados.</p>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>

                <%-- Footer --%>
                    <footer class="py-4 mt-auto" style="background: rgba(0,0,0,0.8); backdrop-filter: blur(10px);">
                        <div class="container">
                            <div class="row align-items-center">
                                <div class="col-md-6 text-center text-md-start">
                                    <div
                                        class="d-flex align-items-center justify-content-center justify-content-md-start mb-2 mb-md-0">
                                        <img src="${pageContext.request.contextPath}/images/sumixkids.png"
                                            alt="SumixKids" style="height: 24px; margin-right: 8px;">
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

                    <!-- Scripts -->
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                        crossorigin="anonymous"></script>
                    <!-- SweetAlert2 -->
                    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                    <script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>
                    <script>
                        document.getElementById('year').textContent = new Date().getFullYear();

                        // Funciones de filtrado
                        function filtrarUsuarios() {
                            const busqueda = document.getElementById('buscarUsuario').value.toLowerCase();
                            const filtroRol = document.getElementById('filtroRol').value;
                            const filtroEstado = document.getElementById('filtroEstado').value;
                            const filas = document.querySelectorAll('#tablaUsuarios tbody .usuario-row');
                            let contador = 0;

                            filas.forEach(fila => {
                                const texto = fila.textContent.toLowerCase();
                                const rol = fila.getAttribute('data-rol');
                                const activo = fila.getAttribute('data-estado') === 'true';

                                let mostrar = true;

                                // Filtro de búsqueda
                                if (busqueda && !texto.includes(busqueda)) {
                                    mostrar = false;
                                }

                                // Filtro de rol
                                if (filtroRol && rol !== filtroRol) {
                                    mostrar = false;
                                }

                                // Filtro de estado
                                if (filtroEstado === 'activo' && !activo) {
                                    mostrar = false;
                                } else if (filtroEstado === 'inactivo' && activo) {
                                    mostrar = false;
                                }

                                fila.style.display = mostrar ? '' : 'none';
                                if (mostrar) contador++;
                            });

                            // Actualizar contador
                            document.getElementById('contadorUsuarios').textContent = contador + ' usuarios mostrados';
                        }

                        function limpiarFiltros() {
                            document.getElementById('buscarUsuario').value = '';
                            document.getElementById('filtroRol').value = '';
                            document.getElementById('filtroEstado').value = '';
                            filtrarUsuarios();
                            document.getElementById('contadorUsuarios').textContent = '${totalUsuarios} usuarios registrados';
                        }

                        function editarUsuario(id) {
                            window.location.href = '${pageContext.request.contextPath}/editar_usuario?id=' + id;
                        }

                        function cambiarEstado(id, bloquear) {
                            const accion = bloquear === 'true' ? 'bloquear' : 'desbloquear';
                            const titulo = bloquear === 'true' ? '🚫 Bloquear Usuario' : '✅ Desbloquear Usuario';
                            const texto = bloquear === 'true'
                                ? 'Este usuario no podrá iniciar sesión hasta que sea desbloqueado.'
                                : 'Este usuario podrá iniciar sesión nuevamente.';
                            const icono = bloquear === 'true' ? 'warning' : 'question';
                            const confirmButtonColor = bloquear === 'true' ? '#dc3545' : '#198754';
                            const confirmButtonText = bloquear === 'true' ? 'Sí, bloquear' : 'Sí, desbloquear';

                            Swal.fire({
                                title: titulo,
                                text: texto,
                                icon: icono,
                                showCancelButton: true,
                                confirmButtonColor: confirmButtonColor,
                                cancelButtonColor: '#6c757d',
                                confirmButtonText: confirmButtonText,
                                cancelButtonText: 'Cancelar',
                                reverseButtons: true
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    // Crear formulario y enviarlo por POST
                                    const form = document.createElement('form');
                                    form.method = 'POST';
                                    form.action = '${pageContext.request.contextPath}/cambiar_estado';

                                    const inputId = document.createElement('input');
                                    inputId.type = 'hidden';
                                    inputId.name = 'id';
                                    inputId.value = id;
                                    form.appendChild(inputId);

                                    const inputBloquear = document.createElement('input');
                                    inputBloquear.type = 'hidden';
                                    inputBloquear.name = 'bloquear';
                                    inputBloquear.value = bloquear;
                                    form.appendChild(inputBloquear);

                                    document.body.appendChild(form);
                                    form.submit();
                                }
                            });
                        }

                        function eliminarUsuarioSeguro(button) {
                            const id = button.getAttribute('data-user-id');

                            Swal.fire({
                                title: '🗑️ Eliminar Usuario',
                                html: '¿Está seguro que desea eliminar este usuario?<br><br><small class="text-muted">Esta acción no se puede deshacer</small>',
                                icon: 'warning',
                                showCancelButton: true,
                                confirmButtonColor: '#dc3545',
                                cancelButtonColor: '#6c757d',
                                confirmButtonText: 'Sí, eliminar',
                                cancelButtonText: 'Cancelar',
                                reverseButtons: true
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    // Redirigir al servlet de eliminación (NO directo a JSP)
                                    window.location.href = '${pageContext.request.contextPath}/eliminar_usuario?id=' + id;
                                }
                            });
                        }

                        // Mantener función original para compatibilidad
                        function eliminarUsuario(id, username) {
                            eliminarUsuarioSeguro({ getAttribute: () => id });
                        }

                        // Inicializar tooltips
                        document.addEventListener('DOMContentLoaded', function () {
                            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
                            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                                return new bootstrap.Tooltip(tooltipTriggerEl);
                            });
                        });
                    </script>

            </body>

            </html>
