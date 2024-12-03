package com.bazarboost.system.service.impl;

import com.bazarboost.shared.exception.*;
import com.bazarboost.system.repository.*;
import com.bazarboost.system.service.ProductoCarritoService;
import com.bazarboost.system.dto.*;
import com.bazarboost.system.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
        log.debug("Actualizando carrito para el usuario {}.", usuarioId);
        Producto producto = obtenerProducto(solicitudCarritoDTO.getProductoId());
        Usuario usuario = obtenerUsuario(usuarioId);
        String accion = solicitudCarritoDTO.getAccion().toLowerCase();

        return switch (accion) {
            case "agregar" -> agregarProductoAlCarrito(usuario, producto);
            case "quitar" -> quitarProductoDelCarrito(usuario, producto);
            default -> {
                log.debug("La acción '{}' no es válida.", accion);
                throw new AccionNoValidaException("Acción no válida: se esperaba 'agregar' o 'quitar'");
            }
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerTotalProductosEnCarrito(Integer usuarioId) {
        log.debug("Obteniendo el total de productos en el carrito del usuario {}.", usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        Integer total = productoCarritoRepository.totalProductosEnCarrito(usuario.getUsuarioId());
        return total != null ? total : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoDTO obtenerCarrito(Integer usuarioId) {
        log.debug("Obteniendo información del carrito para el usuario {}.", usuarioId);

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

        log.debug("Cambiando la cantidad del producto {} a {} en el carrito del usuario {}.",
                carritoProductoCantidadDTO.getProductoId(), carritoProductoCantidadDTO.getCantidad(), usuarioId);

        Usuario usuario = obtenerUsuario(usuarioId);
        Producto producto = obtenerProducto(carritoProductoCantidadDTO.getProductoId());
        ProductoCarrito productoCarrito = obtenerProductoCarrito(usuario, producto);
        productoCarrito.setCantidad(carritoProductoCantidadDTO.getCantidad());
        productoCarritoRepository.save(productoCarrito);

        log.debug("Se cambió la cantidad del producto {} a {} en el carrito del usuario {} con éxito.",
                carritoProductoCantidadDTO.getProductoId(), carritoProductoCantidadDTO.getCantidad(), usuarioId);

        return new RespuestaCarritoDTO(productoCarritoRepository.totalProductosEnCarrito(usuarioId));

    }

    private RespuestaCarritoDTO agregarProductoAlCarrito(Usuario usuario, Producto producto) {
        log.debug("Agregando producto {} al carrito del usuario {}.", producto.getProductoId(), usuario.getUsuarioId());
        if (producto.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())) {
            log.debug("El producto {} le pertenece al usuario {}; no puede agregarse a su carrito.", producto.getProductoId(), usuario.getUsuarioId());
            throw new ProductoPropioException("No puede agregar un producto propio a su carrito");
        }

        Optional<ProductoCarrito> productoExistente = productoCarritoRepository.findByUsuarioAndProducto(usuario, producto);
        if (productoExistente.isPresent()) {
            log.debug("El producto {} ya se encuentra en el carrito del usuario {}.", producto.getProductoId(), usuario.getUsuarioId());
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

        log.debug("Carrito actualizado con éxito; se agregó el producto {} al carrito del usuario {}.", producto.getUsuario(), usuario.getUsuarioId());

        return obtenerRespuestaCarrito(usuario.getUsuarioId());
    }

    private RespuestaCarritoDTO quitarProductoDelCarrito(Usuario usuario, Producto producto) {
        log.debug("Quitando producto {} al carrito del usuario {}.", producto.getProductoId(), usuario.getUsuarioId());

        Optional<ProductoCarrito> productoCarritoOptional = productoCarritoRepository.findByUsuarioAndProducto(usuario, producto);
        if (productoCarritoOptional.isEmpty()) {
            log.debug("El producto {} no está en el carrito del usuario {}.", producto.getProductoId(), usuario.getUsuarioId());
            throw new ProductoNoEnCarritoException(
                    String.format("El producto con ID %d no está en el carrito del usuario con ID %d",
                            producto.getProductoId(), usuario.getUsuarioId())
            );
        }
        ProductoCarrito productoCarrito = productoCarritoOptional.get();


        productoCarritoRepository.delete(productoCarrito);
        log.debug("Producto {} eliminado con éxito del carrito del usuario {}.", producto.getProductoId(), usuario.getUsuarioId());

        return obtenerRespuestaCarrito(usuario.getUsuarioId());
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

    private ProductoCarrito obtenerProductoCarrito(Usuario usuario, Producto producto) {
        log.debug("Obteniendo el ProductoCarrito a partir del producto {} y el usuario {}.", producto.getProductoId(), usuario.getUsuarioId());
        Optional<ProductoCarrito> productoCarritoOptional = productoCarritoRepository.findByUsuarioAndProducto(usuario, producto);
        if (productoCarritoOptional.isEmpty()) {
            log.debug("El producto {} no está en el carrito del usuario {}.", producto.getProductoId(), usuario.getUsuarioId());
            throw new ProductoNoEnCarritoException(
                    String.format("El producto con ID %d no está en el carrito del usuario con ID %d",
                            producto.getProductoId(), usuario.getUsuarioId()));
        }
        return productoCarritoOptional.get();
    }

    private RespuestaCarritoDTO obtenerRespuestaCarrito(Integer usuarioId) {
        log.debug("Obteniendo el nuevo total de productos en el carrito del usuario {}.", usuarioId);
        Integer totalProductos = productoCarritoRepository.totalProductosEnCarrito(usuarioId);
        return new RespuestaCarritoDTO(totalProductos != null ? totalProductos : 0);
    }

    private CarritoProductoDTO convertirACarritoProductoDTO(ProductoCarrito productoCarrito) {

        log.debug("Convirtiendo el ProductoCarrito {} a ProductoCarritoDTO.", productoCarrito.getProductoCarritoId());

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

        log.debug("Convirtiendo la dirección {} a CarritoDireccionDTO.", direccion.getDireccionId());

        CarritoDireccionDTO carritoDireccionDTO = modelMapper.map(direccion, CarritoDireccionDTO.class);

        carritoDireccionDTO.setDireccion(
                direccion.getCalle() + " #" + direccion.getNumeroDomicilio() +
                        ", " + direccion.getColonia() + ", " + direccion.getCiudad() +
                        ", " + direccion.getEstado() + ", C.P. " + direccion.getCodigoPostal()
        );

        return carritoDireccionDTO;
    }

    private CarritoMetodoPagoDTO convertirACarritoMetodoPagoDTO(MetodoPago metodoPago) {

        log.debug("Convirtiendo el método de pago {} a CarritoMetodoPagoDTO.", metodoPago.getMetodoPagoId());

        CarritoMetodoPagoDTO carritoMetodoPagoDTO = modelMapper.map(metodoPago, CarritoMetodoPagoDTO.class);

        carritoMetodoPagoDTO.setTipo(metodoPago.getTipoTarjeta().name());
        carritoMetodoPagoDTO.setTerminacion(metodoPago.getNumeroTarjeta().substring(metodoPago.getNumeroTarjeta().length() - 4));
        carritoMetodoPagoDTO.setFechaExpiracion(metodoPago.getFechaExpiracion().format(DateTimeFormatter.ofPattern("MM/yyyy")));

        return carritoMetodoPagoDTO;
    }
}