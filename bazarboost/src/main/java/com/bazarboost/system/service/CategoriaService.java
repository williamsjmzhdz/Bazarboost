package com.bazarboost.system.service;

import com.bazarboost.system.dto.CategoriaCreacionDTO;
import com.bazarboost.system.dto.CategoriaEdicionDTO;
import com.bazarboost.shared.exception.AccesoDenegadoException;
import com.bazarboost.shared.exception.CategoriaNoEncontradaException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.system.model.Categoria;

import java.util.List;

public interface CategoriaService {

    /**
     * Crea una categoria.
     *
     * @param categoriaCreacionDTO Datos de la categoria. No puede ser null.
     * @param usuarioId ID del usuario. No puede ser null.
     * @throws UsuarioNoEncontradoException si el usuario no existe.
     */
    Void crear(CategoriaCreacionDTO categoriaCreacionDTO, Integer usuarioId);

    /**
     * Obtiene todas las categorías.
     *
     * @return Lista de todas las categorías.
     */
    List<Categoria> obtenerTodasLasCategorias();


    /**
     * Obtiene una categoría por su ID.
     *
     * @param id El ID de la categoría.
     * @return  La categoría encontrada
     */
    Categoria obtenerCategoriaPorId(Integer id);

    /**
     * Recupera los datos necesarios para editar una categoria específica.
     *
     * @param categoriaId ID de la categoria a editar.
     * @return DTO con los datos necesarios para el formulario de edición.
     * @throws CategoriaNoEncontradaException cuando el ID de la dirección no existe.
     */
    CategoriaEdicionDTO obtenerDatosEdicion(Integer categoriaId);

    /**
     * Recupera todas las categorias.
     *
     * @return Lista de Categorias.
     */
    List<Categoria> obtenerTodas();

    /**
     * Actualiza una categoria existente
     *
     * @param categoriaEdicionDTO Datos actualizados de la categoria. No puede ser null.
     * @param usuarioId ID del usuario administrador. No puede ser null.
     * @throws UsuarioNoEncontradoException cuando el ID del usuario no existe.
     * @throws CategoriaNoEncontradaException cuando el ID de la categoria no existe.
     * @throws AccesoDenegadoException cuando el usuario no tiene el rol de Administrador.
     */
    Void actualizar(CategoriaEdicionDTO categoriaEdicionDTO, Integer usuarioId);

    /**
     * Elimina una categoria específica.
     * Solo permite eliminar categorias por el usuario administrador.
     *
     * @param categoriaId ID de la categoria a eliminar
     * @param usuarioId ID del usuario administrador
     * @throws UsuarioNoEncontradoException cuando el ID de usuario administrador no existe en el sistema
     * @throws CategoriaNoEncontradaException cuando el ID de la categoria no existe
     * @throws AccesoDenegadoException cuando el usuario no tiene el rol de administador
     */
    void eliminar(Integer categoriaId, Integer usuarioId);

}
