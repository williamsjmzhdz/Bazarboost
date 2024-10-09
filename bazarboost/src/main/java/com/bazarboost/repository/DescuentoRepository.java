package com.bazarboost.repository;

import com.bazarboost.model.entity.Descuento;
import com.bazarboost.model.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface DescuentoRepository extends CrudRepository<Descuento, Integer> {

    // Buscar todos los descuentos de un usuario
    List<Descuento> findByUsuario(Usuario usuario);

    // Buscar un descuento por su ID y por usuario
    Optional<Descuento> findByDescuentoIdAndUsuarioUsuarioId(Integer descuentoId, Integer usuarioId);

    // Crear o editar un descuento (método heredado de CrudRepository)
    // save() ya está heredado de CrudRepository.

    // Eliminar un descuento de un usuario específico
    @Transactional
    void deleteByDescuentoIdAndUsuarioUsuarioId(Integer descuentoId, Integer usuarioId);
}


