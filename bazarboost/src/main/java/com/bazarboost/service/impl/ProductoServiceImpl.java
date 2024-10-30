package com.bazarboost.service.impl;

import com.bazarboost.dto.*;
import com.bazarboost.exception.ProductoNoEncontradoException;
import com.bazarboost.model.*;
import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.repository.*;
import com.bazarboost.service.ProductoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Alumno: Francisco Williams Jiménez Hernández
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
    private ReseniaRepository reseniaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<Producto> buscarProductosConFiltros(String keyword, String categoria, String orden, Pageable pageable) {

        Sort sort;
        if ("asc".equalsIgnoreCase(orden)) {
            sort = Sort.by("precio").ascending();
        } else if ("desc".equalsIgnoreCase(orden)) {
            sort = Sort.by("precio").descending();
        } else {
            sort = Sort.by("id").descending();
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return productoRepository.buscarProductosConFiltros(keyword, categoria, sortedPageable);
    }

    @Override
    public List<ProductoVendedorDTO> obtenerProductosPorVendedor(Integer vendedorId) {
        Usuario usuario = usuarioRepository.findById(vendedorId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + vendedorId + " no encontrado"));

        List<Producto> productos = productoRepository.findByUsuario(usuario);

        return productos.stream()
                .map(this::convertirAProductoVendedorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void guardarProducto(Producto producto, Integer vendedorId) {
        productoRepository.save(producto);
    }

    @Override
    public ProductoDetalladoDTO obtenerProductoDetalle(Integer productoId, Integer usuarioId, Pageable pageable) {

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + productoId + " no encontrado"));

        Categoria categoria = producto.getCategoria();

        Descuento descuento = producto.getDescuento();

        Resenia miResenia = reseniaRepository.findByProductoIdAndUsuarioId(productoId, usuarioId)
                .orElse(null);

        Page<Resenia> otrasResenias = reseniaRepository.findByProductoIdAndUsuarioIdNot(productoId, usuarioId, pageable);

        return convertirAProductoDetalladoDTO(producto, categoria, descuento, miResenia, otrasResenias);
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
            dto.setDescuentoValor(BigDecimal.ZERO);
        }

        return dto;
    }

    private ProductoDetalladoDTO convertirAProductoDetalladoDTO(Producto producto, Categoria categoria, Descuento descuento,
                                                                Resenia miResenia, Page<Resenia> otrasResenias) {
        ProductoDetalladoDTO productoDetalladoDTO = new ProductoDetalladoDTO();

        // Mapear propiedades básicas del producto
        modelMapper.map(producto, productoDetalladoDTO);

        // Establecer el nombre de la categoría
        productoDetalladoDTO.setNombreCategoria(categoria.getNombre());

        // Mapear descuento si existe
        if (descuento != null) {
            DescuentoDTO descuentoDTO = modelMapper.map(descuento, DescuentoDTO.class);
            productoDetalladoDTO.setDescuento(descuentoDTO);
        }

        // Calcular y establecer el precio con descuento
        if (producto.getDescuento() != null) {
            BigDecimal precioConDescuento = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(100 - producto.getDescuento().getPorcentaje()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            productoDetalladoDTO.setPrecioConDescuento(precioConDescuento);
        }

        // Mapear mi reseña si existe
        if (miResenia != null) {
            ReseniaDTO miReseniaDTO = modelMapper.map(miResenia, ReseniaDTO.class);

            // Mapear el usuario de la reseña
            UsuarioReseniaDTO usuarioDTO = modelMapper.map(miResenia.getUsuario(), UsuarioReseniaDTO.class);
            miReseniaDTO.setUsuario(usuarioDTO);

            productoDetalladoDTO.setMiResenia(miReseniaDTO);
        }

        // Mapear otras reseñas
        if (otrasResenias != null && !otrasResenias.isEmpty()) {
            List<ReseniaDTO> reseniasDTO = otrasResenias.getContent().stream()
                    .map(resenia -> {
                        ReseniaDTO reseniaDTO = modelMapper.map(resenia, ReseniaDTO.class);

                        // Mapear el usuario de cada reseña
                        UsuarioReseniaDTO usuarioDTO = modelMapper.map(resenia.getUsuario(), UsuarioReseniaDTO.class);
                        reseniaDTO.setUsuario(usuarioDTO);

                        return reseniaDTO;
                    })
                    .collect(Collectors.toList());

            productoDetalladoDTO.setReseniasAdicionales(reseniasDTO);
        }

        // Calcular y establecer la calificación promedio usando el método que retorna AVG
        Double promedioCalificacion = reseniaRepository.obtenerCalificacionPromedio(producto.getProductoId());

        if (promedioCalificacion != null) {
            BigDecimal calificacionPromedio = BigDecimal.valueOf(promedioCalificacion)
                    .setScale(1, RoundingMode.HALF_UP);
            productoDetalladoDTO.setCalificacionPromedio(calificacionPromedio);
        } else {
            productoDetalladoDTO.setCalificacionPromedio(BigDecimal.ZERO);
        }

        return productoDetalladoDTO;
    }

}

