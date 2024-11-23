package com.bazarboost.system.service;

import com.bazarboost.system.dto.DescuentoVendedorDTO;
import com.bazarboost.shared.exception.DescuentoNoEncontradoException;
import com.bazarboost.shared.exception.PorcentajeDescuentoInvalidoException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.system.model.Descuento;

import java.util.List;

/**
 * Servicio que gestiona las operaciones relacionadas con los descuentos en el sistema.
 */
public interface DescuentoService {

    /**
     * Obtiene la lista de descuentos en formato DTO para un usuario específico.
     *
     * @param usuarioId Identificador único del usuario
     * @return Lista de objetos {@link DescuentoVendedorDTO} correspondientes al usuario
     * @throws UsuarioNoEncontradoException si el usuario no existe
     */
    List<DescuentoVendedorDTO> obtenerDescuentosDTOPorUsuario(Integer usuarioId);

    /**
     * Busca un descuento específico por su identificador y el identificador del usuario asociado.
     *
     * @param descuentoId Identificador único del descuento
     * @param usuarioId   Identificador único del usuario
     * @return Objeto {@link Descuento} que coincide con los identificadores proporcionados
     * @throws DescuentoNoEncontradoException si el descuento no existe para el usuario
     * @throws UsuarioNoEncontradoException si el usuario no existe
     */
    Descuento obtenerDescuentoPorIdYUsuarioId(Integer descuentoId, Integer usuarioId);

    /**
     * Crea un nuevo descuento para un usuario específico.
     *
     * @param descuento Objeto Descuento con los datos del nuevo descuento
     * @param usuarioId ID del usuario que está creando el descuento
     * @return El descuento creado con su ID asignado
     * @throws UsuarioNoEncontradoException si el usuario no existe
     * @throws PorcentajeDescuentoInvalidoException si el porcentaje no es válido
     */
    Descuento crearDescuento(Descuento descuento, Integer usuarioId);

    /**
     * Actualiza un descuento existente en el sistema.
     *
     * @param descuentoId Identificador único del descuento a actualizar
     * @param descuentoActualizado Objeto {@link Descuento} con los nuevos datos
     * @param usuarioId Identificador único del usuario propietario del descuento
     * @return El {@link Descuento} actualizado
     * @throws DescuentoNoEncontradoException si el descuento no existe para el usuario
     * @throws PorcentajeDescuentoInvalidoException si el porcentaje no está entre 1 y 100
     * @throws UsuarioNoEncontradoException si el usuario no existe
     */
    Descuento actualizarDescuento(Integer descuentoId, Descuento descuentoActualizado, Integer usuarioId);

    /**
     * Elimina un descuento específico del sistema.
     *
     * @param descuentoId Identificador único del descuento a eliminar
     * @param usuarioId Identificador único del usuario propietario del descuento
     * @throws DescuentoNoEncontradoException si el descuento no existe para el usuario
     * @throws UsuarioNoEncontradoException si el usuario no existe
     */
    void eliminarDescuento(Integer descuentoId, Integer usuarioId);
}