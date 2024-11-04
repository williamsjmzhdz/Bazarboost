function mostrarMensajeError(mensaje) {
    const warningElement = document.getElementById('warning-danger');
    const warningMessage = document.getElementById('warning-message');
    warningMessage.textContent = mensaje;
    warningElement.classList.remove('d-none');
}

function mostrarMensajeErrorSolo(mensaje) {
    const warningElement = document.getElementById('warning-danger');
    const warningMessage = document.getElementById('warning-message');
    warningMessage.textContent = mensaje;
    warningElement.classList.remove('d-none');

    document.getElementById('bodyContainer').style.height = "100%";
}


function mostrarListaErrores(errores) {
    const warningElement = document.getElementById('warning-danger');
    warningElement.innerHTML = `
        <i class="bi bi-exclamation-triangle"></i> No se pudo completar la acción. Solucione los siguientes errores:
        <ul></ul>
    `;

    const errorList = warningElement.querySelector('ul');
    errores.forEach(error => {
        const errorItem = document.createElement('li');
        errorItem.textContent = error;
        errorList.appendChild(errorItem);
    });

    warningElement.classList.remove('d-none');
}

function mostrarMensajeExitoURL() {
    const urlParams = new URLSearchParams(window.location.search);
    const mensajeExito = urlParams.get('mensajeExito');
    if (mensajeExito) {
        const successAlert = document.getElementById('success-alert');
        successAlert.querySelector('div').textContent = mensajeExito;
        successAlert.classList.remove('d-none');

        setTimeout(() => {
            successAlert.classList.add('fade-out');

            setTimeout(() => {
                successAlert.classList.add('d-none');
                successAlert.classList.remove('fade-out');

                const url = new URL(window.location);
                url.searchParams.delete('mensajeExito');
                window.history.replaceState(null, '', url);
            }, 500);
        }, 5000);
    }
}

// Función para mostrar mensaje de éxito
function mostrarMensajeExito(mensaje) {
    const successElement = document.getElementById('success-alert');
    successElement.querySelector('div').textContent = mensaje;
    successElement.classList.remove('d-none');

    // Ocultar automáticamente después de 5 segundos
    setTimeout(() => {
        successElement.classList.add('fade-out');

        setTimeout(() => {
            successElement.classList.add('d-none');
            successElement.classList.remove('fade-out');
        }, 500);
    }, 5000);
}

function mostrarMensajeErrorURL() {
    const urlParams = new URLSearchParams(window.location.search);
    const mensajeError = urlParams.get('mensajeError');
    if (mensajeError) {
        const warningAlert = document.getElementById('warning-danger');
        const warningMessage = document.getElementById('warning-message');
        warningMessage.textContent = mensajeError;
        warningAlert.classList.remove('d-none');

        setTimeout(() => {
            warningAlert.classList.add('fade-out');

            setTimeout(() => {
                warningAlert.classList.add('d-none');
                warningAlert.classList.remove('fade-out');

                const url = new URL(window.location);
                url.searchParams.delete('mensajeError');
                window.history.replaceState(null, '', url);
            }, 500);
        }, 5000);
    }
}

// Función para mostrar mensaje de error
function mostrarMensajeErrorReview(mensaje) {
    const warningElement = document.getElementById('warning-danger');
    const warningMessage = document.getElementById('warning-message');
    warningMessage.textContent = mensaje;
    warningElement.classList.remove('d-none');

    // Ocultar automáticamente después de 5 segundos
    setTimeout(() => {
        warningElement.classList.add('fade-out');

        setTimeout(() => {
            warningElement.classList.add('d-none');
            warningElement.classList.remove('fade-out');
        }, 500);
    }, 5000);
}

function mostrarMensajeErrorDesaparece(mensaje) {
    const warningElement = document.getElementById('warning-danger');
    const warningMessage = document.getElementById('warning-message');
    warningMessage.textContent = mensaje;
    warningElement.classList.remove('d-none');

    // Scroll automático hacia la parte superior para ver el mensaje
    window.scrollTo({ top: 0, behavior: 'smooth' });

    // Ocultar el mensaje después de 5 segundos
    setTimeout(() => {
        warningElement.classList.add('fade-out');
        setTimeout(() => {
            warningElement.classList.add('d-none');
            warningElement.classList.remove('fade-out');
        }, 500);
    }, 5000);
}

export { mostrarMensajeError, mostrarMensajeErrorSolo, mostrarMensajeErrorDesaparece, mostrarMensajeExito, mostrarMensajeExitoURL, mostrarMensajeErrorURL, mostrarListaErrores };
