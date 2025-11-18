<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>${accion == 'crear' ? 'Nuevo' : 'Editar'} Nivel de Dificultad · SumixKids</title>
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
                        <i class="fas fa-signal me-2"></i>
                        ${accion == 'crear' ? 'Nuevo Nivel de Dificultad' : 'Editar Nivel de Dificultad'}
                    </h1>
                    <a href="${pageContext.request.contextPath}/niveles" class="btn btn-outline-light">
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
                                <form method="post" action="${pageContext.request.contextPath}/niveles" id="nivelForm">
                                    <input type="hidden" name="action" value="${accion}">
                                    <c:if test="${accion == 'actualizar'}">
                                        <input type="hidden" name="idNivelDificultad" value="${nivel.idNivelDificultad}">
                                    </c:if>

                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="nombreNivel" class="form-label fw-semibold">
                                                Nombre del Nivel <span class="text-danger">*</span>
                                            </label>
                                            <input type="text" 
                                                   class="form-control form-control-lg" 
                                                   id="nombreNivel" 
                                                   name="nombreNivel" 
                                                   value="${nivel.nombreNivel}" 
                                                   required
                                                   placeholder="Ej: Básico, Intermedio, Avanzado">
                                            <div class="form-text">Nombre descriptivo del nivel</div>
                                        </div>

                                        <div class="col-md-6">
                                            <label for="orden" class="form-label fw-semibold">
                                                Orden <span class="text-danger">*</span>
                                            </label>
                                            <input type="number" 
                                                   class="form-control form-control-lg" 
                                                   id="orden" 
                                                   name="orden" 
                                                   value="${nivel.orden}" 
                                                   required
                                                   min="1" 
                                                   max="10"
                                                   placeholder="1">
                                            <div class="form-text">Orden de dificultad (1 = más fácil)</div>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="icono" class="form-label fw-semibold">
                                                Icono/Emoji
                                            </label>
                                            <input type="text" 
                                                   class="form-control form-control-lg" 
                                                   id="icono" 
                                                   name="icono" 
                                                   value="${nivel.icono}" 
                                                   placeholder="⭐"
                                                   maxlength="5">
                                            <div class="form-text">Emoji representativo del nivel</div>
                                        </div>

                                        <div class="col-md-6">
                                            <label for="colorHex" class="form-label fw-semibold">
                                                Color
                                            </label>
                                            <div class="input-group">
                                                <input type="color" 
                                                       class="form-control form-control-color" 
                                                       id="colorHex" 
                                                       name="colorHex" 
                                                       value="${not empty nivel.colorHex ? nivel.colorHex : '#4CAF50'}"
                                                       title="Seleccionar color">
                                                <input type="text" 
                                                       class="form-control" 
                                                       id="colorHexText" 
                                                       value="${not empty nivel.colorHex ? nivel.colorHex : '#4CAF50'}"
                                                       readonly>
                                            </div>
                                            <div class="form-text">Color representativo del nivel</div>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="descripcionTexto" class="form-label fw-semibold">Descripción</label>
                                        <textarea class="form-control" 
                                                  id="descripcionTexto" 
                                                  name="descripcionTexto" 
                                                  rows="3"
                                                  placeholder="Ejemplo: Ideal para estudiantes de 3° grado. Sumas básicas del 1 al 50 con números de una y dos cifras.">${not empty nivel ? nivel.descripcionLegible : ''}</textarea>
                                        <div class="form-text">Descripción clara: grado recomendado, rango de números, tipo de ejercicios (opcional)</div>
                                    </div>

                                    <c:if test="${accion == 'actualizar'}">
                                        <div class="mb-3">
                                            <div class="form-check form-switch">
                                                <input class="form-check-input" 
                                                       type="checkbox" 
                                                       id="activo" 
                                                       name="activo" 
                                                       ${nivel.activo ? 'checked' : ''}>
                                                <label class="form-check-label fw-semibold" for="activo">
                                                    Nivel activo
                                                </label>
                                            </div>
                                            <div class="form-text">Los niveles inactivos no están disponibles para ejercicios</div>
                                        </div>
                                    </c:if>

                                    <hr class="my-4">

                                    <div class="d-flex justify-content-between">
                                        <a href="${pageContext.request.contextPath}/niveles" class="btn btn-outline-secondary btn-lg">
                                            <i class="fas fa-times me-2"></i>Cancelar
                                        </a>
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <i class="fas fa-save me-2"></i>
                                            ${accion == 'crear' ? 'Crear Nivel' : 'Guardar Cambios'}
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
                                    <strong>Niveles de Dificultad:</strong> Definen la complejidad de ejercicios y juegos.
                                </p>
                                <ul class="small">
                                    <li><strong>Orden:</strong> Determina la progresión (1, 2, 3...)</li>
                                    <li><strong>Color:</strong> Identificación visual del nivel</li>
                                    <li><strong>Descripción:</strong> Texto simple que explica el nivel</li>
                                </ul>
                                
                                <div class="alert alert-success mt-3">
                                    <i class="fas fa-pencil-alt me-2"></i>
                                    <small><strong>Cómo escribir la descripción:</strong><br>
                                    • Usa lenguaje simple y claro<br>
                                    • Indica el grado recomendado<br>
                                    • Menciona el rango de números<br>
                                    • Describe el tipo de ejercicios<br>
                                    • Máximo 200 caracteres</small>
                                </div>
                                
                                <div class="alert alert-info mt-3">
                                    <i class="fas fa-lightbulb me-2"></i>
                                    <small><strong>Ejemplo completo:</strong><br>
                                    <strong>Nombre:</strong> Básico<br>
                                    <strong>Orden:</strong> 1<br>
                                    <strong>Icono:</strong> ⭐<br>
                                    <strong>Color:</strong> #4CAF50<br>
                                    <strong>Descripción:</strong> "Ideal para estudiantes de 3° grado. Sumas básicas del 1 al 50 con números de una y dos cifras. Incluye ejercicios con objetos cotidianos y situaciones familiares."</small>
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
<script>
    // Sincronizar el color picker con el input de texto
    const colorPicker = document.getElementById('colorHex');
    const colorText = document.getElementById('colorHexText');
    
    colorPicker.addEventListener('input', function() {
        colorText.value = this.value.toUpperCase();
    });
</script>
</body>
</html>
