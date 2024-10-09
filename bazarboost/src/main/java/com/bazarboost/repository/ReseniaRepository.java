package com.bazarboost.repository;

import com.bazarboost.model.entity.Resenia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ReseniaRepository extends CrudRepository<Resenia, Integer> {

    // Obtener todas las reseñas de un producto
    @Query("SELECT r FROM Resenia r WHERE r.producto.productoId = :productoId")
    List<Resenia> findReseniasByProductoId(@Param("productoId") Integer productoId);

    // Guardar o editar una reseña (método heredado de CrudRepository)
    // save() ya está heredado de CrudRepository.

    // Eliminar una reseña del usuario actual para un producto específico
    @Transactional
    @Query("DELETE FROM Resenia r WHERE r.producto.productoId = :productoId AND r.usuario.usuarioId = :usuarioId")
    void deleteReseniaByProductoIdAndUsuarioId(@Param("productoId") Integer productoId, @Param("usuarioId") Integer usuarioId);
}

