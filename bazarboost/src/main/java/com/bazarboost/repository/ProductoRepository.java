package com.bazarboost.repository;

import com.bazarboost.model.Producto;
import com.bazarboost.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    /**
     * Busca productos con filtros opcionales de nombre, categoría, y orden por precio.
     * Devuelve solo productos con existencia disponible y aplica paginación.
     *
     * @param keyword   palabra clave para buscar en el nombre del producto (opcional).
     * @param categoria categoría del producto para filtrar (opcional).
     * @param orden     orden de precio, "asc" para ascendente o "desc" para descendente (opcional).
     * @param pageable  objeto de paginación para limitar los resultados y establecer el tamaño de página.
     * @return una página de productos que cumplen con los filtros y orden especificados.
     */
    @Query("SELECT p FROM Producto p " +
            "WHERE (:keyword IS NULL OR LOWER(p.nombre) LIKE %:keyword%) " +
            "AND (:categoria IS NULL OR p.categoria.nombre = :categoria) " +
            "AND p.existencia > 0 " +
            "ORDER BY " +
            "CASE WHEN :orden = 'asc' THEN p.precio END ASC, " +
            "CASE WHEN :orden = 'desc' THEN p.precio END DESC")
    Page<Producto> buscarProductosConFiltros(
            @Param("keyword") String keyword,
            @Param("categoria") String categoria,
            @Param("orden") String orden,
            Pageable pageable);

    /**
     * Encuentra todos los productos asociados a un usuario específico.
     *
     * @param usuario Usuario cuyos productos se desean obtener.
     * @return        Lista de productos asociados al usuario.
     */
    List<Producto> findByUsuario(Usuario usuario);

}
