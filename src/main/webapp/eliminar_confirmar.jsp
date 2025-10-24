<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmar Eliminación | SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/bienvenida">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" style="height: 36px; width: auto; margin-right: 8px;"/>
            SumixKids
        </a>
    </div>
</nav>

        <div class="custom-container-wide">
        <div class="title-section">
            <i class="bi bi-exclamation-triangle-fill warning-icon"></i>
            <h2>
                <c:choose>
                    <c:when test="${esAutoEliminacion}">Confirmar Eliminación de tu Cuenta</c:when>
                    <c:otherwise>Confirmar Eliminación</c:otherwise>
                </c:choose>
            </h2>
            
            <c:if test="${not empty usuario}">
                <!-- Información detallada del usuario -->
                <div class="row justify-content-center mt-4">
                    <div class="col-12 col-md-11 col-lg-10 col-xl-9">
                        <div class="card border-info user-info-card">
                            <div class="card-header bg-info text-white">
                                <h5 class="mb-0">
                                    <i class="bi bi-person-circle"></i> 
                                    Información del Usuario a Eliminar
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-12">
                                        <div class="mb-3">
                                            <strong class="d-block text-primary mb-1">🔑 ID:</strong>
                                            <span class="badge bg-secondary fs-6">${usuario.id}</span>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <strong class="d-block text-primary mb-1">👤 Usuario:</strong>
                                            <code class="fs-6">${usuario.username}</code>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <strong class="d-block text-primary mb-1">📝 Nombre Completo:</strong>
                                            <span class="fs-6">${usuario.nombres} ${usuario.apellidos}</span>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <strong class="d-block text-primary mb-1">📧 Email:</strong>
                                            <a href="mailto:${usuario.email}" class="text-decoration-none fs-6">${usuario.email}</a>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <strong class="d-block text-primary mb-1">🏷️ Rol:</strong>
                                            <c:choose>
                                                <c:when test="${usuario.rolId == 1}">
                                                    <span class="badge bg-danger fs-6">👑 Administrador</span>
                                                </c:when>
                                                <c:when test="${usuario.rolId == 2}">
                                                    <span class="badge bg-primary fs-6">👩‍🏫 Profesor</span>
                                                </c:when>
                                                <c:when test="${usuario.rolId == 3}">
                                                    <span class="badge bg-success fs-6">👨‍🎓 Estudiante</span>
                                                </c:when>
                                                <c:when test="${usuario.rolId == 4}">
                                                    <span class="badge bg-info fs-6">👨‍👩‍👧‍👦 Familiar</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary fs-6">❓ Desconocido</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        
                                        <c:if test="${not empty usuario.grado}">
                                            <div class="mb-3">
                                                <strong class="d-block text-primary mb-1">🎓 Grado:</strong>
                                                <span class="badge bg-light text-dark fs-6">${usuario.grado}</span>
                                            </div>
                                        </c:if>
                                        
                                        <div class="mb-3">
                                            <strong class="d-block text-primary mb-1">📅 Fecha Registro:</strong>
                                            <small class="text-muted fs-6">
                                                ${util:formatearFecha(usuario.fechaRegistro)}
                                            </small>
                                        </div>
                                        
                                        <c:if test="${not empty usuario.ultimaConexion}">
                                            <div class="mb-3">
                                                <strong class="d-block text-primary mb-1">🕒 Última Conexión:</strong>
                                                <small class="text-muted fs-6">
                                                    ${util:formatearFecha(usuario.ultimaConexion)}
                                                </small>
                                            </div>
                                        </c:if>
                                        
                                        <div class="mb-3">
                                            <strong class="d-block text-primary mb-1">📊 Estado:</strong>
                                            <c:choose>
                                                <c:when test="${usuario.bloqueado}">
                                                    <span class="badge bg-danger fs-6">🔒 Bloqueado</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-success fs-6">✅ Activo</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        
                                        <c:if test="${not empty usuario.autenticacion2fa}">
                                            <div class="mb-3">
                                                <strong class="d-block text-primary mb-1">🔐 Autenticación 2FA:</strong>
                                                <span class="badge bg-warning text-dark fs-6">🔐 Habilitado</span>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${empty usuario}">
                <p class="text-muted">Usuario ID: <strong>${userId}</strong></p>
            </c:if>
        </div>
        
        <c:if test="${not empty error}">
            <div class="error-message">
                <i class="bi bi-exclamation-circle-fill"></i>
                ${error}
            </div>
        </c:if>

        <form id="deleteForm" method="POST" action="${pageContext.request.contextPath}/eliminar_usuario" onsubmit="return confirmarEliminacion(event)">
            <input type="hidden" name="userId" value="${userId}">
            <input type="hidden" name="esAutoEliminacion" value="${esAutoEliminacion}">
            
            <div class="form-group">
                <label class="form-label">Para confirmar, escriba "ELIMINAR":</label>
                <input type="text" name="confirmacion" class="form-control" required 
                       placeholder="Escriba ELIMINAR" autocomplete="off">
            </div>

            <div class="form-group">
                <c:choose>
                    <c:when test="${esAutoEliminacion}">
                        <label class="form-label">Tu contraseña actual:</label>
                        <input type="password" name="adminPassword" class="form-control" required
                               placeholder="Ingrese su contraseña actual para confirmar">
                    </c:when>
                    <c:otherwise>
                        <label class="form-label">Contraseña de administrador:</label>
                        <input type="password" name="adminPassword" class="form-control" required
                               placeholder="Ingrese su contraseña de administrador">
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="d-grid gap-2">
                <c:choose>
                    <c:when test="${esAutoEliminacion}">
                        <button type="submit" class="btn btn-danger">
                            <i class="bi bi-person-x-fill"></i> Eliminar mi cuenta
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" class="btn btn-danger">
                            <i class="bi bi-trash3-fill"></i> Eliminar Usuario
                        </button>
                    </c:otherwise>
                </c:choose>
                
                <!-- Botón para ver registros asociados - disponible para todos los usuarios -->
                <c:choose>
                    <c:when test="${esAutoEliminacion}">
                        <a href="${pageContext.request.contextPath}/registros_asociados?esAutoEliminacion=true" 
                           class="btn btn-warning text-center">
                            <i class="bi bi-list-ul"></i> Ver Mis Registros Asociados
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/registros_asociados?userId=${userId}&esAutoEliminacion=false" 
                           class="btn btn-warning text-center">
                            <i class="bi bi-list-ul"></i> Ver Registros Asociados
                        </a>
                    </c:otherwise>
                </c:choose>
                
                <a href="${pageContext.request.contextPath}/bienvenida" 
                   class="btn btn-secondary text-center">
                    <i class="bi bi-x-circle"></i> Cancelar
                </a>
            </div>
        </form>
    </div>

    <!-- Sweet Alert 2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <script>
    function confirmarEliminacion(event) {
        event.preventDefault();
        
        const form = document.getElementById('deleteForm');
        const confirmacionInput = form.querySelector('input[name="confirmacion"]');
        const passwordInput = form.querySelector('input[name="adminPassword"]');
        
        if (!confirmacionInput.value || !passwordInput.value) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor complete todos los campos'
            });
            return false;
        }

        if (confirmacionInput.value !== 'ELIMINAR') {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Debe escribir ELIMINAR exactamente'
            });
            return false;
        }

        Swal.fire({
            title: '¿Está seguro?',
            text: "Esta acción no se puede deshacer",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                form.submit();
            }
        });

        return false;
    }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>