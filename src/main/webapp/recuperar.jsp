<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Recuperar contraseña · SumixKids</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body class="bg-gradient-primary d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/images/sumixkids.png" alt="Logo SumixKids" style="height: 36px; width: auto; margin-right: 8px;"/>
            SumixKids
        </a>
    </div>
</nav>
<main class="flex-fill d-flex align-items-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-sm-10 col-md-7 col-lg-5">
                <div class="card shadow-lg border-0 rounded-4">
                    <div class="card-body p-4 p-md-5">
                        <h1 class="h4 mb-4 text-center fw-bold text-primary">Recuperar contraseña</h1>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>
                        <c:if test="${not empty mensaje}">
                            <div class="alert alert-success">${mensaje}</div>
                        </c:if>
                        <form method="post" action="${pageContext.request.contextPath}/recuperar">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Correo electrónico <span class="text-danger">*</span></label>
                                <input type="email" name="correo" class="form-control" placeholder="Ingresa tu correo" required />
                                <div class="invalid-feedback">Campo obligatorio.</div>
                            </div>
                            <div class="d-grid mb-2">
                                <button type="submit" class="btn btn-primary">Enviar código</button>
                            </div>
                        </form>
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/login">Volver a iniciar sesión</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<script>
    setTimeout(() => {
        document.querySelectorAll('.alert-danger, .alert-success').forEach(alert => {
            alert.style.display = 'none';
        });
    }, 3000);
</script>
</body>
</html>
