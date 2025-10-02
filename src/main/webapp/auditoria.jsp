<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Auditoría del Sistema · SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">

<!-- Navbar principal -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/bienvenida">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" style="height: 36px; width: auto; margin-right: 8px;"/>
            SumixKids
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/bienvenida">
                        🏠 Dashboard
                    </a>
                </li>
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.rolId == 1}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">
                            👥 Usuarios
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/auditoria">
                            📊 Auditoría
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/carga_masiva">
                            📁 Carga Masiva
                        </a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center rounded-pill px-3" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-crown me-2"></i>
                        <span class="fw-semibold">${sessionScope.usuario.nombres}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0">
                        <li><h6 class="dropdown-item">👑 Administrador</h6></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil"><i class="fas fa-user me-2"></i>Mi Perfil</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/configuracion"><i class="fas fa-cog me-2"></i>Configuración</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-2 text-danger"></i>Cerrar Sesión</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<main class="flex-fill py-4">
    <div class="container">
        <!-- Header de la página -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="text-center">
                    <div class="page-header">
                        <h1 class="display-6 fw-bold text-white-contrast mb-2">📊 Auditoría del Sistema</h1>
                        <p class="lead text-white-contrast mb-0">Monitoreo y seguimiento de todas las actividades del sistema en tiempo real</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Estadísticas principales -->
        <div class="row g-4 mb-4">
            <div class="col-md-3">
                <div class="card stat-card h-100 border-0 shadow-lg">
                    <div class="card-body text-center">
                        <div class="stat-icon text-success">
                            <i class="fas fa-eye"></i>
                        </div>
                        <h3 class="text-success">${totalEventos != null ? totalEventos : 0}</h3>
                        <p class="mb-0 fw-semibold text-dark">👁️ Total Eventos</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card h-100 border-0 shadow-lg">
                    <div class="card-body text-center">
                        <div class="stat-icon text-info">
                            <i class="fas fa-calendar-day"></i>
                        </div>
                        <h3 class="text-info">${eventosHoy != null ? eventosHoy : 0}</h3>
                        <p class="mb-0 fw-semibold text-dark">📅 Hoy</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card h-100 border-0 shadow-lg">
                    <div class="card-body text-center">
                        <div class="stat-icon text-warning">
                            <i class="fas fa-exclamation-triangle"></i>
                        </div>
                        <h3 class="text-warning">${eventosFallidos != null ? eventosFallidos : 0}</h3>
                        <p class="mb-0 fw-semibold text-dark">⚠️ Fallidos</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card h-100 border-0 shadow-lg">
                    <div class="card-body text-center">
                        <div class="stat-icon text-primary">
                            <i class="fas fa-users"></i>
                        </div>
                        <h3 class="text-primary">${usuariosActivos != null ? usuariosActivos : 0}</h3>
                        <p class="mb-0 fw-semibold text-dark">👥 Usuarios Activos</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Filtros de búsqueda -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="filter-panel">
                    <h5 class="mb-3">
                        🔍 Filtros de Búsqueda
                    </h5>
                    <form method="get" action="${pageContext.request.contextPath}/auditoria" id="formFiltros" class="row g-3">
                        <div class="col-md-3">
                            <label for="fechaInicio" class="form-label fw-semibold">
                                <i class="fas fa-calendar-alt me-1"></i>Fecha Inicio
                            </label>
                            <input type="date" class="form-control" id="fechaInicio" name="fechaInicio" 
                                   value="${param.fechaInicio}">
                        </div>
                        <div class="col-md-3">
                            <label for="fechaFin" class="form-label fw-semibold">
                                <i class="fas fa-calendar-alt me-1"></i>Fecha Fin
                            </label>
                            <input type="date" class="form-control" id="fechaFin" name="fechaFin" 
                                   value="${param.fechaFin}">
                        </div>
                        <div class="col-md-3">
                            <label for="tipoEvento" class="form-label fw-semibold">
                                <i class="fas fa-tag me-1"></i>Tipo de Evento
                            </label>
                            <select class="form-select" id="tipoEvento" name="tipoEvento">
                                <option value="">Todos los tipos</option>
                                <option value="LOGIN" ${param.tipoEvento == 'LOGIN' ? 'selected' : ''}>🔑 Login</option>
                                <option value="LOGOUT" ${param.tipoEvento == 'LOGOUT' ? 'selected' : ''}>🚪 Logout</option>
                                <option value="REGISTRO" ${param.tipoEvento == 'REGISTRO' ? 'selected' : ''}>📝 Registro</option>
                                <option value="ACTUALIZACION" ${param.tipoEvento == 'ACTUALIZACION' ? 'selected' : ''}>✏️ Actualización</option>
                                <option value="ELIMINACION" ${param.tipoEvento == 'ELIMINACION' ? 'selected' : ''}>🗑️ Eliminación</option>
                                <option value="ERROR" ${param.tipoEvento == 'ERROR' ? 'selected' : ''}>❌ Error</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label for="usuario" class="form-label fw-semibold">
                                <i class="fas fa-user me-1"></i>Usuario
                            </label>
                            <input type="text" class="form-control" id="usuario" name="usuario" 
                                   placeholder="Buscar por usuario..." value="${param.usuario}">
                        </div>
                        <div class="col-12">
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search me-2"></i>Buscar
                                </button>
                                <a href="${pageContext.request.contextPath}/auditoria" class="btn btn-outline-secondary">
                                    <i class="fas fa-broom me-2"></i>Limpiar
                                </a>
                                <button type="button" class="btn btn-success" onclick="exportarExcel()">
                                    <i class="fas fa-file-excel me-2"></i>Exportar Excel
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Tabla de eventos de auditoría -->
        <div class="row">
            <div class="col-12">
                <div class="card border-0 shadow-lg">
                    <div class="card-header bg-gradient-primary d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                            📋 Registro de Eventos
                        </h5>
                        <span class="badge bg-light text-dark fs-6">
                            ${totalRegistros != null ? totalRegistros : 0} eventos encontrados
                        </span>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead class="table-dark">
                                    <tr>
                                        <th scope="col" class="text-center">
                                            🔑 ID
                                        </th>
                                        <th scope="col">
                                            📅 Fecha/Hora
                                        </th>
                                        <th scope="col">
                                            👤 Usuario
                                        </th>
                                        <th scope="col">
                                            🏷️ Tipo
                                        </th>
                                        <th scope="col">
                                            📝 Descripción
                                        </th>
                                        <th scope="col">
                                            🌐 IP
                                        </th>
                                        <th scope="col" class="text-center">
                                            ✅ Estado
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty eventos}">
                                            <c:forEach var="evento" items="${eventos}" varStatus="status">
                                                <tr class="align-middle">
                                                    <td class="text-center">
                                                        <span class="badge bg-secondary">${evento.id}</span>
                                                    </td>
                                                    <td>
                                                        <div class="d-flex flex-column">
                                                            <span class="fw-semibold">
                                                                <i class="fas fa-calendar-alt me-1"></i>
                                                                <c:choose>
                                                                    <c:when test="${evento.fechaHora != null}">
                                                                        ${util:formatearFecha(evento.fechaHora)}
                                                                    </c:when>
                                                                    <c:otherwise>N/A</c:otherwise>
                                                                </c:choose>
                                                            </span>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="avatar-circle me-2">
                                                                <i class="fas fa-user text-primary"></i>
                                                            </div>
                                                            <div>
                                                                <div class="fw-semibold">${evento.nombreUsuario}</div>
                                                                <small class="text-muted">ID: ${evento.idUsuario}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${evento.accion == 'LOGIN'}">
                                                                <span class="badge bg-success">🔑 Login</span>
                                                            </c:when>
                                                            <c:when test="${evento.accion == 'LOGOUT'}">
                                                                <span class="badge bg-secondary">🚪 Logout</span>
                                                            </c:when>
                                                            <c:when test="${evento.accion == 'REGISTRO'}">
                                                                <span class="badge bg-primary">📝 Registro</span>
                                                            </c:when>
                                                            <c:when test="${evento.accion == 'ACTUALIZACION'}">
                                                                <span class="badge bg-warning">✏️ Actualización</span>
                                                            </c:when>
                                                            <c:when test="${evento.accion.contains('ELIMINAR')}">
                                                                <span class="badge bg-danger">🗑️ Eliminación</span>
                                                            </c:when>
                                                            <c:when test="${evento.estado == 'ERROR'}">
                                                                <span class="badge bg-danger">❌ Error</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-info">ℹ️ ${evento.accion}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <div class="text-truncate" style="max-width: 250px;" 
                                                             title="${evento.descripcion}">
                                                            ${evento.descripcion}
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <span class="font-monospace text-muted">
                                                            <i class="fas fa-map-marker-alt me-1"></i>
                                                            ${evento.ipUsuario}
                                                        </span>
                                                    </td>
                                                    <td class="text-center">
                                                        <c:choose>
                                                            <c:when test="${evento.estado == 'EXITOSO'}">
                                                                <span class="badge bg-success">✅ Exitoso</span>
                                                            </c:when>
                                                            <c:when test="${evento.estado == 'FALLIDO'}">
                                                                <span class="badge bg-warning">⚠️ Fallido</span>
                                                            </c:when>
                                                            <c:when test="${evento.estado == 'ERROR'}">
                                                                <span class="badge bg-danger">❌ Error</span>
                                                            </c:when>
                                                            <c:when test="${evento.estado == 'INICIADO'}">
                                                                <span class="badge bg-info">🔄 En Proceso</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">${evento.estado}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="7" class="text-center py-5">
                                                    <div class="text-muted">
                                                        <i class="fas fa-exclamation-circle fa-3x mb-3"></i>
                                                        <h5>📋 No hay eventos de auditoría</h5>
                                                        <p>No se encontraron registros que coincidan con los filtros aplicados.</p>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Paginación -->
        <c:if test="${totalPaginas > 1}">
            <div class="row mt-4">
                <div class="col-12">
                    <nav aria-label="Navegación de páginas">
                        <ul class="pagination justify-content-center">
                            <c:if test="${paginaActual > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="?pagina=${paginaActual - 1}${not empty param.fechaInicio ? '&fechaInicio='.concat(param.fechaInicio) : ''}${not empty param.fechaFin ? '&fechaFin='.concat(param.fechaFin) : ''}${not empty param.tipoEvento ? '&tipoEvento='.concat(param.tipoEvento) : ''}${not empty param.usuario ? '&usuario='.concat(param.usuario) : ''}">
                                        <i class="fas fa-chevron-left"></i> Anterior
                                    </a>
                                </li>
                            </c:if>
                            
                            <c:forEach begin="1" end="${totalPaginas}" var="i">
                                <li class="page-item ${i == paginaActual ? 'active' : ''}">
                                    <a class="page-link" href="?pagina=${i}${not empty param.fechaInicio ? '&fechaInicio='.concat(param.fechaInicio) : ''}${not empty param.fechaFin ? '&fechaFin='.concat(param.fechaFin) : ''}${not empty param.tipoEvento ? '&tipoEvento='.concat(param.tipoEvento) : ''}${not empty param.usuario ? '&usuario='.concat(param.usuario) : ''}">${i}</a>
                                </li>
                            </c:forEach>
                            
                            <c:if test="${paginaActual < totalPaginas}">
                                <li class="page-item">
                                    <a class="page-link" href="?pagina=${paginaActual + 1}${not empty param.fechaInicio ? '&fechaInicio='.concat(param.fechaInicio) : ''}${not empty param.fechaFin ? '&fechaFin='.concat(param.fechaFin) : ''}${not empty param.tipoEvento ? '&tipoEvento='.concat(param.tipoEvento) : ''}${not empty param.usuario ? '&usuario='.concat(param.usuario) : ''}">
                                        Siguiente <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </c:if>
    </div>
</main>

<footer class="py-3 bg-dark mt-auto text-center text-white-50 small">
    © <span id="year"></span> SumixKids · Todos los derechos reservados
</footer>

<!-- Debug Info -->
<c:if test="${pageContext.request.getParameter('debug') != null}">
    <div class="alert alert-info m-3">
        <strong>🐛 Debug Info:</strong><br/>
        Total Eventos: ${totalEventos}<br/>
        Eventos lista size: ${eventos != null ? eventos.size() : 'null'}<br/>
        Eventos empty: ${empty eventos}<br/>
    </div>
</c:if>

<!-- Bootstrap Bundle con Popper (Solo una vez) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>

<script>
document.getElementById('year').textContent = new Date().getFullYear();

// Function to export to Excel
function exportarExcel() {
    // Mostrar confirmación
    if (!confirm('¿Desea exportar los registros de auditoría a Excel?')) {
        return;
    }
    
    // Obtener los parámetros del formulario
    const fechaInicio = document.getElementById('fechaInicio').value;
    const fechaFin = document.getElementById('fechaFin').value;
    const tipoEvento = document.getElementById('tipoEvento').value;
    const usuario = document.getElementById('usuario').value;
    
    // Construir URL con parámetros
    const params = new URLSearchParams();
    params.append('exportar', 'excel');
    
    if (fechaInicio) params.append('fechaInicio', fechaInicio);
    if (fechaFin) params.append('fechaFin', fechaFin);
    if (tipoEvento) params.append('tipoEvento', tipoEvento);
    if (usuario) params.append('usuario', usuario);
    
    // Crear enlace temporal para descargar
    const url = '${pageContext.request.contextPath}/auditoria?' + params.toString();
    
    // Mostrar mensaje de procesamiento
    const btnExport = event.target;
    const originalText = btnExport.innerHTML;
    btnExport.disabled = true;
    btnExport.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Exportando...';
    
    // Redirigir para descargar
    window.location.href = url;
    
    // Restaurar botón después de 3 segundos
    setTimeout(() => {
        btnExport.disabled = false;
        btnExport.innerHTML = originalText;
    }, 3000);
}

// Auto-refresh every 30 seconds for real-time monitoring
setInterval(function() {
    if (document.visibilityState === 'visible') {
        // Only refresh if no filters are applied
        const hasFilters = new URLSearchParams(window.location.search).has('fechaInicio') || 
                          new URLSearchParams(window.location.search).has('tipoEvento') ||
                          new URLSearchParams(window.location.search).has('usuario');
        
        if (!hasFilters) {
            window.location.reload();
        }
    }
}, 30000);

// Add timestamp tooltip to date cells
document.querySelectorAll('td').forEach(cell => {
    if (cell.querySelector('.fa-clock')) {
        const fullDateTime = cell.textContent.trim();
        cell.setAttribute('title', 'Hora exacta: ' + fullDateTime);
    }
});

// Enhanced search functionality
document.getElementById('usuario').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        e.target.closest('form').submit();
    }
});

// Date range validation
document.getElementById('fechaInicio').addEventListener('change', function() {
    const fechaFin = document.getElementById('fechaFin');
    if (this.value && fechaFin.value && this.value > fechaFin.value) {
        alert('⚠️ La fecha de inicio no puede ser posterior a la fecha de fin');
        this.value = '';
    }
});

document.getElementById('fechaFin').addEventListener('change', function() {
    const fechaInicio = document.getElementById('fechaInicio');
    if (this.value && fechaInicio.value && this.value < fechaInicio.value) {
        alert('⚠️ La fecha de fin no puede ser anterior a la fecha de inicio');
        this.value = '';
    }
});
</script>

<style>
.avatar-circle {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.table-hover tbody tr:hover {
    background-color: rgba(0, 123, 255, 0.05);
    transform: translateY(-1px);
    transition: all 0.2s ease;
}

.page-link {
    border-radius: 8px;
    margin: 0 2px;
    border: none;
    background: rgba(255, 255, 255, 0.1);
    color: white;
    backdrop-filter: blur(10px);
}

.page-link:hover {
    background: rgba(255, 255, 255, 0.2);
    color: white;
    transform: translateY(-1px);
}

.page-item.active .page-link {
    background: linear-gradient(135deg, #2563eb 0%, #9333ea 100%);
    border: none;
}

.font-monospace {
    font-family: 'Courier New', monospace;
    font-size: 0.875rem;
}
</style>

</body>
</html>