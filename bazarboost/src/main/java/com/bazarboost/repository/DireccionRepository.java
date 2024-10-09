package com.bazarboost.repository;

import com.bazarboost.model.entity.Direccion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface DireccionRepository extends CrudRepository<Direccion, Integer> {

    // Encontrar todas las direcciones de un usuario
    List<Direccion> findByUsuarioUsuarioId(Integer usuarioId);

    // Buscar una dirección por su ID y validar que pertenece al usuario
    Optional<Direccion> findByDireccionIdAndUsuarioUsuarioId(Integer direccionId, Integer usuarioId);

    // Eliminar una dirección de un usuario específico
    @Transactional
    void deleteByDireccionIdAndUsuarioUsuarioId(Integer direccionId, Integer usuarioId);

    // Crear o editar una dirección (método heredado de CrudRepository)
    // save() ya está heredado de CrudRepository.
}

