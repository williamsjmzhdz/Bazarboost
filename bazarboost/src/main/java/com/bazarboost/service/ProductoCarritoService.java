package com.bazarboost.service;

import com.bazarboost.dto.RespuestaCarritoDTO;
import com.bazarboost.dto.SolicitudCarritoDTO;
import com.bazarboost.exception.*;

/**
 * Servicio que gestiona las operaciones relacionadas con el carrito de productos.
 * Proporciona métodos para agregar/quitar productos del carrito y consultar información del mismo.
 */
public interface ProductoCarritoService {

    /**
     * Actualiza el carrito del usuario con la acción especificada (agregar o quitar).
     *
     * @param solicitudCarritoDTO contiene el ID del producto y la acción a realizar
     * @param usuarioId ID del usuario
     * @return RespuestaCarritoDTO con el número total de productos en el carrito
     * @throws ProductoNoEncontradoException si el producto no existe
     * @throws UsuarioNoEncontradoException si el usuario no existe
     * @throws ProductoPropioException si el usuario intenta agregar su propio producto
     * @throws ProductoYaEnCarritoException si el producto ya está en el carrito
     * @throws ProductoNoEnCarritoException si se intenta quitar un producto que no está en el carrito
     * @throws AccionNoValidaException si la acción especificada no es válida
     */
    RespuestaCarritoDTO actualizarCarrito(SolicitudCarritoDTO solicitudCarritoDTO, Integer usuarioId);

    /**
     * Obtiene el número total de productos en el carrito del usuario especificado.
     *
     * @param usuarioId ID del usuario del cual se quiere obtener el total de productos en su carrito
     * @return Integer con el número total de productos en el carrito. Retorna 0 si el carrito está vacío
     * @throws UsuarioNoEncontradoException si no se encuentra el usuario con el ID especificado
     */
    Integer obtenerTotalProductosEnCarrito(Integer usuarioId);
}
