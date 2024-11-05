import { mostrarMensajeError } from './mensajes-estado.js';

// Función para formatear números como moneda
const formatearMoneda = (valor) => {
    return valor ? `$${valor.toFixed(2)}` : '$0.00';
};

// Función para crear una fila de producto
const crearFilaProducto = (producto) => {
    const fila = document.createElement('tr');
    fila.setAttribute('data-producto-id', producto.productoCarritoId);

    // Formatear el descuento para mostrar
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
            <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal">
                <i class="bi bi-trash"></i> Eliminar
            </button>
        </td>
    `;
    return fila;
};

// Función para cargar las direcciones
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

// Función para cargar los métodos de pago
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

// Función para actualizar el total del carrito
const actualizarTotalCarrito = (productos) => {
    const totalCarrito = productos.reduce((sum, producto) => sum + producto.totalFinal, 0);
    document.getElementById('precioTotal').textContent = formatearMoneda(totalCarrito);
};

// Función principal para cargar el carrito
const cargarCarrito = async () => {
    try {
        const response = await fetch('/api/producto-carrito');

        if (!response.ok) {
            if (response.status === 404) {
                mostrarMensajeError('No se encontró información del carrito de compras.');
                return;
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

        // Actualizar total
        actualizarTotalCarrito(data.carritoProductoDTOS);

        // Cargar direcciones y métodos de pago
        cargarDirecciones(data.carritoDireccionDTOS);
        cargarMetodosPago(data.carritoMetodoPagoDTOS);

    } catch (error) {
        console.error('Error:', error);
        mostrarMensajeError('Ocurrió un error al cargar el carrito de compras.');
    }
};

// Cargar el carrito cuando el documento esté listo
document.addEventListener('DOMContentLoaded', cargarCarrito);

export { cargarCarrito };