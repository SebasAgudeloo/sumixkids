<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sumixkids.com/functions" prefix="util" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Mis Hijos · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <style>
        .coming-soon-container {
            background: linear-gradient(135deg, #e83e8c 0%, #fd7e14 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .coming-soon-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 25px;
            padding: 3rem;
            text-align: center;
            max-width: 600px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            border: 1px solid rgba(255, 255, 255, 0.2);
        }
        .coming-soon-icon {
            font-size: 8rem;
            background: linear-gradient(45deg, #e83e8c, #fd7e14);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            animation: pulse 2s ease-in-out infinite;
        }
        .coming-soon-title {
            font-size: 3.5rem;
            font-weight: 900;
            background: linear-gradient(45deg, #e83e8c, #fd7e14);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin: 1rem 0;
        }
        .coming-soon-subtitle {
            font-size: 2rem;
            color: #6c757d;
            font-weight: 600;
            margin-bottom: 2rem;
        }
        .coming-soon-description {
            font-size: 1.2rem;
            color: #495057;
            margin-bottom: 3rem;
            line-height: 1.6;
        }
        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }
        .floating-animation {
            animation: floating 3s ease-in-out infinite;
        }
        @keyframes floating {
            0%, 100% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
        }
        .gradient-button {
            background: linear-gradient(45deg, #e83e8c, #fd7e14);
            border: none;
            color: white;
            padding: 15px 30px;
            font-size: 1.1rem;
            font-weight: 600;
            border-radius: 50px;
            transition: all 0.3s ease;
            box-shadow: 0 10px 30px rgba(232, 62, 140, 0.3);
        }
        .gradient-button:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(232, 62, 140, 0.4);
            color: white;
        }
    </style>
</head>
<body>

<div class="coming-soon-container">
    <div class="coming-soon-card floating-animation">
        <div class="coming-soon-icon mb-4">
            👶
        </div>
        
        <h1 class="coming-soon-title">
            Próximamente
        </h1>
        
        <h2 class="coming-soon-subtitle">
            Mis Hijos
        </h2>
        
        <p class="coming-soon-description">
            Visualiza y gestiona el progreso académico de todos tus hijos en un solo lugar. Monitorea calificaciones, tareas y logros.
            <br><br>
            ¡Mantente conectado con su educación!
        </p>
        
        <div class="d-grid gap-3">
            <a href="${pageContext.request.contextPath}/bienvenida" class="gradient-button btn">
                <i class="fas fa-arrow-left me-2"></i>
                Volver al Dashboard
            </a>
            
            <div class="mt-3">
                <small class="text-muted">
                    <i class="fas fa-clock me-1"></i>
                    Mientras tanto, explora las otras secciones disponibles
                </small>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/session-timeout.js?v=1.6"></script>
<script>
document.addEventListener('DOMContentLoaded', function() {
    const card = document.querySelector('.coming-soon-card');
    card.style.opacity = '0';
    card.style.transform = 'translateY(50px)';
    
    setTimeout(() => {
        card.style.transition = 'all 1s ease-out';
        card.style.opacity = '1';
        card.style.transform = 'translateY(0)';
    }, 100);
});
</script>

</body>
</html>