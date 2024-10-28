package com.bazarboost.service;

import com.bazarboost.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

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

}
