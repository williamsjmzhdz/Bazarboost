package com.bazarboost.repository;

import com.bazarboost.model.entity.Categoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface CategoriaRepository extends CrudRepository<Categoria, Integer> {

    // Encontrar todas las categorías
    List<Categoria> findAll();

    // Buscar una categoría por su ID
    Optional<Categoria> findById(Integer categoriaId);

    // Eliminar una categoría por su ID
    @Transactional
    void deleteById(Integer categoriaId);

    // Crear o editar una categoría (método heredado de CrudRepository)
    // save() ya está heredado de CrudRepository.
}


