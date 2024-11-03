/**
 * Actualiza el contador de productos en el carrito en la barra de navegación.
 * Si no hay productos, oculta el contador.
 */
function actualizarContadorCarrito() {
    const contadorCarrito = document.getElementById('cart-counter');

    if (!contadorCarrito) {
        console.error('No se encontró el elemento del contador del carrito');
        return;
    }

    fetch('/api/producto-carrito/total')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener el total del carrito');
            }
            return response.json();
        })
        .then(data => {
            const totalProductos = data.totalProductos;
            contadorCarrito.textContent = totalProductos;

            console.log("Total productos: ", totalProductos);

            // Mostrar u ocultar el contador según si hay productos
            if (totalProductos > 0) {
                contadorCarrito.innerText = totalProductos;
                contadorCarrito.style.display = 'inline';
            } else {
                contadorCarrito.style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Error al actualizar el contador del carrito:', error);
            contadorCarrito.style.display = 'none';
        });
}

// Actualizar el contador cuando se carga la página
document.addEventListener('DOMContentLoaded', actualizarContadorCarrito);