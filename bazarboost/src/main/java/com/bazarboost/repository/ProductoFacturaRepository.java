package com.bazarboost.repository;

import com.bazarboost.model.entity.ProductoFactura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoFacturaRepository extends CrudRepository<ProductoFactura, Integer> {

    // Encontrar ventas de productos de un vendedor, incluyendo nombre del cliente
    @Query("SELECT p.nombre, pf.cantidad, pf.total, f.fecha, u.nombre, u.apellido_paterno " +
            "FROM Producto p " +
            "JOIN ProductosFacturas pf ON p.productoId = pf.producto.productoId " +
            "JOIN Factura f ON f.facturaId = pf.factura.facturaId " +
            "JOIN Usuario u ON f.usuario.usuarioId = u.usuarioId " +
            "WHERE p.usuario.usuarioId = :usuarioId")
    List<Object[]> getSalesBySeller(@Param("usuarioId") Integer usuarioId);

    // Obtener todos los productos asociados a una factura específica
    List<ProductoFactura> findByFacturaFacturaId(Integer facturaId);

    // Crear registros de productos asociados a la factura (saveAll() ya está heredado)
}


