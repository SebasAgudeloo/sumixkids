<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %><%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>

<!DOCTYPE html>

<!DOCTYPE html><html lang="es">

<html lang="es"><head>

<head>    <meta charset="UTF-8" />

    <meta charset="UTF-8">    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <meta name="viewport" content="width=device-width, initial-scale=1.0">    <title>Mi Perfil · SumixKids</title>

    <title>Mi Perfil - SumixKids</title>    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />

</head></head>

<body class="bg-light"><body class="bg-gradient-primary d-flex flex-column min-vh-100">

    <div class="container-fluid">

        <div class="row"><!-- Navbar -->

            <!-- Sidebar --><nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">

            <div class="col-md-3 col-lg-2 px-0">    <div class="container">

                <jsp:include page="includes/sidebar.jsp" />        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/bienvenida">

            </div>            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" 

                 style="height: 40px; width: auto; margin-right: 12px; border-radius: 8px;"/>

            <!-- Main Content -->            SumixKids

            <div class="col-md-9 col-lg-10">        </a>

                <div class="container mt-4">        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">

                    <div class="row justify-content-center">            <span class="navbar-toggler-icon"></span>

                        <div class="col-lg-8">        </button>

                            <div class="card shadow-lg border-0">        <div class="collapse navbar-collapse" id="navbarNav">

                                <div class="card-header bg-primary text-white text-center py-3">            <ul class="navbar-nav me-auto">

                                    <h3 class="mb-0">                <li class="nav-item">

                                        <i class="bi bi-person-circle me-2"></i>                    <a class="nav-link" href="${pageContext.request.contextPath}/bienvenida">

                                        Mi Perfil                        🏠 Dashboard

                                    </h3>                    </a>

                                </div>                </li>

                                <div class="card-body text-center py-5">            </ul>

                                    <div class="mb-4">            <ul class="navbar-nav">

                                        <i class="bi bi-tools text-primary" style="font-size: 4rem;"></i>                <li class="nav-item">

                                    </div>                    <a class="nav-link active" href="${pageContext.request.contextPath}/perfil">

                                    <h4 class="text-primary mb-3">Próximamente</h4>                        <i class="fas fa-user me-2"></i>Mi Perfil

                                    <p class="text-muted mb-4">                    </a>

                                        Estamos trabajando en una experiencia increíble para gestionar tu perfil.                 </li>

                                        Pronto podrás actualizar tu información personal, cambiar tu contraseña,             </ul>

                                        configurar la autenticación de dos factores y mucho más.        </div>

                                    </p>    </div>

                                    <div class="row text-center mt-4"></nav>

                                        <div class="col-md-4 mb-3">

                                            <i class="bi bi-person-gear text-info" style="font-size: 2rem;"></i><main class="flex-fill py-5">

                                            <h6 class="mt-2">Información Personal</h6>    <div class="container">

                                        </div>        <!-- Header -->

                                        <div class="col-md-4 mb-3">        <div class="row justify-content-center mb-4">

                                            <i class="bi bi-shield-lock text-success" style="font-size: 2rem;"></i>            <div class="col-lg-10">

                                            <h6 class="mt-2">Seguridad</h6>                <div class="text-center mb-4">

                                        </div>                    <h1 class="display-5 fw-bold text-white-contrast drop-shadow">

                                        <div class="col-md-4 mb-3">                        <i class="fas fa-user-circle me-3"></i>Mi Perfil

                                            <i class="bi bi-gear text-warning" style="font-size: 2rem;"></i>                    </h1>

                                            <h6 class="mt-2">Configuración</h6>                    <p class="lead text-white-contrast">

                                        </div>                        Gestiona tu información personal y configuraciones de seguridad

                                    </div>                    </p>

                                </div>                </div>

                            </div>            </div>

                        </div>        </div>

                    </div>

                </div>        <!-- Alertas -->

            </div>        <c:if test="${not empty error}">

        </div>            <div class="alert alert-danger alert-dismissible fade show shadow-lg" role="alert">

    </div>                <i class="fas fa-exclamation-triangle me-2"></i>

                <strong>❌ Error:</strong> ${error}

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>

</body>            </div>

</html>        </c:if>
        
        <c:if test="${not empty errores}">
            <div class="alert alert-danger alert-dismissible fade show shadow-lg" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <strong>❌ Errores encontrados:</strong>
                <ul class="mb-0 mt-2">
                    <c:forEach var="error" items="${errores}">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show shadow-lg" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                <strong>✅ ¡Éxito!</strong> ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty info}">
            <div class="alert alert-info alert-dismissible fade show shadow-lg" role="alert">
                <i class="fas fa-info-circle me-2"></i>
                <strong>ℹ️ Información:</strong> ${info}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="row">
            <!-- Información Personal -->
            <div class="col-lg-8">
                <!-- Datos Básicos -->
                <div class="card shadow-lg border-0 mb-4">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-id-card me-2"></i>Información Personal
                        </h5>
                    </div>
                    <div class="card-body">
                        <form method="post" action="${pageContext.request.contextPath}/perfil">
                            <input type="hidden" name="accion" value="cambiar_nombres">
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="nuevos_nombres" class="form-label">
                                        <i class="fas fa-user me-1"></i>Nombres
                                    </label>
                                    <input type="text" class="form-control" id="nuevos_nombres" 
                                           name="nuevos_nombres" value="${sessionScope.usuario.nombres}" 
                                           required maxlength="50">
                                </div>
                                <div class="col-md-6">
                                    <label for="nuevos_apellidos" class="form-label">
                                        <i class="fas fa-user me-1"></i>Apellidos
                                    </label>
                                    <input type="text" class="form-control" id="nuevos_apellidos" 
                                           name="nuevos_apellidos" value="${sessionScope.usuario.apellidos}" 
                                           required maxlength="50">
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-at me-1"></i>Usuario
                                    </label>
                                    <input type="text" class="form-control" 
                                           value="${sessionScope.usuario.username}" readonly>
                                    <small class="text-muted">El nombre de usuario no se puede cambiar</small>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">
                                        <i class="fas fa-user-tag me-1"></i>Rol
                                    </label>
                                    <input type="text" class="form-control" 
                                           value="<c:choose>
                                               <c:when test='${sessionScope.usuario.rolId == 1}'>👑 Administrador</c:when>
                                               <c:when test='${sessionScope.usuario.rolId == 2}'>👨‍🏫 Docente</c:when>
                                               <c:when test='${sessionScope.usuario.rolId == 3}'>🎓 Estudiante</c:when>
                                               <c:when test='${sessionScope.usuario.rolId == 4}'>👥 Acompañante</c:when>
                                               <c:otherwise>Usuario</c:otherwise>
                                           </c:choose>" readonly>
                                </div>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-save me-2"></i>Actualizar Nombres
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Cambiar Email -->
                <div class="card shadow-lg border-0 mb-4">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-envelope me-2"></i>Correo Electrónico
                        </h5>
                    </div>
                    <div class="card-body">
                        <form method="post" action="${pageContext.request.contextPath}/perfil" 
                              onsubmit="return confirmarCambioEmail()">
                            <input type="hidden" name="accion" value="cambiar_email">
                            
                            <div class="mb-3">
                                <label for="nuevo_email" class="form-label">
                                    <i class="fas fa-envelope me-1"></i>Nuevo Email
                                </label>
                                <input type="email" class="form-control" id="nuevo_email" 
                                       name="nuevo_email" value="${sessionScope.usuario.email}" 
                                       required maxlength="100">
                            </div>
                            
                            <div class="mb-3">
                                <label for="password_confirmacion" class="form-label">
                                    <i class="fas fa-lock me-1"></i>Confirma tu Contraseña
                                </label>
                                <input type="password" class="form-control" id="password_confirmacion" 
                                       name="password_confirmacion" required>
                                <small class="text-muted">Por seguridad, confirma tu contraseña actual</small>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-info btn-lg">
                                    <i class="fas fa-sync-alt me-2"></i>Actualizar Email
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Cambiar Contraseña -->
                <div class="card shadow-lg border-0 mb-4">
                    <div class="card-header bg-warning text-dark">
                        <h5 class="mb-0">
                            <i class="fas fa-key me-2"></i>Cambiar Contraseña
                        </h5>
                    </div>
                    <div class="card-body">
                        <form method="post" action="${pageContext.request.contextPath}/perfil" 
                              onsubmit="return validarCambioPassword()">
                            <input type="hidden" name="accion" value="cambiar_password">
                            
                            <div class="mb-3">
                                <label for="password_actual" class="form-label">
                                    <i class="fas fa-lock me-1"></i>Contraseña Actual
                                </label>
                                <input type="password" class="form-control" id="password_actual" 
                                       name="password_actual" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="password_nueva" class="form-label">
                                    <i class="fas fa-lock me-1"></i>Nueva Contraseña
                                </label>
                                <input type="password" class="form-control" id="password_nueva" 
                                       name="password_nueva" required minlength="8">
                                <small class="text-muted">Mínimo 8 caracteres</small>
                            </div>
                            
                            <div class="mb-3">
                                <label for="password_confirmacion_nueva" class="form-label">
                                    <i class="fas fa-lock me-1"></i>Confirmar Nueva Contraseña
                                </label>
                                <input type="password" class="form-control" id="password_confirmacion_nueva" 
                                       name="password_confirmacion_nueva" required minlength="8">
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-warning btn-lg">
                                    <i class="fas fa-shield-alt me-2"></i>Cambiar Contraseña
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Panel de Seguridad -->
            <div class="col-lg-4">
                <!-- Estado de Seguridad -->
                <div class="card shadow-lg border-0 mb-4">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-shield-alt me-2"></i>Estado de Seguridad
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span><i class="fas fa-mobile-alt me-2"></i>Autenticación 2FA</span>
                            <form method="post" action="${pageContext.request.contextPath}/perfil" class="d-inline">
                                <input type="hidden" name="accion" value="toggle_2fa">
                                <button type="submit" class="btn btn-sm ${tiene2FA ? 'btn-success' : 'btn-outline-secondary'}">
                                    ${tiene2FA ? '✅ Activado' : '❌ Desactivado'}
                                </button>
                            </form>
                        </div>
                        
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span><i class="fas fa-devices me-2"></i>Dispositivos</span>
                            <span class="badge bg-primary">${totalDispositivos}</span>
                        </div>
                        
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/dispositivos_reconocidos" 
                               class="btn btn-outline-primary btn-sm">
                                <i class="fas fa-list me-2"></i>Ver Dispositivos
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Información de la Cuenta -->
                <div class="card shadow-lg border-0 mb-4">
                    <div class="card-header bg-secondary text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-info-circle me-2"></i>Información de Cuenta
                        </h5>
                    </div>
                    <div class="card-body">
                        <small class="text-muted d-block mb-2">
                            <i class="fas fa-calendar me-1"></i>
                            Registro: <c:if test="${not empty sessionScope.usuario.fechaRegistro}">
                                ${util:formatearFecha(sessionScope.usuario.fechaRegistro)}
                            </c:if>
                        </small>
                        
                        <small class="text-muted d-block mb-2">
                            <i class="fas fa-clock me-1"></i>
                            Última conexión: <c:if test="${not empty sessionScope.usuario.ultimaConexion}">
                                ${util:formatearFecha(sessionScope.usuario.ultimaConexion)}
                            </c:if>
                        </small>
                        
                        <small class="text-muted d-block">
                            <i class="fas fa-id-badge me-1"></i>
                            ID de Usuario: #${sessionScope.usuario.id}
                        </small>
                    </div>
                </div>

                <!-- Acciones de Cuenta -->
                <div class="card shadow-lg border-0">
                    <div class="card-header bg-danger text-white">
                        <h5 class="mb-0">
                            <i class="fas fa-exclamation-triangle me-2"></i>Zona Peligrosa
                        </h5>
                    </div>
                    <div class="card-body">
                        <p class="text-muted small mb-3">
                            Estas acciones son irreversibles. Procede con precaución.
                        </p>
                        
                        <c:if test="${sessionScope.usuario.rolId != 1}">
                            <div class="d-grid">
                                <a href="${pageContext.request.contextPath}/eliminar_usuario?tipo=usuario" 
                                   class="btn btn-outline-danger" 
                                   onclick="return confirm('¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.')">
                                    <i class="fas fa-user-times me-2"></i>Eliminar mi Cuenta
                                </a>
                            </div>
                        </c:if>
                        
                        <c:if test="${sessionScope.usuario.rolId == 1}">
                            <p class="text-muted small">
                                <i class="fas fa-crown me-1"></i>
                                Los administradores no pueden eliminar su propia cuenta por seguridad.
                            </p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Botón de regreso -->
        <div class="row mt-4">
            <div class="col-12 text-center">
                <a href="${pageContext.request.contextPath}/bienvenida" class="btn btn-secondary btn-lg">
                    <i class="fas fa-arrow-left me-2"></i>Volver al Dashboard
                </a>
            </div>
        </div>
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
                <p class="text-white-50 small mb-0">Tu perfil, bajo tu control</p>
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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>
<script>
document.getElementById('year').textContent = new Date().getFullYear();

// Auto-hide alerts after 8 seconds
setTimeout(function() {
    document.querySelectorAll('.alert-dismissible').forEach(alert => {
        if (alert.classList.contains('show')) {
            bootstrap.Alert.getOrCreateInstance(alert).close();
        }
    });
}, 8000);

function confirmarCambioEmail() {
    const nuevoEmail = document.getElementById('nuevo_email').value;
    const emailActual = '${sessionScope.usuario.email}';
    
    if (nuevoEmail === emailActual) {
        Swal.fire({
            title: 'Sin cambios',
            text: 'El nuevo email es igual al actual',
            icon: 'info'
        });
        return false;
    }
    
    return confirm('¿Estás seguro de cambiar tu email? Recibirás notificaciones en ambas direcciones.');
}

function validarCambioPassword() {
    const passwordNueva = document.getElementById('password_nueva').value;
    const passwordConfirmacion = document.getElementById('password_confirmacion_nueva').value;
    
    if (passwordNueva !== passwordConfirmacion) {
        Swal.fire({
            title: 'Error',
            text: 'Las contraseñas no coinciden',
            icon: 'error'
        });
        return false;
    }
    
    if (passwordNueva.length < 8) {
        Swal.fire({
            title: 'Error',
            text: 'La contraseña debe tener al menos 8 caracteres',
            icon: 'error'
        });
        return false;
    }
    
    return confirm('¿Estás seguro de cambiar tu contraseña?');
}

// Mostrar/ocultar contraseñas
document.querySelectorAll('input[type="password"]').forEach(input => {
    const toggleBtn = document.createElement('button');
    toggleBtn.type = 'button';
    toggleBtn.className = 'btn btn-outline-secondary position-absolute end-0 top-50 translate-middle-y me-2';
    toggleBtn.innerHTML = '<i class="fas fa-eye"></i>';
    
    input.parentNode.classList.add('position-relative');
    input.parentNode.appendChild(toggleBtn);
    
    toggleBtn.addEventListener('click', function() {
        if (input.type === 'password') {
            input.type = 'text';
            this.innerHTML = '<i class="fas fa-eye-slash"></i>';
        } else {
            input.type = 'password';
            this.innerHTML = '<i class="fas fa-eye"></i>';
        }
    });
});
</script>

</body>
</html>