// Importaciones de utilidades
import { mostrarMensajeError, mostrarMensajeExito } from './mensajes-estado.js';

/**
 * Elementos DOM globales
 */
const contadorCarrito = document.querySelector(".nav-link .badge.bg-danger");

/**
 * Utilidades de formateo y visualización
 */

/**
 * Actualiza el contador visual del carrito en la navegación
 * @param {number} cantidad - Cantidad de productos en el carrito
 */
const actualizarContadorCarrito = (cantidad) => {
    contadorCarrito.textContent = cantidad;
    contadorCarrito.style.display = cantidad > 0 ? 'inline' : 'none';
};

/**
 * Formatea un valor numérico a formato de moneda
 * @param {number} valor - Valor a formatear
 * @returns {string} Valor formateado como moneda
 */
const formatearMoneda = (valor) => {
    return valor ? `$${valor.toFixed(2)}` : '$0.00';
};

/**
 * Funciones de creación de elementos DOM
 */

/**
 * Crea una fila de producto para la tabla del carrito
 * @param {Object} producto - Datos del producto
 * @returns {HTMLElement} Elemento TR con los datos del producto
 */
const crearFilaProducto = (producto) => {
    const fila = document.createElement('tr');
    fila.setAttribute('data-producto-id', producto.productoCarritoId);
    fila.setAttribute('data-producto-base-id', producto.productoId);

    const descuentoUnitarioTexto = producto.descuentoUnitarioPorcentaje
        ? `${producto.descuentoUnitarioPorcentaje}% (${formatearMoneda(producto.descuentoUnitarioValor)})`
        : 'No aplica';

    fila.innerHTML = `
        <td>${producto.nombre}</td>
        <td>${formatearMoneda(producto.precio)}</td>
        <td>${descuentoUnitarioTexto}</td>
        <td>
            <input type="number" class="form-control cantidad" value="${producto.cantidad}" min="1"/>
        </td>
        <td>${formatearMoneda(producto.totalSinDescuento)}</td>
        <td>${formatearMoneda(producto.descuentoTotal || 0)}</td>
        <td>${formatearMoneda(producto.totalFinal)}</td>
        <td class="btn-cell">
            <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal"
            onclick="prepararEliminarProductoCarrito(${producto.productoId}, '${producto.nombre}')">
                <i class="bi bi-trash"></i> Eliminar
            </button>
        </td>
    `;
    return fila;
};

/**
 * Funciones de carga de datos
 */

/**
 * Carga las direcciones disponibles en el select correspondiente
 * @param {Array} direcciones - Lista de direcciones del usuario
 */
const cargarDirecciones = (direcciones) => {
    const selectDireccion = document.querySelector('select[aria-label="Seleccionar Dirección"]');
    selectDireccion.innerHTML = '<option selected>Selecciona tu dirección de envío</option>';

    direcciones.forEach(direccion => {
        const option = document.createElement('option');
        option.value = direccion.direccionId;
        option.textContent = direccion.direccion;
        selectDireccion.appendChild(option);
    });
};

/**
 * Carga los métodos de pago disponibles en el select correspondiente
 * @param {Array} metodosPago - Lista de métodos de pago del usuario
 */
const cargarMetodosPago = (metodosPago) => {
    const selectMetodoPago = document.querySelector('select[aria-label="Seleccionar Método de Pago"]');
    selectMetodoPago.innerHTML = '<option selected>Selecciona tu método de pago</option>';

    metodosPago.forEach(metodo => {
        const option = document.createElement('option');
        option.value = metodo.metodoPagoId;
        option.textContent = `${metodo.tipo} - Terminada en ${metodo.terminacion} (Expira: ${metodo.fechaExpiracion})`;
        selectMetodoPago.appendChild(option);
    });
};

/**
 * Actualiza el total mostrado en el carrito
 * @param {Array} productos - Lista de productos en el carrito
 */
const actualizarTotalCarrito = (productos) => {
    const totalCarrito = productos.reduce((sum, producto) => sum + producto.totalFinal, 0);
    document.getElementById('precioTotal').textContent = formatearMoneda(totalCarrito);
};

/**
 * Funciones principales del carrito
 */

/**
 * Carga todos los datos del carrito desde el servidor
 * @throws {Error} Si hay problemas al cargar los datos
 */
const cargarCarrito = async () => {
    try {
        const response = await fetch('/api/producto-carrito');

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('No se encontró información del carrito de compras.');
            }
            throw new Error('Error al cargar el carrito');
        }

        const data = await response.json();

        // Limpiar y cargar productos
        const tbody = document.querySelector('table tbody');
        tbody.innerHTML = '';
        data.carritoProductoDTOS.forEach(producto => {
            tbody.appendChild(crearFilaProducto(producto));
        });

        // Actualizar interfaz
        actualizarTotalCarrito(data.carritoProductoDTOS);
        cargarDirecciones(data.carritoDireccionDTOS);
        cargarMetodosPago(data.carritoMetodoPagoDTOS);

    } catch (error) {
        console.error('Error:', error);
        mostrarMensajeError(error.message || 'Ocurrió un error al cargar el carrito de compras.');
    }
};

/**
 * Prepara el modal de confirmación para eliminar un producto
 * @param {number} productoId - ID del producto a eliminar
 * @param {string} nombreProducto - Nombre del producto a eliminar
 */
window.prepararEliminarProductoCarrito = (productoId, nombreProducto) => {
    const modal = document.getElementById('confirmDeleteModal');
    const btnEliminar = modal.querySelector('.btn-danger');
    const modalBody = modal.querySelector('.modal-body');

    modalBody.textContent = `¿Estás seguro de que deseas eliminar el producto "${nombreProducto}" del carrito?`;
    btnEliminar.onclick = () => eliminarProductoCarrito(productoId);
};

/**
 * Elimina un producto del carrito
 * @param {number} productoId - ID del producto a eliminar
 */
const eliminarProductoCarrito = async (productoId) => {
    try {
        const response = await fetch('/api/producto-carrito/actualizar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ productoId, accion: "quitar" })
        });

        if (!response.ok) {
            const errorMessage = await response.text();

            switch (response.status) {
                case 404:
                    if (errorMessage.includes('Usuario')) {
                        throw new Error('Error al encontrar el usuario. Por favor, inicia sesión nuevamente.');
                    } else if (errorMessage.includes('Producto')) {
                        throw new Error('El producto que intentas quitar del carrito no existe.');
                    } else if (errorMessage.includes('carrito')) {
                        throw new Error('El producto que intentas quitar, no se encuentra en el carrito.');
                    }
                    throw new Error('Recurso no encontrado.');

                case 400:
                    throw new Error('La acción que intentas hacer no es válida. La única acción válida es "quitar" del carrito.');

                default:
                    throw new Error('Ocurrió un error al quitar el producto del carrito. Inténtalo más tarde.');
            }
        }

        const data = await response.json();
        mostrarMensajeExito('Producto eliminado del carrito exitosamente');
        await cargarCarrito();
        actualizarContadorCarrito(data.totalProductos);

        // Cerrar el modal
        const modal = bootstrap.Modal.getInstance(document.getElementById('confirmDeleteModal'));
        modal.hide();

    } catch (error) {
        console.error('Error:', error);
        mostrarMensajeError(error.message);
    }
};

// Event listeners
document.addEventListener('DOMContentLoaded', () => {
    // Cargar carrito al iniciar
    cargarCarrito();

    // Event listener para los inputs de cantidad
    const tbody = document.querySelector('table tbody');

    // Almacenar la cantidad original cuando el input recibe el foco
    tbody.addEventListener('focus', (event) => {
        if (event.target.classList.contains('cantidad')) {
            event.target.setAttribute('data-cantidad-original', event.target.value);
        }
    }, true);

    // Manejar el cambio de cantidad
    tbody.addEventListener('change', async (event) => {
        if (event.target.classList.contains('cantidad')) {
            const fila = event.target.closest('tr');
            const productoCarritoId = parseInt(fila.getAttribute('data-producto-id'));
            const productoId = parseInt(fila.getAttribute('data-producto-base-id'));
            const nuevaCantidad = parseInt(event.target.value);
            const cantidadOriginal = parseInt(event.target.getAttribute('data-cantidad-original'));

            if (isNaN(nuevaCantidad) || nuevaCantidad < 1) {
                mostrarMensajeError('La cantidad debe ser un número mayor a 0');
                event.target.value = cantidadOriginal;
                return;
            }

            try {
                const response = await fetch('/api/producto-carrito/modificar-cantidad', {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        productoCarritoId,
                        productoId,
                        cantidad: nuevaCantidad
                    })
                });

                if (!response.ok) {
                    const errorMessage = await response.text();

                    switch (response.status) {
                        case 404:
                            if (errorMessage.includes('Usuario')) {
                                throw new Error('Error al encontrar el usuario. Por favor, inicia sesión nuevamente.');
                            } else if (errorMessage.includes('Producto')) {
                                throw new Error('El producto que intentas modificar no existe en el carrito.');
                            }
                            throw new Error('Recurso no encontrado.');

                        case 400:
                            if (errorMessage.includes('cantidad')) {
                                throw new Error('La cantidad debe ser mayor a 0.');
                            }
                            throw new Error('Datos inválidos. Por favor verifica la información.');

                        default:
                            throw new Error('Error al actualizar la cantidad del producto. Por favor inténtalo más tarde.');
                    }
                }

                const data = await response.json();
                await cargarCarrito();
                actualizarContadorCarrito(data.totalProductos);

            } catch (error) {
                console.error('Error al actualizar cantidad:', error);
                mostrarMensajeError(error.message);
                event.target.value = cantidadOriginal;
            }
        }
    });
});

// Exportaciones
export { cargarCarrito };