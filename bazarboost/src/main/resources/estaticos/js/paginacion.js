const contenedorPaginacion = document.querySelector('.pagination');
let paginaActual = 0;

function actualizarPaginacion(data) {
    contenedorPaginacion.innerHTML = '';
    const totalPaginas = data.totalPaginas;

    const prevDisabled = paginaActual === 0 ? 'disabled' : '';
    contenedorPaginacion.insertAdjacentHTML('beforeend', `
      <li class="page-item ${prevDisabled}">
          <a class="page-link" href="#" onclick="cambiarPagina(${paginaActual - 1})">Anterior</a>
      </li>
    `);

    const maxBotonesPagina = 5;
    const inicioPagina = Math.max(0, paginaActual - Math.floor(maxBotonesPagina / 2));
    const finPagina = Math.min(totalPaginas, inicioPagina + maxBotonesPagina);

    for (let i = inicioPagina; i < finPagina; i++) {
        const claseActiva = i === paginaActual ? 'active' : '';
        contenedorPaginacion.insertAdjacentHTML('beforeend', `
            <li class="page-item ${claseActiva}">
                <a class="page-link" href="#" onclick="cambiarPagina(${i})">${i + 1}</a>
            </li>
        `);
    }

    const nextDisabled = paginaActual === totalPaginas - 1 ? 'disabled' : '';
    contenedorPaginacion.insertAdjacentHTML('beforeend', `
      <li class="page-item ${nextDisabled}">
          <a class="page-link" href="#" onclick="cambiarPagina(${paginaActual + 1})">Siguiente</a>
      </li>
    `);
}

function cambiarPagina(pagina) {
    if (pagina >= 0) {
        paginaActual = pagina;
        obtenerProductos(); // Esta función debe estar en el archivo de la vista específica
    }
}
