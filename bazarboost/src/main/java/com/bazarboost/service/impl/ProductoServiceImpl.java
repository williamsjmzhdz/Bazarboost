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
    private static final int PAGE_SIZE = 9;

    // Operaciones principales CRUD y búsqueda

    @Override
    @Transactional
    public void guardarProducto(Producto producto, Integer vendedorId) {
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductosPaginadosDTO buscarProductosConFiltros(String keyword, String categoria, String orden, int page, Integer usuarioId) {
        validarParametrosBusqueda(categoria, orden, usuarioId);

        List<Producto> productosFiltrados = productoRepository.buscarProductosConFiltros(keyword, categoria);
        List<ProductoListadoDTO> productosListadoDTO = mapearYOrdenarProductos(productosFiltrados, orden, usuarioId);
        List<ProductoListadoDTO> productosPaginados = paginarProductos(productosListadoDTO, page);

        return construirRespuestaPaginada(productosPaginados, page, productosListadoDTO.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoVendedorDTO> obtenerProductosPorVendedor(Integer vendedorId) {
        Usuario vendedor = obtenerUsuario(vendedorId);
        return productoRepository.findByUsuario(vendedor).stream()
                .map(this::convertirAProductoVendedorDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDetalladoDTO obtenerProductoDetalle(Integer productoId, Integer usuarioId, Pageable pageable) {
        Producto producto = obtenerProducto(productoId);
        Resenia miResenia = reseniaRepository.findByProductoIdAndUsuarioId(productoId, usuarioId).orElse(null);
        Page<Resenia> otrasResenias = reseniaRepository.findByProductoIdAndUsuarioIdNot(productoId, usuarioId, pageable);

        return construirProductoDetallado(producto, miResenia, otrasResenias, usuarioId);
    }

    /**
     * Agrega información de paginación a cualquier DTO que implemente PaginatedResult
     */
    private <T extends PaginatedResultDTO> void agregarInformacionPaginacion(T dto, Page<?> page) {
        dto.setPaginaActual(page.getNumber());
        dto.setTotalPaginas(page.getTotalPages());
        dto.setTotalElementos(page.getTotalElements());
    }

    /**
     * Agrega información de paginación para listas paginadas manualmente
     */
    private <T extends PaginatedResultDTO> void agregarInformacionPaginacion(T dto, int paginaActual, int totalElementos) {
        dto.setPaginaActual(paginaActual);
        dto.setTotalPaginas(calcularTotalPaginas(totalElementos));
        dto.setTotalElementos(totalElementos);
    }

    // Métodos de validación

    private void validarParametrosBusqueda(String categoria, String orden, Integer usuarioId) {
        validarCategoria(categoria);
        validarOrden(orden);
        validarUsuario(usuarioId);
    }

    private void validarCategoria(String categoria) {
        if (categoria != null && !categoria.isEmpty() && !categoriaRepository.existsByNombre(categoria)) {
            throw new CategoriaNoEncontradaException("La categoría '" + categoria + "' no fue encontrada.");
        }
    }

    private void validarOrden(String orden) {
        if (orden != null && !orden.equalsIgnoreCase("ASC") && !orden.equalsIgnoreCase("DESC")) {
            throw new OrdenNoValidoException("El parámetro de orden solo puede ser 'ASC' o 'DESC'.");
        }
    }

    private void validarUsuario(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado.");
        }
    }

    // Métodos de obtención de entidades

    private Producto obtenerProducto(Integer productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + productoId + " no encontrado"));
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
    }

    // Métodos de conversión y mapeo

    private ProductoVendedorDTO convertirAProductoVendedorDTO(Producto producto) {
        ProductoVendedorDTO dto = modelMapper.map(producto, ProductoVendedorDTO.class);
        agregarInformacionDescuento(dto, producto);
        return dto;
    }

    private void agregarInformacionDescuento(ProductoVendedorDTO dto, Producto producto) {
        if (producto.getDescuento() != null) {
            dto.setDescuentoPorcentaje(producto.getDescuento().getPorcentaje());
            dto.setDescuentoValor(calcularValorDescuento(producto));
        } else {
            dto.setDescuentoPorcentaje(0);
            dto.setDescuentoValor(BigDecimal.ZERO);
        }
    }

    private ProductoDetalladoDTO construirProductoDetallado(Producto producto, Resenia miResenia,
                                                            Page<Resenia> otrasResenias, Integer usuarioId) {
        ProductoDetalladoDTO dto = modelMapper.map(producto, ProductoDetalladoDTO.class);

        configurarInformacionBasica(dto, producto, usuarioId);
        agregarInformacionDescuentoDetallado(dto, producto);
        agregarResenias(dto, miResenia, otrasResenias);
        agregarCalificacionPromedio(dto, producto.getProductoId());
        agregarInformacionPaginacion(dto, otrasResenias); // Usa el método genérico

        return dto;
    }

    private void configurarInformacionBasica(ProductoDetalladoDTO dto, Producto producto, Integer usuarioId) {
        dto.setProductoId(producto.getProductoId());
        dto.setNombreCategoria(producto.getCategoria().getNombre());
        dto.setEsProductoPropio(checarSiEsProductoPropio(producto, usuarioId));
        dto.setEstaEnCarrito(checarSiEstaEnCarrito(producto, usuarioId));
    }

    private void agregarInformacionDescuentoDetallado(ProductoDetalladoDTO dto, Producto producto) {
        if (producto.getDescuento() != null) {
            dto.setDescuento(modelMapper.map(producto.getDescuento(), DescuentoDTO.class));
            dto.setPrecioConDescuento(calcularPrecioConDescuento(producto));
        }
    }

    private void agregarResenias(ProductoDetalladoDTO dto, Resenia miResenia, Page<Resenia> otrasResenias) {
        if (miResenia != null) {
            ReseniaDTO miReseniaDTO = modelMapper.map(miResenia, ReseniaDTO.class);
            miReseniaDTO.setUsuario(modelMapper.map(miResenia.getUsuario(), UsuarioReseniaDTO.class));
            dto.setMiResenia(miReseniaDTO);
        }

        if (otrasResenias != null && !otrasResenias.isEmpty()) {
            dto.setReseniasAdicionales(mapearResenias(otrasResenias));
        }
    }

    private List<ReseniaDTO> mapearResenias(Page<Resenia> resenias) {
        return resenias.getContent().stream()
                .map(resenia -> {
                    ReseniaDTO reseniaDTO = modelMapper.map(resenia, ReseniaDTO.class);
                    reseniaDTO.setUsuario(modelMapper.map(resenia.getUsuario(), UsuarioReseniaDTO.class));
                    return reseniaDTO;
                })
                .collect(Collectors.toList());
    }

    private void agregarCalificacionPromedio(ProductoDetalladoDTO dto, Integer productoId) {
        Double promedioCalificacion = reseniaRepository.obtenerCalificacionPromedio(productoId);
        dto.setCalificacionPromedio(promedioCalificacion != null ?
                BigDecimal.valueOf(promedioCalificacion).setScale(1, RoundingMode.HALF_UP) :
                BigDecimal.ZERO);
    }

    // Métodos utilitarios

    private List<ProductoListadoDTO> mapearYOrdenarProductos(List<Producto> productos, String orden, Integer usuarioId) {
        return productos.stream()
                .map(producto -> mapearAProductoListadoDTO(producto, usuarioId))
                .sorted((p1, p2) -> ordenarProductos(p1, p2, orden))
                .collect(Collectors.toList());
    }

    private int ordenarProductos(ProductoListadoDTO p1, ProductoListadoDTO p2, String orden) {
        if ("asc".equalsIgnoreCase(orden)) {
            return p1.getPrecioFinalConDescuento().compareTo(p2.getPrecioFinalConDescuento());
        } else if ("desc".equalsIgnoreCase(orden)) {
            return p2.getPrecioFinalConDescuento().compareTo(p1.getPrecioFinalConDescuento());
        }
        return p2.getProductoId().compareTo(p1.getProductoId());
    }

    private ProductoListadoDTO mapearAProductoListadoDTO(Producto producto, Integer usuarioId) {
        ProductoListadoDTO dto = modelMapper.map(producto, ProductoListadoDTO.class);
        configurarDescuentoListado(dto, producto);
        dto.setPrecioFinalConDescuento(calcularPrecioConDescuento(producto));
        dto.setCalificacionPromedio(reseniaService.calcularCalificacionPromedio(producto));
        dto.setEstaEnCarrito(checarSiEstaEnCarrito(producto, usuarioId));
        dto.setEsProductoPropio(checarSiEsProductoPropio(producto, usuarioId));
        return dto;
    }

    private void configurarDescuentoListado(ProductoListadoDTO dto, Producto producto) {
        if (producto.getDescuento() != null) {
            dto.setPorcentajeDescuento(producto.getDescuento().getPorcentaje());
            dto.setNombreDescuento(producto.getDescuento().getNombre());
        }
    }

    private BigDecimal calcularPrecioConDescuento(Producto producto) {
        if (producto.getDescuento() != null) {
            return producto.getPrecio().subtract(calcularValorDescuento(producto));
        }
        return producto.getPrecio();
    }

    private BigDecimal calcularValorDescuento(Producto producto) {
        return producto.getPrecio()
                .multiply(BigDecimal.valueOf(producto.getDescuento().getPorcentaje()))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    private List<ProductoListadoDTO> paginarProductos(List<ProductoListadoDTO> productos, int page) {
        int start = Math.min(page * PAGE_SIZE, productos.size());
        int end = Math.min(start + PAGE_SIZE, productos.size());
        return productos.subList(start, end);
    }

    private ProductosPaginadosDTO construirRespuestaPaginada(List<ProductoListadoDTO> productosPaginados,
                                                             int page, int totalProductos) {
        ProductosPaginadosDTO dto = new ProductosPaginadosDTO(
                productosPaginados,
                page,
                calcularTotalPaginas(totalProductos),
                totalProductos
        );
        agregarInformacionPaginacion(dto, page, totalProductos); // Usa el método genérico
        return dto;
    }

    private int calcularTotalPaginas(int totalProductos) {
        return (int) Math.ceil((double) totalProductos / PAGE_SIZE);
    }

    private boolean checarSiEstaEnCarrito(Producto producto, Integer usuarioId) {
        return productoCarritoRepository.existsByProductoProductoIdAndUsuarioUsuarioId(
                producto.getProductoId(), usuarioId);
    }

    private boolean checarSiEsProductoPropio(Producto producto, Integer usuarioId) {
        return producto.getUsuario().getUsuarioId().equals(usuarioId);
    }
}