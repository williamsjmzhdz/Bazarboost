package com.bazarboost.controller.productocarrito;

import com.bazarboost.dto.RespuestaCarritoDTO;
import com.bazarboost.dto.SolicitudCarritoDTO;
import com.bazarboost.service.ProductoCarritoService;
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
}
