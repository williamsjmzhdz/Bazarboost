package com.bazarboost.repository;

import com.bazarboost.model.entity.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 */
public interface FacturaRepository extends PagingAndSortingRepository<Factura, Integer>, CrudRepository<Factura, Integer> {

    // Crear una nueva factura (save() ya está heredado)

    // Obtener todas las facturas asociadas al usuario con paginación
    Page<Factura> findByUsuarioUsuarioId(Integer usuarioId, Pageable pageable);

    // Ordenar facturas del usuario por monto total (de mayor a menor) con paginación
    @Query("SELECT f FROM Factura f WHERE f.usuario.usuarioId = :usuarioId ORDER BY f.total DESC")
    Page<Factura> findFacturasByUsuarioIdOrderByTotalDesc(@Param("usuarioId") Integer usuarioId, Pageable pageable);

    // Ordenar facturas del usuario por monto total (de menor a mayor) con paginación
    @Query("SELECT f FROM Factura f WHERE f.usuario.usuarioId = :usuarioId ORDER BY f.total ASC")
    Page<Factura> findFacturasByUsuarioIdOrderByTotalAsc(@Param("usuarioId") Integer usuarioId, Pageable pageable);
}
