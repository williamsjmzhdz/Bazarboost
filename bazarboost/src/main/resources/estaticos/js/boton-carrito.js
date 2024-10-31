const contadorCarrito = document.querySelector(".nav-link .badge.bg-danger");

function actualizarContadorCarrito(cantidad) {
    contadorCarrito.textContent = cantidad;
}

async function actualizarCarrito(event) {
    const botonCarrito = event.target.closest("a[data-producto-id]");
    const productoId = botonCarrito.dataset.productoId;
    const accion = botonCarrito.dataset.accion;
    const url = '/api/producto-carrito/actualizar';

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ productoId, accion })
        });

        if (!response.ok) {
            // Manejar errores específicos según el código de estado de la respuesta
            switch (response.status) {
                case 404:
                    mostrarMensajeErrorDesaparece(accion === 'agregar'
                        ? 'El producto que intenta agregar no existe.'
                        : 'El producto que intenta quitar no se encuentra en su carrito.');
                    break;
                case 409:
                    mostrarMensajeErrorDesaparece('Este producto ya está en su carrito.');
                    break;
                case 403:
                    mostrarMensajeErrorDesaparece('No puede agregar su propio producto al carrito.');
                    break;
                case 400:
                    mostrarMensajeErrorDesaparece('Ocurrió un error en la solicitud. Por favor, intente nuevamente.');
                    break;
                default:
                    mostrarMensajeErrorDesaparece('Ocurrió un error inesperado. Intente nuevamente.');
            }
            throw new Error('Error en la actualización del carrito');
        }

        const data = await response.json();
        actualizarContadorCarrito(data.totalProductos);

        if (accion === 'agregar') {
            botonCarrito.classList.replace('btn-primary', 'btn-danger');
            botonCarrito.innerHTML = '<i class="bi bi-cart-dash"></i> Quitar del carrito';
            botonCarrito.dataset.accion = 'quitar';
        } else {
            botonCarrito.classList.replace('btn-danger', 'btn-primary');
            botonCarrito.innerHTML = '<i class="bi bi-cart-plus"></i> Agregar al carrito';
            botonCarrito.dataset.accion = 'agregar';
        }
    } catch (error) {
        console.error('Error al actualizar el carrito:', error);
    }
}
