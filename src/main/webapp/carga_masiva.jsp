<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Carga Masiva · SumixKids</title>
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/auditoria">
                            📊 Auditoría
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/carga_masiva">
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
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/dispositivos_reconocidos"><i class="fa-solid fa-shield-halved me-2"></i>Dispositivos Reconocidos</a></li>
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
                        <h1 class="display-6 fw-bold text-white-contrast mb-2">📁 Carga Masiva de Usuarios</h1>
                        <p class="lead text-white-contrast mb-0">Importa múltiples usuarios desde archivos Excel de manera eficiente y segura</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Alertas de notificación -->
        <c:if test="${not empty mensajeExito}">
            <div class="alert alert-success alert-dismissible fade show shadow-lg mb-4" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                <strong>✅ ¡Éxito!</strong> ${mensajeExito}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty mensajeError}">
            <div class="alert alert-danger alert-dismissible fade show shadow-lg mb-4" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <strong>❌ Error:</strong> ${mensajeError}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="row g-4">
            <!-- Panel de carga de archivo -->
            <div class="col-lg-6">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-header bg-gradient-warning">
                        <h5 class="mb-0">
                            <i class="fas fa-cloud-upload-alt me-2"></i>Subir Archivo Excel
                        </h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/carga_masiva" method="post" enctype="multipart/form-data" id="uploadForm">
                            <div class="mb-4">
                                <label for="archivo" class="form-label fw-semibold">
                                    <i class="fas fa-file-excel me-1"></i>Seleccionar archivo Excel
                                </label>
                                <div class="input-group">
                                    <input type="file" class="form-control" id="archivo" name="archivo" 
                                           accept=".xlsx,.xls" required>
                                    <span class="input-group-text">
                                        <i class="fas fa-file-excel text-success"></i>
                                    </span>
                                </div>
                                <small class="form-text text-muted">
                                    📝 Formatos admitidos: .xlsx, .xls (máximo 10MB)
                                </small>
                            </div>

                            <div class="mb-4">
                                <label for="tipoImportacion" class="form-label fw-semibold">
                                    <i class="fas fa-cogs me-1"></i>Tipo de importación
                                </label>
                                <select class="form-select" id="tipoImportacion" name="tipoImportacion" required>
                                    <option value="">Seleccionar tipo...</option>
                                    <option value="usuarios">👥 Usuarios del sistema</option>
                                    <option value="estudiantes">🎓 Solo estudiantes</option>
                                    <option value="docentes">👨‍🏫 Solo docentes</option>
                                    <option value="padres">👨‍👩‍👧‍👦 Solo padres</option>
                                </select>
                            </div>

                            <div class="mb-4">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="validarDuplicados" name="validarDuplicados" checked>
                                    <label class="form-check-label fw-semibold" for="validarDuplicados">
                                        <i class="fas fa-shield-alt me-1"></i>Validar duplicados
                                    </label>
                                </div>
                                <small class="form-text text-muted">
                                    Evita importar usuarios con nombres de usuario o correos duplicados
                                </small>
                            </div>

                            <div class="mb-4">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" id="enviarEmails" name="enviarEmails" checked>
                                    <label class="form-check-label fw-semibold" for="enviarEmails">
                                        <i class="fas fa-envelope me-1"></i>Enviar emails de bienvenida
                                    </label>
                                </div>
                                <small class="form-text text-muted">
                                    Los usuarios recibirán un correo con sus credenciales de acceso
                                </small>
                            </div>

                            <div class="d-grid">
                                <button type="submit" class="btn btn-warning btn-lg fw-bold">
                                    <i class="fas fa-upload me-2"></i>Procesar Archivo
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Panel de información y formato -->
            <div class="col-lg-6">
                <div class="card h-100 border-0 shadow-lg">
                    <div class="card-header bg-gradient-info">
                        <h5 class="mb-0">
                            <i class="fas fa-info-circle me-2"></i>Formato del Archivo
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="alert alert-info" role="alert">
                            <h6 class="alert-heading">
                                💡 Estructura requerida
                            </h6>
                            <p class="mb-0">El archivo Excel debe contener las siguientes columnas en orden:</p>
                        </div>

                        <div class="table-responsive mb-4">
                            <table class="table table-sm table-bordered">
                                <thead class="table-dark">
                                    <tr>
                                        <th>📍 Columna</th>
                                        <th>📝 Campo</th>
                                        <th>🔧 Ejemplo</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><strong>A</strong></td>
                                        <td>Nombres</td>
                                        <td>María José</td>
                                    </tr>
                                    <tr>
                                        <td><strong>B</strong></td>
                                        <td>Apellidos</td>
                                        <td>González López</td>
                                    </tr>
                                    <tr>
                                        <td><strong>C</strong></td>
                                        <td>Usuario</td>
                                        <td>mariagonzalez</td>
                                    </tr>
                                    <tr>
                                        <td><strong>D</strong></td>
                                        <td>Email</td>
                                        <td>maria@gmail.com</td>
                                    </tr>
                                    <tr>
                                        <td><strong>E</strong></td>
                                        <td>Contraseña</td>
                                        <td>MiPass123@</td>
                                    </tr>
                                    <tr>
                                        <td><strong>F</strong></td>
                                        <td>Rol ID</td>
                                        <td>3 (estudiante)</td>
                                    </tr>
                                    <tr>
                                        <td><strong>G</strong></td>
                                        <td>Grado</td>
                                        <td>3, 4 o 5</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <div class="row g-3 mb-4">
                            <div class="col-12">
                                <h6 class="fw-bold text-primary">🏷️ Roles disponibles:</h6>
                                <div class="row g-2">
                                    <div class="col-6">
                                        <span class="badge bg-success w-100 p-2">1 - 👑 Admin</span>
                                    </div>
                                    <div class="col-6">
                                        <span class="badge bg-primary w-100 p-2">2 - 👨‍🏫 Docente</span>
                                    </div>
                                    <div class="col-6">
                                        <span class="badge bg-warning w-100 p-2">3 - 🎓 Estudiante</span>
                                    </div>
                                    <div class="col-6">
                                        <span class="badge bg-info w-100 p-2">4 - 👨‍👩‍👧‍👦 Padre</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12">
                                <h6 class="fw-bold text-success">🎯 Grados válidos:</h6>
                                <div class="row g-2">
                                    <div class="col-4">
                                        <span class="badge bg-secondary w-100 p-2">🎓 3° Grado</span>
                                    </div>
                                    <div class="col-4">
                                        <span class="badge bg-secondary w-100 p-2">🎓 4° Grado</span>
                                    </div>
                                    <div class="col-4">
                                        <span class="badge bg-secondary w-100 p-2">🎓 5° Grado</span>
                                    </div>
                                </div>
                                <small class="text-muted d-block mt-2">
                                    <i class="fas fa-info-circle me-1"></i>Solo estos grados son válidos para estudiantes. Dejar en blanco para otros roles.
                                </small>
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/carga_masiva?action=plantilla" class="btn btn-outline-primary">
                                <i class="fas fa-download me-2"></i>Descargar Plantilla
                            </a>
                            <a href="${pageContext.request.contextPath}/carga_masiva?action=ejemplo" class="btn btn-outline-secondary">
                                <i class="fas fa-eye me-2"></i>Ver Ejemplo
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Panel de validaciones -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card border-0 shadow-lg">
                    <div class="card-header bg-gradient-success">
                        <h5 class="mb-0">
                            ✅ Validaciones Automáticas
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row g-4">
                            <div class="col-md-6">
                                <h6 class="fw-bold text-success">
                                    🛡️ Validaciones de Seguridad
                                </h6>
                                <ul class="list-unstyled">
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success me-2"></i>
                                        <strong>Nombres y apellidos:</strong> Solo letras y espacios (máx. 30 chars)
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success me-2"></i>
                                        <strong>Usuario:</strong> Entre 5 y 15 caracteres, sin espacios
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success me-2"></i>
                                        <strong>Email:</strong> Formato válido de correo electrónico
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-check text-success me-2"></i>
                                        <strong>Contraseña:</strong> Mín. 5 letras, 2 números, 1 especial
                                    </li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6 class="fw-bold text-info">
                                    ℹ️ Información Adicional
                                </h6>
                                <ul class="list-unstyled">
                                    <li class="mb-2">
                                        <i class="fas fa-clock text-info me-2"></i>
                                        <strong>Tiempo de procesamiento:</strong> ~1 segundo por usuario
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-users text-info me-2"></i>
                                        <strong>Límite recomendado:</strong> 100 usuarios por archivo
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-envelope text-info me-2"></i>
                                        <strong>Emails automáticos:</strong> Credenciales y bienvenida
                                    </li>
                                    <li class="mb-2">
                                        <i class="fas fa-history text-info me-2"></i>
                                        <strong>Registro de auditoría:</strong> Todas las acciones se registran
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Resultados de importación -->
        <c:if test="${not empty resultadosImportacion}">
            <div class="row mt-4">
                <div class="col-12">
                    <div class="card border-0 shadow-lg">
                        <div class="card-header bg-gradient-primary">
                            <h5 class="mb-0">
                                📊 Resultados de la Importación
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row g-3 mb-4">
                                <div class="col-md-3">
                                    <div class="text-center p-3 bg-success bg-opacity-10 rounded border border-success border-opacity-25">
                                        <h3 class="text-success mb-1">${usuariosCreados}</h3>
                                        <small class="text-success fw-semibold">✅ Usuarios Creados</small>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="text-center p-3 bg-warning bg-opacity-10 rounded border border-warning border-opacity-25">
                                        <h3 class="text-warning mb-1">${usuariosDuplicados}</h3>
                                        <small class="text-warning fw-semibold">⚠️ Duplicados</small>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="text-center p-3 bg-danger bg-opacity-10 rounded border border-danger border-opacity-25">
                                        <h3 class="text-danger mb-1">${usuariosErrores}</h3>
                                        <small class="text-danger fw-semibold">❌ Errores</small>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="text-center p-3 bg-info bg-opacity-10 rounded border border-info border-opacity-25">
                                        <h3 class="text-info mb-1">${totalProcesados}</h3>
                                        <small class="text-info fw-semibold">📊 Total Procesados</small>
                                    </div>
                                </div>
                            </div>

                            <c:if test="${not empty erroresDetallados}">
                                <div class="alert alert-warning" role="alert">
                                    <h6 class="alert-heading">
                                        <i class="fas fa-exclamation-triangle me-2"></i>⚠️ Errores Encontrados
                                    </h6>
                                    <ul class="mb-0">
                                        <c:forEach var="error" items="${erroresDetallados}">
                                            <li>${error}</li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
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

// Auto-hide alerts after 5 seconds
setTimeout(function() {
    document.querySelectorAll('.alert-dismissible').forEach(alert => {
        if (alert.classList.contains('show')) {
            bootstrap.Alert.getOrCreateInstance(alert).close();
        }
    });
}, 5000);

// File validation
document.getElementById('archivo').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        const fileSize = file.size / 1024 / 1024; // MB
        const allowedTypes = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'application/vnd.ms-excel'];
        
        if (fileSize > 10) {
            Swal.fire({
                title: '📁 Archivo muy grande',
                text: 'El archivo es demasiado grande. Máximo 10MB permitido.',
                icon: 'error',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#dc3545'
            });
            e.target.value = '';
            return;
        }
        
        if (!allowedTypes.includes(file.type)) {
            Swal.fire({
                title: '📄 Formato inválido',
                text: 'Formato de archivo no válido. Solo se permiten archivos Excel (.xlsx, .xls).',
                icon: 'error',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#dc3545'
            });
            e.target.value = '';
            return;
        }
        
        // Remove previous file info
        const existingInfo = e.target.parentNode.parentNode.querySelector('.file-info');
        if (existingInfo) {
            existingInfo.remove();
        }
        
        // Show file info
        const fileName = file.name;
        const fileInfo = document.createElement('small');
        fileInfo.className = 'form-text text-success fw-semibold file-info d-block mt-2';
        fileInfo.innerHTML = `<i class="fas fa-check-circle me-1"></i>✅ Archivo seleccionado: <strong>${fileName}</strong> (${fileSize.toFixed(2)} MB)`;
        
        e.target.parentNode.parentNode.appendChild(fileInfo);
    }
});

// Form submission loading
document.getElementById('uploadForm').addEventListener('submit', function(e) {
    const submitBtn = e.target.querySelector('button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Procesando...';
    
    // Re-enable after 30 seconds as fallback
    setTimeout(function() {
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalText;
    }, 30000);
});
</script>
</body>
</html>
