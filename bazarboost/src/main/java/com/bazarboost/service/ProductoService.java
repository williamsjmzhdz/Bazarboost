package com.bazarboost.service;

import com.bazarboost.dto.ProductoDetalladoDTO;
import com.bazarboost.dto.ProductoVendedorDTO;
import com.bazarboost.dto.ProductosPaginadosDTO;
import com.bazarboost.model.Producto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoService {

    /**
     * Obtiene todos los productos asociados a un vendedor específico.
     *
     * @param vendedorId ID del vendedor cuyos productos queremos obtener.
     * @return           Lista de productos asociados al vendedor.
     */
    List<ProductoVendedorDTO> obtenerProductosPorVendedor(Integer vendedorId);

    /**
     * Guarda un producto en la base de datos.
     *
     * Este método persiste el objeto Producto recibido, ya sea creando un nuevo registro
     * o actualizando uno existente, dependiendo de si el producto tiene un ID ya asignado.
     *
     * @param producto Objeto Producto que contiene la información a guardar en la base de datos.
     */
    void guardarProducto(Producto producto, Integer vendedorId);

    /**
     * Obtiene el detalle de un producto en la base de datos, incluyendo la información de la categoría y las reseñas.
     *
     * Este método recupera el detalle completo de un producto, incluyendo su categoría y reseñas. La reseña del usuario
     * actual (si existe) se recupera por separado, mientras que las reseñas de otros usuarios se obtienen de forma
     * paginada según el `Pageable` especificado.
     *
     * @param productoId El ID del producto en la base de datos.
     * @param usuarioId El ID del usuario que consulta el detalle del producto.
     * @param pageable  El objeto Pageable que especifica la página actual y el tamaño para la paginación de las reseñas de otros usuarios.
     * @return Un objeto ProductoDetalleDTO que contiene toda la información detallada del producto.
     */
    ProductoDetalladoDTO obtenerProductoDetalle(Integer productoId, Integer usuarioId, Pageable pageable);

    /**
     * Obtiene una página de productos que coinciden con los filtros especificados.
     *
     * @param keyword   Palabra clave para buscar en el nombre del producto;
     *                  si es null o vacío, se omite este filtro.
     * @param categoria Nombre de la categoría para filtrar productos;
     *                  si es null, se omite este filtro.
     * @param orden     Orden de los resultados por precio ("asc" para ascendente,
     *                  "desc" para descendente); si es null, no se aplica ordenamiento.
     * @param page      Número de página.
     * @param usuarioId ID del usuario actual para verificar si el producto está en su carrito.
     * @return          Página de productos que cumplen con los filtros y criterios de orden.
     */
    ProductosPaginadosDTO buscarProductosConFiltros(String keyword, String categoria, String orden, int page, Integer usuarioId);

}
