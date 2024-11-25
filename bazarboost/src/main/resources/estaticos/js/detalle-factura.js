import { mostrarMensajeExito, mostrarMensajeError, mostrarListaErrores, mostrarMensajeExitoURL } from './mensajes-estado.js';

/**
 * Clase para gestionar la vista de detalle de factura
 */
class DetalleFactura {
    constructor() {
        // Elementos del DOM
        this.cardHeader = document.querySelector('.card-header h4');
        this.fechaEmision = document.querySelector('.card-body p:first-child strong');
        this.totalFactura = document.querySelector('.card-body p:last-child');
        this.tabla = document.querySelector('table tbody');
        this.totalFinal = document.querySelector('tfoot th:last-child');

        // Obtener el ID de la factura de la URL
        this.facturaId = this.obtenerFacturaIdDeURL();

        // Inicializar la carga de datos
        this.inicializar();
    }

    /**
     * Obtiene el ID de la factura desde la URL
     * @returns {string} ID de la factura
     */
    obtenerFacturaIdDeURL() {
        const path = window.location.pathname;
        return path.split('/').pop();
    }

    /**
     * Inicializa la carga de datos
     */
    inicializar() {
        this.cargarDetalleFactura();
        mostrarMensajeExitoURL();
    }

    /**
     * Formatea una fecha a formato local
     * @param {string} fecha - Fecha en formato ISO
     * @returns {string} Fecha formateada
     */
    formatearFecha(fecha) {
        return new Date(fecha).toLocaleDateString('es-MX', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    }

    /**
     * Formatea un valor numérico a formato de moneda
     * @param {number} valor - Valor a formatear
     * @returns {string} Valor formateado como moneda
     */
    formatearMoneda(valor) {
        return `$${valor.toFixed(2)}`;
    }

    /**
     * Carga los detalles de la factura desde el servidor
     */
    async cargarDetalleFactura() {
        try {
            const response = await fetch(`/api/facturas/${this.facturaId}`);

            if (!response.ok) {
                const mensajeError = await response.text();
                console.error('Error del servidor:', mensajeError);

                // Manejar diferentes códigos de error
                if (response.status === 404 || response.status === 403) {
                    window.location.href = `/facturas?mensajeError=${encodeURIComponent(mensajeError)}`;
                    return;
                }

                throw new Error('Error al cargar los detalles de la factura');
            }

            const factura = await response.json();
            this.renderizarDetalleFactura(factura);

        } catch (error) {
            console.error('Error:', error);
            if (error.message.includes('failed to fetch')) {
                mostrarMensajeError('No se pudo establecer conexión con el servidor. Por favor, verifique su conexión a internet.');
                return;
            }
            mostrarMensajeError(error.message);
        }
    }

    /**
     * Renderiza los detalles de la factura en la página
     * @param {Object} factura - Datos de la factura
     */
    renderizarDetalleFactura(factura) {
        // Actualizar encabezado y datos generales
        this.cardHeader.textContent = `Factura #${factura.facturaId}`;
        this.fechaEmision.parentElement.innerHTML =
            `<strong>Fecha de emisión:</strong> ${this.formatearFecha(factura.fechaEmision)}`;
        this.totalFactura.innerHTML =
            `<strong>Total Factura:</strong> ${this.formatearMoneda(factura.totalFactura)}`;

        // Limpiar y renderizar productos
        this.tabla.innerHTML = '';
        factura.productos.forEach(producto => {
            const fila = this.crearFilaProducto(producto);
            this.tabla.appendChild(fila);
        });

        // Actualizar total final
        this.totalFinal.textContent = this.formatearMoneda(factura.totalFactura);
    }

    /**
     * Crea una fila de la tabla para un producto
     * @param {Object} producto - Datos del producto
     * @returns {HTMLElement} Elemento TR con los datos del producto
     */
    crearFilaProducto(producto) {
        const tr = document.createElement('tr');

        const descuentoUnitarioTexto = producto.descuentoUnitarioPorcentaje
            ? `${producto.descuentoUnitarioPorcentaje}% (${this.formatearMoneda(producto.descuentoUnitarioValor)})`
            : 'Sin descuento';

        tr.innerHTML = `
            <td>${producto.nombre}</td>
            <td>${this.formatearMoneda(producto.precioUnitario)}</td>
            <td>${descuentoUnitarioTexto}</td>
            <td>${producto.cantidad}</td>
            <td>${this.formatearMoneda(producto.totalSinDescuento)}</td>
            <td>${this.formatearMoneda(producto.descuentoTotal)}</td>
            <td>${this.formatearMoneda(producto.totalFinal)}</td>
        `;

        return tr;
    }
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new DetalleFactura();
});