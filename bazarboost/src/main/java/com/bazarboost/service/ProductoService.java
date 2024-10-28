package com.bazarboost.service;

import com.bazarboost.dto.ProductoVendedorDTO;
import com.bazarboost.model.Categoria;
import com.bazarboost.model.Descuento;
import com.bazarboost.model.Producto;
import com.bazarboost.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoService {

    /**
     * Obtiene una página de productos que coinciden con los filtros especificados.
     *
     * @param keyword   Palabra clave para buscar en el nombre del producto;
     *                  si es null o vacío, se omite este filtro.
     * @param categoria Nombre de la categoría para filtrar productos;
     *                  si es null, se omite este filtro.
     * @param orden     Orden de los resultados por precio ("asc" para ascendente,
     *                  "desc" para descendente); si es null, no se aplica ordenamiento.
     * @param pageable  Parámetros de paginación y tamaño de página.
     * @return          Página de productos que cumplen con los filtros y criterios de orden.
     */
    Page<Producto> buscarProductosConFiltros(String keyword, String categoria, String orden, Pageable pageable);

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

}
