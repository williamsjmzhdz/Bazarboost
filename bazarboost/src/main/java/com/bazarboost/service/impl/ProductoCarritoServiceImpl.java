package com.bazarboost.service.impl;

import com.bazarboost.dto.RespuestaCarritoDTO;
import com.bazarboost.dto.SolicitudCarritoDTO;
import com.bazarboost.exception.*;
import com.bazarboost.model.Producto;
import com.bazarboost.model.ProductoCarrito;
import com.bazarboost.model.Usuario;
import com.bazarboost.repository.ProductoCarritoRepository;
import com.bazarboost.repository.ProductoRepository;
import com.bazarboost.repository.UsuarioRepository;
import com.bazarboost.service.ProductoCarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio que maneja las operaciones relacionadas con el carrito de productos.
 */
@Service
@RequiredArgsConstructor
public class ProductoCarritoServiceImpl implements ProductoCarritoService {

    private final ProductoCarritoRepository productoCarritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

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
        nuevoProductoCarrito.setTotal(producto.getPrecio().doubleValue());

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

    private RespuestaCarritoDTO obtenerRespuestaCarrito(Integer usuarioId) {
        Integer totalProductos = productoCarritoRepository.totalProductosEnCarrito(usuarioId);
        return new RespuestaCarritoDTO(totalProductos != null ? totalProductos : 0);
    }
}