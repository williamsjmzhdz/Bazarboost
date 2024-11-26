import { actualizarPaginacion, cambiarPagina, getPaginaActual, setPaginaActual } from './paginacion.js';
import { mostrarMensajeErrorDesaparece } from './mensajes-estado.js';

class ListaUsuarios {
    constructor() {
        // Elementos del DOM
        this.barraBusqueda = document.getElementById("searchBar");
        this.tablaUsuarios = document.getElementById("userTable");
        this.botonLimpiar = document.querySelector("button.btn.btn-secondary");

        // Estado
        this.tamanioPagina = 10;
        this.terminoBusqueda = '';

        // Vincular métodos
        this.reiniciarBusqueda = this.reiniciarBusqueda.bind(this);
        this.cargarUsuarios = this.cargarUsuarios.bind(this);
        this.renderizarUsuarios = this.renderizarUsuarios.bind(this);

        // Configurar callback para paginación
        window.cambiarPaginaCallback = (pagina) => {
            cambiarPagina(pagina, this.cargarUsuarios);
        };

        // Inicializar
        this.inicializarEventos();
        this.cargarUsuarios();
    }

    inicializarEventos() {
        // Evento para la barra de búsqueda con debounce
        let timeoutId;
        this.barraBusqueda.addEventListener('keyup', () => {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(() => {
                this.terminoBusqueda = this.barraBusqueda.value;
                setPaginaActual(0); // Resetear a primera página
                this.cargarUsuarios();
            }, 300);
        });

        // Evento para el botón de limpiar
        this.botonLimpiar.addEventListener('click', this.reiniciarBusqueda);

        // Evento para los checkboxes
        this.tablaUsuarios.addEventListener('change', (event) => {
            if (event.target.type === 'checkbox') {
                const fila = event.target.closest('tr');
                if (fila) {
                    const usuarioId = fila.getAttribute('data-id');
                    this.actualizarRolVendedor(usuarioId, event.target.checked);
                }
            }
        });
    }

    async cargarUsuarios() {
        try {
            const params = new URLSearchParams({
                pagina: getPaginaActual(),
                tamanioPagina: this.tamanioPagina,
                keyword: this.terminoBusqueda
            });

            const response = await fetch(`/api/usuarios?${params}`, {
                credentials: 'include',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            });

            if (!response.ok) {
                const errorText = await response.text();
                mostrarMensajeErrorDesaparece(errorText);
                throw new Error(`Error ${response.status}: ${errorText}`);
            }

            const data = await response.json();
            this.renderizarUsuarios(data);
            actualizarPaginacion(data, window.cambiarPaginaCallback);

        } catch (error) {
            console.error('Error al cargar usuarios:', error);
        }
    }

    async actualizarRolVendedor(usuarioId, esVendedor) {
        try {
            const response = await fetch(`/api/usuarios/${usuarioId}/rol-vendedor`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'include',
                body: JSON.stringify({ esVendedor })
            });

            const texto = await response.text();
            const mensaje = texto ? texto : 'Error al actualizar el rol de vendedor';

            if (!response.ok) {
                // Si hay error, revertir el checkbox al estado anterior
                const checkbox = this.tablaUsuarios.querySelector(`tr[data-id="${usuarioId}"] input[type="checkbox"]`);
                if (checkbox) {
                    checkbox.checked = !esVendedor;
                }
                mostrarMensajeErrorDesaparece(mensaje);
                throw new Error(`Error ${response.status}: ${mensaje}`);
            }

            // Recargar la lista para asegurar datos actualizados
            await this.cargarUsuarios();

        } catch (error) {
            console.error('Error al actualizar rol de vendedor:', error);
        }
    }

    renderizarUsuarios(data) {
        this.tablaUsuarios.innerHTML = '';

        if (data.usuarios.length === 0) {
            const mensajeVacio = document.createElement('tr');
            mensajeVacio.innerHTML = `
                <td colspan="4" class="text-center">
                    No se encontraron usuarios
                </td>
            `;
            this.tablaUsuarios.appendChild(mensajeVacio);
            return;
        }

        data.usuarios.forEach(usuario => {
            const fila = document.createElement('tr');
            fila.setAttribute('data-id', usuario.usuarioId);
            fila.setAttribute('data-nombre', usuario.nombre);
            fila.setAttribute('data-apellido-paterno', usuario.apellidoPaterno);
            fila.setAttribute('data-apellido-materno', usuario.apellidoMaterno);
            fila.setAttribute('data-correo-electronico', usuario.correoElectronico);
            fila.setAttribute('data-telefono', usuario.telefono);

            fila.innerHTML = `
                <td data-label="Nombre del Usuario">
                    ${usuario.nombre} ${usuario.apellidoPaterno}
                </td>
                <td data-label="Correo Electrónico">
                    ${usuario.correoElectronico}
                </td>
                <td data-label="Teléfono">
                    ${usuario.telefono}
                </td>
                <td>
                    <input type="checkbox" class="form-check-input"
                           ${usuario.esVendedor ? 'checked' : ''}
                           title="Marcar/Desmarcar como vendedor">
                </td>
            `;

            this.tablaUsuarios.appendChild(fila);
        });
    }

    reiniciarBusqueda() {
        this.barraBusqueda.value = '';
        this.terminoBusqueda = '';
        setPaginaActual(0);
        this.cargarUsuarios();
    }
}

// Inicializar cuando el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', () => {
    new ListaUsuarios();
});