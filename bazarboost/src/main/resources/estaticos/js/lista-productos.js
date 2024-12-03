// lista-productos.js
import { actualizarPaginacion, cambiarPagina, getPaginaActual, setPaginaActual } from './paginacion.js';
import { mostrarMensajeErrorURL } from './mensajes-estado.js';

window.cambiarPaginaCallback = (pagina) => cambiarPagina(pagina, obtenerProductos);

const barraBusqueda = document.getElementById('searchBar');
const filtroCategoria = document.getElementById('categoryFilter');
const ordenPrecio = document.getElementById('priceSort');
const listaProductos = document.getElementById('productList');

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

function obtenerProductos() {
    const palabraClave = barraBusqueda.value || null;
    const categoria = filtroCategoria.value || null;
    const orden = ordenPrecio.value || null;

    const url = new URL('/api/productos/filtrados', window.location.origin);
    url.searchParams.append('page', getPaginaActual());

    if (palabraClave) url.searchParams.append('keyword', palabraClave);
    if (categoria) url.searchParams.append('categoria', categoria);
    if (orden) url.searchParams.append('orden', orden);

    fetch(url)
        .then(response => response.json())
        .then(data => {
            actualizarListaProductos(data.productos);
            actualizarPaginacion(data, window.cambiarPaginaCallback);
        });
}

// Función para cargar las categorías dinámicamente
function cargarCategorias() {
    fetch('/api/categorias')
        .then(response => response.json())
        .then(categorias => {
            filtroCategoria.innerHTML = '<option selected value="">Todas las Categorías</option>';
            categorias.forEach(categoria => {
                const option = document.createElement('option');
                option.value = categoria.nombre;
                option.textContent = categoria.nombre;
                filtroCategoria.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar las categorías:', error);
        });
}

// Event Listeners
barraBusqueda.addEventListener('input', () => {
    setPaginaActual(0);
    obtenerProductos();
});

filtroCategoria.addEventListener('change', () => {
    setPaginaActual(0);
    obtenerProductos();
});

ordenPrecio.addEventListener('change', () => {
    setPaginaActual(0);
    obtenerProductos();
});

// Inicialización
obtenerProductos();
window.obtenerProductos = obtenerProductos;  // Exportar globalmente

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    mostrarMensajeErrorURL();
    cargarCategorias();
});
