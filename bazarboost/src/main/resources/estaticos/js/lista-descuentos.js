import { mostrarMensajeExito, mostrarMensajeError, mostrarMensajeExitoURL, mostrarMensajeErrorURL } from './mensajes-estado.js';

function cargarDescuentosVendedor() {
    const url = new URL('/api/descuentos/mis-descuentos', window.location.origin);
    fetch(url)
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    return response.text().then(errorMessage => {
                        mostrarMensajeError("No se encontraron descuentos para el vendedor.");
                        throw new Error(`404 Not Found: ${errorMessage}`);
                    });
                } else {
                    mostrarMensajeError("Ocurrió un error inesperado al cargar los productos. Inténtelo más tarde.");
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
            }
            return response.json();
        })
        .then(data => {
            const descuentosTbody = document.getElementById('descuentos-tbody');
            descuentosTbody.innerHTML = '';

            data.forEach(descuento => {
                const descuentoHTML = `
                    <tr data-descuento-id="${descuento.descuentoId}" data-nombre="${descuento.nombre}" data-porcentaje="${descuento.porcentaje}">
                        <td data-label="Nombre del Descuento">${descuento.nombre}</td>
                        <td data-label="Porcentaje">${descuento.porcentaje}%</td>
                        <td data-label="Acciones">
                            <div class="d-flex gap-2">
                                <a href="/descuentos/editar/${descuento.descuentoId}" class="btn btn-primary btn-sm">
                                    <i class="bi bi-pencil"></i> Editar
                                </a>
                                <button class="btn btn-danger btn-sm"
                                        onclick="prepararEliminarDescuento(${descuento.descuentoId}, '${descuento.nombre}')"
                                        data-bs-toggle="modal"
                                        data-bs-target="#modalConfirmDelete">
                                    <i class="bi bi-trash"></i> Eliminar
                                </button>
                            </div>
                        </td>
                    </tr>`;
                descuentosTbody.insertAdjacentHTML('beforeend', descuentoHTML);
            });
        })
        .catch(error => {
            console.error('Error inesperado al cargar descuentos:', error);
        });
}

// Función para preparar el modal de eliminación
window.prepararEliminarDescuento = function(descuentoId, nombreDescuento) {
    const modal = document.getElementById('modalConfirmDelete');
    const btnEliminar = modal.querySelector('.btn-danger');
    const modalBody = modal.querySelector('.modal-body');

    modalBody.textContent = `¿Estás seguro de que deseas eliminar el descuento "${nombreDescuento}"?`;
    btnEliminar.onclick = () => eliminarDescuento(descuentoId);
};

// Función para eliminar el descuento
function eliminarDescuento(descuentoId) {
    const url = new URL(`/api/descuentos/${descuentoId}`, window.location.origin);

    fetch(url, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(errorMessage => {
                switch (response.status) {
                    case 404:
                        if (errorMessage.includes('Usuario')) {
                            throw new Error('Error al encontrar el usuario. Por favor, inicia sesión nuevamente.');
                        } else if (errorMessage.includes('Descuento')) {
                            throw new Error('El descuento que intentas eliminar no existe.');
                        }
                        throw new Error('Recurso no encontrado.');

                    case 403:
                        throw new Error('No tienes permiso para eliminar este descuento.');

                    default:
                        throw new Error('Ocurrió un error al eliminar el descuento. Inténtalo más tarde.');
                }
            });
        }

        // Mostrar mensaje de éxito
        mostrarMensajeExito('Descuento eliminado exitosamente');

        // Recargar la lista de descuentos
        cargarDescuentosVendedor();
    })
    .catch(error => {
        console.error('Error:', error);
        mostrarMensajeError(error.message);
    });

    // Cerrar el modal
    const modal = bootstrap.Modal.getInstance(document.getElementById('modalConfirmDelete'));
    modal.hide();
}

window.onload = () => {
    cargarDescuentosVendedor();
    mostrarMensajeExitoURL();
    mostrarMensajeErrorURL();
};