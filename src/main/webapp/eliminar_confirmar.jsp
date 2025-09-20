<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmar Eliminación | SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .logo-container {
            text-align: center;
            margin-top: 20px;
            margin-bottom: 20px;
        }
        .logo-container img {
            max-width: 150px;
            cursor: pointer;
            transition: transform 0.3s ease;
        }
        .logo-container img:hover {
            transform: scale(1.1);
        }
        .custom-container {
            max-width: 500px;
            margin: 20px auto;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(8px);
            border: 1px solid rgba(255, 255, 255, 0.18);
        }
        .title-section {
            text-align: center;
            margin-bottom: 30px;
            color: #2C3E50;
        }
        .warning-icon {
            color: #E74C3C;
            font-size: 3rem;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .btn-danger {
            background-color: #E74C3C;
            border: none;
            transition: all 0.3s ease;
        }
        .btn-danger:hover {
            background-color: #C0392B;
            transform: translateY(-2px);
        }
        .btn-secondary {
            background-color: #7F8C8D;
            border: none;
            color: white;
            text-decoration: none;
            padding: 8px 16px;
            border-radius: 4px;
            transition: all 0.3s ease;
        }
        .btn-secondary:hover {
            background-color: #95A5A6;
            color: white;
            transform: translateY(-2px);
        }
        .error-message {
            background-color: #FADBD8;
            color: #E74C3C;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .form-control {
            border-radius: 5px;
            border: 1px solid #ddd;
            padding: 10px 15px;
        }
        .form-control:focus {
            border-color: #764ba2;
            box-shadow: 0 0 0 0.2rem rgba(118, 75, 162, 0.25);
        }
    </style>
</head>
<body>
    <div class="logo-container">
        <a href="${pageContext.request.contextPath}/bienvenida">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="SumixKids Logo" 
                 title="Volver al inicio">
        </a>
    </div>

    <div class="custom-container">
        <div class="title-section">
            <i class="bi bi-exclamation-triangle-fill warning-icon"></i>
            <h2>Confirmar Eliminación</h2>
            <p class="text-muted">Usuario ID: ${param.userId}</p>
        </div>
        
        <c:if test="${not empty error}">
            <div class="error-message">
                <i class="bi bi-exclamation-circle-fill"></i>
                ${error}
            </div>
        </c:if>

        <form id="deleteForm" method="POST" action="${pageContext.request.contextPath}/eliminarUsuario" onsubmit="return confirmarEliminacion(event)">
            <input type="hidden" name="userId" value="${param.userId}">
            
            <div class="form-group">
                <label class="form-label">Para confirmar, escriba "ELIMINAR":</label>
                <input type="text" name="confirmacion" class="form-control" required 
                       placeholder="Escriba ELIMINAR" autocomplete="off">
            </div>

            <div class="form-group">
                <label class="form-label">Contraseña de administrador:</label>
                <input type="password" name="adminPassword" class="form-control" required
                       placeholder="Ingrese su contraseña">
            </div>

            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-danger">
                    <i class="bi bi-trash3-fill"></i> Eliminar Usuario
                </button>
                <a href="${pageContext.request.contextPath}/bienvenida" 
                   class="btn btn-secondary text-center">
                    <i class="bi bi-x-circle"></i> Cancelar
                </a>
            </div>
        </form>
    </div>

    <!-- Sweet Alert 2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <script>
    function confirmarEliminacion(event) {
        event.preventDefault();
        
        const form = document.getElementById('deleteForm');
        const confirmacionInput = form.querySelector('input[name="confirmacion"]');
        const passwordInput = form.querySelector('input[name="adminPassword"]');
        
        if (!confirmacionInput.value || !passwordInput.value) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Por favor complete todos los campos'
            });
            return false;
        }

        if (confirmacionInput.value !== 'ELIMINAR') {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Debe escribir ELIMINAR exactamente'
            });
            return false;
        }

        Swal.fire({
            title: '¿Está seguro?',
            text: "Esta acción no se puede deshacer",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                form.submit();
            }
        });

        return false;
    }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>