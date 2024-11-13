import { mostrarMensajeExito, mostrarMensajeError, mostrarListaErrores, mostrarMensajeExitoURL, mostrarMensajeErrorDesaparece } from './mensajes-estado.js';

/**
 * Clase para gestionar la lista de métodos de pago
 */
class ListaMetodosPago {
    constructor() {
        this.tabla = document.querySelector('table tbody');
        this.modal = new bootstrap.Modal(document.querySelector('#confirmDeleteModal'));
        this.metodoPagoIdAEliminar = null;
        this.inicializar();
    }

    /**
     * Inicializa la carga de datos y eventos
     */
    inicializar() {
        this.cargarMetodosPago();
        this.inicializarEventos();
    }

    /**
     * Configura los manejadores de eventos para la interfaz
     */
    inicializarEventos() {
        // Manejador para el botón de confirmación de eliminación
        document.querySelector('#confirmDeleteModal .btn-danger')
            .addEventListener('click', () => this.eliminarMetodoPago());

        // Delegación de eventos para botones de eliminación en la tabla
        this.tabla.addEventListener('click', (event) => {
            const deleteBtn = event.target.closest('.btn-danger');
            if (deleteBtn) {
                const fila = deleteBtn.closest('tr');
                this.metodoPagoIdAEliminar = fila.dataset.metodoPagoId;
            }
        });
    }

    /**
     * Realiza la petición al servidor para eliminar un método de pago
     */
    async eliminarMetodoPago() {
        try {
            const response = await fetch(`/api/metodos-pago/${this.metodoPagoIdAEliminar}`, {
                method: 'DELETE'
            });

            this.modal.hide(); // Cerrar el modal inmediatamente después de la acción

            if (!response.ok) {
                const errorData = await response.text();

                if (response.status === 404) {
                    mostrarMensajeErrorDesaparece("El método de pago no fue encontrado.");
                    return;
                }

                mostrarMensajeErrorDesaparece('No se pudo eliminar el método de pago. Por favor, intente nuevamente.');
                return;
            }

            await this.cargarMetodosPago();
            mostrarMensajeExito('Método de pago eliminado exitosamente');

        } catch (error) {
            this.modal.hide(); // Asegurar que el modal se cierre incluso si hay un error de red
            console.error('Error al eliminar:', error);
            mostrarMensajeErrorDesaparece('Error de conexión al intentar eliminar. Por favor, intente nuevamente.');
        } finally {
            this.metodoPagoIdAEliminar = null;
        }
    }
    /**
     * Obtiene el mensaje amigable para el usuario según el error
     * @param {Response} response - Respuesta del servidor
     * @param {string} mensajeError - Mensaje de error técnico
     * @returns {string} Mensaje amigable para mostrar al usuario
     */
    obtenerMensajeAmigable(response, mensajeError) {
        console.error('Error técnico:', mensajeError);

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
            if (error.message.includes('failed to fetch')) {
                console.error('Error de conexión:', error);
                mostrarMensajeError('No se pudo establecer conexión con el servidor. Por favor, verifique su conexión a internet.');
                return;
            }

            mostrarMensajeError(error.message);
        }
    }

    /**
     * Renderiza los métodos de pago en la tabla
     * @param {Array} metodosPago - Lista de métodos de pago a renderizar
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
     * @param {Object} metodo - Datos del método de pago
     * @returns {HTMLElement} Elemento TR con los datos del método
     */
    crearFilaMetodoPago(metodo) {
        const tr = document.createElement('tr');
        tr.dataset.metodoPagoId = metodo.metodoPagoId;

        tr.innerHTML = `
            <td data-label="Nombre del Titular">${metodo.nombreTitular}</td>
            <td data-label="Número de Tarjeta">**** **** **** ${metodo.terminacion}</td>
            <td data-label="Fecha de Expiración">${metodo.fechaExpiracion}</td>
            <td data-label="Tipo de Tarjeta">${metodo.tipo}</td>
            <td data-label="Monto Disponible">$${this.formatearMonto(metodo.monto)}</td>
            <td data-label="Acciones">
                <a href="/metodos-pago/editar/${metodo.metodoPagoId}" class="btn btn-primary btn-sm">
                    <i class="bi bi-pencil"></i> Editar
                </a>
                <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal">
                    <i class="bi bi-trash"></i> Eliminar
                </button>
            </td>
        `;

        return tr;
    }

    /**
     * Formatea un número a formato de moneda con dos decimales
     * @param {number} monto - Monto a formatear
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