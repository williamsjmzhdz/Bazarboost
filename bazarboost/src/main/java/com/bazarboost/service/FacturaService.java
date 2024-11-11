package com.bazarboost.service;

import com.bazarboost.dto.CarritoPagoRespuestaDTO;
import com.bazarboost.dto.CarritoPagoSolicitudDTO;
import com.bazarboost.dto.FacturaDTO;
import com.bazarboost.dto.FacturasPaginadasDTO;
import org.springframework.data.domain.Page;

import java.util.List;

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
