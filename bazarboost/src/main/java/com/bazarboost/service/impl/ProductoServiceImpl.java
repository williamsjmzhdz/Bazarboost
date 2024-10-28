package com.bazarboost.service.impl;

import com.bazarboost.dto.ProductoVendedorDTO;
import com.bazarboost.model.Categoria;
import com.bazarboost.model.Descuento;
import com.bazarboost.model.Producto;
import com.bazarboost.model.Usuario;
import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.repository.CategoriaRepository;
import com.bazarboost.repository.DescuentoRepository;
import com.bazarboost.repository.ProductoRepository;
import com.bazarboost.repository.UsuarioRepository;
import com.bazarboost.service.ProductoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Page<Producto> buscarProductosConFiltros(String keyword, String categoria, String orden, Pageable pageable) {
        return productoRepository.buscarProductosConFiltros(keyword, categoria, orden, pageable);
    }

    @Override
    public List<ProductoVendedorDTO> obtenerProductosPorVendedor(Integer vendedorId) {
        Usuario usuario = usuarioRepository.findById(vendedorId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + vendedorId + " no encontrado"));

        List<Producto> productos = productoRepository.findByUsuario(usuario);

        return productos.stream()
                .map(this::convertirAProductoVendedorDTO)
                .toList();
    }

    @Override
    public void guardarProducto(Producto producto, Integer vendedorId) {
        productoRepository.save(producto);
    }

    private ProductoVendedorDTO convertirAProductoVendedorDTO(Producto producto) {
        ProductoVendedorDTO dto = modelMapper.map(producto, ProductoVendedorDTO.class);

        if (producto.getDescuento() != null) {
            int porcentaje = producto.getDescuento().getPorcentaje();
            dto.setDescuentoPorcentaje(porcentaje);
            dto.setDescuentoValor(
                    producto.getPrecio()
                            .multiply(BigDecimal.valueOf(porcentaje))
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
            );

        } else {
            dto.setDescuentoPorcentaje(0);
            dto.setDescuentoValor(BigDecimal.valueOf(0.0));
        }

        return dto;
    }

}
