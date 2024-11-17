package com.bazarboost.service;

import com.bazarboost.dto.CategoriaCreacionDTO;
import com.bazarboost.dto.CategoriaEdicionDTO;
import com.bazarboost.dto.DireccionEdicionDTO;
import com.bazarboost.exception.AccesoDenegadoException;
import com.bazarboost.exception.CategoriaNoEncontradaException;
import com.bazarboost.exception.DireccionNoEncontradaException;
import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.model.Categoria;

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

}
