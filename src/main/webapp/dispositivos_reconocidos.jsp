<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%-- Página de gestión de dispositivos reconocidos --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dispositivos Reconocidos - SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="bg-gradient-primary">

<%-- Navegación --%>
<nav class="navbar navbar-expand-lg navbar-dark shadow-lg" style="background: rgba(37, 99, 235, 0.95); backdrop-filter: blur(10px);">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/bienvenida">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" 
                 style="height: 40px; width: auto; margin-right: 12px; border-radius: 8px;"/>
            SumixKids
        </a>
        <div class="navbar-nav ms-auto">
            <a class="nav-link text-white" href="${pageContext.request.contextPath}/bienvenida">
                <i class="bi bi-arrow-left me-1"></i>Volver al Dashboard
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-lg-10">
            
            <%-- Encabezado --%>
            <div class="card shadow-lg mb-4 rounded-4 overflow-hidden">
                <div class="card-header bg-primary text-white rounded-top-4">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-shield-check me-3" style="font-size: 1.5rem;"></i>
                        <div>
                            <h4 class="mb-1">Dispositivos Reconocidos</h4>
                            <small class="opacity-75">Gestiona los dispositivos en los que no necesitas autenticación de dos factores</small>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <div class="alert alert-info d-flex align-items-center">
                        <i class="bi bi-info-circle me-2"></i>
                        <div>
                            <strong>¿Cómo funciona?</strong> Después de 3 inicios exitosos con 2FA desde el mismo dispositivo, 
                            ya no se te pedirá el código por 15 días. Puedes eliminar dispositivos si ya no los usas.
                        </div>
                    </div>
                </div>
            </div>

            <%-- Mensajes --%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="bi bi-exclamation-triangle me-2"></i>
                    ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <c:if test="${not empty mensaje}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="bi bi-check-circle me-2"></i>
                    ${mensaje}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <%-- Lista de dispositivos --%>
            <div class="card shadow-lg rounded-4 overflow-hidden">
                <div class="card-header bg-light rounded-top-4">
                    <h5 class="mb-0">
                        <i class="bi bi-devices me-2"></i>
                        Tus Dispositivos (${dispositivos.size()})
                    </h5>
                </div>
                <div class="card-body">
                    
                    <c:choose>
                        <c:when test="${empty dispositivos}">
                            <div class="text-center py-5">
                                <i class="bi bi-device-hdd text-muted" style="font-size: 3rem;"></i>
                                <h5 class="text-muted mt-3">No hay dispositivos reconocidos</h5>
                                <p class="text-muted">Los dispositivos aparecerán aquí después de completar 3 veces el proceso de 2FA.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <c:forEach var="dispositivo" items="${dispositivos}" varStatus="status">
                                    <div class="col-lg-6 mb-4">
                                        <div class="card h-100 ${dispositivo.activo ? 'border-success' : 'border-warning'}">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between align-items-start mb-3">
                                                    <div class="d-flex align-items-center">
                                                        <c:choose>
                                                            <c:when test="${dispositivo.navegador == 'Chrome'}">
                                                                <i class="bi bi-browser-chrome me-2 text-primary" style="font-size: 1.25rem;"></i>
                                                            </c:when>
                                                            <c:when test="${dispositivo.navegador == 'Firefox'}">
                                                                <i class="bi bi-browser-firefox me-2 text-primary" style="font-size: 1.25rem;"></i>
                                                            </c:when>
                                                            <c:when test="${dispositivo.navegador == 'Safari'}">
                                                                <i class="bi bi-browser-safari me-2 text-primary" style="font-size: 1.25rem;"></i>
                                                            </c:when>
                                                            <c:when test="${dispositivo.navegador == 'Edge'}">
                                                                <i class="bi bi-browser-edge me-2 text-primary" style="font-size: 1.25rem;"></i>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <i class="bi bi-globe me-2 text-primary" style="font-size: 1.25rem;"></i>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <h6 class="mb-0">${dispositivo.navegador}</h6>
                                                    </div>
                                                    <span class="badge ${dispositivo.activo ? 'bg-success' : 'bg-warning text-dark'}">
                                                        ${dispositivo.activo ? 'Activo' : 'Expirado'}
                                                    </span>
                                                </div>
                                                
                                                <div class="mb-3">
                                                    <small class="text-muted d-block mb-1">Device ID:</small>
                                                    <code class="small">${dispositivo.deviceId}</code>
                                                </div>
                                                
                                                <div class="row text-center mb-3">
                                                    <div class="col-6">
                                                        <div class="border-end">
                                                            <h5 class="text-primary mb-0">${dispositivo.contador2FA}/3</h5>
                                                            <small class="text-muted">Completados</small>
                                                        </div>
                                                    </div>
                                                    <div class="col-6">
                                                        <h6 class="mb-0">
                                                            <fmt:formatDate value="${dispositivo.fechaUltimo2FA}" pattern="dd/MM/yyyy"/>
                                                        </h6>
                                                        <small class="text-muted">Último 2FA</small>
                                                    </div>
                                                </div>
                                                
                                                <div class="progress mb-3" style="height: 8px;">
                                                    <c:choose>
                                                        <c:when test="${dispositivo.contador2FA == 1}">
                                                            <div class="progress-bar bg-primary" role="progressbar" style="width: 33%"></div>
                                                        </c:when>
                                                        <c:when test="${dispositivo.contador2FA == 2}">
                                                            <div class="progress-bar bg-primary" role="progressbar" style="width: 66%"></div>
                                                        </c:when>
                                                        <c:when test="${dispositivo.contador2FA >= 3}">
                                                            <div class="progress-bar bg-success" role="progressbar" style="width: 100%"></div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="progress-bar bg-secondary" role="progressbar" style="width: 0%"></div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <small class="text-muted">
                                                        <i class="bi bi-calendar-plus me-1"></i>
                                                        Registrado: <fmt:formatDate value="${dispositivo.fechaRegistro}" pattern="dd/MM/yyyy"/>
                                                    </small>
                                                    
                                                    <form method="post" class="d-inline" 
                                                          onsubmit="return confirm('¿Estás seguro de eliminar este dispositivo? Tendrás que completar 2FA nuevamente.')">
                                                        <input type="hidden" name="action" value="eliminar">
                                                        <input type="hidden" name="deviceId" value="${dispositivo.deviceId}">
                                                        <button type="submit" class="btn btn-outline-danger btn-sm">
                                                            <i class="bi bi-trash3"></i>
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <%-- Información adicional --%>
            <div class="card mt-4 border-info">
                <div class="card-body">
                    <h6 class="text-info">
                        <i class="bi bi-info-circle me-2"></i>
                        Información sobre la seguridad
                    </h6>
                    <ul class="list-unstyled mb-0 small text-muted">
                        <li class="mb-2">
                            <i class="bi bi-check-circle me-2 text-success"></i>
                            Los dispositivos se identifican por IP + navegador utilizado
                        </li>
                        <li class="mb-2">
                            <i class="bi bi-check-circle me-2 text-success"></i>
                            Después de 15 días de inactividad, se volverá a solicitar 2FA
                        </li>
                        <li class="mb-2">
                            <i class="bi bi-check-circle me-2 text-success"></i>
                            Los dispositivos antiguos (más de 30 días) se eliminan automáticamente
                        </li>
                        <li>
                            <i class="bi bi-shield-check me-2 text-warning"></i>
                            Si sospechas actividad no autorizada, elimina todos los dispositivos
                        </li>
                    </ul>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>