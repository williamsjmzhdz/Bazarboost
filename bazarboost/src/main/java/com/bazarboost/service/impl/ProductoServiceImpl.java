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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio que maneja las operaciones relacionadas con los productos.
 */
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final DescuentoRepository descuentoRepository;
    private final ReseniaRepository reseniaRepository;
    private final ProductoCarritoRepository productoCarritoRepository;
    private final ReseniaService reseniaService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductosPaginadosDTO buscarProductosConFiltros(String keyword, String categoria, String orden, int page, Integer usuarioId) {
        validarCategoria(categoria);
        validarUsuario(usuarioId);
        validarOrden(orden);

        List<Producto> productosFiltrados = productoRepository.buscarProductosConFiltros(keyword, categoria);
        List<ProductoListadoDTO> productosListadoDTO = mapearYOrdenarProductos(productosFiltrados, orden, usuarioId);
        List<ProductoListadoDTO> productosPaginadosOrdenados = paginarProductos(productosListadoDTO, page);

        return new ProductosPaginadosDTO(
                productosPaginadosOrdenados,
                page,
                calcularTotalPaginas(productosListadoDTO.size()),
                productosListadoDTO.size()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoVendedorDTO> obtenerProductosPorVendedor(Integer vendedorId) {
        Usuario usuario = obtenerUsuario(vendedorId);
        List<Producto> productos = productoRepository.findByUsuario(usuario);
        return productos.stream()
                .map(this::convertirAProductoVendedorDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void guardarProducto(Producto producto, Integer vendedorId) {
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDetalladoDTO obtenerProductoDetalle(Integer productoId, Integer usuarioId, Pageable pageable) {
        Producto producto = obtenerProducto(productoId);
        Categoria categoria = producto.getCategoria();
        Descuento descuento = producto.getDescuento();
        Resenia miResenia = reseniaRepository.findByProductoIdAndUsuarioId(productoId, usuarioId).orElse(null);
        Page<Resenia> otrasResenias = reseniaRepository.findByProductoIdAndUsuarioIdNot(productoId, usuarioId, pageable);
        boolean esProductoPropio = checarSiEsProductoPropio(producto, usuarioId);

        return convertirAProductoDetalladoDTO(producto, categoria, descuento, miResenia, otrasResenias, esProductoPropio);
    }

    private void validarCategoria(String categoria) {
        if (categoria != null && !categoria.isEmpty()) {
            boolean categoriaExiste = categoriaRepository.existsByNombre(categoria);
            if (!categoriaExiste) {
                throw new CategoriaNoEncontradaException("La categoría '" + categoria + "' no fue encontrada.");
            }
        }
    }

    private void validarUsuario(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado.");
        }
    }

    private void validarOrden(String orden) {
        if (orden != null && !orden.equalsIgnoreCase("ASC") && !orden.equalsIgnoreCase("DESC")) {
            throw new OrdenNoValidoException("El parámetro de orden solo puede ser 'ASC' o 'DESC'.");
        }
    }

    private List<ProductoListadoDTO> mapearYOrdenarProductos(List<Producto> productos, String orden, Integer usuarioId) {
        return productos.stream()
                .map(producto -> mapearAProductoListadoDTO(producto, usuarioId))
                .sorted((p1, p2) -> {
                    if ("asc".equalsIgnoreCase(orden)) {
                        return p1.getPrecioFinalConDescuento().compareTo(p2.getPrecioFinalConDescuento());
                    } else if ("desc".equalsIgnoreCase(orden)) {
                        return p2.getPrecioFinalConDescuento().compareTo(p1.getPrecioFinalConDescuento());
                    } else {
                        return p2.getProductoId().compareTo(p1.getProductoId());
                    }
                })
                .collect(Collectors.toList());
    }

    private List<ProductoListadoDTO> paginarProductos(List<ProductoListadoDTO> productos, int page) {
        int pageSize = 9;
        int start = Math.min(page * pageSize, productos.size());
        int end = Math.min(start + pageSize, productos.size());
        return productos.subList(start, end);
    }

    private int calcularTotalPaginas(int totalProductos) {
        int pageSize = 9;
        return (int) Math.ceil((double) totalProductos / pageSize);
    }

    private ProductoListadoDTO mapearAProductoListadoDTO(Producto producto, Integer usuarioId) {
        ProductoListadoDTO dto = modelMapper.map(producto, ProductoListadoDTO.class);
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

    private ProductoVendedorDTO convertirAProductoVendedorDTO(Producto producto) {
        ProductoVendedorDTO dto = modelMapper.map(producto, ProductoVendedorDTO.class);
        dto.setDescuentoPorcentaje(producto.getDescuento() != null ? producto.getDescuento().getPorcentaje() : 0);
        dto.setDescuentoValor(producto.getDescuento() != null ?
                producto.getPrecio()
                        .multiply(BigDecimal.valueOf(producto.getDescuento().getPorcentaje()))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP) :
                BigDecimal.ZERO);
        return dto;
    }

    private ProductoDetalladoDTO convertirAProductoDetalladoDTO(Producto producto, Categoria categoria, Descuento descuento,
                                                                Resenia miResenia, Page<Resenia> otrasResenias, boolean esProductoPropio) {
        ProductoDetalladoDTO productoDetalladoDTO = modelMapper.map(producto, ProductoDetalladoDTO.class);
        productoDetalladoDTO.setNombreCategoria(categoria.getNombre());
        productoDetalladoDTO.setEsProductoPropio(esProductoPropio);

        if (descuento != null) {
            productoDetalladoDTO.setDescuento(modelMapper.map(descuento, DescuentoDTO.class));
            productoDetalladoDTO.setPrecioConDescuento(calcularPrecioConDescuento(producto));
        }

        if (miResenia != null) {
            ReseniaDTO miReseniaDTO = modelMapper.map(miResenia, ReseniaDTO.class);
            miReseniaDTO.setUsuario(modelMapper.map(miResenia.getUsuario(), UsuarioReseniaDTO.class));
            productoDetalladoDTO.setMiResenia(miReseniaDTO);
        }

        if (otrasResenias != null && !otrasResenias.isEmpty()) {
            List<ReseniaDTO> reseniasDTO = otrasResenias.getContent().stream()
                    .map(resenia -> {
                        ReseniaDTO reseniaDTO = modelMapper.map(resenia, ReseniaDTO.class);
                        reseniaDTO.setUsuario(modelMapper.map(resenia.getUsuario(), UsuarioReseniaDTO.class));
                        return reseniaDTO;
                    })
                    .collect(Collectors.toList());

            productoDetalladoDTO.setReseniasAdicionales(reseniasDTO);
        }

        Double promedioCalificacion = reseniaRepository.obtenerCalificacionPromedio(producto.getProductoId());
        productoDetalladoDTO.setCalificacionPromedio(promedioCalificacion != null ?
                BigDecimal.valueOf(promedioCalificacion).setScale(1, RoundingMode.HALF_UP) :
                BigDecimal.ZERO);

        return productoDetalladoDTO;
    }

    private Producto obtenerProducto(Integer productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + productoId + " no encontrado"));
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
    }
}