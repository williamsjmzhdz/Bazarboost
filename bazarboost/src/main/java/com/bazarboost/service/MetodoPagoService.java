package com.bazarboost.service;

import com.bazarboost.dto.MetodoPagoCreacionDTO;
import com.bazarboost.dto.MetodoPagoDTO;
import com.bazarboost.exception.UsuarioNoEncontradoException;

import java.util.List;

/**
 * Servicio que gestiona las operaciones relacionadas con los métodos de pago en el sistema.
 * Define las operaciones básicas para la consulta de métodos de pago asociados a un usuario.
 */
public interface MetodoPagoService {

    /**
     * Recupera todos los métodos de pago asociados a un usuario específico.
     *
     * @param usuarioId Identificador único del usuario del cual se desean obtener sus métodos de pago.
     *                  No puede ser null.
     * @return Lista de DTOs con la información de los métodos de pago del usuario.
     *         Retorna una lista vacía si el usuario no tiene métodos de pago registrados.
     * @throws UsuarioNoEncontradoException si no se encuentra un usuario asociado a usuarioId.
     */
    List<MetodoPagoDTO> obtenerTodos(Integer usuarioId);


    /**
     * Crea un método de pago para un usuario.
     *
     * @param metodoPagoCreacionDTO Datos del método de pago. No puede ser null.
     * @param usuarioId ID del usuario. No puede ser null.
     * @throws UsuarioNoEncontradoException si el usuario no existe.
     */
    Void crear(MetodoPagoCreacionDTO metodoPagoCreacionDTO, Integer usuarioId);

}
