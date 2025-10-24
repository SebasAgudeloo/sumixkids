<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Restablecer contraseña · SumixKids</title>
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
            <div class="col-12 col-sm-10 col-md-7 col-lg-6">
                <div class="card shadow-lg border-0 rounded-4">
                    <div class="card-body p-4 p-md-5">
                        <h1 class="h4 mb-4 text-center fw-bold text-primary">
                            <i class="fas fa-key me-2"></i>Restablecer contraseña
                        </h1>
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
                        <form id="restablecerForm" method="post" action="${pageContext.request.contextPath}/restablecer_password">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">
                                    <i class="fas fa-user me-1"></i>Usuario o correo <span class="text-danger">*</span>
                                </label>
                                <input type="text" name="usuario_correo" id="usuario_correo" class="form-control ${errorUsuarioCorreo ? 'is-invalid' : ''}" 
                                       placeholder="Ingresa tu usuario o correo" value="${usuario_correo}" required />
                                <div class="form-text">
                                    <i class="fas fa-info-circle me-1"></i>Debes ingresar tu usuario o tu correo electrónico
                                </div>
                                <c:if test="${errorUsuarioCorreo}">
                                    <div class="invalid-feedback">Este campo es obligatorio</div>
                                </c:if>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label fw-semibold">
                                    <i class="fas fa-lock me-1"></i>Código enviado <span class="text-danger">*</span>
                                </label>
                                <input type="text" name="codigo" id="codigo" class="form-control ${errorCodigo ? 'is-invalid' : ''}" 
                                       placeholder="ABCDEF" value="${codigo}" required 
                                       maxlength="6" pattern="[A-Za-z]{6}" style="text-transform: uppercase;" />
                                <div class="form-text">
                                    <i class="fas fa-envelope me-1"></i>Ingresa el código de <b>6 letras</b> que recibiste por correo
                                </div>
                                <c:if test="${errorCodigo}">
                                    <div class="invalid-feedback">El código debe tener <b>6 letras</b> (solo letras, sin números)</div>
                                </c:if>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label fw-semibold">
                                    <i class="fas fa-shield-alt me-1"></i>Contraseña nueva <span class="text-danger">*</span>
                                </label>
                                <div class="position-relative">
                                    <input type="password" name="nueva1" id="nueva1" class="form-control password-field ${errorPassword ? 'is-invalid' : ''}" 
                                           placeholder="Crea tu nueva contraseña" required maxlength="20" />
                                    <button class="btn btn-sm btn-outline-secondary position-absolute top-50 end-0 translate-middle-y me-2 toggle-pass" type="button" style="z-index: 10;">
                                        <i class="fas fa-eye"></i> Ver
                                    </button>
                                    <c:if test="${errorPassword}">
                                        <div class="invalid-feedback">La contraseña no cumple los requisitos</div>
                                    </c:if>
                                </div>
                                
                                <!-- Validación visual en tiempo real -->
                                <div class="mt-2 border rounded p-2 bg-light">
                                    <small class="fw-bold text-muted d-block mb-2">
                                        <i class="fas fa-clipboard-check me-1"></i>Requisitos de contraseña:
                                    </small>
                                    <div class="password-requirement d-flex align-items-center mb-1" id="req-letters">
                                        <span class="requirement-icon me-2">❌</span>
                                        <small>Mínimo 5 letras</small>
                                    </div>
                                    <div class="password-requirement d-flex align-items-center mb-1" id="req-numbers">
                                        <span class="requirement-icon me-2">❌</span>
                                        <small>Mínimo 2 números</small>
                                    </div>
                                    <div class="password-requirement d-flex align-items-center mb-1" id="req-special">
                                        <span class="requirement-icon me-2">❌</span>
                                        <small>Mínimo 1 carácter especial (!@#$%^&*)</small>
                                    </div>
                                    <div class="password-requirement d-flex align-items-center mb-1" id="req-length">
                                        <span class="requirement-icon me-2">❌</span>
                                        <small>Máximo 20 caracteres</small>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label fw-semibold">
                                    <i class="fas fa-check-double me-1"></i>Repetir contraseña nueva <span class="text-danger">*</span>
                                </label>
                                <div class="position-relative">
                                    <input type="password" name="nueva2" id="nueva2" class="form-control password-field" 
                                           placeholder="Repite la contraseña" required maxlength="20" />
                                    <button class="btn btn-sm btn-outline-secondary position-absolute top-50 end-0 translate-middle-y me-2 toggle-pass" type="button" style="z-index: 10;">
                                        <i class="fas fa-eye"></i> Ver
                                    </button>
                                </div>
                                <div class="form-text">
                                    <i class="fas fa-info-circle me-1"></i>Asegúrate de que <b>ambas contraseñas</b> coincidan
                                </div>
                            </div>
                            
                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-check me-2"></i>Confirmar cambio
                                </button>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Volver a iniciar sesión
                                </a>
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

// Toggle mostrar/ocultar contraseña
document.querySelectorAll('.toggle-pass').forEach(btn => {
    btn.addEventListener('click', () => {
        const input = btn.closest('.position-relative').querySelector('.password-field');
        const icon = btn.querySelector('i');
        const show = input.type === 'password';
        input.type = show ? 'text' : 'password';
        icon.className = show ? 'fas fa-eye-slash' : 'fas fa-eye';
        btn.innerHTML = show ? '<i class="fas fa-eye-slash"></i> Ocultar' : '<i class="fas fa-eye"></i> Ver';
    });
});

// Auto-hide alerts después de 10 segundos
setTimeout(() => {
    document.querySelectorAll('.alert-danger, .alert-success').forEach(alert => {
        alert.style.display = 'none';
    });
}, 10000);

// Validación visual en tiempo real de la contraseña
const passwordInput = document.getElementById('nueva1');
if (passwordInput) {
    passwordInput.addEventListener('input', function() {
        validatePasswordVisual(this.value);
    });
}

function validatePasswordVisual(password) {
    // Contar letras
    const letters = (password.match(/[a-zA-ZáéíóúÁÉÍÓÚñÑ]/g) || []).length;
    updateRequirement('req-letters', letters >= 5);
    
    // Contar números
    const numbers = (password.match(/[0-9]/g) || []).length;
    updateRequirement('req-numbers', numbers >= 2);
    
    // Verificar caracteres especiales
    const specialChars = (password.match(/[!@#$%^&*]/g) || []).length;
    updateRequirement('req-special', specialChars >= 1);
    
    // Verificar longitud máxima
    const validLength = password.length <= 20;
    updateRequirement('req-length', validLength);
}

function updateRequirement(elementId, isValid) {
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

// Validación del formulario antes de enviar
document.getElementById('restablecerForm').addEventListener('submit', function(e) {
    e.preventDefault();
    let isValid = true;
    
    // Validar usuario_correo
    const usuarioCorreo = document.getElementById('usuario_correo');
    if (!usuarioCorreo.value.trim()) {
        mostrarError(usuarioCorreo, 'Este campo es obligatorio');
        isValid = false;
    } else {
        limpiarError(usuarioCorreo);
    }
    
    // Validar código (6 letras)
    const codigo = document.getElementById('codigo');
    const codigoPattern = /^[A-Za-z]{6}$/;
    if (!codigoPattern.test(codigo.value)) {
        mostrarError(codigo, 'El código debe tener 6 letras (solo letras, sin números)');
        isValid = false;
    } else {
        limpiarError(codigo);
    }
    
    // Validar contraseña nueva
    const nueva1 = document.getElementById('nueva1');
    const passwordPattern = /^(?=.*[A-Za-záéíóúÁÉÍÓÚñÑ]{5,})(?=.*\d.*\d)(?=.*[!@#$%^&*])[A-Za-záéíóúÁÉÍÓÚñÑ\d!@#$%^&*]{8,20}$/;
    if (!passwordPattern.test(nueva1.value)) {
        mostrarError(nueva1, 'La contraseña no cumple los requisitos (mín. 5 letras, 2 números, 1 especial, máx. 20 caracteres)');
        isValid = false;
    } else {
        limpiarError(nueva1);
    }
    
    // Validar que las contraseñas coincidan
    const nueva2 = document.getElementById('nueva2');
    if (nueva1.value !== nueva2.value) {
        mostrarError(nueva2, 'Las contraseñas no coinciden');
        isValid = false;
    } else {
        limpiarError(nueva2);
    }
    
    if (isValid) {
        this.submit();
    }
});

// Validación en tiempo real del código (solo letras)
document.getElementById('codigo').addEventListener('input', function() {
    // Convertir a mayúsculas y permitir solo letras
    this.value = this.value.replace(/[^A-Za-z]/g, '').toUpperCase();
    if (this.value.length > 6) {
        this.value = this.value.substring(0, 6);
    }
});

function mostrarError(input, mensaje) {
    const parent = input.closest('.mb-3');
    let errorDiv = parent.querySelector('.custom-error-message');
    
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'custom-error-message text-danger mt-1 small';
        parent.appendChild(errorDiv);
    }
    
    errorDiv.innerHTML = '<i class="fas fa-exclamation-circle me-1"></i>' + mensaje;
    input.classList.add('is-invalid');
}

function limpiarError(input) {
    const parent = input.closest('.mb-3');
    const errorDiv = parent.querySelector('.custom-error-message');
    if (errorDiv) {
        errorDiv.remove();
    }
    input.classList.remove('is-invalid');
}
</script>
</body>
</html>