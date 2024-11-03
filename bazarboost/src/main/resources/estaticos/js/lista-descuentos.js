import { mostrarMensajeError, mostrarMensajeExitoURL } from './mensajes-estado.js';

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
                        <td data-label="Porcentaje">${descuento.porcentaje}</td>
                        <td data-label="Acciones">
                            <button class="btn btn-primary btn-sm">
                                <i class="bi bi-pencil"></i> Editar
                            </button>
                            <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#modalConfirmDelete">
                                <i class="bi bi-trash"></i> Eliminar
                            </button>
                        </td>
                    </tr>`;
                descuentosTbody.insertAdjacentHTML('beforeend', descuentoHTML);
            });
        })
        .catch(error => {
            console.error('Error inesperado al cargar descuentos:', error);
        });
}

window.onload = () => {
  cargarDescuentosVendedor();
  mostrarMensajeExitoURL();
};