let paginaActual = 0;

function getPaginaActual() {
    return paginaActual;
}

function setPaginaActual(nuevaPagina) {
    paginaActual = nuevaPagina;
}

function actualizarPaginacion(data, cambiarPaginaCallback) {
    const contenedorPaginacion = document.querySelector('.pagination');
    contenedorPaginacion.innerHTML = '';
    const totalPaginas = data.totalPaginas;
    const prevDisabled = paginaActual === 0 ? 'disabled' : '';
    contenedorPaginacion.insertAdjacentHTML('beforeend', `
      <li class="page-item ${prevDisabled}">
          <a class="page-link" style="cursor:pointer" onclick="window.cambiarPaginaCallback(${paginaActual - 1})">Anterior</a>
      </li>
    `);
    const maxBotonesPagina = 5;
    const inicioPagina = Math.max(0, paginaActual - Math.floor(maxBotonesPagina / 2));
    const finPagina = Math.min(totalPaginas, inicioPagina + maxBotonesPagina);
    for (let i = inicioPagina; i < finPagina; i++) {
        const claseActiva = i === paginaActual ? 'active' : '';
        contenedorPaginacion.insertAdjacentHTML('beforeend', `
            <li class="page-item ${claseActiva}">
                <a class="page-link" style="cursor:pointer" onclick="window.cambiarPaginaCallback(${i})">${i + 1}</a>
            </li>
        `);
    }
    const nextDisabled = paginaActual === totalPaginas - 1 ? 'disabled' : '';
    contenedorPaginacion.insertAdjacentHTML('beforeend', `
      <li class="page-item ${nextDisabled}">
          <a class="page-link" style="cursor: pointer" onclick="window.cambiarPaginaCallback(${paginaActual + 1})">Siguiente</a>
      </li>
    `);
}

function cambiarPagina(pagina, obtenerRecursosCallback) {
    if (pagina >= 0) {
        setPaginaActual(pagina);
        obtenerRecursosCallback();
        // Scroll suave al contenedor de paginación después de cambiar de página
        const contenedorPaginacion = document.querySelector('.pagination');
        if (contenedorPaginacion) {
            setTimeout(() => {
                contenedorPaginacion.scrollIntoView({ behavior: 'smooth' });
            }, 100); // Pequeño delay para asegurar que el contenido se haya actualizado
        }
    }
}


export { actualizarPaginacion, cambiarPagina, getPaginaActual, setPaginaActual };
