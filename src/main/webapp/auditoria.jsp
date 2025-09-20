<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Log de Auditoría | SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <div class="container-fluid mt-4">
        <h2 class="mb-4">Log de Auditoría</h2>
        
        <!-- Filtros -->
        <form action="${pageContext.request.contextPath}/auditoria" method="get" class="mb-4">
            <div class="row g-3">
                <div class="col-md-3">
                    <input type="text" name="filtro" class="form-control" 
                           placeholder="Buscar..." value="${filtroActual}">
                </div>
                <div class="col-md-2">
                    <input type="date" name="desde" class="form-control" 
                           value="${desdeActual}">
                </div>
                <div class="col-md-2">
                    <input type="date" name="hasta" class="form-control" 
                           value="${hastaActual}">
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-search"></i> Buscar
                    </button>
                </div>
            </div>
        </form>

        <!-- Tabla de logs -->
        <c:if test="${not empty logs}">
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="table-primary">
                        <tr>
                            <th>Fecha/Hora</th>
                            <th>Usuario</th>
                            <th>Acción</th>
                            <th>Descripción</th>
                            <th>Estado</th>
                            <th>IP</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="log" items="${logs}">
                            <tr>
                                <td>${log.fechaHora}</td>
                                <td>${log.nombreUsuario}</td>
                                <td>${log.accion}</td>
                                <td>${log.descripcion}</td>
                                <td>${log.estado}</td>
                                <td>${log.ipUsuario}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <c:if test="${empty logs}">
            <div class="alert alert-info">
                No se encontraron registros de auditoría.
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>