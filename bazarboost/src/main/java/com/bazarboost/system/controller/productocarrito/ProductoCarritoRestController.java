package com.bazarboost.system.controller.productocarrito;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.CarritoProductoCantidadDTO;
import com.bazarboost.system.dto.CarritoDTO;
import com.bazarboost.system.dto.RespuestaCarritoDTO;
import com.bazarboost.system.dto.SolicitudCarritoDTO;
import com.bazarboost.system.service.ProductoCarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/producto-carrito")
@Slf4j
public class ProductoCarritoRestController {

    @Autowired
    private ProductoCarritoService productoCarritoService;

    @PostMapping("/actualizar")
    public ResponseEntity<RespuestaCarritoDTO> actualizarCarrito(
            @RequestBody SolicitudCarritoDTO solicitudCarritoDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.info("Actualizando carrito para usuario {}", usuarioId);
        RespuestaCarritoDTO respuesta = productoCarritoService.actualizarCarrito(solicitudCarritoDTO, usuarioId);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/total")
    public ResponseEntity<RespuestaCarritoDTO> obtenerTotalProductos(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo total de productos en carrito para usuario {}", usuarioId);
        Integer totalProductos = productoCarritoService.obtenerTotalProductosEnCarrito(usuarioId);
        return ResponseEntity.ok(new RespuestaCarritoDTO(totalProductos));
    }

    @GetMapping
    public ResponseEntity<CarritoDTO> obtenerCarrito(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo carrito para usuario {}", usuarioId);
        CarritoDTO carritoDTO = productoCarritoService.obtenerCarrito(usuarioId);
        return ResponseEntity.ok(carritoDTO);
    }

    @PatchMapping("/modificar-cantidad")
    public ResponseEntity<RespuestaCarritoDTO> modificarCantidad(
            @Valid @RequestBody CarritoProductoCantidadDTO carritoProductoCantidadDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.info("Modificando cantidad en carrito para usuario {}", usuarioId);
        RespuestaCarritoDTO respuestaCarritoDTO = productoCarritoService.cambiarCantidadProducto(carritoProductoCantidadDTO, usuarioId);
        return ResponseEntity.ok(respuestaCarritoDTO);
    }

}
