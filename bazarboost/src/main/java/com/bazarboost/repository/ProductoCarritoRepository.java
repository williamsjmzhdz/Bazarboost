package com.bazarboost.repository;

import com.bazarboost.model.ProductoCarrito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoCarritoRepository extends CrudRepository<ProductoCarrito, Integer> {

    // Verificar si un producto está en el carrito del usuario
    @Query("SELECT CASE WHEN (COUNT(pc) > 0) THEN true ELSE false END " +
            "FROM ProductoCarrito pc WHERE pc.producto.productoId = :productoId AND pc.usuario.usuarioId = :usuarioId")
    boolean existsInCarrito(@Param("productoId") Integer productoId, @Param("usuarioId") Integer usuarioId);

    // Agregar un producto al carrito (método heredado de CrudRepository)
    // save() ya está heredado de CrudRepository.

    // Eliminar un producto del carrito del usuario
    @Transactional
    @Query("DELETE FROM ProductoCarrito pc WHERE pc.producto.productoId = :productoId AND pc.usuario.usuarioId = :usuarioId")
    void deleteByProductoIdAndUsuarioId(@Param("productoId") Integer productoId, @Param("usuarioId") Integer usuarioId);

    // Obtener todos los productos en el carrito del usuario
    @Query("SELECT pc, p, (pc.cantidad * p.precio) AS total " +
            "FROM ProductoCarrito pc JOIN pc.producto p " +
            "WHERE pc.usuario.usuarioId = :usuarioId")
    List<Object[]> findProductosCarritoByUsuarioId(@Param("usuarioId") Integer usuarioId);

    // Actualizar la cantidad de un producto en el carrito
    @Transactional
    @Query("UPDATE ProductoCarrito pc SET pc.cantidad = :cantidad " +
            "WHERE pc.producto.productoId = :productoId AND pc.usuario.usuarioId = :usuarioId")
    void updateCantidadInCarrito(@Param("cantidad") Integer cantidad,
                                 @Param("productoId") Integer productoId,
                                 @Param("usuarioId") Integer usuarioId);
    @Transactional
    @Query("DELETE FROM ProductoCarrito pc WHERE pc.usuario.usuarioId = :usuarioId")
    void deleteProductsCartByUserId(@Param("usuarioId") Integer usuarioId);

}

