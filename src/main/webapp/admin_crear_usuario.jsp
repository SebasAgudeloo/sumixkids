<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Crear Usuario · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">

    <!-- Navbar principal -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/bienvenida">
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
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-user-circle me-2" style="font-size: 1.2rem;"></i>
                            ${sessionScope.usuario.nombres} ${sessionScope.usuario.apellidos}
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/dispositivos_reconocidos">
                                <i class="fas fa-mobile-alt me-2"></i>Dispositivos Reconocidos</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt me-2"></i>Cerrar sesión</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <%-- Contenido principal --%>
    <main class="flex-fill py-4">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                    <div class="card shadow-lg border-0 rounded-4">
                        <div class="card-body p-4 p-md-5">
                            <h1 class="h3 mb-4 text-center fw-bold text-primary">
                                <i class="fas fa-user-plus me-2"></i>Registrar Usuario
                            </h1>
                            
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
                            <form id="registroForm" method="post" action="${pageContext.request.contextPath}/admin_crear_usuario">
                                <div class="form-group">
                                    <label for="nombres">Nombres:</label>
                                    <input type="text" class="form-control ${errorNombres ? 'is-invalid' : ''}" id="nombres" name="nombres" 
                                           value="${nombres}" required maxlength="30"
                                           placeholder="Ingrese los nombres del usuario a registrar (solo letras, máx. 30 caracteres)">
                                    <c:if test="${errorNombres}">
                                        <div class="invalid-feedback">El nombre solo puede contener letras y espacios (máximo 30 caracteres)</div>
                                    </c:if>
                                </div>
                                
                                <div class="form-group">
                                    <label for="apellidos">Apellidos:</label>
                                    <input type="text" class="form-control ${errorApellidos ? 'is-invalid' : ''}" id="apellidos" name="apellidos" 
                                           value="${apellidos}" required maxlength="30"
                                           placeholder="Ingrese los apellidos del usuario a registrar (solo letras, máx. 30 caracteres)">
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
                                
                                <%-- Selector de rol --%>
                                <div class="form-group">
                                    <label for="rolId" class="form-label fw-semibold">
                                        <i class="fas fa-user-tag me-1"></i>Rol <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select ${errorRolId ? 'is-invalid' : ''}" id="rolId" name="rolId" required>
                                        <option value="">Seleccionar rol...</option>
                                        <option value="1" ${rolId == '1' ? 'selected' : ''}>👑 Administrador</option>
                                        <option value="2" ${rolId == '2' ? 'selected' : ''}>👨‍🏫 Docente</option>
                                        <option value="3" ${rolId == '3' ? 'selected' : ''}>🎓 Estudiante</option>
                                        <option value="4" ${rolId == '4' ? 'selected' : ''}>👨‍👩‍👧‍👦 Padre</option>
                                    </select>
                                    <c:if test="${errorRolId}">
                                        <div class="invalid-feedback">Debe seleccionar un rol válido</div>
                                    </c:if>
                                </div>
                                
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
                                               placeholder="Crea su contraseña" />
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
                                    <button class="btn btn-success btn-lg shadow-sm fw-bold" type="submit">
                                        <i class="fas fa-user-plus me-2"></i>Registrar Usuario
                                    </button>
                                    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/usuarios">
                                        <i class="fas fa-arrow-left me-2"></i>Volver a Gestión de Usuarios
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
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>
    <script>
        document.getElementById('year').textContent = new Date().getFullYear();

        // Lógica para mostrar/ocultar campo de grado según el rol seleccionado
        document.getElementById('rolId').addEventListener('change', function() {
            const gradoContainer = document.getElementById('gradoContainer');
            const gradoSelect = document.getElementById('grado');
            const gradoRequired = document.getElementById('gradoRequired');
            
            if (this.value === '3') { // Estudiante
                gradoContainer.style.display = 'block';
                gradoSelect.required = true;
                gradoRequired.style.display = 'inline';
            } else {
                gradoContainer.style.display = 'none';
                gradoSelect.required = false;
                gradoSelect.value = '';
                gradoRequired.style.display = 'none';
            }
        });

        // Mostrar el campo de grado si ya está seleccionado "Estudiante" (para mantener estado en validación)
        if (document.getElementById('rolId').value === '3') {
            document.getElementById('gradoContainer').style.display = 'block';
            document.getElementById('grado').required = true;
        }

        // Validación de contraseñas en tiempo real
        const passwordInput = document.getElementById('passwordInput');
        const confirmInput = document.querySelector('input[name="confirm"]');

        function validatePassword(password) {
            const requirements = {
                letters: (password.match(/[a-zA-Z]/g) || []).length >= 5,
                numbers: (password.match(/[0-9]/g) || []).length >= 2,
                special: /[!@#$%^&*]/.test(password),
                length: password.length <= 20 && password.length > 0
            };

            // Actualizar indicadores visuales
            document.getElementById('req-letters-reg').querySelector('.requirement-icon').textContent = requirements.letters ? '✅' : '❌';
            document.getElementById('req-numbers-reg').querySelector('.requirement-icon').textContent = requirements.numbers ? '✅' : '❌';
            document.getElementById('req-special-reg').querySelector('.requirement-icon').textContent = requirements.special ? '✅' : '❌';
            document.getElementById('req-length-reg').querySelector('.requirement-icon').textContent = requirements.length ? '✅' : '❌';

            return requirements.letters && requirements.numbers && requirements.special && requirements.length;
        }

        passwordInput.addEventListener('input', function() {
            validatePassword(this.value);
        });

        // Funcionalidad para mostrar/ocultar contraseñas
        document.querySelectorAll('.toggle-pass').forEach(button => {
            button.addEventListener('click', function() {
                const passwordField = this.parentNode.querySelector('.password-field');
                const isPassword = passwordField.type === 'password';
                passwordField.type = isPassword ? 'text' : 'password';
                this.textContent = isPassword ? 'Ocultar' : 'Ver';
            });
        });

        // Validación del formulario
        document.getElementById('registroForm').addEventListener('submit', function(e) {
            const password = passwordInput.value;
            const confirm = confirmInput.value;

            if (!validatePassword(password)) {
                e.preventDefault();
                Swal.fire({
                    title: '🔒 Contraseña inválida',
                    text: 'La contraseña no cumple con todos los requisitos.',
                    icon: 'error',
                    confirmButtonText: 'Entendido',
                    confirmButtonColor: '#dc3545'
                });
                return;
            }

            if (password !== confirm) {
                e.preventDefault();
                Swal.fire({
                    title: '🔑 Error de confirmación',
                    text: 'Las contraseñas no coinciden.',
                    icon: 'error',
                    confirmButtonText: 'Entendido',
                    confirmButtonColor: '#dc3545'
                });
                return;
            }
        });
    </script>
</body>
</html>
