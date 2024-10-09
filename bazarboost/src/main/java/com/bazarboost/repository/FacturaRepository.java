package com.bazarboost.repository;

import com.bazarboost.model.entity.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface FacturaRepository extends CrudRepository<Factura, Integer> {
    // Crear una nueva factura (save() ya está heredado)

    // Obtener todas las facturas asociadas al usuario
    List<Factura> findByUsuarioUsuarioId(Integer usuarioId);

    // Ordenar facturas del usuario por monto total (de mayor a menor)
    @Query("SELECT f FROM Factura f WHERE f.usuario.usuarioId = :usuarioId ORDER BY f.total DESC")
    List<Factura> findFacturasByUsuarioIdOrderByTotalDesc(@Param("usuarioId") Integer usuarioId);

    // Ordenar facturas del usuario por monto total (de menor a mayor)
    @Query("SELECT f FROM Factura f WHERE f.usuario.usuarioId = :usuarioId ORDER BY f.total ASC")
    List<Factura> findFacturasByUsuarioIdOrderByTotalAsc(@Param("usuarioId") Integer usuarioId);

}
