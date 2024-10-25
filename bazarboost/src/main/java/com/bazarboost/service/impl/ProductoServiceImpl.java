package com.bazarboost.service.impl;

import com.bazarboost.model.entity.Producto;
import com.bazarboost.repository.ProductoRepository;
import com.bazarboost.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Page<Producto> obtenerProductos(Pageable pageable) {
        return productoRepository.findAllWithExistence(pageable);
    }

    @Override
    public List<Producto> buscarProductosPorNombre(String keyword) {
        return productoRepository.findByNombreContainingIgnoreCase(keyword);
    }
}
