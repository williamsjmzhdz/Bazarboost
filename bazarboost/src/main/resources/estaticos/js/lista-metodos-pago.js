import { mostrarMensajeExito, mostrarMensajeError, mostrarListaErrores, mostrarMensajeExitoURL } from './mensajes-estado.js';

/**
 * Clase para gestionar la lista de métodos de pago
 */
class ListaMetodosPago {
    constructor() {
        this.tabla = document.querySelector('table tbody');
        this.inicializar();
    }

    /**
     * Inicializa los eventos y carga los datos
     */
    inicializar() {
        this.cargarMetodosPago();
        this.inicializarEventos();
    }

    /**
     * Inicializa los eventos de los botones
     */
    inicializarEventos() {
        // Evento para eliminar método de pago
        document.querySelector('#confirmDeleteModal .btn-danger').addEventListener('click', () => {
            // Aquí iría la lógica para eliminar el método de pago
            // Por ahora solo cerramos el modal
            const modal = bootstrap.Modal.getInstance(document.querySelector('#confirmDeleteModal'));
            modal.hide();
        });
    }

    /**
     * Obtiene el mensaje amigable para el usuario según el error
     * @param {Response} response Respuesta del servidor
     * @param {string} mensajeError Mensaje de error técnico
     * @returns {string} Mensaje amigable para mostrar al usuario
     */
    obtenerMensajeAmigable(response, mensajeError) {
        // Log del error técnico para desarrollo
        console.error('Error técnico:', mensajeError);

        // Determinar mensaje amigable según el tipo de error
        switch (response.status) {
            case 404:
                return 'No se pudo acceder a su información de usuario. Por favor, inicie sesión nuevamente.';
            case 500:
                return 'Hubo un problema en el servidor. Por favor, intente más tarde.';
            default:
                return 'Ocurrió un error al cargar los métodos de pago. Por favor, intente nuevamente.';
        }
    }

    /**
     * Carga los métodos de pago desde el servidor
     */
    async cargarMetodosPago() {
        try {
            const response = await fetch('/api/metodos-pago');

            if (!response.ok) {
                const mensajeError = await response.text();
                const mensajeAmigable = this.obtenerMensajeAmigable(response, mensajeError);
                throw new Error(mensajeAmigable);
            }

            const metodosPago = await response.json();
            this.renderizarMetodosPago(metodosPago);

        } catch (error) {
            // Para errores de red/cliente
            if (error.message.includes('failed to fetch')) {
                console.error('Error de conexión:', error);
                mostrarMensajeError('No se pudo establecer conexión con el servidor. Por favor, verifique su conexión a internet.');
                return;
            }

            // Para otros errores ya procesados
            mostrarMensajeError(error.message);
        }
    }

    /**
     * Renderiza los métodos de pago en la tabla
     * @param {Array} metodosPago Lista de métodos de pago a renderizar
     */
    renderizarMetodosPago(metodosPago) {
        this.tabla.innerHTML = '';

        if (metodosPago.length === 0) {
            this.mostrarTablaVacia();
            return;
        }

        metodosPago.forEach(metodo => {
            const fila = this.crearFilaMetodoPago(metodo);
            this.tabla.appendChild(fila);
        });
    }

    /**
     * Muestra un mensaje cuando no hay métodos de pago
     */
    mostrarTablaVacia() {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td colspan="6" class="text-center">
                No hay métodos de pago registrados
            </td>
        `;
        this.tabla.appendChild(tr);
    }

    /**
     * Crea una fila de la tabla para un método de pago
     * @param {Object} metodo Datos del método de pago
     * @returns {HTMLElement} Elemento TR con los datos del método
     */
    crearFilaMetodoPago(metodo) {
        const tr = document.createElement('tr');
        tr.dataset.metodoPagoId = metodo.metodoPagoId;
        tr.dataset.nombreTitular = metodo.nombreTitular;
        tr.dataset.numeroTarjeta = metodo.terminacion;
        tr.dataset.fechaExpiracion = metodo.fechaExpiracion;
        tr.dataset.tipoTarjeta = metodo.tipo;
        tr.dataset.monto = metodo.monto;

        tr.innerHTML = `
            <td data-label="Nombre del Titular">${metodo.nombreTitular}</td>
            <td data-label="Número de Tarjeta">**** **** **** ${metodo.terminacion}</td>
            <td data-label="Fecha de Expiración">${metodo.fechaExpiracion}</td>
            <td data-label="Tipo de Tarjeta">${metodo.tipo}</td>
            <td data-label="Monto Disponible">$${this.formatearMonto(metodo.monto)}</td>
            <td data-label="Acciones">
                <button class="btn btn-primary btn-sm">
                    <i class="bi bi-pencil"></i> Editar
                </button>
                <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal">
                    <i class="bi bi-trash"></i> Eliminar
                </button>
            </td>
        `;

        return tr;
    }

    /**
     * Formatea un número a formato de moneda con dos decimales
     * @param {number} monto Monto a formatear
     * @returns {string} Monto formateado
     */
    formatearMonto(monto) {
        return monto.toFixed(2);
    }
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new ListaMetodosPago();
    mostrarMensajeExitoURL();
});