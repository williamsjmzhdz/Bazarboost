import { mostrarMensajeError, mostrarListaErrores } from './mensajes-estado.js';

/**
 * Clase para gestionar el formulario de creación o edición de descuentos
 */
class CrearEditarDescuento {
    constructor() {
        this.formulario = document.getElementById('descuentoForm');
        this.modo = document.getElementById('modo').value;
        this.descuentoId = this.modo === 'editar' ? document.getElementById('descuentoId').value : null;
        this.btnGuardar = document.getElementById('guardarDescuentoBtn');
        this.inicializarEventos();
    }

    /**
     * Inicializa los eventos del formulario
     */
    inicializarEventos() {
        if (!this.btnGuardar) {
            console.error('No se encontró el botón de guardar');
            return;
        }

        this.btnGuardar.addEventListener('click', (e) => this.manejarDescuento(e));
    }

    /**
     * Maneja la creación o actualización del descuento
     * @param {Event} e Evento del click
     */
    async manejarDescuento(e) {
        e.preventDefault();

        const descuentoDTO = this.obtenerDatosFormulario();

        if (!descuentoDTO) {
            mostrarMensajeError('Por favor, completa todos los campos requeridos correctamente.');
            return;
        }

        try {
            const url = new URL(
                this.modo === 'crear'
                    ? '/api/descuentos'
                    : `/api/descuentos/${this.descuentoId}`,
                window.location.origin
            );

            const response = await fetch(url, {
                method: this.modo === 'crear' ? 'POST' : 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(descuentoDTO)
            });

            if (!response.ok) {
                await this.handleErrorResponse(response);
                return;
            }

            this.handleSuccessResponse();

        } catch (error) {
            console.error('Error:', error);
            mostrarMensajeError('Error de conexión. Por favor, intenta nuevamente.');
        }
    }

    /**
     * Obtiene y valida los datos del formulario
     * @returns {Object|null} Datos del formulario o null si hay errores
     */
    obtenerDatosFormulario() {
        const nombre = document.getElementById('nombre').value.trim();
        const porcentaje = document.getElementById('porcentaje').value;

        if (!nombre || !porcentaje) {
            return null;
        }

        // Validar que el porcentaje sea un número entre 1 y 100
        const porcentajeNum = parseInt(porcentaje);
        if (isNaN(porcentajeNum) || porcentajeNum < 1 || porcentajeNum > 100) {
            mostrarMensajeError('El porcentaje debe ser un número entre 1 y 100');
            return null;
        }

        return {
            nombre,
            porcentaje: porcentajeNum
        };
    }

    /**
     * Maneja la respuesta de error de la API
     * @param {Response} response Respuesta de la API
     */
    async handleErrorResponse(response) {
        const errorData = await response.text();

        try {
            switch (response.status) {
                case 400:
                    // Intentar parsear como JSON por si son errores de validación
                    const validationErrors = JSON.parse(errorData);
                    if (Array.isArray(validationErrors)) {
                        mostrarListaErrores(validationErrors);
                    } else {
                        mostrarMensajeError(errorData);
                    }
                    break;

                case 404:
                    mostrarMensajeError('No se encontró el descuento especificado.');
                    break;

                case 409:
                    mostrarMensajeError(errorData);
                    break;

                case 403:
                    mostrarMensajeError('No tienes permiso para realizar esta acción.');
                    break;

                default:
                    mostrarMensajeError('Ocurrió un error inesperado. Por favor, intenta nuevamente.');
            }
        } catch (e) {
            // Si no es JSON, mostrar el mensaje directamente
            mostrarMensajeError(errorData);
        }
    }

    /**
     * Maneja la respuesta exitosa de la API
     */
    handleSuccessResponse() {
        const mensajeExito = encodeURIComponent(
            this.modo === 'crear'
                ? 'Descuento creado exitosamente'
                : 'Descuento actualizado exitosamente'
        );
        window.location.href = `/descuentos?mensajeExito=${mensajeExito}`;
    }
}

// Inicializar la clase cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new CrearEditarDescuento();
});