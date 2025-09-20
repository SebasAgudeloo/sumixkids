function checkSessionTimeout() {
    let inactivityTime = 60000; // 1 minuto en milisegundos
    let timeoutWarning = 50000; // Mostrar advertencia 10 segundos antes
    let warningShown = false;
    let timerId;

    function resetTimer() {
        clearTimeout(timerId);
        if (!warningShown) {
            timerId = setTimeout(showWarning, timeoutWarning);
        }
    }

    function showWarning() {
        warningShown = true;
        if (confirm('Su sesión está por expirar. ¿Desea mantenerla activa?')) {
            // Hacer una petición al servidor para mantener la sesión
            fetch('ping')
                .then(() => {
                    warningShown = false;
                    resetTimer();
                })
                .catch(() => window.location.href = 'login?timeout=true');
        } else {
            window.location.href = 'login?timeout=true';
        }
    }

    // Reiniciar el temporizador en cada actividad
    document.addEventListener('mousemove', resetTimer);
    document.addEventListener('keypress', resetTimer);
    document.addEventListener('click', resetTimer);
    document.addEventListener('scroll', resetTimer);

    resetTimer();
}

// Iniciar monitoreo cuando el documento esté listo
document.addEventListener('DOMContentLoaded', checkSessionTimeout);