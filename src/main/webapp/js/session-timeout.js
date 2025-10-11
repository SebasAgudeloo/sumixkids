function checkSessionTimeout() {
    const inactivityTime = 60000; // 1 minuto
    const warningTime = 50000;    // Advertencia a los 50 segundos
    let warningShown = false;
    let timerId;

    function forceLogout() {
        // Solo redirigir si no estamos ya en la página de login
        if (!window.location.pathname.endsWith('/login')) {
            window.location.href = window.location.origin + '/sumixkids/login?timeout=true';
        }
    }

    function showWarning() {
        warningShown = true;
        const userResponse = confirm('Su sesión está por expirar. ¿Desea mantenerla activa?');
        
        if (userResponse) {
            // Hacer ping al servidor para mantener la sesión viva
            fetch(window.location.origin + '/sumixkids/ping')
                .then(() => {
                    warningShown = false;
                    resetTimer();
                })
                .catch(() => forceLogout());
        } else {
            forceLogout();
        }
    }

    function resetTimer() {
        clearTimeout(timerId);
        if (!warningShown) {
            timerId = setTimeout(showWarning, warningTime);
        }
    }

    // Solo iniciar el temporizador si no estamos en la página de login
    if (!window.location.pathname.endsWith('/login')) {
        ['mousemove', 'keypress', 'click', 'scroll'].forEach(event => 
            document.addEventListener(event, resetTimer)
        );
        resetTimer();
    }
}

document.addEventListener('DOMContentLoaded', checkSessionTimeout);
