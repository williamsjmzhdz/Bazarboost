package com.bazarboost.service.impl;

import com.bazarboost.dto.CarritoPagoRespuestaDTO;
import com.bazarboost.dto.CarritoPagoSolicitudDTO;
import com.bazarboost.dto.ProductoPagoDTO;
import com.bazarboost.dto.RespuestaCarritoDTO;
import com.bazarboost.exception.*;
import com.bazarboost.model.*;
import com.bazarboost.repository.*;
import com.bazarboost.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Override
    @Transactional
    public CarritoPagoRespuestaDTO procesarPago(CarritoPagoSolicitudDTO carritoPagoSolicitudDTO, Integer usuarioId) {

        // Verificaciones
        Usuario usuario = obtenerUsuario(usuarioId);
        List<Producto> productos = carritoPagoSolicitudDTO.getProductos().stream().map(productoPagoDTO -> obtenerProducto(productoPagoDTO.getProductoId())).toList();
        productos.forEach(producto -> obtenerProductoCarrito(usuario, producto));
        MetodoPago metodoPago = obtenerMetodoPago(carritoPagoSolicitudDTO.getMetodoPagoId(), usuario);
        Direccion direccion = obtenerDireccion(carritoPagoSolicitudDTO.getDireccionId(), usuario);
        BigDecimal precioTotal = productoCarritoRepository.obtenerPrecioTotalCarrito(usuario);

        // Procesar pago

        // Actualizar stock
        carritoPagoSolicitudDTO.getProductos().forEach(this::actualizarStock);

        // Actualizar monto
        actualizarMonto(metodoPago, precioTotal);

        // Generar factura
        //generarFactura(); Necesitaremos modificar la estructura de la tabla Facturas para quitar subtotal y porcentajeImpuestos

        // Vaciar carrito
        // vaciarCarrito();


        return null;
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

    private MetodoPago obtenerMetodoPago(Integer metodoPagoId, Usuario usuario) {
        return metodoPagoRepository.findByMetodoPagoIdAndUsuario(metodoPagoId, usuario)
                .orElseThrow(() -> new MetodoPagoNoEncontradoException(
                        String.format("No se encontró un método de pago con ID %d para el usuario con ID %d",
                                metodoPagoId, usuario.getUsuarioId())
                ));
    }

    private Direccion obtenerDireccion(Integer direccionId, Usuario usuario) {
        return direccionRepository.findByDireccionIdAndUsuario(direccionId, usuario)
                .orElseThrow(() -> new DireccionNoEncontradaException(
                        String.format("No se encontró una dirección con ID %d para el usuario con ID %d",
                                direccionId, usuario.getUsuarioId())
                ));
    }

    private void actualizarStock(ProductoPagoDTO productoPagoDTO) {
        Producto producto = obtenerProducto(productoPagoDTO.getProductoId()); // Obtener el producto para acceder a la existencia actual
        int nuevaCantidad = producto.getExistencia() - productoPagoDTO.getCantidad();
        if (nuevaCantidad < 0) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para el producto '" + producto.getNombre() + "'. Disponible: " + producto.getExistencia()
            );
        }
        productoRepository.actualizarStock(productoPagoDTO.getProductoId(), nuevaCantidad);
    }

    private void actualizarMonto(MetodoPago metodoPago, BigDecimal precioTotal) {
        if (metodoPago.getMonto() == null || BigDecimal.valueOf(metodoPago.getMonto()).compareTo(precioTotal) < 0) {
            throw new FondosInsuficientesException(
                    "Fondos insuficientes en el método de pago. Disponible: "
                            + metodoPago.getMonto() + ", Requerido: " + precioTotal
            );
        }
        metodoPagoRepository.reducirMonto(metodoPago.getMetodoPagoId(), precioTotal.doubleValue());
    }


}
