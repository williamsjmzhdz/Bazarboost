package com.bazarboost.system.service.impl;


import com.bazarboost.shared.exception.*;
import com.bazarboost.system.service.ProductoService;
import com.bazarboost.system.service.ReseniaService;
import com.bazarboost.system.dto.*;
import com.bazarboost.system.model.Producto;
import com.bazarboost.system.model.Resenia;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ReseniaRepository reseniaRepository;

    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @Autowired
    private ReseniaService reseniaService;

    @Autowired
    private ModelMapper modelMapper;

    private static final int PAGE_SIZE = 9;

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerProductoPorId(Integer productoId, Integer usuarioId) {
        log.debug("Obteniendo producto {} para el usuario {}.", productoId, usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        Producto producto = obtenerProducto(productoId);
        boolean esProductoPropio = checarSiEsProductoPropio(producto, usuario.getUsuarioId());
        if (!esProductoPropio) {
            log.debug("El producto {} no le pertenece al usuario {}.", productoId, usuarioId);
            throw new AccesoDenegadoException("El producto no te pertenece.");
        }
        return producto;
    }

    @Override
    @Transactional
    public Producto eliminarProductoPorId(Integer productoId, Integer usuarioId) {
        log.debug("Eliminando el producto {} para el usuario {}.", productoId, usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        Producto producto = obtenerProducto(productoId);
        boolean esProductoPropio = checarSiEsProductoPropio(producto, usuario.getUsuarioId());
        if (!esProductoPropio) {
            log.debug("El producto {} no le pertenece al usuario {}.", usuarioId);
            throw new AccesoDenegadoException("El producto que intentas eliminar no te pertenece.");
        }
        productoRepository.delete(producto);
        log.debug("Producto {} eliminado con éxito.", productoId);
        return producto;
    }

    @Override
    @Transactional
    public void guardarProducto(Producto producto, Integer vendedorId) {
        log.debug("Guardando producto '{}' para el usuario {}.", producto.getNombre(), vendedorId);
        productoRepository.save(producto);
        log.debug("Producto {} guardado con éxito.", producto.getNombre());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductosPaginadosDTO buscarProductosConFiltros(String keyword, String categoria, String orden, int page, Integer usuarioId) {
        log.debug("Buscando productos con filtros: keyword = {}, categoria = {}, ordern = {}, página = {}. Para el usuario {}.",
                keyword, categoria, orden, page, usuarioId);

        validarParametrosBusqueda(categoria, orden, usuarioId);

        log.debug("Buscando productos con filtros.");
        List<Producto> productosFiltrados = productoRepository.buscarProductosConFiltros(keyword, categoria);
        List<ProductoListadoDTO> productosListadoDTO = mapearYOrdenarProductos(productosFiltrados, orden, usuarioId);
        List<ProductoListadoDTO> productosPaginados = paginarProductos(productosListadoDTO, page);

        return construirRespuestaPaginada(productosPaginados, page, productosListadoDTO.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoVendedorDTO> obtenerProductosPorVendedor(Integer vendedorId) {
        log.debug("Obteniendo los productos del usuario {}.", vendedorId);
        Usuario vendedor = obtenerUsuario(vendedorId);
        return productoRepository.findByUsuario(vendedor).stream()
                .map(this::convertirAProductoVendedorDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDetalladoDTO obtenerProductoDetalle(Integer productoId, Integer usuarioId, Pageable pageable) {
        log.debug("Obteniendo el detalle del producto {} para el usuario {}.", productoId, usuarioId);
        Producto producto = obtenerProducto(productoId);
        Resenia miResenia = reseniaRepository.findByProductoIdAndUsuarioId(productoId, usuarioId).orElse(null);
        Page<Resenia> otrasResenias = reseniaRepository.findByProductoIdAndUsuarioIdNot(productoId, usuarioId, pageable);

        return construirProductoDetallado(producto, miResenia, otrasResenias, usuarioId);
    }

    private <T extends PaginatedResultDTO> void agregarInformacionPaginacion(T dto, Page<?> page) {
        dto.setPaginaActual(page.getNumber());
        dto.setTotalPaginas(page.getTotalPages());
        dto.setTotalElementos(page.getTotalElements());
    }

    private <T extends PaginatedResultDTO> void agregarInformacionPaginacion(T dto, int paginaActual, int totalElementos) {
        log.debug("Agregando información de paginación a la respuesta.");
        dto.setPaginaActual(paginaActual);
        dto.setTotalPaginas(calcularTotalPaginas(totalElementos));
        dto.setTotalElementos(totalElementos);
    }

    private void validarParametrosBusqueda(String categoria, String orden, Integer usuarioId) {
        log.debug("Validando parámetros de búsqueda: categoria = {}, orden = {}. Para el usuario {}.", categoria, orden, usuarioId);
        validarCategoria(categoria);
        validarOrden(orden);
        validarUsuario(usuarioId);
    }

    private void validarCategoria(String categoria) {
        if (categoria != null && !categoria.isEmpty() && !categoriaRepository.existsByNombre(categoria)) {
            log.debug("La categoria '{}' no fue encontrada.", categoria);
            throw new CategoriaNoEncontradaException("La categoría '" + categoria + "' no fue encontrada.");
        }
    }

    private void validarOrden(String orden) {
        if (orden != null && !orden.equalsIgnoreCase("ASC") && !orden.equalsIgnoreCase("DESC")) {
            log.debug("El parámetro de ordenamiento '{}' no es válido.", orden);
            throw new OrdenNoValidoException("El parámetro de orden solo puede ser 'ASC' o 'DESC'.");
        }
    }

    private void validarUsuario(Integer usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            log.debug("El usuario {} no fue encontrado.", usuarioId);
            throw new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado.");
        }
    }

    private Producto obtenerProducto(Integer productoId) {
        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        if (productoOptional.isEmpty()) {
            log.debug("El producto {} no se encontró.", productoId);
            throw new ProductoNoEncontradoException("Producto con ID " + productoId + " no encontrado");
        }
        return productoOptional.get();
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isEmpty()) {
            log.debug("El usuario {} no fue encontrado.", usuarioId);
            throw new UsuarioNoEncontradoException("No se encontró información de su usuario. Inicie sesión nuevamente e inténtelo de nuevo.");
        }
        return usuarioOptional.get();
    }

    private ProductoVendedorDTO convertirAProductoVendedorDTO(Producto producto) {
        log.debug("Mapeando producto {} a ProductoVendedorDTO.", producto.getProductoId());
        ProductoVendedorDTO dto = modelMapper.map(producto, ProductoVendedorDTO.class);
        agregarInformacionDescuento(dto, producto);
        return dto;
    }

    private void agregarInformacionDescuento(ProductoVendedorDTO dto, Producto producto) {
        log.debug("Agregando información de descuento para el producto {}.", producto.getProductoId());
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
        log.debug("Mapeando el producto {} a ProductoDetalladoDTO.", producto.getProductoId());
        ProductoDetalladoDTO dto = modelMapper.map(producto, ProductoDetalladoDTO.class);

        configurarInformacionBasica(dto, producto, usuarioId);
        agregarInformacionDescuentoDetallado(dto, producto);
        agregarResenias(dto, miResenia, otrasResenias);
        agregarCalificacionPromedio(dto, producto.getProductoId());
        agregarInformacionPaginacion(dto, otrasResenias);

        return dto;
    }

    private void configurarInformacionBasica(ProductoDetalladoDTO dto, Producto producto, Integer usuarioId) {
        log.debug("Configurando información básica del producto {} en ProductoDetalladoDTO.", producto.getProductoId());
        dto.setProductoId(producto.getProductoId());
        dto.setNombreCategoria(producto.getCategoria().getNombre());
        dto.setEsProductoPropio(checarSiEsProductoPropio(producto, usuarioId));
        dto.setEstaEnCarrito(checarSiEstaEnCarrito(producto, usuarioId));
    }

    private void agregarInformacionDescuentoDetallado(ProductoDetalladoDTO dto, Producto producto) {
        log.debug("Agregando información de descuento del producto {} a ProductoDetalladoDTO.", producto.getProductoId());
        if (producto.getDescuento() != null) {
            dto.setDescuento(modelMapper.map(producto.getDescuento(), DescuentoDTO.class));
            dto.setPrecioConDescuento(calcularPrecioConDescuento(producto));
        }
    }

    private void agregarResenias(ProductoDetalladoDTO dto, Resenia miResenia, Page<Resenia> otrasResenias) {
        log.debug("Agregando reseñas del producto {} a ProductoDetalladoDTO.", dto.getProductoId());

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
        log.debug("Agregando calificación promedio del producto {} a ProductoDetalladoDTO.", productoId);
        Double promedioCalificacion = reseniaRepository.obtenerCalificacionPromedio(productoId);
        dto.setCalificacionPromedio(promedioCalificacion != null ?
                BigDecimal.valueOf(promedioCalificacion).setScale(1, RoundingMode.HALF_UP) :
                BigDecimal.ZERO);
    }

    private List<ProductoListadoDTO> mapearYOrdenarProductos(List<Producto> productos, String orden, Integer usuarioId) {
        return productos.stream()
                .map(producto -> mapearAProductoListadoDTO(producto, usuarioId))
                .sorted((p1, p2) -> ordenarProductos(p1, p2, orden))
                .collect(Collectors.toList());
    }

    private int ordenarProductos(ProductoListadoDTO p1, ProductoListadoDTO p2, String orden) {
        log.debug("Ordenando productos. {}", orden != null ? (orden.equalsIgnoreCase("asc") ? "Orden ascendente" : "Orden descendente") : "");
        if ("asc".equalsIgnoreCase(orden)) {
            return p1.getPrecioFinalConDescuento().compareTo(p2.getPrecioFinalConDescuento());
        } else if ("desc".equalsIgnoreCase(orden)) {
            return p2.getPrecioFinalConDescuento().compareTo(p1.getPrecioFinalConDescuento());
        }
        return p2.getProductoId().compareTo(p1.getProductoId());
    }

    private ProductoListadoDTO mapearAProductoListadoDTO(Producto producto, Integer usuarioId) {
        log.debug("Mapenado Producto {} a ProductoListadoDTO para el usuario {}.", producto.getProductoId(), usuarioId);
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
        log.debug("Paginando productos.");
        int start = Math.min(page * PAGE_SIZE, productos.size());
        int end = Math.min(start + PAGE_SIZE, productos.size());
        return productos.subList(start, end);
    }

    private ProductosPaginadosDTO construirRespuestaPaginada(List<ProductoListadoDTO> productosPaginados,
                                                             int page, int totalProductos) {
        log.debug("Construyendo respuesta paginada. Página = {}, Total de productos = {}.", page, totalProductos);
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
        log.debug("Calculando el total de páginas.");
        return (int) Math.ceil((double) totalProductos / PAGE_SIZE);
    }

    private boolean checarSiEstaEnCarrito(Producto producto, Integer usuarioId) {
        return productoCarritoRepository.existsByProductoProductoIdAndUsuarioUsuarioId(
                producto.getProductoId(), usuarioId);
    }

    private boolean checarSiEsProductoPropio(Producto producto, Integer usuarioId) {
        log.debug("Revisando si el producto {} pertenece al usuario {}.", producto.getProductoId(), usuarioId);
        return producto.getUsuario().getUsuarioId().equals(usuarioId);
    }
}