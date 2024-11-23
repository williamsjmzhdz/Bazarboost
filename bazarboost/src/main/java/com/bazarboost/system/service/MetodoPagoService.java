package com.bazarboost.service;

import com.bazarboost.dto.MetodoPagoCreacionDTO;
import com.bazarboost.dto.MetodoPagoDTO;
import com.bazarboost.dto.MetodoPagoEdicionDTO;
import com.bazarboost.exception.AccesoDenegadoException;
import com.bazarboost.exception.MetodoPagoNoEncontradoException;
import com.bazarboost.exception.NumeroTarjetaDuplicadoException;
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
     * @throws NumeroTarjetaDuplicadoException si el número de tarjeta ya está registrado.
     */
    Void crear(MetodoPagoCreacionDTO metodoPagoCreacionDTO, Integer usuarioId);

    /**
     * Recupera los datos necesarios para editar un método de pago específico.
     * Solo permite acceder a métodos de pago que pertenezcan al usuario especificado.
     *
     * @param metodoPagoId ID del método de pago a editar
     * @param usuarioId ID del usuario propietario
     * @return DTO con los datos necesarios para el formulario de edición
     * @throws UsuarioNoEncontradoException cuando el ID de usuario no existe en el sistema
     * @throws MetodoPagoNoEncontradoException cuando el ID del método de pago no existe
     * @throws AccesoDenegadoException cuando el método de pago no pertenece al usuario especificado
     */
    MetodoPagoEdicionDTO obtenerDatosEdicion(Integer metodoPagoId, Integer usuarioId);

    /**
     * Actualiza un método de pago existente.
     *
     * @param metodoPagoEdicionDTO Datos actualizados del método de pago. No puede ser null.
     * @param usuarioId ID del usuario propietario. No puede ser null.
     * @throws UsuarioNoEncontradoException cuando el ID de usuario no existe en el sistema
     * @throws MetodoPagoNoEncontradoException cuando el ID del método de pago no existe
     * @throws AccesoDenegadoException cuando el método de pago no pertenece al usuario especificado
     * @throws NumeroTarjetaDuplicadoException si el nuevo número de tarjeta ya está registrado
     */
    Void actualizar(MetodoPagoEdicionDTO metodoPagoEdicionDTO, Integer usuarioId);

    /**
     * Elimina un método de pago específico.
     * Solo permite eliminar métodos de pago que pertenezcan al usuario especificado.
     *
     * @param metodoPagoId ID del método de pago a eliminar
     * @param usuarioId ID del usuario propietario
     * @throws UsuarioNoEncontradoException cuando el ID de usuario no existe en el sistema
     * @throws MetodoPagoNoEncontradoException cuando el ID del método de pago no existe
     * @throws AccesoDenegadoException cuando el método de pago no pertenece al usuario especificado
     */
    void eliminar(Integer metodoPagoId, Integer usuarioId);
}
