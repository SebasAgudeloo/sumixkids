<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Página de registro: crea una cuenta nueva --%>
<!DOCTYPE html>
<html lang="es">
<head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Registro · SumixKids</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<%-- Barra de navegación --%>
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
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login">Iniciar sesión</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/registro">Registro</a></li>
            </ul>
        </div>
    </div>
</nav>

<%-- Contenido principal con tarjeta de formulario --%>
<main class="flex-fill d-flex align-items-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                <div class="card shadow-lg border-0 rounded-4">
                    <div class="card-body p-4 p-md-5">
                        <h1 class="h3 mb-4 text-center fw-bold text-primary">Crear cuenta</h1>
                        <%-- Mensajes de error de validación --%>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <%-- Formulario de registro --%>
                        <form id="registroForm" method="post" action="${pageContext.request.contextPath}/registro">
                            <div class="form-group">
                                <label for="nombres">Nombres:</label>
                                <input type="text" class="form-control" id="nombres" name="nombres" 
                                       value="${nombres}" required 
                                       placeholder="Ingrese sus nombres (solo letras)">
                            </div>
                            
                            <div class="form-group">
                                <label for="apellidos">Apellidos:</label>
                                <input type="text" class="form-control" id="apellidos" name="apellidos" 
                                       value="${apellidos}" required 
                                       placeholder="Ingrese sus apellidos (solo letras)">
                            </div>
                            
                            <div class="form-group">
                                <label for="username">Usuario:</label>
                                <input type="text" class="form-control" id="username" name="username" 
                                       value="${username}" required 
                                       placeholder="Mínimo 4 caracteres y 2 números">
                            </div>
                            
                            <div class="form-group">
                                <label for="email">Correo electrónico:</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="${email}" required 
                                       placeholder="ejemplo@gmail.com">
                            </div>
                            
                            <!-- El rol se asigna automáticamente como estudiante. -->
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Contraseña <span class="text-danger">*</span></label>
                                <div class="position-relative">
                                    <input type="password" name="password" class="form-control password-field" minlength="6" required />
                                    <button class="btn btn-sm btn-outline-secondary position-absolute top-50 end-0 translate-middle-y me-2 toggle-pass" type="button">Ver</button>
                                    <div class="invalid-feedback">Mínimo 6 caracteres.</div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Confirmar contraseña <span class="text-danger">*</span></label>
                                <div class="position-relative">
                                    <input type="password" name="confirm" class="form-control password-field" minlength="6" required />
                                    <button class="btn btn-sm btn-outline-secondary position-absolute top-50 end-0 translate-middle-y me-2 toggle-pass" type="button">Ver</button>
                                    <div class="invalid-feedback">Repite la contraseña.</div>
                                </div>
                            </div>
                            <div class="d-grid gap-2 mt-4">
                                <button class="btn btn-success btn-lg shadow-sm" type="submit">Registrarme</button>
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/login">Volver a iniciar sesión</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="py-3 bg-dark mt-auto text-center text-white-50 small">
    © <span id="year"></span> SumixKids · Todos los derechos reservados
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script>
    document.getElementById('year').textContent = new Date().getFullYear();
    // Toggle pass
    document.querySelectorAll('.toggle-pass').forEach(btn => {
        btn.addEventListener('click', () => {
            const input = btn.closest('.position-relative').querySelector('.password-field');
            const show = input.type === 'password';
            input.type = show ? 'text' : 'password';
            btn.textContent = show ? 'Ocultar' : 'Ver';
        });
    });
    // Validation
    (() => { const forms = document.querySelectorAll('.needs-validation');
        Array.from(forms).forEach(form => { form.addEventListener('submit', evt => { if (!form.checkValidity()) { evt.preventDefault(); evt.stopPropagation(); } form.classList.add('was-validated'); }, false); }); })();
</script>
<script>
    setTimeout(() => {
        document.querySelectorAll('.alert-danger, .alert-success').forEach(alert => {
            alert.style.display = 'none';
        });
    }, 3000);
</script>
<script>
// Expresiones regulares para validación
const regexNombre = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,50}$/;
const regexUsuario = /^(?=.*[0-9].*[0-9])[a-zA-Z0-9]{4,20}$/;
const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

// Validación en tiempo real
document.getElementById('nombres').addEventListener('input', function() {
    if (!regexNombre.test(this.value)) {
        mostrarError(this, 'El nombre solo puede contener letras');
    } else {
        limpiarError(this);
    }
});

document.getElementById('apellidos').addEventListener('input', function() {
    if (!regexNombre.test(this.value)) {
        mostrarError(this, 'El apellido solo puede contener letras');
    } else {
        limpiarError(this);
    }
});

document.getElementById('username').addEventListener('input', function() {
    if (!regexUsuario.test(this.value)) {
        mostrarError(this, 'El usuario debe contener al menos 2 números y entre 4-20 caracteres');
    } else {
        limpiarError(this);
    }
});

document.getElementById('email').addEventListener('input', function() {
    if (!regexCorreo.test(this.value)) {
        mostrarError(this, 'Ingrese un correo electrónico válido (ejemplo@gmail.com)');
    } else {
        limpiarError(this);
    }
});

// Validación al enviar el formulario
document.getElementById('registroForm').addEventListener('submit', function(e) {
    e.preventDefault();
    let isValid = true;

    // Validar nombres
    const nombres = document.getElementById('nombres');
    if (!regexNombre.test(nombres.value)) {
        mostrarError(nombres, 'El nombre solo puede contener letras');
        isValid = false;
    }

    // Validar apellidos
    const apellidos = document.getElementById('apellidos');
    if (!regexNombre.test(apellidos.value)) {
        mostrarError(apellidos, 'El apellido solo puede contener letras');
        isValid = false;
    }

    // Validar usuario
    const username = document.getElementById('username');
    if (!regexUsuario.test(username.value)) {
        mostrarError(username, 'El usuario debe contener al menos 2 números y entre 4-20 caracteres');
        isValid = false;
    }

    // Validar email
    const email = document.getElementById('email');
    if (!regexCorreo.test(email.value)) {
        mostrarError(email, 'Ingrese un correo electrónico válido (ejemplo@gmail.com)');
        isValid = false;
    }

    if (isValid) {
        this.submit();
    }
});

function mostrarError(input, mensaje) {
    const formGroup = input.closest('.form-group');
    let errorDiv = formGroup.querySelector('.error-message');
    
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'error-message text-danger mt-1';
        formGroup.appendChild(errorDiv);
    }
    
    errorDiv.textContent = mensaje;
    input.classList.add('is-invalid');
}

function limpiarError(input) {
    const formGroup = input.closest('.form-group');
    const errorDiv = formGroup.querySelector('.error-message');
    if (errorDiv) {
        errorDiv.remove();
    }
    input.classList.remove('is-invalid');
}
</script>
</body>
</html>
