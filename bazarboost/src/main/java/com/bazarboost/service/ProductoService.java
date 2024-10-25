package com.bazarboost.service;

import com.bazarboost.model.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductoService {

    // Método para obtener productos con paginación
    Page<Producto> obtenerProductos(Pageable pageable);

    // Método para buscar productos por nombre
    List<Producto> buscarProductosPorNombre(String keyword);

}
