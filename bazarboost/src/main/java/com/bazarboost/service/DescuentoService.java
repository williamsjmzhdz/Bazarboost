package com.bazarboost.service;

import com.bazarboost.model.Descuento;

import java.util.List;
import java.util.Optional;

public interface DescuentoService {

    /**
     * Obtiene los descuentos del usuario actual.
     *
     * @param usuarioId ID del usuario.
     * @return Lista de descuentos asociados al usuario.
     */
    List<Descuento> obtenerDescuentosPorUsuario(Integer usuarioId);

    /**
     * Obtiene un descuento por su ID y el ID del usuario.
     *
     * @param id El ID del descuento.
     * @param usuarioId El ID del usuario dueño del descuento.
     * @return El descuento encontrado.
     */
    Descuento obtenerDescuentoPorIdYUsuario(Integer id, Integer usuarioId);

}
