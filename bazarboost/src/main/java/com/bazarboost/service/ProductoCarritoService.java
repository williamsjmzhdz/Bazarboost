package com.bazarboost.service;

import com.bazarboost.dto.RespuestaCarritoDTO;
import com.bazarboost.dto.SolicitudCarritoDTO;

public interface ProductoCarritoService {

    /**
     * Actualiza el carrito del usuario con la acción especificada (agregar o quitar)
     *
     * @param solicitudCarritoDTO contiene el ID del producto y la acción a realizar
     * @param usuarioId ID del usuario
     * @return  RespuestaCarritoDTO con el número total de productos en el carrito.
     */
    RespuestaCarritoDTO actualizarCarrito(SolicitudCarritoDTO solicitudCarritoDTO, Integer usuarioId);

}
