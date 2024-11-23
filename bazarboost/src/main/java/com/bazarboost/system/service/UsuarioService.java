package com.bazarboost.service;

import com.bazarboost.dto.UsuarioRegistroDTO;
import com.bazarboost.model.Usuario;

import java.util.Optional;

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

}
