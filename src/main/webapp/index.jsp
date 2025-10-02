<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%-- Página de inicio pública (landing) --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>SumixKids · Inicio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<%-- Barra de navegación principal --%>
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
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/registro">Registro</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="flex-fill d-flex align-items-center py-5">
    <div class="container">
        <div class="row justify-content-center text-center">
            <div class="col-lg-8">
                <h1 class="display-5 fw-bold text-white-contrast drop-shadow mb-4">Bienvenido a <span class="text-warning">SumixKids</span></h1>
                <p class="lead text-white-contrast mb-4">Plataforma educativa ligera para estudiantes, padres y docentes.</p>
                <div class="d-flex flex-wrap justify-content-center gap-3">
                    <a class="btn btn-lg btn-light shadow-sm px-4" href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
                    <a class="btn btn-lg btn-outline-light shadow-sm px-4" href="${pageContext.request.contextPath}/registro">Crear cuenta</a>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="py-3 bg-dark mt-auto text-center text-white-50 small">
    © <span id="year"></span> SumixKids · Todos los derechos reservados
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script>document.getElementById('year').textContent = new Date().getFullYear();</script>
</body>
</html>
