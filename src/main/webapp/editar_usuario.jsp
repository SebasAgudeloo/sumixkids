<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Editar Usuario · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">

<%-- Barra de navegación --%>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/bienvenida">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" style="height: 36px; width: auto; margin-right: 8px;"/>
            SumixKids
        </a>
    </div>
</nav>

<%-- Contenido principal --%>
<main class="flex-fill d-flex align-items-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                <div class="card shadow-lg border-0 rounded-4">
                    <div class="card-body p-4 p-md-5">
                        <h1 class="h3 mb-4 text-center fw-bold text-primary">
                            <i class="fas fa-user-edit me-2"></i>Editar Usuario
                        </h1>
                        
                        <%-- Mensajes de error --%>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty errores}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                <strong>Errores encontrados:</strong>
                                <ul class="mb-0 mt-2">
                                    <c:forEach var="err" items="${errores}">
                                        <li>${err}</li>
                                    </c:forEach>
                                </ul>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <%-- Formulario de edición --%>
                        <form id="editarForm" method="post" action="${pageContext.request.contextPath}/editar_usuario">
                            <input type="hidden" name="id" value="${usuarioEditar.id}" />
                            
                            <%-- Username (solo lectura) --%>
                            <div class="form-group mb-3">
                                <label for="username" class="form-label fw-semibold">
                                    <i class="fas fa-user me-1"></i>Usuario
                                </label>
                                <input type="text" class="form-control" id="username" value="${usuarioEditar.username}" readonly disabled />
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>El nombre de usuario no se puede modificar
                                </small>
                            </div>
                            
                            <%-- Nombres --%>
                            <div class="form-group mb-3">
                                <label for="nombres" class="form-label fw-semibold">
                                    <i class="fas fa-signature me-1"></i>Nombres <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control ${errorNombres ? 'is-invalid' : ''}" id="nombres" name="nombres" 
                                       value="${usuarioEditar.nombres}" required maxlength="30"
                                       placeholder="Ingrese los nombres">
                                <c:if test="${errorNombres}">
                                    <div class="invalid-feedback">El nombre solo puede contener letras y espacios (máximo 30 caracteres)</div>
                                </c:if>
                            </div>
                            
                            <%-- Apellidos --%>
                            <div class="form-group mb-3">
                                <label for="apellidos" class="form-label fw-semibold">
                                    <i class="fas fa-file-signature me-1"></i>Apellidos <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control ${errorApellidos ? 'is-invalid' : ''}" id="apellidos" name="apellidos" 
                                       value="${usuarioEditar.apellidos}" required maxlength="30"
                                       placeholder="Ingrese los apellidos">
                                <c:if test="${errorApellidos}">
                                    <div class="invalid-feedback">El apellido solo puede contener letras y espacios (máximo 30 caracteres)</div>
                                </c:if>
                            </div>
                            
                            <%-- Email --%>
                            <div class="form-group mb-3">
                                <label for="email" class="form-label fw-semibold">
                                    <i class="fas fa-envelope me-1"></i>Correo electrónico <span class="text-danger">*</span>
                                </label>
                                <input type="email" class="form-control ${errorEmail ? 'is-invalid' : ''}" id="email" name="email" 
                                       value="${usuarioEditar.email}" required 
                                       placeholder="ejemplo@gmail.com">
                                <c:if test="${errorEmail}">
                                    <div class="invalid-feedback">El correo electrónico debe tener un formato válido</div>
                                </c:if>
                            </div>
                            
                            <%-- Rol --%>
                            <div class="form-group mb-3">
                                <label for="rolId" class="form-label fw-semibold">
                                    <i class="fas fa-user-tag me-1"></i>Rol <span class="text-danger">*</span>
                                </label>
                                <select class="form-select ${errorRolId ? 'is-invalid' : ''}" id="rolId" name="rolId" required>
                                    <option value="">Seleccionar rol...</option>
                                    <option value="1" ${usuarioEditar.rolId == 1 ? 'selected' : ''}>👑 Administrador</option>
                                    <option value="2" ${usuarioEditar.rolId == 2 ? 'selected' : ''}>👨‍🏫 Docente</option>
                                    <option value="3" ${usuarioEditar.rolId == 3 ? 'selected' : ''}>🎓 Estudiante</option>
                                    <option value="4" ${usuarioEditar.rolId == 4 ? 'selected' : ''}>👨‍👩‍👧‍👦 Acompañante</option>
                                </select>
                                <c:if test="${errorRolId}">
                                    <div class="invalid-feedback">Debe seleccionar un rol válido</div>
                                </c:if>
                            </div>
                            
                            <%-- Grado (solo para estudiantes) --%>
                            <c:choose>
                                <c:when test="${usuarioEditar.rolId == 3}">
                                    <div class="form-group mb-3" id="gradoContainer" style="display:block;">
                                </c:when>
                                <c:otherwise>
                                    <div class="form-group mb-3" id="gradoContainer" style="display:none;">
                                </c:otherwise>
                            </c:choose>
                                <label for="grado" class="form-label fw-semibold">
                                    <i class="fas fa-graduation-cap me-1"></i>Grado <span class="text-danger" id="gradoRequired">*</span>
                                </label>
                                <select class="form-select ${errorGrado ? 'is-invalid' : ''}" id="grado" name="grado">
                                    <option value="">Seleccionar grado...</option>
                                    <option value="3" ${usuarioEditar.grado == '3' || usuarioEditar.grado == '3°' ? 'selected' : ''}>3° (Tercero)</option>
                                    <option value="4" ${usuarioEditar.grado == '4' || usuarioEditar.grado == '4°' ? 'selected' : ''}>4° (Cuarto)</option>
                                    <option value="5" ${usuarioEditar.grado == '5' || usuarioEditar.grado == '5°' ? 'selected' : ''}>5° (Quinto)</option>
                                </select>
                                <c:if test="${errorGrado}">
                                    <div class="invalid-feedback">El grado debe ser 3°, 4° o 5°</div>
                                </c:if>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>El grado es obligatorio para estudiantes
                                </small>
                            </div>
                            
                            <%-- Nueva contraseña (opcional) --%>
                            <div class="form-group mb-3">
                                <label for="nuevaPassword" class="form-label fw-semibold">
                                    <i class="fas fa-key me-1"></i>Nueva contraseña (opcional)
                                </label>
                                <div class="position-relative">
                                    <input type="password" name="nuevaPassword" id="nuevaPassword" class="form-control password-field ${errorPassword ? 'is-invalid' : ''}" 
                                           maxlength="20" placeholder="Dejar en blanco para no cambiar" />
                                    <button class="btn btn-sm btn-outline-secondary position-absolute top-50 end-0 translate-middle-y me-2 toggle-pass" type="button" style="z-index: 10;">
                                        <i class="fas fa-eye"></i> Ver
                                    </button>
                                    <c:if test="${errorPassword}">
                                        <div class="invalid-feedback">La contraseña no cumple los requisitos</div>
                                    </c:if>
                                </div>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>Deja este campo vacío si no deseas cambiar la contraseña
                                </small>
                                
                                <%-- Validación visual --%>
                                <div class="mt-2" id="passwordRequirements" style="display: none;">
                                    <div class="border rounded p-2 bg-light">
                                        <small class="fw-bold text-muted d-block mb-2">
                                            <i class="fas fa-clipboard-check me-1"></i>Requisitos:
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
                            </div>
                            
                            <%-- Información adicional --%>
                            <div class="alert alert-info mb-4">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>Información del usuario:</strong>
                                <ul class="mb-0 mt-2">
                                    <li><strong>ID:</strong> ${usuarioEditar.id}</li>
                                    <li><strong>Fecha de registro:</strong> ${util:formatearFecha(usuarioEditar.fechaRegistro)}</li>
                                    <c:if test="${not empty usuarioEditar.ultimaConexion}">
                                        <li><strong>Última conexión:</strong> ${util:formatearFecha(usuarioEditar.ultimaConexion)}</li>
                                    </c:if>
                                </ul>
                            </div>
                            
                            <%-- Botones --%>
                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-save me-2"></i>Guardar Cambios
                                </button>
                                <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Cancelar
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
<script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>
<script>
document.getElementById('year').textContent = new Date().getFullYear();

// Mostrar/ocultar campo de grado según el rol
const rolSelect = document.getElementById('rolId');
const gradoContainer = document.getElementById('gradoContainer');
const gradoSelect = document.getElementById('grado');

if (rolSelect) {
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

// Auto-hide alerts
setTimeout(() => {
    document.querySelectorAll('.alert-danger, .alert-success').forEach(alert => {
        alert.style.display = 'none';
    });
}, 10000);

// Validación visual de contraseña
const passwordInput = document.getElementById('nuevaPassword');
const passwordRequirements = document.getElementById('passwordRequirements');

if (passwordInput) {
    passwordInput.addEventListener('input', function() {
        if (this.value.length > 0) {
            passwordRequirements.style.display = 'block';
            validatePasswordVisual(this.value);
        } else {
            passwordRequirements.style.display = 'none';
        }
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

// Validación en tiempo real de campos
const regexNombre = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{1,30}$/;
const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

document.getElementById('nombres').addEventListener('input', function() {
    if (!regexNombre.test(this.value)) {
        mostrarError(this, 'El nombre solo puede contener letras y espacios (máx. 30)');
    } else {
        limpiarError(this);
    }
});

document.getElementById('apellidos').addEventListener('input', function() {
    if (!regexNombre.test(this.value)) {
        mostrarError(this, 'El apellido solo puede contener letras y espacios (máx. 30)');
    } else {
        limpiarError(this);
    }
});

document.getElementById('email').addEventListener('input', function() {
    if (!regexCorreo.test(this.value)) {
        mostrarError(this, 'Ingrese un correo electrónico válido');
    } else {
        limpiarError(this);
    }
});

function mostrarError(input, mensaje) {
    const parent = input.closest('.form-group');
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
    const parent = input.closest('.form-group');
    const errorDiv = parent.querySelector('.custom-error-message');
    if (errorDiv) {
        errorDiv.remove();
    }
    input.classList.remove('is-invalid');
}
</script>
</body>
</html>
