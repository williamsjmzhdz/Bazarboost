package com.bazarboost.system.controller.productocarrito;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.CarritoProductoCantidadDTO;
import com.bazarboost.system.dto.CarritoDTO;
import com.bazarboost.system.dto.RespuestaCarritoDTO;
import com.bazarboost.system.dto.SolicitudCarritoDTO;
import com.bazarboost.system.service.ProductoCarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/producto-carrito")
@RequiredArgsConstructor
public class ProductoCarritoRestController {

    private final ProductoCarritoService productoCarritoService;

    @PostMapping("/actualizar")
    public ResponseEntity<RespuestaCarritoDTO> actualizarCarrito(
            @RequestBody SolicitudCarritoDTO solicitudCarritoDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        RespuestaCarritoDTO respuesta = productoCarritoService.actualizarCarrito(solicitudCarritoDTO, usuarioId);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/total")
    public ResponseEntity<RespuestaCarritoDTO> obtenerTotalProductos(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        Integer totalProductos = productoCarritoService.obtenerTotalProductosEnCarrito(usuarioId);
        return ResponseEntity.ok(new RespuestaCarritoDTO(totalProductos));
    }

    @GetMapping
    public ResponseEntity<CarritoDTO> obtenerCarrito(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        CarritoDTO carritoDTO = productoCarritoService.obtenerCarrito(usuarioId);
        return ResponseEntity.ok(carritoDTO);
    }

    @PatchMapping("/modificar-cantidad")
    public ResponseEntity<RespuestaCarritoDTO> modificarCantidad(
            @Valid @RequestBody CarritoProductoCantidadDTO carritoProductoCantidadDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        RespuestaCarritoDTO respuestaCarritoDTO = productoCarritoService.cambiarCantidadProducto(carritoProductoCantidadDTO, usuarioId);
        return ResponseEntity.ok(respuestaCarritoDTO);
    }

}
