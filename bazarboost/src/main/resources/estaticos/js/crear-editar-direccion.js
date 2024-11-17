import { mostrarMensajeError, mostrarListaErrores } from './mensajes-estado.js';

/**
 * Clase para gestionar el formulario de creación o edición de direcciones
 */
class CrearEditarDireccion {
    constructor() {
        this.formulario = document.getElementById('direccionForm');
        this.modo = this.determinarModo();
        this.direccionId = this.obtenerDireccionId();
        this.inicializarEventos();

        if (this.modo === 'editar') {
            this.cargarDatosDireccion();
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
     * Obtiene el ID de la dirección de la URL si estamos en modo edición
     * @returns {string|null} ID de la dirección o null si estamos en modo creación
     */
    obtenerDireccionId() {
        if (this.modo === 'editar') {
            const path = window.location.pathname;
            const matches = path.match(/\/editar\/(\d+)/);
            return matches ? matches[1] : null;
        }
        return null;
    }

    /**
     * Carga los datos de la dirección para edición
     */
    async cargarDatosDireccion() {
        try {
            const response = await fetch(`/api/direcciones/${this.direccionId}/edicion`);

            if (!response.ok) {
                const errorData = await response.text();

                if (response.status === 404) {
                    console.error(`Error 404: ${errorData}`);
                    if (errorData.includes("Usuario")) {
                        mostrarMensajeError("No se encontró la información del usuario. Por favor, verifica y vuelve a intentar");
                    } else if (errorData.includes("dirección")) {
                        mostrarMensajeError("No se encontró la información de la dirección. Por favor, verifica y vuelve a intentar.");
                    }
                } else {
                    mostrarMensajeError('Error al cargar los datos de la dirección');
                }
                return;
            }

            const direccion = await response.json();
            this.llenarFormulario(direccion);

        } catch (error) {
            mostrarMensajeError('Error de conexión al cargar los datos. Por favor, intenta nuevamente.');
        }
    }

    /**
    * Llena el formulario con los datos de la dirección
    * @param {Object} direccion Datos de la dirección
    */
    llenarFormulario(direccion) {
        document.getElementById('estado').value = direccion.estado;
        document.getElementById('ciudad').value = direccion.ciudad;
        document.getElementById('colonia').value = direccion.colonia;
        document.getElementById('calle').value = direccion.calle;
        document.getElementById('numero_domicilio').value = direccion.numeroDomicilio;
        document.getElementById('codigo_postal').value = direccion.codigoPostal;
    }

    /**
     * Envía el formulario al backend para crear o editar una dirección
     * @param {Event} event Evento de envío del formulario
     */
    async enviarFormulario(event) {
        event.preventDefault();

        const direccionDTO = this.obtenerDatosFormulario();

        if (!direccionDTO) {
            mostrarMensajeError('Por favor, completa todos los campos requeridos.');
            return;
        }

        if (this.modo === 'editar') {
            direccionDTO.direccionId = this.direccionId;
        }

        try {
            const url = '/api/direcciones';

            const response = await fetch(url, {
                method: this.modo === 'crear' ? 'POST' : 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(direccionDTO)
            });

            if (!response.ok) {
                const errorData = await response.text();

                if (response.status === 400 && Array.isArray(JSON.parse(errorData))) {
                    mostrarListaErrores(JSON.parse(errorData));
                } else if (response.status === 404) {
                    console.error(`Error 404: ${errorData}`);
                    if (errorData.includes("Usuario")) {
                        mostrarMensajeError("No se encontró la información del usuario. Por favor, verifica y vuelve a intentar");
                    } else if (errorData.includes("dirección")) {
                        mostrarMensajeError("No se encontró la dirección a actualizar. Por favor, verifica y vuelve a intentar.");
                    } else {
                        // Fallback por si no coincide ninguna condición
                        mostrarMensajeError(errorData);
                    }
                } else {
                    mostrarMensajeError(
                        this.modo === 'crear'
                            ? 'Error al crear la dirección'
                            : 'Error al actualizar la dirección'
                    );
                }
                return;
            }

            const mensajeExito = encodeURIComponent(
                this.modo === 'crear'
                    ? 'Dirección creada exitosamente.'
                    : 'Dirección actualizada exitosamente.'
            );
            window.location.href = `/direcciones?mensajeExito=${mensajeExito}`;

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
        const estado = document.getElementById('estado').value.trim();
        const ciudad = document.getElementById('ciudad').value.trim();
        const colonia = document.getElementById('colonia').value.trim();
        const calle = document.getElementById('calle').value.trim();
        const numeroDomicilio = parseInt(document.getElementById('numero_domicilio').value);
        const codigoPostal = document.getElementById('codigo_postal').value.trim();

        if (!estado || !ciudad || !colonia || !calle || isNaN(numeroDomicilio) || !codigoPostal) {
            return null;
        }

        return {
            estado,
            ciudad,
            colonia,
            calle,
            numeroDomicilio,
            codigoPostal
        };
    }
}

// Inicializar la clase cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new CrearEditarDireccion();
});