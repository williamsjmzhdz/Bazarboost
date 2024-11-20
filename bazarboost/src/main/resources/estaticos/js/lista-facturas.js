import { actualizarPaginacion, cambiarPagina, getPaginaActual, setPaginaActual } from './paginacion.js';
import { mostrarMensajeError, mostrarListaErrores } from './mensajes-estado.js';

const listaFacturas = document.querySelector('table tbody');
const ordenSelector = document.getElementById('orderBy');
const mensajeNoFacturas = document.getElementById('no-facturas');
const tablaFacturas = document.querySelector('.table-responsive');
const contenedorPaginacion = document.getElementById("paginationContainer");

// Función para formatear la fecha en formato legible
function formatearFecha(fechaISO) {
    const fecha = new Date(fechaISO);
    return fecha.toLocaleDateString() + ' ' + fecha.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

// Función para actualizar la lista de facturas en la tabla
function actualizarListaFacturas(facturas) {
    listaFacturas.innerHTML = '';

    if (facturas.length === 0) {
        // Muestra mensaje y oculta tabla y select si no hay facturas
        mensajeNoFacturas.style.display = 'block';
        ordenSelector.style.display = 'none';
        tablaFacturas.style.display = 'none';
        contenedorPaginacion.style.display = 'none';
    } else {
        // Oculta el mensaje y muestra tabla y select si hay facturas
        mensajeNoFacturas.style.display = 'none';
        ordenSelector.style.display = 'block';
        tablaFacturas.style.display = 'block';

        // Inserta cada factura en la tabla
        facturas.forEach(factura => {
            const fila = `
                <tr>
                    <td>${factura.facturaId}</td>
                    <td>${formatearFecha(factura.fecha)}</td>
                    <td>$${factura.monto.toFixed(2)}</td>
                    <td>
                        <a href="/facturas/detalle/${factura.facturaId}" class="btn btn-primary btn-sm" data-factura-id="${factura.facturaId}">
                            <i class="bi bi-eye"></i> Ver Detalles
                        </a>
                    </td>
                </tr>`;
            listaFacturas.insertAdjacentHTML('beforeend', fila);
        });
    }
}

// Función auxiliar para obtener datos de error
async function obtenerErrorData(response) {
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        return await response.json();
    } else {
        return await response.text();  // Manejo para HTML u otros formatos
    }
}

// Función para obtener y cargar facturas con filtros y paginación
async function obtenerFacturas() {
    const orden = ordenSelector.value;
    const [ordenarPor, direccionOrden] = orden ? orden.split(/(?=[A-Z])/).map(str => str.toLowerCase()) : ['fecha', 'desc'];
    const url = new URL('/api/facturas', window.location.origin);

    url.searchParams.append('pagina', getPaginaActual());
    url.searchParams.append('ordenarPor', ordenarPor);
    url.searchParams.append('direccionOrden', direccionOrden);

    try {
        const response = await fetch(url);

        if (!response.ok) {
            const errorData = await obtenerErrorData(response);
            console.error("Error del servidor:", errorData);

            // Identificación específica de excepciones
            if (response.status === 400) {
                if (typeof errorData === "string" && errorData.includes('Parámetro de ordenación no válido.')) {
                    mostrarMensajeError(errorData);
                } else if (typeof errorData === "string" && errorData.includes('Dirección de orden no válida.')) {
                    mostrarMensajeError(errorData);
                } else {
                    mostrarMensajeError("Solicitud incorrecta. Verifica los parámetros e inténtalo de nuevo.");
                }
            } else if (response.status === 404) {
                if (typeof errorData === "string" && errorData.includes('Usuario')) {
                    mostrarMensajeError("Usuario no encontrado. Por favor, verifica tu cuenta.");
                } else if (typeof errorData === "string" && errorData.includes('Número de página fuera de rango.')) {
                    mostrarMensajeError("La página solicitada está fuera del rango de resultados disponibles.");
                } else {
                    mostrarMensajeError("Recurso no encontrado. Asegúrate de que el recurso solicitado existe.");
                }
            } else {
                mostrarMensajeError("Ocurrió un error inesperado al cargar las facturas. Por favor, intenta más tarde.");
            }
            return;
        }

        const data = await response.json();
        actualizarListaFacturas(data.facturas);
        actualizarPaginacion(data, cambiarPaginaCallback);

    } catch (error) {
        console.error("Error inesperado al obtener facturas:", error);
        mostrarMensajeError("Ocurrió un problema al cargar las facturas. Intenta nuevamente.");
    }
}


// Callback de cambio de página
function cambiarPaginaCallback(pagina) {
    setPaginaActual(pagina);
    obtenerFacturas();
}

// Event listener para el ordenamiento
ordenSelector.addEventListener('change', () => {
    setPaginaActual(0);
    obtenerFacturas();
});

// Inicializar la lista de facturas al cargar la página
obtenerFacturas();
window.cambiarPaginaCallback = cambiarPaginaCallback;  // Exportar callback para paginación
