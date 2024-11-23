package com.bazarboost.system.service;

import com.bazarboost.system.dto.DireccionCreacionDTO;
import com.bazarboost.system.dto.DireccionDTO;
import com.bazarboost.system.dto.DireccionEdicionDTO;
import com.bazarboost.shared.exception.AccesoDenegadoException;
import com.bazarboost.shared.exception.DireccionNoEncontradaException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;

import java.util.List;

public interface DireccionService {

    /**
     * Crea un método de pago para un usuario.
     *
     * @param direccionCreacionDTO Datos de la dirección. No puede ser null.
     * @param usuarioId ID del usuario. No puede ser null.
     * @throws UsuarioNoEncontradoException si el usuario no existe.
     */
    Void crear(DireccionCreacionDTO direccionCreacionDTO, Integer usuarioId);

    /**
     * Recupera todas las direcciones asociadas a un usuario específico.
     *
     * @param usuarioId Identificador único del usuario del cual se desean obtener sus direcciones.
     *                  No puede ser null.
     * @return Lista de DTOs con la información de las direcciones del usuario.
     * @throws UsuarioNoEncontradoException si no se encuentra un usuario asociado a usuarioId.
     */
    List<DireccionDTO> obtenerTodas(Integer usuarioId);

    /**
     * Recupera los datos necesarios para editar una dirección específica.
     * Solo permite acceder a direcciones que pertenezcan al usuario especificado.
     *
     * @param direccionId ID de la dirección a editar.
     * @param usuarioId ID del usuario propietario.
     * @return DTO con los datos necesarios para el formulario de edición.
     * @throws UsuarioNoEncontradoException cuando el ID de usuario no existe en el sistema.
     * @throws DireccionNoEncontradaException cuando el ID de la dirección no existe.
     * @throws AccesoDenegadoException cuando la dirección no pertenece al usuario especificado
     */
    DireccionEdicionDTO obtenerDatosEdicion(Integer direccionId, Integer usuarioId);

    /**
     * Actualiza una dirección existente
     *
     * @param direccionEdicionDTO Datos actualizados de la dirección. No puede ser null.
     * @param usuarioId ID del usuario propietario. No puede ser null.
     * @throws UsuarioNoEncontradoException cuando el ID del usuario no existe.
     * @throws DireccionNoEncontradaException cuando el ID de la dirección no existe.
     * @throws AccesoDenegadoException cuando la dirección no pertenece al usuario especificado.
     */
    Void actualizar(DireccionEdicionDTO direccionEdicionDTO, Integer usuarioId);

    /**
     * Elimina una dirección específica.
     * Solo permite eliminar direcciones que pertenezcan al usuario especificado.
     *
     * @param direccionId ID de la dirección a eliminar
     * @param usuarioId ID del usuario propietario
     * @throws UsuarioNoEncontradoException cuando el ID de usuario no existe en el sistema
     * @throws DireccionNoEncontradaException cuando el ID de la dirección no existe
     * @throws AccesoDenegadoException cuando la dirección no pertenece al usuario especificado
     */
    void eliminar(Integer direccionId, Integer usuarioId);

}
