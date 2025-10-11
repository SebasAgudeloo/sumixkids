<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Verificación 2FA · SumixKids</title>
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
            <div class="col-12 col-md-7 col-lg-5 col-xl-4">
                <div class="card shadow-lg border-0 rounded-4">
                    <div class="card-body p-4 p-md-5">
                        <h1 class="h4 mb-4 text-center fw-bold text-primary">Verificación en dos pasos</h1>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <form method="post" action="${pageContext.request.contextPath}/2fa" class="needs-validation" novalidate>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">
                                    <i class="fas fa-shield-alt me-1"></i>Código de verificación <span class="text-danger">*</span>
                                </label>
                                <input type="text" name="codigo" id="codigo" class="form-control" 
                                       placeholder="123456" maxlength="6" pattern="[0-9]{6}" required autofocus />
                                <div class="form-text">
                                    <i class="fas fa-envelope me-1"></i>Ingresa el código de <strong>6 dígitos numéricos</strong> que recibiste por correo
                                </div>
                                <div class="invalid-feedback">El código debe tener exactamente 6 dígitos numéricos.</div>
                            </div>
                            <div class="d-grid gap-2 mt-4">
                                <button class="btn btn-success btn-lg shadow-sm" type="submit">Verificar</button>
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/login">Volver a inicio de sesión</a>
                            </div>
                        </form>
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

// Validación en tiempo real del código 2FA (solo números)
document.getElementById('codigo').addEventListener('input', function() {
    // Permitir solo números
    this.value = this.value.replace(/[^0-9]/g, '');
    if (this.value.length > 6) {
        this.value = this.value.substring(0, 6);
    }
});

// Auto-hide alerts después de 10 segundos
setTimeout(() => {
    document.querySelectorAll('.alert-danger').forEach(alert => {
        alert.style.display = 'none';
    });
}, 10000);
</script>
</body>
</html>
