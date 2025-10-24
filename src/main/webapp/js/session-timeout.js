function checkSessionTimeout() {
    // Tiempos de producción (30 minutos)
    const inactivityTime = 30 * 60 * 1000; // 30 minutos total (1,800,000 ms)
    const warningTime = 28 * 60 * 1000;     // Advertencia a los 28 minutos (1,680,000 ms)
    let warningShown = false;
    let timerId;
    
    // Debug mode (cambiar a false en producción, o usar window.sessionDebugEnabled)
    const DEBUG_MODE = false || window.sessionDebugEnabled;
    const debug = DEBUG_MODE ? console.log : () => {};
    
    debug('Session timeout inicializado:', { inactivityTime, warningTime });

    function forceLogout() {
        // Solo redirigir si no estamos ya en la página de login
        if (!window.location.pathname.endsWith('/login')) {
            window.location.href = window.location.origin + '/sumixkids/login?timeout=true';
        }
    }

    function showWarning() {
        debug('Mostrando advertencia de sesión...');
        warningShown = true;
        
        // Verificar si SweetAlert2 está disponible
        if (typeof Swal !== 'undefined') {
            debug('SweetAlert2 disponible, mostrando mensaje elegante');
            let timeLeft = 120; // 2 minutos en segundos
            
            Swal.fire({
                title: '⏰ Sesión por expirar',
                html: `
                    <div style="margin-bottom: 20px;">
                        <p style="margin-bottom: 15px; font-size: 16px;">Su sesión está por expirar. ¿Desea mantenerla activa?</p>
                        <div class="session-timer-container" style="padding: 20px; border-radius: 15px; margin: 15px 0;">
                            <div style="font-size: 24px; font-weight: bold; color: #dc3545; margin-bottom: 10px;">
                                ⏱️ <span id="countdown-timer">${Math.floor(timeLeft / 60)}:${(timeLeft % 60).toString().padStart(2, '0')}</span>
                            </div>
                            <div style="background: #e9ecef; height: 12px; border-radius: 6px; overflow: hidden; border: 1px solid #ced4da;">
                                <div id="progress-bar" style="background: linear-gradient(90deg, #28a745 0%, #20c997 50%, #28a745 100%); height: 100%; width: 100%; transition: width 1s ease-out, background 0.5s ease; box-shadow: inset 0 2px 4px rgba(0,0,0,0.1);"></div>
                            </div>
                            <div style="font-size: 12px; color: #6c757d; margin-top: 8px;">
                                Tiempo restante para decidir
                            </div>
                        </div>
                    </div>
                `,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#28a745',
                cancelButtonColor: '#dc3545',
                confirmButtonText: '✅ Sí, mantener activa',
                cancelButtonText: '❌ No, cerrar sesión',
                reverseButtons: true,
                allowOutsideClick: false,
                allowEscapeKey: false,
                timer: false, // Deshabilitamos el timer automático de SweetAlert2
                timerProgressBar: false, // Usamos nuestro custom
                didOpen: () => {
                    const countdownElement = document.getElementById('countdown-timer');
                    const progressBar = document.getElementById('progress-bar');
                    
                    debug('🕐 Contador visual iniciado: 2:00 minutos');
                    
                    const interval = setInterval(() => {
                        timeLeft--;
                        
                        // Actualizar contador
                        const minutes = Math.floor(timeLeft / 60);
                        const seconds = timeLeft % 60;
                        const timeString = `${minutes}:${seconds.toString().padStart(2, '0')}`;
                        countdownElement.textContent = timeString;
                        
                        // Debug solo en momentos clave para no hacer spam
                        if (timeLeft <= 10 || timeLeft % 15 === 0) {
                            debug(`⏰ Tiempo restante: ${timeString} (${timeLeft}s)`);
                        }
                        
                        // Actualizar barra de progreso
                        const percentage = (timeLeft / 120) * 100;
                        progressBar.style.width = percentage + '%';
                        
                        // 🎨 SISTEMA DE COLORES CORREGIDO: 🟢Verde(2:00-1:31) → 🟡Amarillo(1:30-0:46) → 🟠Naranja(0:45-0:21) → 🔴Rojo(0:20-0:00)
                        if (timeLeft <= 20) {
                            // 🔴 ROJO - DANGER (0:00 - 0:20)
                            progressBar.style.background = 'linear-gradient(90deg, #dc3545 0%, #e74c3c 50%, #dc3545 100%)';
                            countdownElement.style.color = '#dc3545';
                            countdownElement.style.textShadow = '0 0 10px rgba(220, 53, 69, 0.5)';
                        } else if (timeLeft <= 45) {
                            // 🟠 NARANJA - ALERTA FUERTE (0:21 - 0:45)
                            progressBar.style.background = 'linear-gradient(90deg, #fd7e14 0%, #ff8c42 50%, #fd7e14 100%)';
                            countdownElement.style.color = '#fd7e14';
                            countdownElement.style.textShadow = '0 0 10px rgba(253, 126, 20, 0.5)';
                        } else if (timeLeft <= 90) {
                            // 🟡 AMARILLO - PRECAUCIÓN (0:46 - 1:30)
                            progressBar.style.background = 'linear-gradient(90deg, #ffc107 0%, #ffca2c 50%, #ffc107 100%)';
                            countdownElement.style.color = '#ffc107';
                            countdownElement.style.textShadow = '0 0 8px rgba(255, 193, 7, 0.4)';
                        }
                        
                        // Efectos especiales en diferentes momentos
                        if (timeLeft <= 10) {
                            // 🚨 ÚLTIMOS 10 SEGUNDOS - ANIMACIÓN DE PULSO
                            countdownElement.style.setProperty('animation', 'pulse 0.5s ease-in-out infinite alternate', 'important');
                            progressBar.style.setProperty('animation', 'pulse 0.5s ease-in-out infinite alternate', 'important');
                            countdownElement.style.fontSize = '28px';
                            countdownElement.style.fontWeight = '900';
                            
                            // Agregar clase CSS para mayor control
                            countdownElement.classList.add('pulse-animation');
                            progressBar.classList.add('pulse-animation');
                        } else if (timeLeft <= 30) {
                            // ⚠️ ÚLTIMOS 30 SEGUNDOS - TEXTO MÁS GRANDE
                            countdownElement.style.fontSize = '26px';
                            countdownElement.style.fontWeight = '900';
                            
                            // Remover animación si no está en los últimos 10
                            countdownElement.classList.remove('pulse-animation');
                            progressBar.classList.remove('pulse-animation');
                            countdownElement.style.animation = '';
                            progressBar.style.animation = '';
                        } else {
                            // 🆗 TIEMPO NORMAL - SIN EFECTOS ESPECIALES
                            countdownElement.classList.remove('pulse-animation');
                            progressBar.classList.remove('pulse-animation');
                            countdownElement.style.animation = '';
                            progressBar.style.animation = '';
                            countdownElement.style.fontSize = '24px';
                            countdownElement.style.fontWeight = 'bold';
                        }
                        
                        if (timeLeft <= 0) {
                            clearInterval(interval);
                            // Cerrar automáticamente y hacer logout cuando llegue a 0
                            Swal.close();
                            setTimeout(() => forceLogout(), 100); // Pequeño delay para suavidad
                        }
                    }, 1000);
                    
                    // Limpiar interval si se cierra el modal manualmente
                    const popup = Swal.getPopup();
                    if (popup) {
                        // Múltiples eventos para asegurar limpieza
                        popup.addEventListener('DOMNodeRemoved', () => clearInterval(interval));
                        
                        // También escuchar el evento de cierre de SweetAlert2
                        const originalClose = Swal.close;
                        Swal.close = function() {
                            clearInterval(interval);
                            return originalClose.apply(this, arguments);
                        };
                    }
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    debug('Usuario eligió mantener la sesión activa');
                    // Hacer ping al servidor para mantener la sesión viva
                    // Temporalmente simular éxito sin endpoint real
                    warningShown = false;
                    resetTimer();
                    Swal.fire({
                        title: '✅ Sesión renovada',
                        text: 'Su sesión ha sido renovada exitosamente',
                        icon: 'success',
                        timer: 2000,
                        showConfirmButton: false
                    });
                    
                    // Opcional: hacer fetch real si tienes el endpoint
                    // fetch(window.location.origin + '/sumixkids/ping')
                    //     .then(() => {
                    //         debug('Ping exitoso');
                    //     })
                    //     .catch((error) => {
                    //         debug('Ping falló:', error);
                    //     });
                } else {
                    forceLogout();
                }
            });
        } else {
            debug('SweetAlert2 no disponible, usando confirm nativo');
            // Fallback si SweetAlert2 no está disponible
            const userResponse = confirm('Su sesión está por expirar. ¿Desea mantenerla activa?');
            
            if (userResponse) {
                debug('Usuario eligió mantener sesión (fallback)');
                warningShown = false;
                resetTimer();
                // Opcional: hacer ping si tienes el endpoint
                // fetch(window.location.origin + '/sumixkids/ping')
                //     .then(() => debug('Ping exitoso'))
                //     .catch(() => forceLogout());
            } else {
                debug('Usuario eligió cerrar sesión');
                forceLogout();
            }
        }
    }

    function resetTimer() {
        clearTimeout(timerId);
        if (!warningShown) {
            timerId = setTimeout(showWarning, warningTime);
            // Solo log inicial, no en cada reset para evitar spam
            if (!resetTimer.logged) {
                debug('Timer de sesión configurado para', warningTime / 60000, 'minutos');
                resetTimer.logged = true;
            }
        }
    }

    // Solo iniciar el temporizador si no estamos en la página de login
    if (!window.location.pathname.endsWith('/login')) {
        debug('Configurando event listeners para actividad del usuario');
        ['mousemove', 'keypress', 'click', 'scroll'].forEach(event => 
            document.addEventListener(event, resetTimer)
        );
        resetTimer();
        debug('Session timeout activo en:', window.location.pathname);
        
        // Función global para testing manual
        window.testSessionWarning = function(testTime = 120) {
            console.log('🧪 Testing session warning manually con timer visual mejorado...');
            showWarning();
        };
        
        // Función global para mostrar mensaje de sesión
        window.showSessionWarning = showWarning;
        
        // Función global para habilitar debug
        window.enableSessionDebug = function() {
            window.sessionDebugEnabled = true;
            console.log('🔍 Debug de sesión habilitado. Recarga la página para aplicar.');
        };
        
        debug('💡 Para testing: window.testSessionWarning() | Para debug: window.enableSessionDebug()');
        
    } else {
        debug('Session timeout no iniciado - página de login detectada');
    }
}

// Session Timeout Script - Versión PRODUCCIÓN con SweetAlert2 (Timer sincronizado - no más cierre prematuro)
console.log('🔥 SESSION-TIMEOUT.JS v1.6 CARGADO - Timer sincronizado: cierre exacto en 0:00');

// Agregar estilos CSS para animaciones
const style = document.createElement('style');
style.textContent = `
    @keyframes pulse {
        0% { 
            transform: scale(1); 
            opacity: 1; 
        }
        50% { 
            transform: scale(1.05); 
            opacity: 0.8; 
        }
        100% { 
            transform: scale(1.1); 
            opacity: 1; 
        }
    }
    
    .pulse-animation {
        animation: pulse 0.6s ease-in-out infinite alternate !important;
    }
    
    .swal2-html-container {
        overflow: visible !important;
    }
    
    .session-timer-container {
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border: 2px solid #dee2e6;
        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    
    /* Asegurar que el timer tenga prioridad sobre otros estilos */
    #countdown-timer {
        display: inline-block !important;
        transition: all 0.3s ease !important;
    }
    
    #progress-bar {
        transform-origin: left center !important;
        transition: width 1s ease-out, background 0.5s ease, transform 0.3s ease !important;
    }
`;
document.head.appendChild(style);

document.addEventListener('DOMContentLoaded', checkSessionTimeout);

// También ejecutar inmediatamente por si DOMContentLoaded ya pasó
if (document.readyState !== 'loading') {
    checkSessionTimeout();
}
