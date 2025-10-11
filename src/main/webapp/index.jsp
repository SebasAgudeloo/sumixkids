<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%-- Página de inicio pública - SumixKids Software Educativo --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>SumixKids - Software Educativo para la Práctica de Sumas</title>
    <meta name="description" content="Software educativo adaptativo para estudiantes de 2°, 3° y 4° grado. Aprende sumas de forma divertida, contextualizada y personalizada.">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">

<%-- Navegación principal --%>
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
                    <a class="nav-link text-white fw-semibold px-3" href="${pageContext.request.contextPath}/registro">
                        <i class="bi bi-person-plus me-1"></i>Registro
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<%-- Sección Hero --%>
<section class="hero-section py-5 flex-fill d-flex align-items-center">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-lg-6 text-center text-lg-start mb-5 mb-lg-0">
                <div class="hero-content">
                    <h1 class="display-4 fw-bold text-white-contrast drop-shadow mb-4">
                        Aprende <span class="text-warning">Sumas</span><br>
                        de forma <span class="text-info">Divertida</span>
                    </h1>
                    <p class="lead text-white-contrast mb-4 fs-5">
                        Software educativo adaptativo para estudiantes de <strong>3°, 4° y 5° grado</strong>. 
                        Practica sumas con ejercicios contextualizados, personajes animados y niveles adaptativos.
                    </p>
                    <div class="d-flex flex-column flex-sm-row gap-3 justify-content-center justify-content-lg-start">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-light btn-lg shadow-lg px-4 py-3 fw-bold">
                            <i class="bi bi-play-circle me-2"></i>Comenzar a Aprender
                        </a>
                        <a href="${pageContext.request.contextPath}/registro" class="btn btn-outline-light btn-lg shadow-lg px-4 py-3 fw-bold">
                            <i class="bi bi-person-plus me-2"></i>Crear Cuenta Gratis
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="hero-image text-center">
                    <div class="card border-0 shadow-lg mx-auto" style="max-width: 400px; background: rgba(255,255,255,0.95);">
                        <div class="card-body p-4">
                            <div class="display-1 text-primary mb-3">
                                <i class="bi bi-calculator"></i>
                            </div>
                            <h4 class="text-primary fw-bold mb-3">¡Practica Sumas!</h4>
                            <div class="text-center mb-3">
                                <span class="example-badge example-badge-primary">2 + 3 = ?</span>
                                <span class="example-badge example-badge-success">15 + 27 = ?</span>
                                <span class="example-badge example-badge-warning">123 + 456 = ?</span>
                            </div>
                            <p class="text-muted small mb-0">Ejercicios adaptativos para cada nivel</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<%-- Sección de Características --%>
<div class="container py-5">
        <div class="row text-center mb-5">
            <div class="col-12">
                <h2 class="display-6 fw-bold text-white-contrast drop-shadow mb-3">¿Por qué elegir SumixKids?</h2>
                <p class="lead text-white-contrast">Diseñado específicamente para el aprendizaje de sumas en educación primaria</p>
            </div>
        </div>
        <div class="row g-4">
            <div class="col-md-6 col-lg-3">
                <div class="card h-100 border-0 shadow-lg text-center">
                    <div class="card-body p-4">
                        <div class="feature-icon text-primary mb-3">
                            <i class="bi bi-trophy-fill display-4"></i>
                        </div>
                        <h5 class="card-title fw-bold text-primary">Dificultad Adaptativa</h5>
                        <p class="card-text text-muted">El sistema ajusta automáticamente la dificultad según tu progreso y rendimiento.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3">
                <div class="card h-100 border-0 shadow-lg text-center">
                    <div class="card-body p-4">
                        <div class="feature-icon text-success mb-3">
                            <i class="bi bi-emoji-smile-fill display-4"></i>
                        </div>
                        <h5 class="card-title fw-bold text-success">Aprendizaje Lúdico</h5>
                        <p class="card-text text-muted">Personajes animados, recompensas y juegos que hacen divertido aprender sumas.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3">
                <div class="card h-100 border-0 shadow-lg text-center">
                    <div class="card-body p-4">
                        <div class="feature-icon text-warning mb-3">
                            <i class="bi bi-house-heart-fill display-4"></i>
                        </div>
                        <h5 class="card-title fw-bold text-warning">Contextualizado</h5>
                        <p class="card-text text-muted">Ejercicios basados en situaciones cotidianas: compras, juegos, familia.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3">
                <div class="card h-100 border-0 shadow-lg text-center">
                    <div class="card-body p-4">
                        <div class="feature-icon text-info mb-3">
                            <i class="bi bi-shield-fill-check display-4"></i>
                        </div>
                        <h5 class="card-title fw-bold text-info">Entorno Seguro</h5>
                        <p class="card-text text-muted">Sin anuncios, sin enlaces externos. Un espacio seguro para que los niños aprendan.</p>
                    </div>
                </div>
            </div>
        </div>
</div>

<%-- Sección para quién es --%>
<div class="container py-5">
        <div class="row text-center mb-5">
            <div class="col-12">
                <h2 class="display-6 fw-bold text-white-contrast drop-shadow mb-3">¿Para quién es SumixKids?</h2>
            </div>
        </div>
        <div class="row g-4 justify-content-center">
            <div class="col-lg-4 col-md-6">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="feature-icon text-primary mb-3">
                            <i class="bi bi-mortarboard-fill display-3"></i>
                        </div>
                        <h4 class="fw-bold text-primary mb-3">Estudiantes</h4>
                        <p class="text-muted mb-3">De 3°, 4° y 5° grado que quieren aprender sumas de forma divertida y a su propio ritmo.</p>
                        <div class="badge bg-primary-soft text-primary">8 - 10 años</div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="feature-icon text-success mb-3">
                            <i class="bi bi-person-workspace display-3"></i>
                        </div>
                        <h4 class="fw-bold text-success mb-3">Docentes</h4>
                        <p class="text-muted mb-3">Profesores que buscan herramientas innovadoras para reforzar el aprendizaje de matemáticas.</p>
                        <div class="badge bg-success-soft text-success">Herramienta didáctica</div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-6">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-body text-center p-4">
                        <div class="feature-icon text-warning mb-3">
                            <i class="bi bi-heart-fill display-3"></i>
                        </div>
                        <h4 class="fw-bold text-warning mb-3">Padres</h4>
                        <p class="text-muted mb-3">Familias que quieren apoyar el aprendizaje de sus hijos con control de tiempo y progreso.</p>
                        <div class="badge bg-warning-soft text-warning">Control parental</div>
                    </div>
                </div>
            </div>
        </div>
</div>

<%-- Call to Action Final --%>
<div class="container py-5">
        <div class="row justify-content-center text-center">
            <div class="col-lg-8">
                <h2 class="display-5 fw-bold text-white-contrast drop-shadow mb-4">
                    ¡Comienza la aventura de aprender sumas!
                </h2>
                <p class="lead text-white-contrast mb-4">
                    Únete a cientos de estudiantes que ya están mejorando sus habilidades matemáticas con SumixKids.
                </p>
                <div class="d-flex flex-column flex-sm-row gap-3 justify-content-center">
                    <a href="${pageContext.request.contextPath}/registro" class="btn btn-warning btn-lg shadow-lg px-5 py-3 fw-bold">
                        <i class="bi bi-star-fill me-2"></i>Crear Cuenta Gratis
                    </a>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-lg shadow-lg px-5 py-3 fw-bold">
                        <i class="bi bi-box-arrow-in-right me-2"></i>Ya tengo cuenta
                    </a>
                </div>
            </div>
        </div>
    </div>
</section>

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

// Animación suave para los botones
document.querySelectorAll('.btn').forEach(btn => {
    btn.addEventListener('mouseenter', function() {
        this.style.transform = 'translateY(-2px)';
    });
    btn.addEventListener('mouseleave', function() {
        this.style.transform = 'translateY(0)';
    });
});
</script>
</body>
</html>
