package com.bazarboost.service.impl;

import com.bazarboost.dto.*;
import com.bazarboost.exception.CategoriaNoEncontradaException;
import com.bazarboost.exception.OrdenNoValidoException;
import com.bazarboost.exception.ProductoNoEncontradoException;
import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.model.*;
import com.bazarboost.repository.*;
import com.bazarboost.service.ProductoService;
import com.bazarboost.service.ReseniaService;
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
    private ProductoCarritoRepository productoCarritoRepository;

    @Autowired
    private ReseniaService reseniaService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductosPaginadosDTO buscarProductosConFiltros(String keyword, String categoria, String orden, int page, Integer usuarioId) {

        // Validar existencia de la categoría si se proporciona
        if (categoria != null && !categoria.isEmpty()) {
            boolean categoriaExiste = categoriaRepository.existsByNombre(categoria);
            if (!categoriaExiste) {
                throw new CategoriaNoEncontradaException("La categoría '" + categoria + "' no fue encontrada.");
            }
        }

        // Verificar si el usuario existe antes de proceder con la operación
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado.");
        }

        // Validar que el valor de "orden" sea "ASC", "DESC" o NULL
        if (orden != null && !orden.equalsIgnoreCase("ASC") && !orden.equalsIgnoreCase("DESC")) {
            throw new OrdenNoValidoException("El parámetro de orden solo puede ser 'ASC' o 'DESC'.");
        }

        // Recuperar todos los productos que cumplen los filtros sin paginar
        List<Producto> productosFiltrados = productoRepository.buscarProductosConFiltros(keyword, categoria);

        // Mapear a DTO y calcular el precio con descuento
        List<ProductoListadoDTO> productosListadoDTO = productosFiltrados.stream()
                .map(producto -> mapearAProductoListadoDTO(producto, usuarioId))
                .sorted((p1, p2) -> {
                    if ("asc".equalsIgnoreCase(orden)) {
                        return p1.getPrecioFinalConDescuento().compareTo(p2.getPrecioFinalConDescuento());
                    } else if ("desc".equalsIgnoreCase(orden)) {
                        return p2.getPrecioFinalConDescuento().compareTo(p1.getPrecioFinalConDescuento());
                    } else {
                        // Ordenamiento por ID descendente si `orden` es NULL
                        return p2.getProductoId().compareTo(p1.getProductoId());
                    }
                })
                .collect(Collectors.toList());

        // Aplicar la paginación después del ordenamiento
        int pageSize = 9; // Número de elementos por página
        int start = Math.min(page * pageSize, productosListadoDTO.size());
        int end = Math.min(start + pageSize, productosListadoDTO.size());
        List<ProductoListadoDTO> productosPaginadosOrdenados = productosListadoDTO.subList(start, end);

        // Retornar el DTO de paginación con los productos mapeados y la información de paginación
        return new ProductosPaginadosDTO(
                productosPaginadosOrdenados,
                page,
                (int) Math.ceil((double) productosListadoDTO.size() / pageSize),
                productosListadoDTO.size()
        );
    }



    private ProductoListadoDTO mapearAProductoListadoDTO(Producto producto, Integer usuarioId) {
        ProductoListadoDTO dto = new ProductoListadoDTO();
        dto.setProductoId(producto.getProductoId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setPorcentajeDescuento(producto.getDescuento() != null ? producto.getDescuento().getPorcentaje() : null);
        dto.setNombreDescuento(producto.getDescuento() != null ? producto.getDescuento().getNombre() : null);
        dto.setPrecioFinalConDescuento(calcularPrecioConDescuento(producto));
        dto.setCalificacionPromedio(reseniaService.calcularCalificacionPromedio(producto));
        dto.setEstaEnCarrito(checarSiEstaEnCarrito(producto, usuarioId));
        dto.setEsProductoPropio(checarSiEsProductoPropio(producto, usuarioId));
        return dto;
    }

    private BigDecimal calcularPrecioConDescuento(Producto producto) {
        if (producto.getDescuento() != null) {
            BigDecimal descuento = producto.getPrecio().multiply(
                    BigDecimal.valueOf(producto.getDescuento().getPorcentaje()).divide(BigDecimal.valueOf(100))
            );
            return producto.getPrecio().subtract(descuento);
        }
        return producto.getPrecio();
    }

    private boolean checarSiEstaEnCarrito(Producto producto, Integer usuarioId) {
        return productoCarritoRepository.existsByProductoProductoIdAndUsuarioUsuarioId(producto.getProductoId(), usuarioId);
    }

    private boolean checarSiEsProductoPropio(Producto producto, Integer usuarioId) {
        return producto.getUsuario().getUsuarioId().equals(usuarioId);
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
        Resenia miResenia = reseniaRepository.findByProductoIdAndUsuarioId(productoId, usuarioId).orElse(null);
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
        modelMapper.map(producto, productoDetalladoDTO);
        productoDetalladoDTO.setNombreCategoria(categoria.getNombre());

        if (descuento != null) {
            DescuentoDTO descuentoDTO = modelMapper.map(descuento, DescuentoDTO.class);
            productoDetalladoDTO.setDescuento(descuentoDTO);
        }

        if (producto.getDescuento() != null) {
            BigDecimal precioConDescuento = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(100 - producto.getDescuento().getPorcentaje()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            productoDetalladoDTO.setPrecioConDescuento(precioConDescuento);
        }

        if (miResenia != null) {
            ReseniaDTO miReseniaDTO = modelMapper.map(miResenia, ReseniaDTO.class);
            UsuarioReseniaDTO usuarioDTO = modelMapper.map(miResenia.getUsuario(), UsuarioReseniaDTO.class);
            miReseniaDTO.setUsuario(usuarioDTO);
            productoDetalladoDTO.setMiResenia(miReseniaDTO);
        }

        if (otrasResenias != null && !otrasResenias.isEmpty()) {
            List<ReseniaDTO> reseniasDTO = otrasResenias.getContent().stream()
                    .map(resenia -> {
                        ReseniaDTO reseniaDTO = modelMapper.map(resenia, ReseniaDTO.class);
                        UsuarioReseniaDTO usuarioDTO = modelMapper.map(resenia.getUsuario(), UsuarioReseniaDTO.class);
                        reseniaDTO.setUsuario(usuarioDTO);
                        return reseniaDTO;
                    })
                    .collect(Collectors.toList());

            productoDetalladoDTO.setReseniasAdicionales(reseniasDTO);
        }

        Double promedioCalificacion = reseniaRepository.obtenerCalificacionPromedio(producto.getProductoId());
        if (promedioCalificacion != null) {
            productoDetalladoDTO.setCalificacionPromedio(
                    BigDecimal.valueOf(promedioCalificacion).setScale(1, RoundingMode.HALF_UP)
            );
        } else {
            productoDetalladoDTO.setCalificacionPromedio(BigDecimal.ZERO);
        }

        return productoDetalladoDTO;
    }
}
