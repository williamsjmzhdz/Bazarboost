package com.bazarboost.repository;

import com.bazarboost.model.entity.Categoria;
import com.bazarboost.model.entity.Producto;
import com.bazarboost.model.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoRepository extends CrudRepository<Producto, Integer> {

    // Encontrar todos los productos de un usuario específico
    List<Producto> findByUsuario(Usuario usuario);

    // Crear o editar un producto (método heredado de CrudRepository)
    // save() ya está heredado de CrudRepository.

    // Eliminar un producto de un usuario específico
    @Transactional
    void deleteByProductoIdAndUsuarioUsuarioId(Integer productoId, Integer usuarioId);

    // Verificar si un producto pertenece a un usuario específico antes de editarlo
    Optional<Producto> findByIdAndUsuarioId(Integer productoId, Integer usuarioId);

    // Encontrar todos los productos que tengan existencia disponible
    @Query("SELECT p FROM Producto p WHERE p.existencia > 0")
    List<Producto> searchAllWithExistence();

    // Encontrar productos por categoría que tengan existencia
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.existencia > 0")
    List<Producto> searchByCategoryWithExistence(@Param("categoria") Categoria categoria);

    // Filtrar productos por categoría y ordenar por precio ascendente (productos con existencia)
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.existencia > 0 ORDER BY p.precio ASC")
    List<Producto> searchByCategoryOrderByPriceAscendingWithStock(@Param("categoria") Categoria categoria);

    // Filtrar productos por categoría y ordenar por precio descendente (productos con existencia)
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.existencia > 0 ORDER BY p.precio DESC")
    List<Producto> searchByCategoryOrderByPriceDescendingWithStock(@Param("categoria") Categoria categoria);

    // Ordenar todos los productos por precio ascendente que tengan existencia
    @Query("SELECT p FROM Producto p WHERE p.existencia > 0 ORDER BY p.precio ASC")
    List<Producto> sortAllByPriceAscendingWithStock();

    // Ordenar todos los productos por precio descendente que tengan existencia
    @Query("SELECT p FROM Producto p WHERE p.existencia > 0 ORDER BY p.precio DESC")
    List<Producto> sortAllByPriceDescendingWithStock();

    // Buscar todos los productos con indicador si están en el carrito del usuario y con la calificación promedio
    @Query("SELECT p, " +
            "CASE WHEN (pc.usuario.usuarioId = :usuarioId) THEN true ELSE false END AS inCart, " +
            "(SELECT AVG(r.calificacion) FROM Resenia r WHERE r.producto.productoId = p.productoId) AS promedioCalificacion " +
            "FROM Producto p " +
            "LEFT JOIN ProductosCarrito pc ON p.productoId = pc.producto.productoId AND pc.usuario.usuarioId = :usuarioId " +
            "WHERE p.existencia > 0")
    List<Object[]> findAllProductsWithCartIndicatorAndRating(@Param("usuarioId") Integer usuarioId);

    // Obtener detalles completos de un producto junto con la calificación promedio
    @Query("SELECT p, (SELECT AVG(r.calificacion) FROM Resenia r WHERE r.producto.productoId = p.productoId) AS promedioCalificacion " +
            "FROM Producto p WHERE p.productoId = :productoId")
    Object[] findProductoWithAverageRating(@Param("productoId") Integer productoId);

}






