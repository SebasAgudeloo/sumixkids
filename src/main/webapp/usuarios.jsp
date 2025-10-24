<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html>
<head>
    <title>Administración de Usuarios | SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
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
                                            onclick="confirmarEliminacion('${usuario.id}')">
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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        document.getElementById('year').textContent = new Date().getFullYear();
        
        function confirmarEliminacion(userId) {
            Swal.fire({
                title: '🗑️ Eliminar Usuario',
                text: '¿Está seguro que desea eliminar este usuario?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#dc3545',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = '${pageContext.request.contextPath}/eliminar_usuario?id=' + userId;
                }
            });
        }
    </script>
</body>
</html>
