<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>${accion == 'crear' ? 'Nuevo' : 'Editar'} Grado · SumixKids</title>
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
                 style="height: 40px; width: auto; margin-right: 12px; border-radius: 8px;"/>
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
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">
                        👥 Usuarios
                    </a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link active dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        📚 Académico
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/grados"><i class="fas fa-graduation-cap me-2"></i>Grados Escolares</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/niveles"><i class="fas fa-signal me-2"></i>Niveles de Dificultad</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/asignaciones"><i class="fas fa-users me-2"></i>Asignaciones</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/vinculos"><i class="fas fa-user-friends me-2"></i>Vínculos</a></li>
                    </ul>
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
                    <a class="nav-link dropdown-toggle d-flex align-items-center rounded-pill px-3" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-crown me-2"></i>
                        <span class="fw-semibold">${sessionScope.usuario.nombreCorto}</span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0">
                        <li>
                            <h6 class="dropdown-item">👑 Administrador</h6>
                        </li>
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

<main class="flex-fill py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <!-- Header -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1 class="display-6 fw-bold text-white-contrast drop-shadow">
                        <i class="fas fa-graduation-cap me-2"></i>
                        ${accion == 'crear' ? 'Nuevo Grado Escolar' : 'Editar Grado Escolar'}
                    </h1>
                    <a href="${pageContext.request.contextPath}/grados" class="btn btn-outline-light">
                        <i class="fas fa-arrow-left me-2"></i>Volver
                    </a>
                </div>

                <!-- Mensajes de error -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show shadow-sm">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Formulario -->
                <div class="row">
                    <div class="col-lg-8">
                        <div class="card shadow-lg border-0">
                            <div class="card-body p-4">
                                <form method="post" action="${pageContext.request.contextPath}/grados" id="gradoForm">
                                    <input type="hidden" name="action" value="${accion}">
                                    <c:if test="${accion == 'actualizar'}">
                                        <input type="hidden" name="idGrado" value="${grado.idGrado}">
                                    </c:if>

                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="nombreGrado" class="form-label fw-semibold">
                                                Nombre del Grado <span class="text-danger">*</span>
                                            </label>
                                            <input type="text" 
                                                   class="form-control form-control-lg" 
                                                   id="nombreGrado" 
                                                   name="nombreGrado" 
                                                   value="${grado.nombreGrado}" 
                                                   required
                                                   placeholder="Ej: 1°, 2°, 3°">
                                            <div class="form-text">Número o nombre del grado</div>
                                        </div>

                                        <div class="col-md-6">
                                            <label for="nombreGrupo" class="form-label fw-semibold">
                                                Grupo/Sección <span class="text-danger">*</span>
                                            </label>
                                            <input type="text" 
                                                   class="form-control form-control-lg" 
                                                   id="nombreGrupo" 
                                                   name="nombreGrupo" 
                                                   value="${grado.nombreGrupo}" 
                                                   required
                                                   placeholder="Ej: A, B, C">
                                            <div class="form-text">Letra o nombre del grupo</div>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="nivelEducativo" class="form-label fw-semibold">
                                                Nivel Educativo <span class="text-danger">*</span>
                                            </label>
                                            <select class="form-select form-select-lg" id="nivelEducativo" name="nivelEducativo" required>
                                                <option value="">Selecciona un nivel</option>
                                                <option value="PREESCOLAR" ${grado.nivelEducativo == 'PREESCOLAR' ? 'selected' : ''}>Preescolar</option>
                                                <option value="PRIMARIA" ${grado.nivelEducativo == 'PRIMARIA' ? 'selected' : ''}>Primaria</option>
                                                <option value="SECUNDARIA" ${grado.nivelEducativo == 'SECUNDARIA' ? 'selected' : ''}>Secundaria</option>
                                            </select>
                                            <div class="form-text">Nivel al que pertenece el grado</div>
                                        </div>

                                        <div class="col-md-6">
                                            <label for="capacidadMaxima" class="form-label fw-semibold">
                                                Capacidad Máxima
                                            </label>
                                            <input type="number" 
                                                   class="form-control form-control-lg" 
                                                   id="capacidadMaxima" 
                                                   name="capacidadMaxima" 
                                                   value="${grado.capacidadMaxima != null ? grado.capacidadMaxima : 30}" 
                                                   min="1" 
                                                   max="100"
                                                   placeholder="30">
                                            <div class="form-text">Número máximo de estudiantes</div>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="descripcion" class="form-label fw-semibold">Descripción</label>
                                        <textarea class="form-control" 
                                                  id="descripcion" 
                                                  name="descripcion" 
                                                  rows="3"
                                                  placeholder="Ejemplo: Grupo de estudiantes de 8-9 años. Turno matutino. Enfoque en matemáticas básicas y lectoescritura.">${grado.descripcion}</textarea>
                                        <div class="form-text">Descripción clara del grado: edades, horarios, características especiales, metodología (opcional)</div>
                                    </div>

                                    <c:if test="${accion == 'actualizar'}">
                                        <div class="mb-3">
                                            <div class="form-check form-switch">
                                                <input class="form-check-input" 
                                                       type="checkbox" 
                                                       id="activo" 
                                                       name="activo" 
                                                       ${grado.activo ? 'checked' : ''}>
                                                <label class="form-check-label fw-semibold" for="activo">
                                                    Grado activo
                                                </label>
                                            </div>
                                            <div class="form-text">Los grados inactivos no aparecen en los listados</div>
                                        </div>
                                    </c:if>

                                    <hr class="my-4">

                                    <div class="d-flex justify-content-between">
                                        <a href="${pageContext.request.contextPath}/grados" class="btn btn-outline-secondary btn-lg">
                                            <i class="fas fa-times me-2"></i>Cancelar
                                        </a>
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <i class="fas fa-save me-2"></i>
                                            ${accion == 'crear' ? 'Crear Grado' : 'Guardar Cambios'}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Panel de ayuda -->
                    <div class="col-lg-4">
                        <div class="card shadow-lg border-0 bg-light">
                            <div class="card-body">
                                <h5 class="card-title">
                                    <i class="fas fa-info-circle text-primary me-2"></i>
                                    Información
                                </h5>
                                <hr>
                                <p class="card-text">
                                    <strong>Grados Escolares:</strong> Organizan a los estudiantes por nivel y grupo.
                                </p>
                                <ul class="small">
                                    <li><strong>Nombre del Grado:</strong> Use números (1°, 2°, 3°) o texto</li>
                                    <li><strong>Grupo:</strong> Generalmente letras (A, B, C)</li>
                                    <li><strong>Capacidad:</strong> Límite de estudiantes por grupo</li>
                                    <li><strong>Descripción:</strong> Información adicional sobre el grado</li>
                                </ul>
                                
                                <div class="alert alert-success mt-3">
                                    <i class="fas fa-pencil-alt me-2"></i>
                                    <small><strong>Cómo escribir la descripción:</strong><br>
                                    • Menciona las edades típicas<br>
                                    • Describe características del grupo<br>
                                    • Indica horarios o turnos especiales<br>
                                    • Añade notas sobre metodología<br>
                                    • Máximo 200 caracteres</small>
                                </div>
                                
                                <div class="alert alert-info mt-3">
                                    <i class="fas fa-lightbulb me-2"></i>
                                    <small><strong>Ejemplo completo:</strong><br>
                                    <strong>Grado:</strong> 3°<br>
                                    <strong>Grupo:</strong> A<br>
                                    <strong>Nivel:</strong> Primaria<br>
                                    <strong>Capacidad:</strong> 30<br>
                                    <strong>Descripción:</strong> "Grupo de estudiantes de 8-9 años. Turno matutino de 7:00 AM a 12:00 PM. Enfoque en matemáticas básicas y lectoescritura."</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
