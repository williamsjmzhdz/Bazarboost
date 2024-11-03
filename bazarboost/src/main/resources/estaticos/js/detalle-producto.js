import { mostrarMensajeError, mostrarMensajeErrorSolo, mostrarMensajeExito, mostrarMensajeErrorDesaparece } from './mensajes-estado.js';
import { actualizarPaginacion, cambiarPagina, getPaginaActual, setPaginaActual } from './paginacion.js';

window.submitReview = submitReview;
window.deleteReview = deleteReview;
window.actualizarCalificacionPromedio = actualizarCalificacionPromedio;
window.cambiarPaginaCallback = (pagina) => cambiarPagina(pagina, cargarResenias);

let productoActual = null;

document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    setPaginaActual(parseInt(urlParams.get('page')) || 0);
    cargarProductoYResenias();
});

function cargarProductoYResenias() {
    const productId = window.location.pathname.split('/').pop();
    const endpoint = `/api/productos/detalle-producto/${productId}?page=${getPaginaActual()}`;

    fetch(endpoint)
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else if (response.status === 404) {
                throw new Error('El producto que usted intenta ver no existe.');
            } else {
                throw new Error('Ocurrió un error inesperado al cargar el producto. Inténtelo más tarde.');
            }
        })
        .then(data => {
            productoActual = data;
            renderProductDetails(data);
            if (!data.esProductoPropio) {
                renderMyReview(data.miResenia);
            }
            renderAdditionalReviews(data.reseniasAdicionales);
            actualizarPaginacion({
                paginaActual: data.paginaActual,
                totalPaginas: data.totalPaginas,
                totalElementos: data.totalElementos
            }, window.cambiarPaginaCallback);
        })
        .catch(error => {
            mostrarMensajeErrorSolo(error.message);
        });
}

function cargarResenias() {
    const productId = window.location.pathname.split('/').pop();
    const endpoint = `/api/productos/detalle-producto/${productId}?page=${getPaginaActual()}`;

    fetch(endpoint)
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar las reseñas');
            return response.json();
        })
        .then(data => {
            productoActual = data;
            if (!data.esProductoPropio) {
                renderMyReview(data.miResenia);
            }
            renderAdditionalReviews(data.reseniasAdicionales);
            actualizarPaginacion({
                paginaActual: data.paginaActual,
                totalPaginas: data.totalPaginas,
                totalElementos: data.totalElementos
            }, window.cambiarPaginaCallback);
        })
        .catch(error => {
            mostrarMensajeError(error.message);
        });
}

function renderProductDetails(producto) {
    const productContainer = document.getElementById('product-container');
    const bodyContainer = document.getElementById('bodyContainer');
    const descuento = producto.descuento;
    const precioConDescuento = producto.precioConDescuento;

    let productHTML = `
        <div class="col-md-6">
            <img src="/api/productos/imagenes/${producto.imagenUrl}" alt="${producto.nombre}" class="product-image" />
        </div>
        <div class="col-md-6 product-info">
            <h2>${producto.nombre}</h2>
            <p>
                <strong>Descripción:</strong> ${producto.descripcion}
            </p>
            <p><strong>Precio:</strong> $${producto.precio}</p>
    `;

    if (precioConDescuento && descuento) {
        productHTML += `
            <p>
                <strong>Precio con descuento:</strong>
                <span style="color: #dc3545;">$${precioConDescuento}</span>
                <small>(${descuento.porcentaje}% de descuento aplicado - <strong>${descuento.nombre}</strong>)</small>
            </p>
        `;
    }

    productHTML += `
            <p><strong>Existencia:</strong> ${producto.existencia} unidades disponibles</p>
            <p><strong>Categoría:</strong> ${producto.nombreCategoria}</p>
            <p><strong>Calificación promedio:</strong></p>
            <div class="average-rating">
                <span class="review-stars">
                    ${renderStars(producto.calificacionPromedio)} ${producto.calificacionPromedio} de 5
                </span>
            </div>

            ${producto.esProductoPropio ? '' : `
                <a class="btn ${producto.estaEnCarrito ? 'btn-danger' : 'btn-primary'} me-2" role="button"
                    data-producto-id="${producto.productoId}"
                    data-accion="${producto.estaEnCarrito ? 'quitar' : 'agregar'}"
                    onclick="actualizarCarrito(event)">
                    <i class="bi ${producto.estaEnCarrito ? 'bi-cart-dash' : 'bi-cart-plus'}"></i>
                    ${producto.estaEnCarrito ? 'Quitar del carrito' : 'Agregar al carrito'}
                </a>
            `}
            <button class="btn btn-secondary" onclick="window.history.back();">
                <i class="bi bi-arrow-left"></i> Volver a la lista
            </button>
        </div>
    `;

    productContainer.innerHTML = productHTML;
}

function submitReview() {
    const calificacion = document.getElementById('calificacion').value;
    const comentario = document.getElementById('comentario').value;
    const reseniaId = document.querySelector('button[data-resenia-id]')?.getAttribute('data-resenia-id');
    const productId = window.location.pathname.split('/').pop();

    if (!calificacion || !comentario) {
        mostrarMensajeError('Por favor, completa la calificación y el comentario.');
        return;
    }

    const isEdit = !!reseniaId;
    const method = isEdit ? 'PUT' : 'POST';
    const url = isEdit ? `/api/resenias/${reseniaId}` : `/api/resenias`;

    const bodyData = {
        calificacion: parseInt(calificacion),
        comentario: comentario
    };

    if (!isEdit) {
        bodyData.productoId = productId;
    }

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bodyData)
    })
    .then(response => response.json().then(data => ({ status: response.status, data })))
    .then(({ status, data }) => {
        if ((isEdit && status === 200) || (!isEdit && status === 201)) {
            mostrarMensajeExito(`Reseña ${isEdit ? 'actualizada' : 'enviada'} con éxito.`);
            renderMyReview(data);
            actualizarCalificacionPromedio(data.calificacionPromedioActualizada);
            cargarResenias(); // Recargar reseñas después de enviar una nueva
        } else {
            throw new Error(`Error al ${isEdit ? 'actualizar' : 'enviar'} la reseña.`);
        }
    })
    .catch(error => {
        mostrarMensajeError(error.message);
    });
}

function deleteReview(reseniaId) {
    fetch(`/api/resenias/${reseniaId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.status === 200) {
            return response.json();
        } else {
            throw new Error('Error al eliminar la reseña.');
        }
    })
    .then(data => {
        mostrarMensajeExito('Reseña eliminada con éxito.');

        const deleteModal = document.getElementById('deleteModal');
        const modal = bootstrap.Modal.getInstance(deleteModal);
        if (modal) {
            modal.hide();
        }

        renderMyReview(null);
        actualizarCalificacionPromedio(data.calificacionPromedioActualizada);
        cargarResenias(); // Recargar reseñas después de eliminar
    })
    .catch(error => {
        mostrarMensajeError(error.message);
    });
}

function renderMyReview(miResenia) {
    const myReviewSection = document.getElementById('my-review-section');

    const calificacionValue = miResenia ? miResenia.calificacion : '';
    const comentarioValue = miResenia ? miResenia.comentario : '';

    let reviewFormHTML = `
        <h5>${miResenia ? 'Edita tu reseña' : 'Deja tu reseña'}</h5>
        <div class="input-group mb-3">
            <select class="form-select" id="calificacion" aria-label="Calificación" required>
                <option value="" disabled ${!calificacionValue ? 'selected' : ''}>Calificación</option>
                <option value="1" ${calificacionValue == 1 ? 'selected' : ''}>1 estrella</option>
                <option value="2" ${calificacionValue == 2 ? 'selected' : ''}>2 estrellas</option>
                <option value="3" ${calificacionValue == 3 ? 'selected' : ''}>3 estrellas</option>
                <option value="4" ${calificacionValue == 4 ? 'selected' : ''}>4 estrellas</option>
                <option value="5" ${calificacionValue == 5 ? 'selected' : ''}>5 estrellas</option>
            </select>
            <input
                type="text"
                class="form-control"
                placeholder="Escribe un comentario"
                id="comentario"
                value="${comentarioValue}"
                maxlength="255"
                required
            />
            <button class="btn btn-success" type="button" onclick="submitReview();" ${miResenia ? `data-resenia-id="${miResenia.reseniaId}"` : ''}>Enviar reseña</button>
        </div>
    `;

    if (miResenia) {
        const autorNombre = miResenia.usuario?.nombre || miResenia.autor;

        reviewFormHTML += `
            <div class="review-item">
                <div class="d-flex justify-content-between">
                    <strong>${autorNombre}</strong>
                    <span class="review-stars">
                        ${renderStars(miResenia.calificacion)} ${miResenia.calificacion} estrellas
                    </span>
                </div>
                <p>${miResenia.comentario}</p>
                <small class="text-muted">Fecha: ${formatDate(miResenia.fecha)}</small>
                <div class="mt-2 text-end">
                    <button
                        class="btn btn-danger btn-sm"
                        data-bs-toggle="modal"
                        data-bs-target="#deleteModal"
                        data-resenia-id="${miResenia.reseniaId}"
                    >
                        <i class="bi bi-trash"></i> Eliminar
                    </button>
                </div>
            </div>

            <div
                class="modal fade"
                id="deleteModal"
                tabindex="-1"
                aria-labelledby="deleteModalLabel"
                aria-hidden="true"
            >
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="deleteModalLabel">
                                Confirmar eliminación
                            </h5>
                            <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                            ></button>
                        </div>
                        <div class="modal-body">
                            ¿Estás seguro de que deseas eliminar esta reseña? Esta acción no se puede deshacer.
                        </div>
                        <div class="modal-footer">
                            <button
                                type="button"
                                class="btn btn-secondary"
                                data-bs-dismiss="modal"
                            >
                                Cancelar
                            </button>
                            <button type="button" class="btn btn-danger" onclick="deleteReview(${miResenia.reseniaId});">Eliminar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }
    myReviewSection.innerHTML = reviewFormHTML;
}

function renderStars(calificacion) {
    let starsHTML = '';
    const fullStars = Math.floor(calificacion);
    const halfStar = calificacion % 1 >= 0.5;

    for (let i = 0; i < fullStars; i++) {
        starsHTML += '<i class="bi bi-star-fill"></i> ';
    }

    if (halfStar) {
        starsHTML += '<i class="bi bi-star-half"></i> ';
    }

    const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);
    for (let i = 0; i < emptyStars; i++) {
        starsHTML += '<i class="bi bi-star"></i> ';
    }

    return starsHTML;
}

function formatDate(fechaISO) {
    const fecha = new Date(fechaISO);
    return fecha.toLocaleDateString('es-ES');
}

function renderAdditionalReviews(reseniasAdicionales) {
    const reviewsSection = document.getElementById('reviews-section');
    let reviewsHTML = `<h4>Reseñas de otros usuarios</h4>`;

    if (reseniasAdicionales && reseniasAdicionales.length > 0) {
        reseniasAdicionales.forEach(resenia => {
            reviewsHTML += `
                <div class="review-item">
                    <div class="d-flex justify-content-between">
                        <strong>${resenia.usuario.nombre}</strong>
                        <span class="review-stars">
                            ${renderStars(resenia.calificacion)} ${resenia.calificacion} estrellas
                        </span>
                    </div>
                    <p>${resenia.comentario}</p>
                    <small class="text-muted">Fecha: ${formatDate(resenia.fecha)}</small>
                </div>
            `;
        });

    } else {
        reviewsHTML += `<p>No hay reseñas adicionales.</p>`;
    }

    // Agregar nav con d-none si no hay reseñas
    reviewsHTML += `
        <nav aria-label="Navegación de reseñas" class="${reseniasAdicionales && reseniasAdicionales.length > 0 ? '' : 'd-none'}">
            <ul class="pagination justify-content-center"></ul>
        </nav>
    `

    reviewsSection.innerHTML = reviewsHTML;
}


// Función para actualizar la calificación promedio en el DOM
function actualizarCalificacionPromedio(calificacionPromedioActualizada) {
  const averageRatingElement = document.querySelector('.average-rating .review-stars');
  if (averageRatingElement) {
    averageRatingElement.innerHTML = `
      ${renderStars(calificacionPromedioActualizada)} ${calificacionPromedioActualizada} de 5
    `;
  }
}