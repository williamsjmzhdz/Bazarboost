// crear-editar-descuento.js

function crearDescuento(event) {
    event.preventDefault();

    const url = new URL('/descuentos/crear', window.location.origin);
    const nombre = document.getElementById('nombre').value;
    const porcentaje = parseInt(document.getElementById('porcentaje').value);

    const data = { nombre, porcentaje };

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            if (response.status === 404) {
                return response.text().then(errorMessage => {
                    mostrarMensajeError(`Ocurrió un error al encontrar su usuario. Inténtelo más tarde.`);
                    throw new Error(`404 Not Found: ${errorMessage}`);
                });
            } else if (response.status === 400) {
                return response.json().then(validationErrors => {
                    mostrarErroresDeValidacion(validationErrors);
                    throw new Error(`Errores de validación: ${validationErrors.join(", ")}`);
                });
            } else {
                mostrarMensajeError("Ocurrió un error inesperado. Inténtelo más tarde.");
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }

        const locationHeader = response.headers.get("Location");
        if (locationHeader) {
            const successUrl = new URL(locationHeader, window.location.origin);
            successUrl.searchParams.append("mensajeExito", "Descuento creado exitosamente");
            window.location.href = successUrl;
        }
    })
    .catch(error => {
        console.error(error);
    });
}

document.querySelector('form').addEventListener('submit', crearDescuento);
