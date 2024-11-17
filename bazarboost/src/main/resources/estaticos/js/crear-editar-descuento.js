import { mostrarMensajeError, mostrarListaErrores } from './mensajes-estado.js';


function manejarDescuento(event) {

    event.preventDefault();

    const modo = document.getElementById('modo').value;
    const nombre = document.getElementById('nombre').value;
    const porcentaje = parseInt(document.getElementById('porcentaje').value);
    const data = { nombre, porcentaje };

    let url, method, successMessage;

    if (modo === 'crear') {
        url = new URL('/api/descuentos', window.location.origin);
        method = 'POST';
        successMessage = 'Descuento creado exitosamente';
    } else {
        const descuentoId = document.getElementById('descuentoId').value;
        url = new URL(`/api/descuentos/${descuentoId}`, window.location.origin);
        method = 'PUT';
        successMessage = 'Descuento actualizado exitosamente';
    }

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            handleErrorResponse(response);
            return;
        }

        handleSuccessResponse(response, successMessage);
    })
    .catch(error => {
        console.error(error);
        mostrarMensajeError("Ocurrió un error inesperado. Inténtelo más tarde.");
    });
}

function handleErrorResponse(response) {
    return response.text().then(errorMessage => {
        switch (response.status) {
            case 404:
                mostrarMensajeError('Ocurrió un error al encontrar su usuario. Inténtelo más tarde.');
                throw new Error(`404 Not Found: ${errorMessage}`);

            case 400:
                // Si es un error de validación, viene como JSON
                try {
                    const validationErrors = JSON.parse(errorMessage);
                    mostrarListaErrores(validationErrors);
                    throw new Error(`Errores de validación: ${validationErrors.join(", ")}`);
                } catch (e) {
                    // Si no es JSON, mostrar el mensaje directamente
                    mostrarError(errorMessage);
                    throw new Error(`Bad Request: ${errorMessage}`);
                }

            case 409:
                mostrarMensajeError(errorMessage);
                throw new Error(`Conflict: ${errorMessage}`);

            case 403:
                mostrarMensajeError('No tienes permiso para realizar esta acción.');
                throw new Error(`Forbidden: ${errorMessage}`);

            default:
                mostrarMensajeError("Ocurrió un error inesperado. Inténtelo más tarde.");
                throw new Error(`HTTP error! status: ${response.status}`);
        }
    });
}

function handleSuccessResponse(response, successMessage) {
    const successUrl = new URL('/descuentos', window.location.origin);
    successUrl.searchParams.append("mensajeExito", successMessage);
    window.location.href = successUrl;
}

// Agregar el event listener
document.querySelector('form').addEventListener('submit', manejarDescuento);