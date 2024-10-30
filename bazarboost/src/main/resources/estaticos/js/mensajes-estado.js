// mensajes-estado.js

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

// Función para mostrar mensaje de éxito en el contenedor de reseñas
function mostrarMensajeExitoReview(mensaje) {
    const successElement = document.getElementById('review-success-alert');
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

// Función para mostrar mensaje de error en el contenedor de reseñas
function mostrarMensajeErrorReview(mensaje) {
    const warningElement = document.getElementById('review-warning-danger');
    const warningMessage = document.getElementById('review-warning-message');
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
