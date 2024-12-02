package com.bazarboost.system.controller.metodopago;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.MetodoPagoCreacionDTO;
import com.bazarboost.system.dto.MetodoPagoDTO;
import com.bazarboost.system.dto.MetodoPagoEdicionDTO;
import com.bazarboost.system.service.MetodoPagoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@Slf4j
public class MetodoPagoRestController {

    @Autowired
    private MetodoPagoService metodoPagoService;

    @GetMapping("/{metodoPagoId}/edicion")
    public ResponseEntity<MetodoPagoEdicionDTO> obtenerDatosEdicion(
            @PathVariable Integer metodoPagoId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo datos del método de pago {} para su edición.", metodoPagoId);
        MetodoPagoEdicionDTO metodoPago = metodoPagoService.obtenerDatosEdicion(metodoPagoId, usuarioId);
        return ResponseEntity.ok(metodoPago);
    }

    @GetMapping
    public ResponseEntity<List<MetodoPagoDTO>> obtenerTodos(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo todos los métodos de pago para el usuario {}.", usuarioId);
        List<MetodoPagoDTO> metodosPago = metodoPagoService.obtenerTodos(usuarioId);
        return ResponseEntity.ok(metodosPago);
    }

    @PostMapping
    public ResponseEntity<Void> crear(
            @RequestBody @Valid MetodoPagoCreacionDTO metodoPagoCreacionDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Creando método de pago para el usuario {}.", usuarioId);
        metodoPagoService.crear(metodoPagoCreacionDTO, usuarioId);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> actualizar(
            @RequestBody @Valid MetodoPagoEdicionDTO dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Actualizando método de pago {} para el usuario {}.", dto.getMetodoPagoId(), usuarioId);
        metodoPagoService.actualizar(dto, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{metodoPagoId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @NotNull(message = "El ID del método de pago es requerido")
            @Min(value = 1, message = "El ID del método de pago debe ser un número positivo")
            Integer metodoPagoId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Eliminando el método de pago {} para el usuario {}.", metodoPagoId, usuarioId);
        metodoPagoService.eliminar(metodoPagoId, usuarioId);
        return ResponseEntity.noContent().build();
    }

}
