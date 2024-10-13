package com.bazarboost.repository;

import com.bazarboost.model.entity.Categoria;
import com.bazarboost.model.entity.Producto;
import com.bazarboost.model.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoRepository extends PagingAndSortingRepository<Producto, Integer> {

    // Encontrar todos los productos de un usuario específico
    Page<Producto> findByUsuario(Usuario usuario, Pageable pageable);

    // Crear o editar un producto (método heredado de CrudRepository)
    // save() ya está heredado de CrudRepository.

    // Eliminar un producto de un usuario específico
    @Transactional
    void deleteByProductoIdAndUsuarioUsuarioId(Integer productoId, Integer usuarioId);

    // Verificar si un producto pertenece a un usuario específico antes de editarlo
    Optional<Producto> findByProductoIdAndUsuarioUsuarioId(Integer productoId, Integer usuarioId);

    // Encontrar todos los productos que tengan existencia disponible con paginación
    @Query("SELECT p FROM Producto p WHERE p.existencia > 0")
    Page<Producto> findAllWithExistence(Pageable pageable);

    // Encontrar productos por categoría que tengan existencia con paginación
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.existencia > 0")
    Page<Producto> searchByCategoryWithExistence(@Param("categoria") Categoria categoria, Pageable pageable);

    // Filtrar productos por categoría y ordenar por precio ascendente (productos con existencia) con paginación
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.existencia > 0 ORDER BY p.precio ASC")
    Page<Producto> searchByCategoryOrderByPriceAscendingWithStock(@Param("categoria") Categoria categoria, Pageable pageable);

    // Filtrar productos por categoría y ordenar por precio descendente (productos con existencia) con paginación
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.existencia > 0 ORDER BY p.precio DESC")
    Page<Producto> searchByCategoryOrderByPriceDescendingWithStock(@Param("categoria") Categoria categoria, Pageable pageable);

    // Ordenar todos los productos por precio ascendente que tengan existencia con paginación
    @Query("SELECT p FROM Producto p WHERE p.existencia > 0 ORDER BY p.precio ASC")
    Page<Producto> sortAllByPriceAscendingWithStock(Pageable pageable);

    // Ordenar todos los productos por precio descendente que tengan existencia con paginación
    @Query("SELECT p FROM Producto p WHERE p.existencia > 0 ORDER BY p.precio DESC")
    Page<Producto> sortAllByPriceDescendingWithStock(Pageable pageable);
}
