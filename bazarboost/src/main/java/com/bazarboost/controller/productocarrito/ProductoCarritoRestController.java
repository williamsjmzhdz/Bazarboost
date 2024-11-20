package com.bazarboost.controller.productocarrito;

import com.bazarboost.dto.CarritoProductoCantidadDTO;
import com.bazarboost.dto.CarritoDTO;
import com.bazarboost.dto.RespuestaCarritoDTO;
import com.bazarboost.dto.SolicitudCarritoDTO;
import com.bazarboost.service.ProductoCarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que maneja las operaciones del carrito de productos.
 */
@RestController
@RequestMapping("/api/producto-carrito")
@RequiredArgsConstructor
public class ProductoCarritoRestController {

    private static final Integer USUARIO_ID_TEMPORAL = 1;
    private final ProductoCarritoService productoCarritoService;

    /**
     * Actualiza el carrito agregando o quitando productos.
     *
     * @param solicitudCarritoDTO DTO con la información de la actualización
     * @return ResponseEntity con la respuesta del carrito actualizado
     */
    @PostMapping("/actualizar")
    public ResponseEntity<RespuestaCarritoDTO> actualizarCarrito(@RequestBody SolicitudCarritoDTO solicitudCarritoDTO) {
        RespuestaCarritoDTO respuesta = productoCarritoService.actualizarCarrito(solicitudCarritoDTO, USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Obtiene el total de productos en el carrito del usuario.
     *
     * @return ResponseEntity con el total de productos en el carrito
     */
    @GetMapping("/total")
    public ResponseEntity<RespuestaCarritoDTO> obtenerTotalProductos() {
        Integer totalProductos = productoCarritoService.obtenerTotalProductosEnCarrito(USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(new RespuestaCarritoDTO(totalProductos));
    }

    /**
     * Obtiene toda la información del carrito de compras del usuario.
     *
     * @return ResponseEntity con toda la información del carrito de compras
     */
    @GetMapping
    public ResponseEntity<CarritoDTO> obtenerCarrito() {
        CarritoDTO carritoDTO = productoCarritoService.obtenerCarrito(USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(carritoDTO);
    }

    /**
     * Modifica la cantidad de un producto en el carrito del usuario.
     *
     * @return ResponseEntity con la cantidad nueva
     */
    @PatchMapping("/modificar-cantidad")
    public ResponseEntity<RespuestaCarritoDTO> modificarCantidad(@Valid @RequestBody CarritoProductoCantidadDTO carritoProductoCantidadDTO) {
        RespuestaCarritoDTO respuestaCarritoDTO = productoCarritoService.cambiarCantidadProducto(carritoProductoCantidadDTO, USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(respuestaCarritoDTO);
    }
}
