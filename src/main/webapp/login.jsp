<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%-- Página de inicio de sesión: recoge usuario/correo y contraseña. --%>
<!DOCTYPE html>
<html lang="es">
<head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Iniciar sesión · SumixKids</title>
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<%-- Barra superior de navegación (logo y enlaces principales) --%>
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
                    <a class="nav-link text-white fw-semibold px-3 active" href="${pageContext.request.contextPath}/login">
                        <i class="bi bi-box-arrow-in-right me-1"></i>Iniciar sesión
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white fw-semibold px-3" href="${pageContext.request.contextPath}/registro">
                        <i class="bi bi-person-plus me-1"></i>Registro
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<%-- Contenido principal centrado verticalmente --%>
<main class="flex-fill d-flex align-items-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-sm-10 col-md-7 col-lg-5">
                <div class="card shadow-lg border-0 rounded-4">
                    <div class="card-body p-4 p-md-5">
                        <h1 class="h3 mb-4 text-center fw-bold text-primary">Iniciar sesión</h1>
                        <%-- Mensaje de error si las credenciales no son válidas --%>
                                                <c:if test="${not empty error}">
                                                        <div class="alert alert-danger alert-dismissible fade show" role="alert" id="login-alert">
                                                                <i class="bi bi-x-circle-fill me-2"></i>${error}
                                                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                                        </div>
                                                        <script>
                                                            setTimeout(function() {
                                                                var alert = document.getElementById('login-alert');
                                                                if(alert) { alert.classList.remove('show'); alert.classList.add('fade'); }
                                                            }, 9000); // 9 segundos visible
                                                        </script>
                                                </c:if>
                        <%-- Mensaje informativo (por ejemplo tras registro) --%>
                        <c:if test="${not empty mensaje}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="bi bi-check-circle-fill me-2"></i>${mensaje}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <%-- Formulario de envío de datos de login --%>
                        <form method="post" action="${pageContext.request.contextPath}/login" class="needs-validation" novalidate>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Usuario o correo <span class="text-danger">*</span></label>
                                <input type="text" name="username" value="${username}" class="form-control" placeholder="Tu usuario o correo" required />
                                <div class="invalid-feedback">Campo obligatorio.</div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Contraseña <span class="text-danger">*</span></label>
                                <div class="position-relative">
                                    <input type="password" name="password" class="form-control password-field" placeholder="••••••••" required />
                                    <button class="btn btn-sm btn-outline-secondary position-absolute top-50 end-0 translate-middle-y me-2 toggle-pass" type="button">Ver</button>
                                </div>
                                <div class="invalid-feedback">Ingresa tu contraseña.</div>
                            </div>
                            
                            <%-- Campo CAPTCHA - Solo aparece después de fallos de login --%>
                            <c:if test="${showCaptcha}">
                                <div class="mb-3" id="captchaSection">
                                    <label class="form-label fw-semibold">
                                        <i class="bi bi-shield-check me-2"></i>Verificación de seguridad 
                                        <span class="text-danger">*</span>
                                    </label>
                                    <div class="alert alert-warning py-2 mb-2">
                                        <small><i class="bi bi-info-circle me-1"></i>
                                        Por seguridad, resuelve esta operación matemática para continuar.
                                        </small>
                                    </div>
                                    <div class="captcha-container border rounded p-3 bg-light">
                                        <div class="d-flex align-items-center justify-content-between mb-2">
                                            <div>
                                                <small class="text-muted d-block mb-2">Resuelve la operación:</small>
                                                <img src="${pageContext.request.contextPath}/captcha" 
                                                     alt="CAPTCHA" 
                                                     id="captchaImage" 
                                                     class="img-fluid rounded shadow-sm"
                                                     style="max-width: 200px; height: 60px; cursor: pointer;"
                                                     title="Haz clic para generar un nuevo CAPTCHA" />
                                            </div>
                                            <button type="button" class="btn btn-sm btn-outline-primary" onclick="refreshCaptcha()" title="Generar nuevo CAPTCHA">
                                                <i class="bi bi-arrow-clockwise"></i> Nuevo
                                            </button>
                                        </div>
                                        <input type="text" 
                                               name="captcha" 
                                               class="form-control captcha-input" 
                                               placeholder="Escribe el resultado" 
                                               autocomplete="off" 
                                               required />
                                    </div>
                                    <div class="invalid-feedback">Por favor, resuelve la operación matemática.</div>
                                </div>
                            </c:if>
                            
                            <div class="d-grid gap-2 mt-4">
                                <button class="btn btn-primary btn-lg shadow-sm" type="submit">Entrar</button>
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/registro">Crear cuenta</a>
                            </div>
                        </form>
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/recuperar_password">¿Olvidaste tu contraseña?</a>
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
<script>
    document.getElementById('year').textContent = new Date().getFullYear();
    
    // Función para refrescar el CAPTCHA (solo si está visible)
    function refreshCaptcha() {
        const img = document.getElementById('captchaImage');
        if (img) {
            img.src = '${pageContext.request.contextPath}/captcha?' + new Date().getTime();
        }
    }
    
    // Hacer clic en la imagen también refresca el CAPTCHA (solo si existe)
    const captchaImg = document.getElementById('captchaImage');
    if (captchaImg) {
        captchaImg.addEventListener('click', refreshCaptcha);
    }
    
    // Toggle password visibility
    document.querySelectorAll('.toggle-pass').forEach(btn => {
        btn.addEventListener('click', () => {
            const input = btn.closest('.position-relative').querySelector('.password-field');
            const show = input.type === 'password';
            input.type = show ? 'text' : 'password';
            btn.textContent = show ? 'Ocultar' : 'Ver';
        });
    });
    
    // Ocultar alertas después de 10 segundos
    setTimeout(() => {
        document.querySelectorAll('.alert').forEach(alert => {
            alert.classList.remove('show');
            alert.classList.add('fade');
            setTimeout(() => alert.style.display = 'none', 300);
        });
    }, 10000);
    
    // Bootstrap validation
    (() => { 
        const forms = document.querySelectorAll('.needs-validation');
        Array.from(forms).forEach(form => { 
            form.addEventListener('submit', evt => { 
                if (!form.checkValidity()) { 
                    evt.preventDefault(); 
                    evt.stopPropagation(); 
                } 
                form.classList.add('was-validated'); 
            }, false); 
        }); 
    })();
</script>
</body>
</html>
