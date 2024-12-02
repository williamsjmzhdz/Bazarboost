package com.bazarboost.system.service.impl;

import com.bazarboost.shared.exception.*;
import com.bazarboost.system.repository.*;
import com.bazarboost.system.service.ProductoCarritoService;
import com.bazarboost.system.dto.*;
import com.bazarboost.system.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio que maneja las operaciones relacionadas con el carrito de productos.
 */
@Service
public class ProductoCarritoServiceImpl implements ProductoCarritoService {

    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public RespuestaCarritoDTO actualizarCarrito(SolicitudCarritoDTO solicitudCarritoDTO, Integer usuarioId) {
        Producto producto = obtenerProducto(solicitudCarritoDTO.getProductoId());
        Usuario usuario = obtenerUsuario(usuarioId);
        String accion = solicitudCarritoDTO.getAccion().toLowerCase();

        return switch (accion) {
            case "agregar" -> agregarProductoAlCarrito(usuario, producto);
            case "quitar" -> quitarProductoDelCarrito(usuario, producto);
            default -> throw new AccionNoValidaException("Acción no válida: se esperaba 'agregar' o 'quitar'");
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerTotalProductosEnCarrito(Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        Integer total = productoCarritoRepository.totalProductosEnCarrito(usuario.getUsuarioId());
        return total != null ? total : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoDTO obtenerCarrito(Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);

        List<CarritoProductoDTO> carritoProductoDTOS = productoCarritoRepository.findByUsuarioUsuarioId(usuario.getUsuarioId())
                .stream()
                .map(this::convertirACarritoProductoDTO)
                .toList();

        List<CarritoDireccionDTO> carritoDireccionDTOS = direccionRepository.findByUsuarioUsuarioId(usuario.getUsuarioId())
                .stream()
                .map(this::convertirACarritoDireccionDTO)
                .toList();

        List<CarritoMetodoPagoDTO> carritoMetodoPagoDTOS = metodoPagoRepository.findByUsuarioUsuarioId(usuario.getUsuarioId())
                .stream()
                .map(this::convertirACarritoMetodoPagoDTO)
                .toList();

        return new CarritoDTO(carritoProductoDTOS,carritoDireccionDTOS, carritoMetodoPagoDTOS);
    }

    @Override
    @Transactional
    public RespuestaCarritoDTO cambiarCantidadProducto(CarritoProductoCantidadDTO carritoProductoCantidadDTO, Integer usuarioId) {

        Usuario usuario = obtenerUsuario(usuarioId);
        Producto producto = obtenerProducto(carritoProductoCantidadDTO.getProductoId());
        ProductoCarrito productoCarrito = obtenerProductoCarrito(usuario, producto);
        productoCarrito.setCantidad(carritoProductoCantidadDTO.getCantidad());
        productoCarritoRepository.save(productoCarrito);

        return new RespuestaCarritoDTO(productoCarritoRepository.totalProductosEnCarrito(usuarioId));

    }

    private RespuestaCarritoDTO agregarProductoAlCarrito(Usuario usuario, Producto producto) {
        if (producto.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new ProductoPropioException("No puede agregar un producto propio a su carrito");
        }

        Optional<ProductoCarrito> productoExistente = productoCarritoRepository.findByUsuarioAndProducto(usuario, producto);
        if (productoExistente.isPresent()) {
            throw new ProductoYaEnCarritoException(
                    String.format("El producto con ID %d ya está en el carrito del usuario con ID %d",
                            producto.getProductoId(), usuario.getUsuarioId())
            );
        }

        ProductoCarrito nuevoProductoCarrito = new ProductoCarrito();
        nuevoProductoCarrito.setUsuario(usuario);
        nuevoProductoCarrito.setProducto(producto);
        nuevoProductoCarrito.setCantidad(1);
        nuevoProductoCarrito.setTotal(producto.getPrecio());

        productoCarritoRepository.save(nuevoProductoCarrito);
        return obtenerRespuestaCarrito(usuario.getUsuarioId());
    }

    private RespuestaCarritoDTO quitarProductoDelCarrito(Usuario usuario, Producto producto) {
        ProductoCarrito productoCarrito = productoCarritoRepository.findByUsuarioAndProducto(usuario, producto)
                .orElseThrow(() -> new ProductoNoEnCarritoException(
                        String.format("El producto con ID %d no está en el carrito del usuario con ID %d",
                                producto.getProductoId(), usuario.getUsuarioId())
                ));

        productoCarritoRepository.delete(productoCarrito);
        return obtenerRespuestaCarrito(usuario.getUsuarioId());
    }

    private Producto obtenerProducto(Integer productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + productoId + " no encontrado"));
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
    }

    private ProductoCarrito obtenerProductoCarrito(Usuario usuario, Producto producto) {
        return productoCarritoRepository.findByUsuarioAndProducto(usuario, producto)
                .orElseThrow(() -> new ProductoNoEnCarritoException(
                        String.format("El producto con ID %d no está en el carrito del usuario con ID %d",
                        producto.getProductoId(), usuario.getUsuarioId()))
                );
    }

    private RespuestaCarritoDTO obtenerRespuestaCarrito(Integer usuarioId) {
        Integer totalProductos = productoCarritoRepository.totalProductosEnCarrito(usuarioId);
        return new RespuestaCarritoDTO(totalProductos != null ? totalProductos : 0);
    }

    private CarritoProductoDTO convertirACarritoProductoDTO(ProductoCarrito productoCarrito) {
        CarritoProductoDTO carritoProductoDTO = modelMapper.map(productoCarrito, CarritoProductoDTO.class);

        BigDecimal precio = productoCarrito.getProducto().getPrecio();
        carritoProductoDTO.setTotalSinDescuento(precio.multiply(BigDecimal.valueOf(carritoProductoDTO.getCantidad())));

        Descuento descuento = productoCarrito.getProducto().getDescuento();
        if (descuento != null) {
            Integer descuentoPorcentaje = descuento.getPorcentaje();
            carritoProductoDTO.setDescuentoId(descuento.getDescuentoId());
            carritoProductoDTO.setDescuentoUnitarioPorcentaje(descuentoPorcentaje);
            carritoProductoDTO.setDescuentoUnitarioValor(precio.multiply(BigDecimal.valueOf(descuentoPorcentaje)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
            carritoProductoDTO.setDescuentoTotal(carritoProductoDTO.getDescuentoUnitarioValor().multiply(BigDecimal.valueOf(carritoProductoDTO.getCantidad())));
            carritoProductoDTO.setTotalFinal(carritoProductoDTO.getTotalSinDescuento().subtract(carritoProductoDTO.getDescuentoTotal()));
        } else {
            carritoProductoDTO.setTotalFinal(carritoProductoDTO.getTotalSinDescuento());
        }

        carritoProductoDTO.setNombre(productoCarrito.getProducto().getNombre());
        carritoProductoDTO.setPrecio(precio);

        return carritoProductoDTO;
    }

    private CarritoDireccionDTO convertirACarritoDireccionDTO(Direccion direccion) {
        CarritoDireccionDTO carritoDireccionDTO = modelMapper.map(direccion, CarritoDireccionDTO.class);

        carritoDireccionDTO.setDireccion(
                direccion.getCalle() + " #" + direccion.getNumeroDomicilio() +
                        ", " + direccion.getColonia() + ", " + direccion.getCiudad() +
                        ", " + direccion.getEstado() + ", C.P. " + direccion.getCodigoPostal()
        );

        return carritoDireccionDTO;
    }

    private CarritoMetodoPagoDTO convertirACarritoMetodoPagoDTO(MetodoPago metodoPago) {
        CarritoMetodoPagoDTO carritoMetodoPagoDTO = modelMapper.map(metodoPago, CarritoMetodoPagoDTO.class);

        carritoMetodoPagoDTO.setTipo(metodoPago.getTipoTarjeta().name());
        carritoMetodoPagoDTO.setTerminacion(metodoPago.getNumeroTarjeta().substring(metodoPago.getNumeroTarjeta().length() - 4));
        carritoMetodoPagoDTO.setFechaExpiracion(metodoPago.getFechaExpiracion().format(DateTimeFormatter.ofPattern("MM/yyyy")));

        return carritoMetodoPagoDTO;
    }
}