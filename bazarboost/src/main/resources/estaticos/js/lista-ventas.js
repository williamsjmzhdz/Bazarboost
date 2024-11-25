import { mostrarMensajeError, mostrarMensajeErrorDesaparece, mostrarMensajeErrorURL } from './mensajes-estado.js';
import { actualizarPaginacion, getPaginaActual, setPaginaActual } from './paginacion.js';

class ListaVentas {
    constructor() {
        this.ventasTableBody = document.querySelector('table tbody');
        this.orderBySelector = document.getElementById('orderBy');
        this.messageNoVentas = document.getElementById('no-facturas');
        this.tablaVentas = document.querySelector('.table-responsive');
        this.contenedorPaginacion = document.getElementById('paginationContainer');

        // Parámetros de ordenamiento
        this.ordenarPor = 'fecha';
        this.direccionOrden = 'desc';

        this.inicializarEventos();
        this.cargarVentas();
    }

    inicializarEventos() {
        this.orderBySelector.addEventListener('change', () => {
            const [ordenarPor, direccionOrden] = this.orderBySelector.value.split(/(?=[A-Z])/).map(str => str.toLowerCase());
            this.ordenarPor = ordenarPor === 'date' ? 'fecha' : 'total';
            this.direccionOrden = direccionOrden;
            setPaginaActual(0); // Reinicia a la primera página
            this.cargarVentas();
        });

        // Exponer el callback de paginación globalmente
        window.cambiarPaginaCallback = (pagina) => {
            setPaginaActual(pagina);
            this.cargarVentas();
        };
    }

    async cargarVentas() {
        const url = new URL('/api/ventas', window.location.origin);
        url.searchParams.append('pagina', getPaginaActual());
        url.searchParams.append('ordenarPor', this.ordenarPor);
        url.searchParams.append('direccionOrden', this.direccionOrden);

        try {
            const response = await fetch(url);

            if (!response.ok) {
                const errorMessage = await response.text();
                this.manejarError(response.status, errorMessage);
                return;
            }

            const data = await response.json();

            // Solo manejamos la visibilidad del mensaje
            if (data.ventas.length === 0) {
                this.messageNoVentas.style.display = 'block';
            } else {
                this.messageNoVentas.style.display = 'none';
            }

            this.renderizarVentas(data.ventas);
            actualizarPaginacion(data, window.cambiarPaginaCallback);

        } catch (error) {
            console.error('Error al cargar ventas:', error);
            mostrarMensajeError('No se pudo establecer conexión con el servidor. Por favor, verifica tu conexión a internet.');
        }
    }

    manejarError(status, errorMessage) {
        switch (status) {
            case 400:
                mostrarMensajeErrorDesaparece(errorMessage);
                break;
            case 403:
                window.location.href = `/productos?mensajeError=${encodeURIComponent(errorMessage)}`;
                break;
            case 404:
                if (errorMessage.contains("Usuario")) {
                    mostrarMensajeErrorDesaparece("No se encontró información de su usuario. Reinicie sesión e intente nuevamente.");
                } else {
                    mostrarMensajeErrorDesaparece(errorMessage);
                }
                break;
            default:
                mostrarMensajeError('Ocurrió un error inesperado. Intenta nuevamente.');
        }
    }

    renderizarVentas(ventas) {
        this.ventasTableBody.innerHTML = '';

        ventas.forEach(venta => {
            const fila = this.crearFilaVenta(venta);
            this.ventasTableBody.appendChild(fila);
        });
    }

    /**
     * Crea una fila en la tabla para una venta.
     * @param {Object} venta - Datos de la venta.
     * @returns {HTMLElement} Fila de la tabla.
     */
    crearFilaVenta(venta) {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${venta.ventaId}</td>
            <td>${this.formatearFecha(venta.fecha)}</td>
            <td>${venta.nombreCliente}</td>
            <td>${venta.producto.nombre}</td>
            <td>${this.formatearMoneda(venta.producto.precioUnitario)}</td>
            <td>${this.formatearDescuento(venta.producto)}</td>
            <td>${venta.producto.cantidad}</td>
            <td>${this.formatearMoneda(venta.producto.totalSinDescuento)}</td>
            <td>${this.formatearMoneda(venta.producto.descuentoTotal)}</td>
            <td>${this.formatearMoneda(venta.producto.totalFinal)}</td>
        `;

        return tr;
    }

    /**
     * Actualiza la paginación delegando en `paginacion.js`.
     * @param {Object} data - Datos de paginación.
     */
    actualizarPaginacion(data) {
        actualizarPaginacion(data, (pagina) => {
            setPaginaActual(pagina);
            this.cargarVentas();
        });
    }

    /**
     * Formatea una fecha a un formato legible.
     * @param {string} fechaISO - Fecha en formato ISO.
     * @returns {string} Fecha formateada.
     */
    formatearFecha(fechaISO) {
        const fecha = new Date(fechaISO);
        return fecha.toLocaleDateString() + ' ' + fecha.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }

    /**
     * Formatea un valor numérico a moneda.
     * @param {number} valor - Valor a formatear.
     * @returns {string} Valor en formato moneda.
     */
    formatearMoneda(valor) {
        return `$${valor.toFixed(2)}`;
    }

    /**
     * Formatea el descuento de un producto.
     * @param {Object} producto - Datos del producto.
     * @returns {string} Texto con el descuento.
     */
    formatearDescuento(producto) {
        return producto.descuentoUnitarioPorcentaje
            ? `${producto.descuentoUnitarioPorcentaje}% (${this.formatearMoneda(producto.descuentoUnitarioValor)})`
            : 'Sin descuento';
    }
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new ListaVentas();
});