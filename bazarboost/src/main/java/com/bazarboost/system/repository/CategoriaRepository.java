package com.bazarboost.repository;

import com.bazarboost.system.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    /**
     * Verifica si existe una categoría con el nombre especificado.
     *
     * @param nombre Nombre de la categoría a buscar.
     * @return true si existe una categoría con el nombre dado, false en caso contrario.
     */
    boolean existsByNombre(String nombre);

    /**
     * Verifica si existe una categoría con el nombre especificado excluyendo la categoría con el ID dado.
     *
     * @param nombre Nombre de la categoría a buscar
     * @param categoriaId ID de la categoría que se excluirá de la búsqueda
     * @return true si existe otra categoría con el mismo nombre (distinta a la ID proporcionada),
     *         false en caso contrario
     */
    boolean existsByNombreAndCategoriaIdNot(String nombre, Integer categoriaId);

}



