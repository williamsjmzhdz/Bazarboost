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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoCarritoServiceImpl implements ProductoCarritoService {

    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public RespuestaCarritoDTO actualizarCarrito(SolicitudCarritoDTO solicitudCarritoDTO, Integer usuarioId) {

        Integer productoId = solicitudCarritoDTO.getProductoId();
        String accion = solicitudCarritoDTO.getAccion();

        // Obtener el producto (lanza una excepción si no se encuentra)
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + productoId + " no encontrado"));

        // Obtener el usuario usando el ID proporcionado por el controlador
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));

        if ("agregar".equalsIgnoreCase(accion)) {
            return agregarProductoAlCarrito(usuario, producto);
        } else if ("quitar".equalsIgnoreCase(accion)) {
            return quitarProductoDelCarrito(usuario, producto);
        } else {
            throw new AccionNoValidaException("Acción no válida: se esperaba 'agregar' o 'quitar'");
        }

    }

    private RespuestaCarritoDTO agregarProductoAlCarrito(Usuario usuario, Producto producto) {

        // Verificar si el producto pertenece al mismo usuario
        if (producto.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new ProductoPropioException("No puede agregar un producto propio a su carrito");
        }

        Optional<ProductoCarrito> productoCarritoOpt = productoCarritoRepository.findByUsuarioAndProducto(usuario, producto);

        if (productoCarritoOpt.isPresent()) {
            throw new ProductoYaEnCarritoException("El producto con ID " + producto.getProductoId() +
                    " ya está en el carrito del usuario con ID " + usuario.getUsuarioId());
        }

        ProductoCarrito nuevoProductoCarrito = new ProductoCarrito();
        nuevoProductoCarrito.setUsuario(usuario);
        nuevoProductoCarrito.setProducto(producto);
        nuevoProductoCarrito.setCantidad(1);
        nuevoProductoCarrito.setTotal(producto.getPrecio().doubleValue());

        productoCarritoRepository.save(nuevoProductoCarrito);

        int totalProductos = productoCarritoRepository.totalProductosEnCarrito(usuario.getUsuarioId());

        return new RespuestaCarritoDTO(totalProductos);
    }

    private RespuestaCarritoDTO quitarProductoDelCarrito(Usuario usuario, Producto producto) {
        ProductoCarrito productoCarrito = productoCarritoRepository.findByUsuarioAndProducto(usuario, producto)
                .orElseThrow(() -> new ProductoNoEnCarritoException("El producto con ID " + producto.getProductoId() +
                        " no está en el carrito del usuario con ID " + usuario.getUsuarioId()));

        productoCarritoRepository.delete(productoCarrito);

        // Obtener el total de productos
        Integer totalProductos = productoCarritoRepository.totalProductosEnCarrito(usuario.getUsuarioId());
        totalProductos = (totalProductos != null) ? totalProductos : 0;

        return new RespuestaCarritoDTO(totalProductos);
    }

}
