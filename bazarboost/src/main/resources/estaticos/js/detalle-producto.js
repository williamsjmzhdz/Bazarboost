document.addEventListener('DOMContentLoaded', () => {
  // Obtener el ID del producto desde la URL
  const urlParams = new URLSearchParams(window.location.search);
  const productId = window.location.pathname.split('/').pop();
  let currentPage = urlParams.get('page') || 0;

  // Endpoint del producto
  const endpoint = `/api/productos/detalle-producto/${productId}?page=${currentPage}`;

  // Realizar la petición al endpoint
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
      // Actualizar el DOM con los datos del producto
      renderProductDetails(data);
      renderMyReview(data.miResenia);
      renderAdditionalReviews(data.reseniasAdicionales);
    })
    .catch(error => {
      // Mostrar mensaje de error
      mostrarMensajeErrorSolo(error.message);

    });
});

// Función para renderizar los detalles del producto
function renderProductDetails(data) {
  const productContainer = document.getElementById('product-container');
  const descuento = data.descuento;
  const precioConDescuento = data.precioConDescuento;

  // Construir el HTML
  let productHTML = `
    <div class="col-md-6">
      <img src="/api/productos/imagenes/${data.imagenUrl}" alt="${data.nombre}" class="product-image" />
    </div>
    <div class="col-md-6 product-info">
      <h2>${data.nombre}</h2>
      <p>
        <strong>Descripción:</strong> ${data.descripcion}
      </p>
      <p><strong>Precio:</strong> $${data.precio}</p>
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
      <p><strong>Existencia:</strong> ${data.existencia} unidades disponibles</p>
      <p><strong>Categoría:</strong> ${data.nombreCategoria}</p>
      <p><strong>Calificación promedio:</strong></p>
      <div class="average-rating">
        <span class="review-stars">
          ${renderStars(data.calificacionPromedio)} ${data.calificacionPromedio} de 5
        </span>
      </div>

      <button class="btn btn-primary me-2">
        <i class="bi bi-cart-plus"></i> Agregar al carrito
      </button>
      <button class="btn btn-secondary" onclick="window.history.back();">
        <i class="bi bi-arrow-left"></i> Volver a la lista
      </button>
    </div>
  `;

  productContainer.innerHTML = productHTML;
}

// Función para renderizar mi reseña
function renderMyReview(miResenia) {
  const myReviewSection = document.getElementById('my-review-section');

  // Valores prellenados si existe miResenia
  const calificacionValue = miResenia ? miResenia.calificacion : '';
  const comentarioValue = miResenia ? miResenia.comentario : '';

  // Formulario para agregar o editar reseña
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

  // Si existe mi reseña, mostrarla con opciones para eliminar
  if (miResenia) {

    // Seleccionar el nombre del autor de forma flexible
    const autorNombre = miResenia.usuario?.nombre || miResenia.autor;

    reviewFormHTML += `
      <!-- Reseña del usuario actual -->
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

      <!-- Modal de confirmación para eliminar reseña -->
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


// Función para renderizar las reseñas adicionales
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

    // Agregar paginación (si es necesario)
    reviewsHTML += `
      <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
          <!-- Aquí se agregarán los enlaces de paginación -->
        </ul>
      </nav>
    `;
  } else {
    reviewsHTML += `<p>No hay reseñas adicionales.</p>`;
  }

  reviewsSection.innerHTML = reviewsHTML;
}

// Función para renderizar las estrellas de calificación
function renderStars(calificacion) {
  let starsHTML = '';
  const fullStars = Math.floor(calificacion);
  const halfStar = calificacion % 1 >= 0.5;

  // Estrellas completas
  for (let i = 0; i < fullStars; i++) {
    starsHTML += '<i class="bi bi-star-fill"></i> ';
  }

  // Media estrella
  if (halfStar) {
    starsHTML += '<i class="bi bi-star-half"></i> ';
  }

  // Estrellas vacías
  const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);
  for (let i = 0; i < emptyStars; i++) {
    starsHTML += '<i class="bi bi-star"></i> ';
  }

  return starsHTML;
}

// Función para formatear la fecha
function formatDate(fechaISO) {
  const fecha = new Date(fechaISO);
  return fecha.toLocaleDateString('es-ES');
}

function submitReview() {
  const calificacion = document.getElementById('calificacion').value;
  const comentario = document.getElementById('comentario').value;
  const reseniaId = document.querySelector('button[data-resenia-id]')?.getAttribute('data-resenia-id');
  const productId = window.location.pathname.split('/').pop();

  if (!calificacion || !comentario) {
    mostrarMensajeErrorReview('Por favor, completa la calificación y el comentario.');
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
      mostrarMensajeExitoReview(`Reseña ${isEdit ? 'actualizada' : 'enviada'} con éxito.`);
      renderMyReview(data);

      // Actualizar la calificación promedio en el DOM
      actualizarCalificacionPromedio(data.calificacionPromedioActualizada);
    } else {
      throw new Error(`Error al ${isEdit ? 'actualizar' : 'enviar'} la reseña.`);
    }
  })
  .catch(error => {
    mostrarMensajeErrorReview(error.message);
  });
}

function deleteReview(reseniaId) {
  fetch(`/api/resenias/${reseniaId}`, {
    method: 'DELETE'
  })
  .then(response => {
    if (response.status === 200) {
      return response.json(); // Convertimos la respuesta a JSON si el status es 200
    } else {
      throw new Error('Error al eliminar la reseña.');
    }
  })
  .then(data => {
    mostrarMensajeExitoReview('Reseña eliminada con éxito.');

    // Cerrar el modal de confirmación de eliminación
    const deleteModal = document.getElementById('deleteModal');
    const modal = bootstrap.Modal.getInstance(deleteModal);
    if (modal) {
      modal.hide();
    }

    // Eliminar la reseña del DOM y actualizar el formulario de reseña
    renderMyReview(null); // Esto restablece el formulario a "Deja tu reseña" y limpia los campos

    // Actualizar la calificación promedio en el DOM con el valor devuelto
    actualizarCalificacionPromedio(data.calificacionPromedioActualizada);
  })
  .catch(error => {
    mostrarMensajeErrorReview(error.message);
  });
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

    // Paginación
    reviewsHTML += `
      <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
          ${renderPagination()}
        </ul>
      </nav>
    `;
  } else {
    reviewsHTML += `<p>No hay reseñas adicionales.</p>`;
  }

  reviewsSection.innerHTML = reviewsHTML;
}

// Función para renderizar la paginación
function renderPagination() {
  // Datos de paginación (deberían provenir del backend)
  const totalPages = 5; // Ejemplo
  const currentPage = parseInt(new URLSearchParams(window.location.search).get('page')) || 0;
  const productId = window.location.pathname.split('/').pop();
  let paginationHTML = '';

  // Botón Anterior
  if (currentPage > 0) {
    paginationHTML += `
      <li class="page-item">
        <a class="page-link" href="?page=${currentPage - 1}">Anterior</a>
      </li>
    `;
  } else {
    paginationHTML += `
      <li class="page-item disabled">
        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Anterior</a>
      </li>
    `;
  }

  // Páginas
  for (let i = 0; i < totalPages; i++) {
    paginationHTML += `
      <li class="page-item ${currentPage === i ? 'active' : ''}">
        <a class="page-link" href="?page=${i}">${i + 1}</a>
      </li>
    `;
  }

  // Botón Siguiente
  if (currentPage < totalPages - 1) {
    paginationHTML += `
      <li class="page-item">
        <a class="page-link" href="?page=${currentPage + 1}">Siguiente</a>
      </li>
    `;
  } else {
    paginationHTML += `
      <li class="page-item disabled">
        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Siguiente</a>
      </li>
    `;
  }

  return paginationHTML;
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