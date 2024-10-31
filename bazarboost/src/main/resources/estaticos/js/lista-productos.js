const barraBusqueda = document.getElementById('searchBar');
const filtroCategoria = document.getElementById('categoryFilter');
const ordenPrecio = document.getElementById('priceSort');
const listaProductos = document.getElementById('productList');

function obtenerProductos() {
    const palabraClave = barraBusqueda.value || null;
    const categoria = filtroCategoria.value || null;
    const orden = ordenPrecio.value || null;

    const url = new URL('/api/productos/filtrados', window.location.origin);
    url.searchParams.append('page', paginaActual);

    if (palabraClave) url.searchParams.append('keyword', palabraClave);
    if (categoria) url.searchParams.append('categoria', categoria);
    if (orden) url.searchParams.append('orden', orden);

    fetch(url)
        .then(response => response.json())
        .then(data => {
            actualizarListaProductos(data.productos);
            actualizarPaginacion(data);  // Función de paginación importada de `paginacion.js`
        });
}

function formatearPrecio(precio) {
    return precio.toFixed(2);
}

function actualizarListaProductos(productos) {
    listaProductos.innerHTML = '';
    if (productos.length > 0) {
        productos.forEach(producto => {
            const precioFinal = formatearPrecio(producto.precioFinalConDescuento);
            const precioOriginal = formatearPrecio(producto.precio);

            const productoHtml = `
              <div class="col-lg-4 col-md-6">
                <div class="card h-100">
                  <img src="/api/productos/imagenes/${producto.imagenUrl}" class="card-img-top" alt="${producto.nombre}" />
                  <div class="card-body">
                    <h5 class="card-title">${producto.nombre}</h5>
                    <p class="card-text">${producto.descripcion}</p>
                    ${producto.porcentajeDescuento ? `
                      <p class="card-text">
                        <del>Precio: $${precioOriginal}</del> <strong> $${precioFinal}</strong>
                        <span class="text-success">(${producto.porcentajeDescuento}% Descuento)</span>
                      </p>
                    ` : `
                      <p class="card-text"><strong>Precio: $${precioOriginal}</strong></p>
                    `}
                    <div class="average-rating">
                      <span class="review-stars">
                        <i class="bi bi-star-fill"></i> ${producto.calificacionPromedio} de 5
                      </span>
                    </div>
                  </div>
                  <div class="card-footer text-center">
                    ${producto.esProductoPropio ? `` : `
                      <a class="btn ${producto.estaEnCarrito ? 'btn-danger' : 'btn-primary'} me-2" role="button"
                         data-producto-id="${producto.productoId}"
                         data-accion="${producto.estaEnCarrito ? 'quitar' : 'agregar'}"
                         onclick="actualizarCarrito(event)">
                         <i class="bi ${producto.estaEnCarrito ? 'bi-cart-dash' : 'bi-cart-plus'}"></i>
                         ${producto.estaEnCarrito ? 'Quitar del carrito' : 'Agregar al carrito'}
                      </a>
                    `}
                    <a class="btn btn-secondary" href="/productos/detalle-producto/${producto.productoId}">
                      <i class="bi bi-eye"></i> Ver producto
                    </a>
                  </div>
                </div>
              </div>`;
            listaProductos.insertAdjacentHTML('beforeend', productoHtml);
        });
    } else {
        listaProductos.innerHTML = '<div class="no-products-message text-center">No se encontraron productos.</div>';
    }
}

barraBusqueda.addEventListener('input', () => {
    paginaActual = 0;
    obtenerProductos();
});

filtroCategoria.addEventListener('change', () => {
    paginaActual = 0;
    obtenerProductos();
});

ordenPrecio.addEventListener('change', () => {
    paginaActual = 0;
    obtenerProductos();
});

obtenerProductos();
