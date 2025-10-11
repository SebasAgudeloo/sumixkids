<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Recuperar contraseña · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" style="height: 36px; width: auto; margin-right: 8px;"/>
            SumixKids
        </a>
    </div>
</nav>
<main class="flex-fill d-flex align-items-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-sm-10 col-md-7 col-lg-5">
                <div class="card shadow-lg border-0 rounded-4">
                    <div class="card-body p-4 p-md-5">
                        <h1 class="h4 mb-4 text-center fw-bold text-primary">
                            <i class="fas fa-envelope me-2"></i>Recuperar contraseña
                        </h1>
                        <p class="text-muted text-center mb-4">
                            <i class="fas fa-info-circle me-1"></i>Ingresa tu correo electrónico y te enviaremos un <b>código</b> para restablecer tu contraseña
                        </p>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <c:if test="${not empty mensaje}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i>${mensaje}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <form id="recuperarForm" method="post" action="${pageContext.request.contextPath}/recuperar">
                            <div class="mb-4">
                                <label class="form-label fw-semibold">
                                    <i class="fas fa-at me-1"></i>Correo electrónico <span class="text-danger">*</span>
                                </label>
                                <input type="email" name="correo" id="correo" class="form-control ${errorCorreo ? 'is-invalid' : ''}" 
                                       placeholder="ejemplo@gmail.com" value="${correo}" required />
                                <div class="form-text">
                                    <i class="fas fa-shield-alt me-1"></i>Recibirás un código de <b>6 dígitos numéricos</b> para restablecer tu contraseña
                                </div>
                                <c:if test="${errorCorreo}">
                                    <div class="invalid-feedback">Ingresa un correo electrónico válido</div>
                                </c:if>
                            </div>
                            <div class="d-grid gap-2 mb-3">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-paper-plane me-2"></i>Enviar código
                                </button>
                            </div>
                        </form>
                        <div class="text-center mt-4">
                            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                                <i class="fas fa-arrow-left me-1"></i>Volver a iniciar sesión
                            </a>
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
<script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
<script>
document.getElementById('year').textContent = new Date().getFullYear();

// Auto-hide alerts después de 10 segundos
setTimeout(() => {
    document.querySelectorAll('.alert-danger, .alert-success').forEach(alert => {
        alert.style.display = 'none';
    });
}, 10000);

// Validación del formulario
document.getElementById('recuperarForm').addEventListener('submit', function(e) {
    const correo = document.getElementById('correo');
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    
    if (!emailPattern.test(correo.value)) {
        e.preventDefault();
        correo.classList.add('is-invalid');
        
        let errorDiv = correo.parentElement.querySelector('.custom-error-message');
        if (!errorDiv) {
            errorDiv = document.createElement('div');
            errorDiv.className = 'custom-error-message text-danger mt-1 small';
            correo.parentElement.appendChild(errorDiv);
        }
        errorDiv.innerHTML = '<i class="fas fa-exclamation-circle me-1"></i>Por favor, ingresa un correo electrónico válido';
    }
});

// Limpiar error al escribir
document.getElementById('correo').addEventListener('input', function() {
    this.classList.remove('is-invalid');
    const errorDiv = this.parentElement.querySelector('.custom-error-message');
    if (errorDiv) {
        errorDiv.remove();
    }
});
</script>
</body>
</html>
