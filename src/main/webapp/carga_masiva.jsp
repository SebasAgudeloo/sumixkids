<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Carga Masiva | SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Carga Masiva de Usuarios</h2>
        
        <c:if test="${not empty mensajeExito}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${mensajeExito}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty errores}">
            <div class="alert alert-danger">
                <h5>Se encontraron los siguientes errores:</h5>
                <ul>
                    <c:forEach var="error" items="${errores}">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <div class="card">
            <div class="card-body">
                <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/cargaMasiva">
                    <div class="mb-3">
                        <label for="archivo" class="form-label">Seleccione archivo Excel (.xlsx)</label>
                        <input type="file" class="form-control" id="archivo" name="archivo" accept=".xlsx" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Cargar Archivo</button>
                </form>
            </div>
        </div>
        
        <div class="mt-3">
            <h5>Formato requerido:</h5>
            <ul>
                <li>Archivo Excel (.xlsx)</li>
                <li>Columnas: Nombres, Apellidos, Email, Usuario, Rol</li>
                <li>Los nombres y apellidos solo deben contener letras</li>
                <li>El usuario debe contener al menos 2 números</li>
                <li>El email debe tener un formato válido</li>
            </ul>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>