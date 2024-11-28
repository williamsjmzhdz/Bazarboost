package com.bazarboost.system.service;

import com.bazarboost.shared.exception.*;
import com.bazarboost.system.dto.*;
import com.bazarboost.system.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface UsuarioService {

    /**
     * Busca un usuario en la base de datos por su identificador único.
     *
     * @param usuarioId el ID del usuario que se desea obtener.
     * @return El usuario si se encuentra sino null.
     */
    Usuario obtenerUsuarioPorId(Integer usuarioId);

    /**
     * Guarda un usuario en la base de datos para su registro.
     *
     * @param usuarioDTO Objeto con la información del registro del usuario
     */
    void guardarUsuario(UsuarioRegistroDTO usuarioDTO);

    /**
     * Obtiene todos los usuarios
     *
     * @param keyword Palabra clave para buscar por nombre, apellidos, correo electrónico o teléfono.
     * @param pagina Número de página.
     * @param tamanioPagina Número de usuarios por página.
     * @param usuarioId ID del usuario que solicita los datos.
     * @return Objeto con una lista de UsuarioDTOs con la información necesaria para su gestión y paginación.
     * @throws UsuarioNoEncontradoException si el usuario no existe.
     * @throws AccesoDenegadoException si el usuario no tiene el rol Administrador.
     * @throws PaginaFueraDeRangoException si el número de página está fuera del rango de páginas.
     */
     UsuariosPaginadosDTO obtenerTodos(String keyword, Integer pagina, Integer tamanioPagina, Integer usuarioId);

    /**
     * Actualiza el rol de vendedor de un usuario.
     *
     * @param usuarioId ID del usuario a agregar/quitar rol de vendedor.
     * @param esVendedor Booleano para saber si hay que agregar (true) o quitar (false) el rol de vendedor.
     * @param usuarioAdminId ID del administrador que solicita el cambio.
     * @throws UsuarioNoEncontradoException si el usuario o administrador no existen.
     * @throws AccesoDenegadoException si el usuario administrador no tiene el rol de Administrador.
     * @throws AsignacionRolInvalidaException si se intenta asignar un rol que el usuario ya tiene o quitar un rol que no tiene.
     * @throws RolNoEncontradoException si el rol que se intenta asignar no existe.
     */
    void actualizarRolVendedor(Integer usuarioId, Boolean esVendedor, Integer usuarioAdminId);

    /**
     * Obtiene los datos del usuario especificado.
     *
     * @param usuarioId ID del usuario a obtener los datos.
     * @return DTO con los datos necesarios para la vista perfíl de usuario.
     * @throws UsuarioNoEncontradoException si el usuario no existe.
     */
    PerfilUsuarioDTO obtenerPerfil(Integer usuarioId);

    /**
     * Actualiza los datos personales y credenciales de un usuario.
     *
     * @param usuarioId ID del usuario
     * @param request Datos actualizados (nombre, apellidos, teléfono, correo, contraseña)
     * @throws UsuarioNoEncontradoException Usuario no existe
     * @throws CorreoElectronicoExistenteException Correo ya registrado por otro usuario
     * @throws TelefonoExistenteException Teléfono ya registrado por otro usuario
     */
    void actualizar(Integer usuarioId, UsuarioActualizacionDTO request);

    /**
     * Obtiene el nombre del usuario especificado.
     *
     * @param usuarioId ID del usuario a recuperar el nombre.
     * @return DTO con el nombre del usuario especificado.
     * @throws UsuarioNoEncontradoException si el usuario no existe.
     */
    UsuarioNombreDTO obtenerNombre(Integer usuarioId);

}
