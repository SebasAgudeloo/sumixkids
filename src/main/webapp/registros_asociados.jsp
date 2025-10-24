<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registros Asociados | SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">

<!-- Navbar principal -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/bienvenida">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" class="logo-navbar"/>
            SumixKids
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/bienvenida">
                        <i class="bi bi-house"></i> Inicio
                    </a>
                </li>
                <li class="nav-item">
                    <c:choose>
                        <c:when test="${esAutoEliminacion}">
                            <a class="nav-link" href="${pageContext.request.contextPath}/eliminar_usuario?tipo=usuario">
                                <i class="bi bi-arrow-left"></i> Volver a Eliminar mi Cuenta
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="nav-link" href="${pageContext.request.contextPath}/eliminar_usuario?id=${usuario.id}">
                                <i class="bi bi-arrow-left"></i> Volver a Eliminar Usuario
                            </a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Contenido principal -->
<div class="container my-5 flex-grow-1">
    <div class="row justify-content-center">
        <div class="col-lg-10">
            <!-- Header -->
            <div class="card shadow-lg border-0 mb-4" style="background: rgba(255, 255, 255, 0.95); backdrop-filter: blur(10px);">
                <div class="card-body text-center">
                    <i class="bi bi-database-exclamation text-warning" style="font-size: 3rem;"></i>
                    <h2 class="card-title text-primary mt-3">
                        <c:choose>
                            <c:when test="${esAutoEliminacion}">Mis Registros Asociados</c:when>
                            <c:otherwise>Registros Asociados</c:otherwise>
                        </c:choose>
                    </h2>
                    
                    <c:if test="${not empty usuario}">
                        <p class="text-muted">
                            <strong>Usuario:</strong> ${usuario.nombres} ${usuario.apellidos} 
                            <span class="badge bg-secondary ms-2">ID: ${usuario.id}</span>
                            <c:if test="${not empty usuario.email}">
                                <br><small class="text-muted">${usuario.email}</small>
                            </c:if>
                        </p>
                    </c:if>
                    
                    <c:if test="${empty usuario}">
                        <p class="text-muted">Usuario ID: <strong>${param.userId}</strong></p>
                    </c:if>
                    
                    <div class="alert alert-warning mt-3">
                        <i class="bi bi-exclamation-triangle"></i>
                        <c:choose>
                            <c:when test="${esAutoEliminacion}">
                                Para eliminar tu cuenta, puedes revisar y limpiar tus registros asociados mostrados a continuación. 
                                <br><small>Nota: Al eliminar tu cuenta, estos registros también serán eliminados automáticamente.</small>
                            </c:when>
                            <c:otherwise>
                                Para eliminar este usuario, primero debe revisar y eliminar los registros asociados mostrados a continuación.
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Mensajes de estado -->
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="bi bi-check-circle"></i> ${mensaje}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="bi bi-exclamation-triangle"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Registros de Auditoría -->
            <c:if test="${not empty registrosAuditoria}">
                <div class="card shadow mb-4" style="background: rgba(255, 255, 255, 0.95);">
                    <div class="card-header bg-warning text-dark">
                        <h5 class="mb-0">
                            <i class="bi bi-journal-text"></i> 
                            Registros de Auditoría (${fn:length(registrosAuditoria)})
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Fecha</th>
                                        <th>Acción</th>
                                        <th>Tabla Afectada</th>
                                        <th>IP</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="registro" items="${registrosAuditoria}">
                                        <tr>
                                            <td class="font-monospace">${registro.id}</td>
                                            <td>
                                                ${util:formatearFecha(registro.fechaHora)}
                                            </td>
                                            <td>
                                                <span class="badge bg-info">${registro.accion}</span>
                                            </td>
                                            <td class="font-monospace">${registro.tablaAfectada}</td>
                                            <td class="font-monospace">${registro.ipUsuario}</td>
                                            <td>
                                                <button class="btn btn-danger btn-sm" 
                                                        onclick="eliminarRegistroAuditoria('${registro.id}')">
                                                    <i class="bi bi-trash"></i> Eliminar
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="mt-3">
                            <button class="btn btn-danger" onclick="eliminarTodosRegistrosAuditoria()">
                                <i class="bi bi-trash3"></i> 
                                <c:choose>
                                    <c:when test="${esAutoEliminacion}">Limpiar Todos Mis Registros de Auditoría</c:when>
                                    <c:otherwise>Eliminar Todos los Registros de Auditoría</c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Otros Registros Asociados -->
            <c:if test="${not empty otrosRegistros}">
                <div class="card shadow mb-4" style="background: rgba(255, 255, 255, 0.95);">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0">
                            <i class="bi bi-files"></i> 
                            Otros Registros Asociados (${fn:length(otrosRegistros)})
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Tipo</th>
                                        <th>ID</th>
                                        <th>Descripción</th>
                                        <th>Fecha</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="registro" items="${otrosRegistros}">
                                        <tr>
                                            <td>
                                                <span class="badge bg-secondary">${registro.tipo}</span>
                                            </td>
                                            <td class="font-monospace">${registro.id}</td>
                                            <td>${registro.descripcion}</td>
                                            <td>
                                                ${util:formatearFecha(registro.fecha)}
                                            </td>
                                            <td>
                                                <button class="btn btn-danger btn-sm" 
                                                        onclick="eliminarOtroRegistro('${registro.tipo}', '${registro.id}')">
                                                    <i class="bi bi-trash"></i> Eliminar
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Sin registros -->
            <c:if test="${empty registrosAuditoria and empty otrosRegistros}">
                <div class="card shadow" style="background: rgba(255, 255, 255, 0.95);">
                    <div class="card-body text-center py-5">
                        <i class="bi bi-check-circle-fill text-success" style="font-size: 4rem;"></i>
                        <h3 class="text-success mt-3">¡Sin registros asociados!</h3>
                        <p class="text-muted mb-4">El usuario no tiene registros asociados que impidan su eliminación.</p>
                        <a href="${pageContext.request.contextPath}/eliminar_usuario?userId=${param.userId}" 
                           class="btn btn-success btn-lg">
                            <i class="bi bi-arrow-left"></i> Proceder con Eliminación
                        </a>
                    </div>
                </div>
            </c:if>

            <!-- Botones de acción -->
            <div class="row mt-4">
                <div class="col-md-6">
                    <a href="${pageContext.request.contextPath}/eliminar_usuario?userId=${param.userId}" 
                       class="btn btn-secondary w-100">
                        <i class="bi bi-arrow-left"></i> Volver a Eliminar Usuario
                    </a>
                </div>
                <div class="col-md-6">
                    <button class="btn btn-warning w-100" onclick="refrescarRegistros()">
                        <i class="bi bi-arrow-clockwise"></i> Refrescar Registros
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="py-4 mt-auto footer-glassmorphism">
    <div class="container text-center">
        <div class="text-white-contrast">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" 
                 alt="Logo SumixKids" class="logo-footer">
            <span>&copy; 2024 SumixKids. Todos los derechos reservados.</span>
        </div>
    </div>
</footer>

<!-- Sweet Alert 2 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>

<script>
// Eliminar registro de auditoría individual
function eliminarRegistroAuditoria(registroId) {
    <c:choose>
        <c:when test="${esAutoEliminacion}">
            const title = '¿Eliminar este registro de auditoría?';
            const confirmText = 'Sí, eliminar';
        </c:when>
        <c:otherwise>
            const title = '¿Eliminar registro de auditoría?';
            const confirmText = 'Sí, eliminar';
        </c:otherwise>
    </c:choose>
    
    Swal.fire({
        title: title,
        text: `ID: ${registroId}`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: confirmText,
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            // Crear formulario para enviar la eliminación
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/registros_asociados';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'eliminar_auditoria';
            
            const userIdInput = document.createElement('input');
            userIdInput.type = 'hidden';
            userIdInput.name = 'userId';
            userIdInput.value = '${usuario.id}';
            
            const registroIdInput = document.createElement('input');
            registroIdInput.type = 'hidden';
            registroIdInput.name = 'registroId';
            registroIdInput.value = registroId;
            
            const esAutoEliminacionInput = document.createElement('input');
            esAutoEliminacionInput.type = 'hidden';
            esAutoEliminacionInput.name = 'esAutoEliminacion';
            esAutoEliminacionInput.value = '${esAutoEliminacion}';
            
            form.appendChild(actionInput);
            form.appendChild(userIdInput);
            form.appendChild(registroIdInput);
            form.appendChild(esAutoEliminacionInput);
            document.body.appendChild(form);
            form.submit();
        }
    });
}

// Eliminar todos los registros de auditoría
function eliminarTodosRegistrosAuditoria() {
    <c:choose>
        <c:when test="${esAutoEliminacion}">
            const title = '¿Limpiar TODOS tus registros de auditoría?';
            const text = 'Esta acción eliminará todos tus registros de auditoría. Al eliminar tu cuenta, estos también se eliminarán automáticamente.';
            const confirmText = 'Sí, limpiar todos';
        </c:when>
        <c:otherwise>
            const title = '¿Eliminar TODOS los registros de auditoría?';
            const text = 'Esta acción eliminará todos los registros de auditoría del usuario';
            const confirmText = 'Sí, eliminar todos';
        </c:otherwise>
    </c:choose>
    
    Swal.fire({
        title: title,
        text: text,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: confirmText,
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/registros_asociados';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'eliminar_todos_auditoria';
            
            const userIdInput = document.createElement('input');
            userIdInput.type = 'hidden';
            userIdInput.name = 'userId';
            userIdInput.value = '${usuario.id}';
            
            const esAutoEliminacionInput = document.createElement('input');
            esAutoEliminacionInput.type = 'hidden';
            esAutoEliminacionInput.name = 'esAutoEliminacion';
            esAutoEliminacionInput.value = '${esAutoEliminacion}';
            
            form.appendChild(actionInput);
            form.appendChild(userIdInput);
            form.appendChild(esAutoEliminacionInput);
            document.body.appendChild(form);
            form.submit();
        }
    });
}

// Eliminar otro tipo de registro
function eliminarOtroRegistro(tipo, registroId) {
    Swal.fire({
        title: `¿Eliminar registro de ${tipo}?`,
        text: `ID: ${registroId}`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/registros_asociados';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'eliminar_otro';
            
            const userIdInput = document.createElement('input');
            userIdInput.type = 'hidden';
            userIdInput.name = 'userId';
            userIdInput.value = '${param.userId}';
            
            const tipoInput = document.createElement('input');
            tipoInput.type = 'hidden';
            tipoInput.name = 'tipo';
            tipoInput.value = tipo;
            
            const registroIdInput = document.createElement('input');
            registroIdInput.type = 'hidden';
            registroIdInput.name = 'registroId';
            registroIdInput.value = registroId;
            
            form.appendChild(actionInput);
            form.appendChild(userIdInput);
            form.appendChild(tipoInput);
            form.appendChild(registroIdInput);
            document.body.appendChild(form);
            form.submit();
        }
    });
}

// Refrescar registros
function refrescarRegistros() {
    window.location.reload();
}
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
