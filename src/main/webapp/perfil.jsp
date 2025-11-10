<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil · SumixKids</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .coming-soon-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 30px;
            padding: 4rem 3rem;
            text-align: center;
            max-width: 500px;
            box-shadow: 0 25px 70px rgba(0, 0, 0, 0.2);
            animation: fadeInUp 1s ease-out;
        }
        .icon {
            font-size: 6rem;
            color: #667eea;
            margin-bottom: 1.5rem;
            animation: bounce 2s infinite;
        }
        .title {
            font-size: 3rem;
            font-weight: 800;
            background: linear-gradient(45deg, #667eea, #764ba2);
            background-clip: text;
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 1rem;
        }
        .subtitle {
            font-size: 1.5rem;
            color: #6c757d;
            font-weight: 600;
            margin-bottom: 2rem;
        }
        .description {
            font-size: 1.1rem;
            color: #495057;
            margin-bottom: 3rem;
            line-height: 1.6;
        }
        .back-btn {
            background: linear-gradient(45deg, #667eea, #764ba2);
            border: none;
            color: white;
            padding: 12px 30px;
            font-size: 1rem;
            font-weight: 600;
            border-radius: 25px;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        .back-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
            color: white;
        }
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        @keyframes bounce {
            0%, 20%, 50%, 80%, 100% {
                transform: translateY(0);
            }
            40% {
                transform: translateY(-10px);
            }
            60% {
                transform: translateY(-5px);
            }
        }
    </style>
</head>
<body>
    <div class="coming-soon-card">
        <div class="icon">
            <i class="fas fa-user-circle"></i>
        </div>
        
        <h1 class="title">Próximamente</h1>
        
        <h2 class="subtitle">Mi Perfil</h2>
        
        <p class="description">
            Estamos trabajando en tu espacio personal donde podrás gestionar tu información, 
            cambiar contraseñas y configurar tu seguridad.
        </p>
        
        <a href="${pageContext.request.contextPath}/bienvenida" class="back-btn">
            <i class="fas fa-arrow-left me-2"></i>Volver al Dashboard
        </a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
