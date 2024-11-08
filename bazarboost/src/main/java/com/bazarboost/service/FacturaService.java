package com.bazarboost.service;

import com.bazarboost.dto.CarritoPagoRespuestaDTO;
import com.bazarboost.dto.CarritoPagoSolicitudDTO;

public interface FacturaService {

    /**
     * Procesa el pago del carrito para el usuario especificado.
     *
     * @param carritoPagoSolicitudDTO con toda la información para procesar el pago
     * @param usuarioId el ID del usuario dueño del carrito
     * @return El {@link CarritoPagoRespuestaDTO} con el ID de la factura generada
     */
    CarritoPagoRespuestaDTO procesarPago(CarritoPagoSolicitudDTO carritoPagoSolicitudDTO, Integer usuarioId);

}
