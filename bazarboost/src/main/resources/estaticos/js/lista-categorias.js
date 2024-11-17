import { mostrarMensajeExito, mostrarMensajeError, mostrarListaErrores, mostrarMensajeExitoURL, mostrarMensajeErrorDesaparece } from './mensajes-estado.js';

/**
 * Clase para gestionar la lista de categorías
 */
class ListaCategorias {
    constructor() {
        this.tabla = document.querySelector('table tbody');
        this.modal = new bootstrap.Modal(document.querySelector('#confirmDeleteModal'));
        this.categoriaIdAEliminar = null;
        this.inicializar();
    }

    /**
     * Inicializa la carga de datos y eventos
     */
    inicializar() {
        this.cargarCategorias();
        this.inicializarEventos();
    }

    /**
     * Configura los manejadores de eventos para la interfaz
     */
    inicializarEventos() {
        document.querySelector('#confirmDeleteModal .btn-danger')
            .addEventListener('click', () => this.eliminarCategoria());

        this.tabla.addEventListener('click', (event) => {
            const deleteBtn = event.target.closest('.btn-danger');
            if (deleteBtn) {
                const fila = deleteBtn.closest('tr');
                this.categoriaIdAEliminar = fila.dataset.categoriaId;
            }
        });
    }

    /**
     * Realiza la petición al servidor para eliminar una categoría
     */
    async eliminarCategoria() {
        try {
            const response = await fetch(`/api/categorias/${this.categoriaIdAEliminar}`, {
                method: 'DELETE'
            });

            this.modal.hide();

            if (!response.ok) {
                const errorData = await response.text();

                if (response.status === 404) {
                    console.error(`Error 404: ${errorData}`);
                    if (errorData.includes("Usuario")) {
                        mostrarMensajeErrorDesaparece("No se encontró la información del usuario. Por favor, verifica y vuelve a intentar");
                    } else if (errorData.includes("categoría")) {
                        mostrarMensajeErrorDesaparece("No se encontró la categoría a eliminar o no tienes permisos para eliminarla. Por favor, verifica y vuelve a intentar.");
                    } else {
                        mostrarMensajeErrorDesaparece(errorData);
                    }
                } else if (response.status === 400) {
                    mostrarMensajeErrorDesaparece("No se puede eliminar la categoría porque tiene productos asociados.");
                }

                return;
            }

            await this.cargarCategorias();
            mostrarMensajeExito('Categoría eliminada exitosamente');

        } catch (error) {
            this.modal.hide();
            console.error('Error al eliminar:', error);
            mostrarMensajeErrorDesaparece('Error de conexión al intentar eliminar. Por favor, intente nuevamente.');
        } finally {
            this.categoriaIdAEliminar = null;
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
            case 403:
                return 'No tienes permisos para ver las categorías.';
            default:
                return 'Ocurrió un error al cargar las categorías. Por favor, intente nuevamente.';
        }
    }

    /**
     * Carga las categorías desde el servidor
     */
    async cargarCategorias() {
        try {
            const response = await fetch('/api/categorias');

            if (!response.ok) {
                const mensajeError = await response.text();
                const mensajeAmigable = this.obtenerMensajeAmigable(response, mensajeError);
                throw new Error(mensajeAmigable);
            }

            const categorias = await response.json();
            this.renderizarCategorias(categorias);

        } catch (error) {
            mostrarMensajeErrorDesaparece(error.message);
        }
    }

    /**
     * Renderiza las categorías en la tabla
     * @param {Array} categorias - Lista de categorías a renderizar
     */
    renderizarCategorias(categorias) {
        this.tabla.innerHTML = '';

        if (categorias.length === 0) {
            this.mostrarTablaVacia();
            return;
        }

        categorias.forEach(categoria => {
            const fila = this.crearFilaCategoria(categoria);
            this.tabla.appendChild(fila);
        });
    }

    /**
     * Muestra un mensaje cuando no hay categorías
     */
    mostrarTablaVacia() {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td colspan="2" class="text-center">
                No hay categorías registradas
            </td>
        `;
        this.tabla.appendChild(tr);
    }

    /**
     * Crea una fila de la tabla para una categoría
     * @param {Object} categoria - Datos de la categoría
     * @returns {HTMLElement} Elemento TR con los datos de la categoría
     */
    crearFilaCategoria(categoria) {
        const tr = document.createElement('tr');
        tr.dataset.categoriaId = categoria.categoriaId;

        tr.innerHTML = `
            <td data-label="Nombre de la Categoría">${categoria.nombre}</td>
            <td data-label="Acciones">
                <a href="/categorias/editar/${categoria.categoriaId}" class="btn btn-primary btn-sm">
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
    new ListaCategorias();
    mostrarMensajeExitoURL();
});