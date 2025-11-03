<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%-- Página de registro: crea una cuenta nueva --%>
<!DOCTYPE html>
<html lang="es">
<head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Registro · SumixKids</title>
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<%-- Barra de navegación --%>
<nav class="navbar navbar-expand-lg navbar-dark shadow-lg" style="background: rgba(37, 99, 235, 0.95); backdrop-filter: blur(10px);">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" 
                 style="height: 40px; width: auto; margin-right: 12px; border-radius: 8px;"/>
            SumixKids
        </a>
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarMain">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link text-white fw-semibold px-3" href="${pageContext.request.contextPath}/login">
                        <i class="bi bi-box-arrow-in-right me-1"></i>Iniciar sesión
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white fw-semibold px-3 active" href="${pageContext.request.contextPath}/registro">
                        <i class="bi bi-person-plus me-1"></i>Registro
                    </a>
                </li>
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
                        <h1 class="h3 mb-4 text-center fw-bold text-primary">
                            <c:choose>
                                <c:when test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 1}">
                                    <i class="fas fa-user-plus me-2"></i>Registrar Usuario
                                </c:when>
                                <c:otherwise>
                                    Crear cuenta
                                </c:otherwise>
                            </c:choose>
                        </h1>
                        
                        <%-- Mensaje informativo para registro público --%>
                        <c:if test="${sessionScope.usuario == null}">
                            <div class="alert alert-info border-0 shadow-sm mb-4" role="alert">
                                <div class="d-flex align-items-start">
                                    <i class="fas fa-info-circle text-info me-3 mt-1" style="font-size: 1.2rem;"></i>
                                    <div>
                                        <h6 class="alert-heading mb-2">📝 Guía de Registro</h6>
                                        <p class="mb-2"><strong>🎓 Si eres estudiante:</strong> Regístrate normalmente.</p>
                                        <p class="mb-2"><strong>👨‍🏫 Si eres docente:</strong> Regístrate como estudiante y contacta al administrador para que te asigne el rol de docente.</p>
                                        <p class="mb-0"><strong>👨‍👩‍👧‍👦 Si eres acompañante/tutor:</strong> Regístrate como estudiante y solicita al docente de tu hijo(a) que te asigne el rol de acompañante.</p>
                                        <hr class="my-2">
                                        <small class="text-muted">
                                            <i class="fas fa-shield-alt me-1"></i>
                                            Esto evita registros incorrectos y mantiene la seguridad del sistema.
                                        </small>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        
                        <%-- Mensajes de error de validación --%>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <c:if test="${not empty success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i>${success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <%-- Formulario de registro --%>
                        <form id="registroForm" method="post" action="${pageContext.request.contextPath}/registro">
                            <div class="form-group">
                                <label for="nombres">Nombres:</label>
                                <input type="text" class="form-control ${errorNombres ? 'is-invalid' : ''}" id="nombres" name="nombres" 
                                       value="${nombres}" required maxlength="30"
                                       placeholder="Ingrese sus nombres (solo letras, máx. 30 caracteres)">
                                <c:if test="${errorNombres}">
                                    <div class="invalid-feedback">El nombre solo puede contener letras y espacios (máximo 30 caracteres)</div>
                                </c:if>
                            </div>
                            
                            <div class="form-group">
                                <label for="apellidos">Apellidos:</label>
                                <input type="text" class="form-control ${errorApellidos ? 'is-invalid' : ''}" id="apellidos" name="apellidos" 
                                       value="${apellidos}" required maxlength="30"
                                       placeholder="Ingrese sus apellidos (solo letras, máx. 30 caracteres)">
                                <c:if test="${errorApellidos}">
                                    <div class="invalid-feedback">El apellido solo puede contener letras y espacios (máximo 30 caracteres)</div>
                                </c:if>
                            </div>
                            
                            <div class="form-group">
                                <label for="username">Usuario:</label>
                                <input type="text" class="form-control ${errorUsername ? 'is-invalid' : ''}" id="username" name="username" 
                                       value="${username}" required maxlength="15" minlength="5"
                                       placeholder="Entre 5 y 15 caracteres, sin espacios">
                                <c:if test="${errorUsername}">
                                    <div class="invalid-feedback">El usuario debe tener entre 5 y 15 caracteres sin espacios</div>
                                </c:if>
                            </div>
                            
                            <div class="form-group">
                                <label for="email">Correo electrónico:</label>
                                <input type="email" class="form-control ${errorEmail ? 'is-invalid' : ''}" id="email" name="email" 
                                       value="${email}" required 
                                       placeholder="ejemplo@gmail.com">
                                <c:if test="${errorEmail}">
                                    <div class="invalid-feedback">El correo electrónico debe tener un formato válido</div>
                                </c:if>
                            </div>
                            
                            <%-- Selector de rol solo para administradores --%>
                            <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 1}">
                                <div class="form-group">
                                    <label for="rolId" class="form-label fw-semibold">
                                        <i class="fas fa-user-tag me-1"></i>Rol <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select ${errorRolId ? 'is-invalid' : ''}" id="rolId" name="rolId" required>
                                        <option value="">Seleccionar rol...</option>
                                        <option value="1" ${rolId == '1' ? 'selected' : ''}>👑 Administrador</option>
                                        <option value="2" ${rolId == '2' ? 'selected' : ''}>👨‍🏫 Docente</option>
                                        <option value="3" ${rolId == '3' ? 'selected' : ''}>🎓 Estudiante</option>
                                        <option value="4" ${rolId == '4' ? 'selected' : ''}>👨‍👩‍👧‍👦 Acompañante</option>
                                    </select>
                                    <c:if test="${errorRolId}">
                                        <div class="invalid-feedback">Debe seleccionar un rol válido</div>
                                    </c:if>
                                </div>
                            </c:if>
                            
                            <%-- Campo de grado (solo visible cuando el rol es Estudiante) --%>
                            <div class="form-group" id="gradoContainer" style="display: none;">
                                <label for="grado" class="form-label fw-semibold">
                                    <i class="fas fa-graduation-cap me-1"></i>Grado <span class="text-danger" id="gradoRequired">*</span>
                                </label>
                                <select class="form-select ${errorGrado ? 'is-invalid' : ''}" id="grado" name="grado">
                                    <option value="">Seleccionar grado...</option>
                                    <option value="3" ${grado == '3' || grado == '3°' ? 'selected' : ''}>3°</option>
                                    <option value="4" ${grado == '4' || grado == '4°' ? 'selected' : ''}>4°</option>
                                    <option value="5" ${grado == '5' || grado == '5°' ? 'selected' : ''}>5°</option>
                                </select>
                                <c:if test="${errorGrado}">
                                    <div class="invalid-feedback">El grado debe ser 3°, 4° o 5°</div>
                                </c:if>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>El grado es obligatorio para estudiantes
                                </small>
                            </div>
                            
                            <%-- Campo de contraseña --%>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Contraseña <span class="text-danger">*</span></label>
                                <div class="position-relative">
                                    <input type="password" name="password" id="passwordInput" class="form-control password-field ${errorPassword ? 'is-invalid' : ''}" 
                                           maxlength="20" required 
                                           placeholder="Crea tu contraseña" />
                                    <button class="btn btn-sm btn-outline-secondary position-absolute top-50 end-0 translate-middle-y me-2 toggle-pass" type="button" style="z-index: 10;">Ver</button>
                                    <c:if test="${errorPassword}">
                                        <div class="invalid-feedback">La contraseña no cumple los requisitos</div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <%-- Requisitos de contraseña en el centro --%>
                            <div class="mb-3">
                                <div class="border rounded p-3 bg-light">
                                    <small class="fw-bold text-muted d-block mb-2">
                                        <i class="fas fa-shield-alt me-1"></i>Requisitos de contraseña:
                                    </small>
                                    <div class="password-requirement d-flex align-items-center mb-1" id="req-letters-reg">
                                        <span class="requirement-icon me-2">❌</span>
                                        <small>Mínimo 5 letras</small>
                                    </div>
                                    <div class="password-requirement d-flex align-items-center mb-1" id="req-numbers-reg">
                                        <span class="requirement-icon me-2">❌</span>
                                        <small>Mínimo 2 números</small>
                                    </div>
                                    <div class="password-requirement d-flex align-items-center mb-1" id="req-special-reg">
                                        <span class="requirement-icon me-2">❌</span>
                                        <small>Mínimo 1 carácter especial (!@#$%^&*)</small>
                                    </div>
                                    <div class="password-requirement d-flex align-items-center mb-1" id="req-length-reg">
                                        <span class="requirement-icon me-2">❌</span>
                                        <small>Máximo 20 caracteres</small>
                                    </div>
                                </div>
                            </div>
                            
                            <%-- Campo de confirmar contraseña --%>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Confirmar contraseña <span class="text-danger">*</span></label>
                                <div class="position-relative">
                                    <input type="password" name="confirm" class="form-control password-field" maxlength="20" required 
                                           placeholder="Repite la contraseña" />
                                    <button class="btn btn-sm btn-outline-secondary position-absolute top-50 end-0 translate-middle-y me-2 toggle-pass" type="button" style="z-index: 10;">Ver</button>
                                    <div class="invalid-feedback">Repite la contraseña.</div>
                                </div>
                                <div class="form-text">
                                    <i class="fas fa-info-circle me-1"></i>Asegúrate de que ambas contraseñas coincidan
                                </div>
                            </div>
                            
                            <div class="d-grid gap-2 mt-4">
                                <c:choose>
                                    <c:when test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 1}">
                                        <button class="btn btn-success btn-lg shadow-sm fw-bold" type="submit">
                                            <i class="fas fa-user-plus me-2"></i>Registrar Usuario
                                        </button>
                                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/usuarios">
                                            <i class="fas fa-arrow-left me-2"></i>Volver a Gestión de Usuarios
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-success btn-lg shadow-sm" type="submit">Registrarme</button>
                                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/login">Volver a iniciar sesión</a>
                                    </c:otherwise>
                                </c:choose>
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
<script>
document.getElementById('year').textContent = new Date().getFullYear();

// Mostrar/ocultar campo de grado según el rol seleccionado
const rolSelect = document.getElementById('rolId');
const gradoContainer = document.getElementById('gradoContainer');
const gradoSelect = document.getElementById('grado');

if (rolSelect) {
    // Verificar estado inicial
    checkRolAndShowGrado();
    
    // Escuchar cambios en el selector de rol
    rolSelect.addEventListener('change', checkRolAndShowGrado);
}

function checkRolAndShowGrado() {
    if (rolSelect && gradoContainer) {
        const selectedRol = rolSelect.value;
        
        if (selectedRol === '3') { // Estudiante
            gradoContainer.style.display = 'block';
            gradoSelect.setAttribute('required', 'required');
        } else {
            gradoContainer.style.display = 'none';
            gradoSelect.removeAttribute('required');
            gradoSelect.value = ''; // Limpiar selección
        }
    }
}

// Para registro público (sin admin), mostrar siempre el grado
if (!rolSelect && gradoContainer) {
    gradoContainer.style.display = 'block';
}

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

// Auto-hide alerts
setTimeout(() => {
    document.querySelectorAll('.alert-danger, .alert-success').forEach(alert => {
        alert.style.display = 'none';
    });
}, 10000);

// Validación visual en tiempo real de la contraseña
const passwordInput = document.getElementById('passwordInput');
if (passwordInput) {
    passwordInput.addEventListener('input', function() {
        validatePasswordVisual(this.value);
    });
}

function validatePasswordVisual(password) {
    // Contar letras
    const letters = (password.match(/[a-zA-ZáéíóúÁÉÍÓÚñÑ]/g) || []).length;
    updateRequirementReg('req-letters-reg', letters >= 5);
    
    // Contar números
    const numbers = (password.match(/[0-9]/g) || []).length;
    updateRequirementReg('req-numbers-reg', numbers >= 2);
    
    // Verificar caracteres especiales
    const specialChars = (password.match(/[!@#$%^&*]/g) || []).length;
    updateRequirementReg('req-special-reg', specialChars >= 1);
    
    // Verificar longitud máxima
    const validLength = password.length <= 20;
    updateRequirementReg('req-length-reg', validLength);
}

function updateRequirementReg(elementId, isValid) {
    const element = document.getElementById(elementId);
    if (element) {
        const icon = element.querySelector('.requirement-icon');
        const text = element.querySelector('small');
        
        if (isValid) {
            icon.textContent = '✅';
            icon.style.color = '#28a745';
            text.style.color = '#28a745';
            text.style.fontWeight = 'bold';
        } else {
            icon.textContent = '❌';
            icon.style.color = '#dc3545';
            text.style.color = '#dc3545';
            text.style.fontWeight = 'normal';
        }
    }
}

// Expresiones regulares para validación
const regexNombre = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,50}$/;
const regexUsuario = /^[^\s]{5,15}$/;
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
        mostrarError(this, 'El usuario debe tener entre 5 y 15 caracteres sin espacios');
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
        mostrarError(username, 'El usuario debe tener entre 5 y 15 caracteres sin espacios');
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
