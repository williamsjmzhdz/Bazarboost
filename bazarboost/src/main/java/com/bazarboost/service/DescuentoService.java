package com.bazarboost.service;

import com.bazarboost.dto.DescuentoVendedorDTO;
import com.bazarboost.model.Descuento;

import java.util.List;

public interface DescuentoService {

    /**
     * Recupera la lista de descuentos asociados a un usuario específico.
     *
     * @param usuarioId Identificador único del usuario.
     * @return Lista de objetos {@link Descuento} correspondientes al usuario.
     */
    List<Descuento> obtenerDescuentosPorUsuario(Integer usuarioId);

    /**
     * Obtiene la lista de descuentos en formato DTO para un usuario dado.
     *
     * @param usuarioId Identificador único del usuario.
     * @return Lista de objetos {@link DescuentoVendedorDTO} representando los descuentos del usuario.
     */
    List<DescuentoVendedorDTO> obtenerDescuentosDTOPorUsuario(Integer usuarioId);

    /**
     * Busca un descuento específico por su identificador y el identificador del usuario asociado.
     *
     * @param id Identificador único del descuento.
     * @param usuarioId Identificador único del usuario.
     * @return Objeto {@link Descuento} que coincide con el ID y el usuario proporcionados.
     */
    Descuento obtenerDescuentoPorIdYUsuario(Integer id, Integer usuarioId);


}
