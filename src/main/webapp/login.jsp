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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<%-- Barra superior de navegación (logo y enlaces principales) --%>
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
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/login">Iniciar sesión</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/registro">Registro</a></li>
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
                                <input type="text" name="username" class="form-control" placeholder="Tu usuario o correo" required />
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
                            <div class="d-grid gap-2 mt-4">
                                <button class="btn btn-primary btn-lg shadow-sm" type="submit">Entrar</button>
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/registro">Crear cuenta</a>
                            </div>
                        </form>
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/recuperar">¿Olvidaste tu contraseña?</a>
                        </div>
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
    document.querySelectorAll('.toggle-pass').forEach(btn => {
        btn.addEventListener('click', () => {
            const input = btn.closest('.position-relative').querySelector('.password-field');
            const show = input.type === 'password';
            input.type = show ? 'text' : 'password';
            btn.textContent = show ? 'Ocultar' : 'Ver';
        });
    });
    // Ocultar alertas después de 3 segundos
    setTimeout(() => {
        document.querySelectorAll('.alert').forEach(alert => {
            alert.classList.remove('show');
            alert.classList.add('fade');
            setTimeout(() => alert.style.display = 'none', 300);
        });
    }, 3000);
    // Bootstrap validation
    (() => { const forms = document.querySelectorAll('.needs-validation');
        Array.from(forms).forEach(form => { form.addEventListener('submit', evt => { if (!form.checkValidity()) { evt.preventDefault(); evt.stopPropagation(); } form.classList.add('was-validated'); }, false); }); })();
</script>
</body>
</html>
