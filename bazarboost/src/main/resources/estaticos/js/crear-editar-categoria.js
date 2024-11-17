import { mostrarMensajeError, mostrarListaErrores } from './mensajes-estado.js';

/**
 * Clase para gestionar el formulario de creación o edición de categorías
 */
class CrearEditarCategoria {
    constructor() {
        this.formulario = document.getElementById('categoriaForm');
        this.modo = this.determinarModo();
        this.categoriaId = this.obtenerCategoriaId();
        this.inicializarEventos();

        if (this.modo === 'editar') {
            this.cargarDatosCategoria();
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
     * Obtiene el ID de la categoría de la URL si estamos en modo edición
     * @returns {string|null} ID de la categoría o null si estamos en modo creación
     */
    obtenerCategoriaId() {
        if (this.modo === 'editar') {
            const path = window.location.pathname;
            const matches = path.match(/\/editar\/(\d+)/);
            return matches ? matches[1] : null;
        }
        return null;
    }

    /**
     * Carga los datos de la categoría para edición
     */
    async cargarDatosCategoria() {
        try {
            const response = await fetch(`/api/categorias/${this.categoriaId}/edicion`);

            if (!response.ok) {
                const errorData = await response.text();
                mostrarMensajeError(errorData);
                return;
            }

            const categoria = await response.json();
            this.llenarFormulario(categoria);

        } catch (error) {
            mostrarMensajeError('Error de conexión al cargar los datos. Por favor, intenta nuevamente.');
        }
    }

    /**
     * Llena el formulario con los datos de la categoría
     * @param {Object} categoria Datos de la categoría
     */
    llenarFormulario(categoria) {
        document.getElementById('nombre').value = categoria.nombre;
    }

    /**
     * Envía el formulario al backend para crear o editar una categoría
     * @param {Event} event Evento de envío del formulario
     */
    async enviarFormulario(event) {
        event.preventDefault();

        const categoriaDTO = this.obtenerDatosFormulario();

        if (!categoriaDTO) {
            mostrarMensajeError('Por favor, completa todos los campos requeridos.');
            return;
        }

        try {
            const url = `/api/categorias`;

            // Si es edición, agregar el ID al DTO
            if (this.modo === 'editar') {
                categoriaDTO.categoriaId = this.categoriaId;
            }

            const response = await fetch(url, {
                method: this.modo === 'crear' ? 'POST' : 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(categoriaDTO)
            });

            if (!response.ok) {
                const errorData = await response.text();

                if (response.status === 400 && Array.isArray(JSON.parse(errorData))) {
                    mostrarListaErrores(JSON.parse(errorData));
                } else {
                    mostrarMensajeError(errorData);
                }
                return;
            }

            const mensajeExito = encodeURIComponent(
                this.modo === 'crear'
                    ? 'Categoría creada exitosamente.'
                    : 'Categoría actualizada exitosamente.'
            );
            window.location.href = `/categorias?mensajeExito=${mensajeExito}`;

        } catch (error) {
            mostrarMensajeError('Error de conexión. Por favor, intenta nuevamente.');
        }
    }

    /**
     * Obtiene y valida los datos del formulario
     * @returns {Object|null} Datos del formulario o null si hay errores
     */
    obtenerDatosFormulario() {
        const nombre = document.getElementById('nombre').value.trim();

        if (!nombre) {
            return null;
        }

        return {
            nombre
        };
    }
}

// Inicializar la clase cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new CrearEditarCategoria();
});