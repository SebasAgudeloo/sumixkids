<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Error · SumixKids</title>
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
                 style="height: 36px; width: auto; margin-right: 8px;"/>
            SumixKids
        </a>
    </div>
</nav>

<main class="flex-fill d-flex align-items-center justify-content-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card shadow-lg border-0">
                    <div class="card-body text-center p-5">
                        <i class="fas fa-exclamation-triangle text-warning fa-5x mb-4"></i>
                        <h1 class="display-4 fw-bold mb-3">¡Oops! Algo salió mal</h1>
                        
                        <c:choose>
                            <c:when test="${not empty error}">
                                <div class="alert alert-danger mt-4">
                                    <i class="fas fa-times-circle me-2"></i>
                                    <strong>Error:</strong> ${error}
                                </div>
                            </c:when>
                            <c:when test="${not empty mensaje}">
                                <div class="alert alert-warning mt-4">
                                    <i class="fas fa-info-circle me-2"></i>
                                    ${mensaje}
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="lead text-muted mb-4">
                                    Ha ocurrido un error inesperado. Por favor, intenta nuevamente.
                                </p>
                            </c:otherwise>
                        </c:choose>

                        <div class="mt-4">
                            <a href="javascript:history.back()" class="btn btn-outline-secondary btn-lg me-2">
                                <i class="fas fa-arrow-left me-2"></i>Volver
                            </a>
                            <a href="${pageContext.request.contextPath}/bienvenida" class="btn btn-primary btn-lg">
                                <i class="fas fa-home me-2"></i>Ir al Inicio
                            </a>
                        </div>

                        <!-- Información técnica (solo en desarrollo) -->
                        <c:if test="${pageContext.request.serverName == 'localhost'}">
                            <div class="mt-5 text-start">
                                <details class="text-muted small">
                                    <summary class="fw-bold cursor-pointer">Información técnica (solo visible en desarrollo)</summary>
                                    <div class="mt-3 p-3 bg-light rounded">
                                        <c:if test="${not empty pageContext.errorData}">
                                            <p><strong>Código de estado:</strong> ${pageContext.errorData.statusCode}</p>
                                            <p><strong>URI solicitada:</strong> ${pageContext.errorData.requestURI}</p>
                                            <p><strong>Servlet:</strong> ${pageContext.errorData.servletName}</p>
                                        </c:if>
                                        <c:if test="${not empty exception}">
                                            <p><strong>Tipo de error:</strong> Error del sistema</p>
                                            <p><strong>Mensaje:</strong> ${exception.message}</p>
                                        </c:if>
                                    </div>
                                </details>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="bg-primary text-white text-center py-3 mt-auto">
    <div class="container">
        <small>&copy; 2025 SumixKids. Todos los derechos reservados.</small>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
