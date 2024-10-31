package com.bazarboost.repository;

import com.bazarboost.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

}



