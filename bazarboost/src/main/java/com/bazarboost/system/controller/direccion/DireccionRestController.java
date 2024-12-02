package com.bazarboost.system.controller.direccion;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.DireccionCreacionDTO;
import com.bazarboost.system.dto.DireccionDTO;
import com.bazarboost.system.dto.DireccionEdicionDTO;
import com.bazarboost.system.service.DireccionService;
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
@RequestMapping("/api/direcciones")
@Slf4j
public class DireccionRestController {

    @Autowired
    private DireccionService direccionService;

    @PostMapping
    public ResponseEntity<Void> crear(
            @RequestBody @Valid DireccionCreacionDTO direccionCreacionDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Creando una dirección para el usuario {}.", usuarioId);
        direccionService.crear(direccionCreacionDTO, usuarioId);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerTodas(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo la lista completa de direcciones del usuario {}.", usuarioId);
        List<DireccionDTO> direcciones = direccionService.obtenerTodas(usuarioId);
        direcciones.forEach(System.out::println);
        return ResponseEntity.ok(direcciones);
    }

    @GetMapping("/{direccionId}/edicion")
    public ResponseEntity<DireccionEdicionDTO> obtenerDatosEdicion(
            @PathVariable Integer direccionId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo los datos para la edición de la dirección {} del usuario {}.", direccionId, usuarioId);
        DireccionEdicionDTO direccion = direccionService.obtenerDatosEdicion(direccionId, usuarioId);
        return ResponseEntity.ok(direccion);
    }

    @PutMapping
    public ResponseEntity<Void> actualizar(
            @RequestBody @Valid DireccionEdicionDTO dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        direccionService.actualizar(dto, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{direccionId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @NotNull(message = "El ID de la dirección es requerido")
            @Min(value = 1, message = "El ID de la dirección debe ser un número positivo")
            Integer direccionId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Eliminando la direccion {} para el usuario {}.", direccionId, usuarioId);
        direccionService.eliminar(direccionId, usuarioId);
        return ResponseEntity.noContent().build();
    }

}
