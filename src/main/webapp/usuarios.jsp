<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Administración de Usuarios | SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body>
    <div class="container mt-4">
        <h2>Administración de Usuarios</h2>
        
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Nombres</th>
                        <th>Apellidos</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${usuarios}" var="usuario">
                        <tr>
                            <td>${usuario.id}</td>
                            <td>${usuario.username}</td>
                            <td>${usuario.nombres}</td>
                            <td>${usuario.apellidos}</td>
                            <td>${usuario.email}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${usuario.rolId == 1}">Administrador</c:when>
                                    <c:otherwise>Usuario</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${usuario.activo}">
                                        <span class="badge bg-success">Activo</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Inactivo</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="btn-group" role="group">
                                    <a href="${pageContext.request.contextPath}/admin/editarUsuario?id=${usuario.id}" 
                                       class="btn btn-sm btn-primary">
                                       <i class="bi bi-pencil"></i> Editar
                                    </a>
                                    <button class="btn btn-sm btn-danger" 
                                            onclick="confirmarEliminacion(${usuario.id})">
                                            <i class="bi bi-trash"></i> Eliminar
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmarEliminacion(userId) {
            if (confirm('¿Está seguro que desea eliminar este usuario?')) {
                window.location.href = '${pageContext.request.contextPath}/eliminarUsuario?id=' + userId;
            }
        }
    </script>
</body>
</html>