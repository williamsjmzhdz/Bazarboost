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
     * Consulta para buscar productos con filtros opcionales de palabra clave y categoría,
     * y una restricción de existencia disponible.
     * No se aplican ordenamiento ni paginación en esta consulta, ya que se gestionan en el servicio.
     *
     * - Filtra por palabra clave en el nombre del producto si se proporciona (`:keyword`).
     *   La búsqueda es insensible a mayúsculas y minúsculas.
     * - Filtra por categoría específica si se proporciona (`:categoria`).
     * - Solo incluye productos con existencia mayor a cero (`p.existencia > 0`).
     *
     * @param keyword   Palabra clave para buscar en el nombre del producto (opcional).
     * @param categoria Nombre de la categoría del producto para filtrar (opcional).
     * @return          Lista completa de productos que cumplen con los filtros especificados.
     */
    @Query("SELECT p FROM Producto p " +
            "WHERE (:keyword IS NULL OR LOWER(p.nombre) LIKE %:keyword%) " +
            "AND (:categoria IS NULL OR p.categoria.nombre = :categoria) " +
            "AND p.existencia > 0")
    List<Producto> buscarProductosConFiltros(
            @Param("keyword") String keyword,
            @Param("categoria") String categoria);

    /**
     * Encuentra todos los productos asociados a un usuario específico.
     *
     * @param usuario Usuario cuyos productos se desean obtener.
     * @return        Lista de productos asociados al usuario.
     */
    List<Producto> findByUsuario(Usuario usuario);

    /**
     * Obtiene el nombre y la cantidad de stock disponible para un producto específico.
     *
     * @param productoId ID del producto a consultar.
     * @return           Un arreglo de objetos con el nombre y la existencia actual del producto.
     */
    @Query("SELECT p.nombre, p.existencia FROM Producto p WHERE p.productoId = :productoId")
    Object[] obtenerNombreYStockActual(@Param("productoId") Integer productoId);

}


