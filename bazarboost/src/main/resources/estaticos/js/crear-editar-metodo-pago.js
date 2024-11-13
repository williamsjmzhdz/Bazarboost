import { mostrarMensajeError, mostrarListaErrores } from './mensajes-estado.js';

/**
 * Clase para gestionar el formulario de creación o edición de métodos de pago
 */
class CrearMetodoPago {
    constructor() {
        this.formulario = document.getElementById('metodo-pago-form');
        this.modo = this.determinarModo();
        this.metodoPagoId = this.obtenerMetodoPagoId();
        this.inicializarEventos();

        // Si estamos en modo edición, cargar los datos
        if (this.modo === 'editar') {
            this.cargarDatosMetodoPago();
        }
    }

    /**
     * Inicializa los eventos del formulario
     */
    inicializarEventos() {
        this.formulario.addEventListener('submit', (event) => this.enviarFormulario(event));
    }

    /**
     * Determina si estamos en modo creación o edición basado en la URL
     * @returns {string} 'crear' o 'editar'
     */
    determinarModo() {
        const path = window.location.pathname;
        return path.includes('/editar/') ? 'editar' : 'crear';
    }

    /**
     * Obtiene el ID del método de pago de la URL si estamos en modo edición
     * @returns {string|null} ID del método de pago o null si estamos en modo creación
     */
    obtenerMetodoPagoId() {
        if (this.modo === 'editar') {
            const path = window.location.pathname;
            const matches = path.match(/\/editar\/(\d+)/);
            return matches ? matches[1] : null;
        }
        return null;
    }

    /**
     * Carga los datos del método de pago para edición
     */
    async cargarDatosMetodoPago() {
        try {
            const response = await fetch(`/api/metodos-pago/${this.metodoPagoId}/edicion`);

            if (!response.ok) {
                const errorData = await response.text();

                if (response.status === 404) {
                    console.error(`Error 404: ${errorData}`);
                    mostrarMensajeError("No se encontró la información del método de pago. Por favor, verifica y vuelve a intentar.");
                } else {
                    mostrarMensajeError('Error al cargar los datos del método de pago');
                }
                return;
            }

            const metodoPago = await response.json();
            this.llenarFormulario(metodoPago);

        } catch (error) {
            mostrarMensajeError('Error de conexión al cargar los datos. Por favor, intenta nuevamente.');
        }
    }

    /**
    * Llena el formulario con los datos del método de pago
    * @param {Object} metodoPago Datos del método de pago
    */
    llenarFormulario(metodoPago) {
        document.getElementById('nombreTitular').value = metodoPago.nombreTitular;
        document.getElementById('numeroTarjeta').value = metodoPago.numeroTarjeta;

        // Convertir la fecha YYYY-MM-DD a YYYY-MM para el input type="month"
        const fecha = metodoPago.fechaExpiracion.substring(0, 7);
        document.getElementById('fechaExpiracion').value = fecha;

        document.getElementById('tipoTarjeta').value = metodoPago.tipoTarjeta;
        document.getElementById('montoDisponible').value = metodoPago.monto;
    }

    /**
     * Envía el formulario al backend para crear o editar un método de pago
     * @param {Event} event Evento de envío del formulario
     */
    async enviarFormulario(event) {
        event.preventDefault();

        const metodoPagoDTO = this.obtenerDatosFormulario();

        if (!metodoPagoDTO) {
            mostrarMensajeError('Por favor, completa todos los campos requeridos.');
            return;
        }

        // Si estamos en modo edición, agregar el ID al DTO
        if (this.modo === 'editar') {
            metodoPagoDTO.metodoPagoId = this.metodoPagoId;
        }

        try {
            // Construir la URL según el modo
            const url = this.modo === 'crear'
                ? '/api/metodos-pago'
                : `/api/metodos-pago/${this.metodoPagoId}`;

            const response = await fetch(url, {
                method: this.modo === 'crear' ? 'POST' : 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(metodoPagoDTO)
            });

            if (!response.ok) {
                const errorData = await response.text();

                if (response.status === 400 && Array.isArray(JSON.parse(errorData))) {
                    mostrarListaErrores(JSON.parse(errorData));
                } else if (response.status === 404) {
                    console.error(`Error 404: ${errorData}`);
                    mostrarMensajeError(
                        this.modo === 'editar'
                            ? "No se encontró el método de pago a actualizar. Por favor, verifica y vuelve a intentar."
                            : "No se encontró la información del usuario. Por favor, verifica y vuelve a intentar."
                    );
                } else if (response.status === 409) {
                    console.error(`Error 409: ${errorData}`);
                    mostrarMensajeError("El número de tarjeta ya está registrado. Use un número diferente.");
                } else {
                    mostrarMensajeError(
                        this.modo === 'crear'
                            ? 'Error al crear el método de pago'
                            : 'Error al actualizar el método de pago'
                    );
                }
                return;
            }

            // Mensaje y redirección según el modo
            const mensajeExito = encodeURIComponent(
                this.modo === 'crear'
                    ? 'Método de pago creado exitosamente.'
                    : 'Método de pago actualizado exitosamente.'
            );
            window.location.href = `/metodos-pago?mensajeExito=${mensajeExito}`;

        } catch (error) {
            mostrarMensajeError(
                error.message || 'Error de conexión. Intenta nuevamente.'
            );
        }
    }

    /**
     * Obtiene y valida los datos del formulario
     * @returns {Object|null} Datos del formulario o null si hay errores
     */
    obtenerDatosFormulario() {
        const nombreTitular = document.getElementById('nombreTitular').value.trim();
        const numeroTarjeta = document.getElementById('numeroTarjeta').value.trim();
        const fechaExpiracionInput = document.getElementById('fechaExpiracion').value;
        const tipoTarjeta = document.getElementById('tipoTarjeta').value;
        const monto = parseFloat(document.getElementById('montoDisponible').value);

        if (!nombreTitular || !numeroTarjeta || !fechaExpiracionInput || !tipoTarjeta || isNaN(monto)) {
            return null;
        }

        // Convertir la fecha de expiración al formato YYYY-MM-DD
        const fechaExpiracion = `${fechaExpiracionInput}-01`; // Agregar día 1

        return {
            nombreTitular,
            numeroTarjeta,
            fechaExpiracion,
            tipoTarjeta,
            monto
        };
    }
}

// Inicializar la clase cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new CrearMetodoPago();
});