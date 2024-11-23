package com.bazarboost.system.service;

import com.bazarboost.system.dto.CarritoPagoRespuestaDTO;
import com.bazarboost.system.dto.CarritoPagoSolicitudDTO;
import com.bazarboost.system.dto.FacturasPaginadasDTO;

public interface FacturaService {

    /**
     * Procesa el pago del carrito para el usuario especificado.
     *
     * @param carritoPagoSolicitudDTO con toda la información para procesar el pago
     * @param usuarioId el ID del usuario dueño del carrito
     * @return El {@link CarritoPagoRespuestaDTO} con el ID de la factura generada
     */
    CarritoPagoRespuestaDTO procesarPago(CarritoPagoSolicitudDTO carritoPagoSolicitudDTO, Integer usuarioId);

    /**
     * Obtiene una página de facturas ordenadas y paginadas según los criterios indicados.
     *
     * @param ordenarPor     Campo por el cual ordenar las facturas, puede ser "fecha" o "total".
     * @param direccionOrden Dirección del orden, "asc" para ascendente o "desc" para descendente.
     * @param pagina         Número de página a obtener (comenzando desde 0).
     * @param tamanoPagina   Tamaño de la página de resultados.
     * @param usuarioId      ID del usuario propietario de las facturas.
     * @return Objeto que contiene las facturas en la página solicitada e información de paginación.
     */
    FacturasPaginadasDTO obtenerFacturasPaginadasYOrdenadas(String ordenarPor, String direccionOrden, Integer pagina, Integer tamanoPagina,
                                                            Integer usuarioId);



}
