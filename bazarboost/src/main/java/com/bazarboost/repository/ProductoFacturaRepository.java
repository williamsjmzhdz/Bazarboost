package com.bazarboost.repository;

import com.bazarboost.model.ProductoFactura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoFacturaRepository extends PagingAndSortingRepository<ProductoFactura, Integer> {

    // Encontrar ventas de productos de un vendedor, incluyendo nombre del cliente, con paginación
    @Query("SELECT p.nombre, pf.cantidad, pf.total, f.fecha, u.nombre, u.apellidoPaterno " +
            "FROM Producto p " +
            "JOIN ProductoFactura pf ON p.productoId = pf.producto.productoId " +
            "JOIN Factura f ON f.facturaId = pf.factura.facturaId " +
            "JOIN Usuario u ON f.usuario.usuarioId = u.usuarioId " +
            "WHERE p.usuario.usuarioId = :usuarioId")
    Page<Object[]> getSalesBySeller(@Param("usuarioId") Integer usuarioId, Pageable pageable);

    // Obtener todos los productos asociados a una factura específica
    List<ProductoFactura> findByFacturaFacturaId(Integer facturaId);
}


