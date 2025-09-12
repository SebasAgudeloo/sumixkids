<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Página mostrada después de iniciar sesión correctamente --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Bienvenido · SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<%-- Barra de navegación con opción de salir --%>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">SumixKids</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarsExample" aria-controls="navbarsExample" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarsExample">
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/logout">Cerrar sesión</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="flex-fill py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8 text-center">
                <h1 class="display-6 fw-bold text-white mb-3">¡Bienvenido!</h1>
                <p class="lead text-white-50 mb-4">Has iniciado sesión correctamente.</p>
                <a class="btn btn-outline-light px-4" href="${pageContext.request.contextPath}/logout">Cerrar sesión</a>
            </div>
        </div>
    </div>
</main>

<footer class="py-3 bg-dark mt-auto text-center text-white-50 small">
    © <span id="year"></span> SumixKids
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script>document.getElementById('year').textContent = new Date().getFullYear();</script>
</body>
</html>
