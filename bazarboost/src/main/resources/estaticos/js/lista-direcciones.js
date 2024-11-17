import { mostrarMensajeExito, mostrarMensajeError, mostrarListaErrores, mostrarMensajeExitoURL, mostrarMensajeErrorDesaparece } from './mensajes-estado.js';

/**
 * Clase para gestionar la lista de direcciones
 */
class ListaDirecciones {
    constructor() {
        this.tabla = document.querySelector('table tbody');
        this.modal = new bootstrap.Modal(document.querySelector('#confirmDeleteModal'));
        this.direccionIdAEliminar = null;
        this.inicializar();
    }

    /**
     * Inicializa la carga de datos y eventos
     */
    inicializar() {
        this.cargarDirecciones();
        this.inicializarEventos();
    }

    /**
     * Configura los manejadores de eventos para la interfaz
     */
    inicializarEventos() {
        document.querySelector('#confirmDeleteModal .btn-danger')
            .addEventListener('click', () => this.eliminarDireccion());

        this.tabla.addEventListener('click', (event) => {
            const deleteBtn = event.target.closest('.btn-danger');
            if (deleteBtn) {
                const fila = deleteBtn.closest('tr');
                this.direccionIdAEliminar = fila.dataset.direccionId;
            }
        });
    }

    /**
     * Realiza la petición al servidor para eliminar una dirección
     */
    async eliminarDireccion() {
        try {
            const response = await fetch(`/api/direcciones/${this.direccionIdAEliminar}`, {
                method: 'DELETE'
            });

            this.modal.hide();

            if (!response.ok) {
                const errorData = await response.text();

                if (response.status === 404) {
                    console.error(`Error 404: ${errorData}`);
                    if (errorData.includes("Usuario")) {
                        mostrarMensajeErrorDesaparece("No se encontró la información del usuario. Por favor, verifica y vuelve a intentar");
                    } else if (errorData.includes("dirección")) {
                        mostrarMensajeErrorDesaparece("No se encontró la dirección a eliminar o no te pertenece. Por favor, verifica y vuelve a intentar.");
                    } else {
                        // Fallback por si no coincide ninguna condición
                        mostrarMensajeErrorDesaparece(errorData);
                    }
                }

                return;
            }

            await this.cargarDirecciones();
            mostrarMensajeExito('Dirección eliminada exitosamente');

        } catch (error) {
            this.modal.hide();
            console.error('Error al eliminar:', error);
            mostrarMensajeErrorDesaparece('Error de conexión al intentar eliminar. Por favor, intente nuevamente.');
        } finally {
            this.direccionIdAEliminar = null;
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
            default:
                return 'Ocurrió un error al cargar las direcciones. Por favor, intente nuevamente.';
        }
    }

    /**
     * Carga las direcciones desde el servidor
     */
    async cargarDirecciones() {
        try {
            const response = await fetch('/api/direcciones');

            if (!response.ok) {
                const mensajeError = await response.text();
                const mensajeAmigable = this.obtenerMensajeAmigable(response, mensajeError);
                throw new Error(mensajeAmigable);
            }

            const direcciones = await response.json();
            this.renderizarDirecciones(direcciones);

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
     * Renderiza las direcciones en la tabla
     * @param {Array} direcciones - Lista de direcciones a renderizar
     */
    renderizarDirecciones(direcciones) {
        this.tabla.innerHTML = '';

        if (direcciones.length === 0) {
            this.mostrarTablaVacia();
            return;
        }

        direcciones.forEach(direccion => {
            const fila = this.crearFilaDireccion(direccion);
            this.tabla.appendChild(fila);
        });
    }

    /**
     * Muestra un mensaje cuando no hay direcciones
     */
    mostrarTablaVacia() {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td colspan="7" class="text-center">
                No hay direcciones registradas
            </td>
        `;
        this.tabla.appendChild(tr);
    }

    /**
     * Crea una fila de la tabla para una dirección
     * @param {Object} direccion - Datos de la dirección
     * @returns {HTMLElement} Elemento TR con los datos de la dirección
     */
    crearFilaDireccion(direccion) {
        const tr = document.createElement('tr');
        tr.dataset.direccionId = direccion.direccionId;

        tr.innerHTML = `
            <td data-label="Estado">${direccion.estado}</td>
            <td data-label="Ciudad">${direccion.ciudad}</td>
            <td data-label="Colonia">${direccion.colonia}</td>
            <td data-label="Calle">${direccion.calle}</td>
            <td data-label="Número">${direccion.numeroDomicilio}</td>
            <td data-label="Código Postal">${direccion.codigoPostal}</td>
            <td data-label="Acciones">
                <a href="/direcciones/editar/${direccion.direccionId}" class="btn btn-primary btn-sm">
                    <i class="bi bi-pencil"></i> Editar
                </a>
                <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal">
                    <i class="bi bi-trash"></i> Eliminar
                </button>
            </td>
        `;

        return tr;
    }
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new ListaDirecciones();
    mostrarMensajeExitoURL();
});