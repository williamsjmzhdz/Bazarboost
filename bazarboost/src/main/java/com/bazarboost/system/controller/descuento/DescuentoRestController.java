package com.bazarboost.system.controller.descuento;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.DescuentoVendedorDTO;
import com.bazarboost.system.model.Descuento;
import com.bazarboost.system.service.DescuentoService;
import com.bazarboost.system.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/descuentos")
@Slf4j
public class DescuentoRestController {

    @Autowired
    private DescuentoService descuentoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/mis-descuentos")
    public ResponseEntity<List<DescuentoVendedorDTO>> mostrarMisDescuentos(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Mostrando los descuentos para el usuario {}.", usuarioId);
        List<DescuentoVendedorDTO> descuentos = descuentoService.obtenerDescuentosDTOPorUsuario(usuarioId);
        return ResponseEntity.ok(descuentos);
    }

    @PostMapping
    public ResponseEntity<Void> crearDescuento(
            @Valid @RequestBody Descuento descuento,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Creando descuento para el usuario {}.", usuarioId);
        descuentoService.crearDescuento(descuento, usuarioId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{descuentoId}")
    public ResponseEntity<Void> actualizarDescuento(
            @PathVariable Integer descuentoId,
            @Valid @RequestBody Descuento descuentoActualizado,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Actualizando descuento {} para el usuario {}.", descuentoId, usuarioId);
        descuentoService.actualizarDescuento(descuentoId, descuentoActualizado, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{descuentoId}")
    public ResponseEntity<Void> eliminarDescuento(
            @PathVariable Integer descuentoId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Eliminando descuento {} para el usuario {}.", descuentoId, usuarioId);
        descuentoService.eliminarDescuento(descuentoId, usuarioId);
        return ResponseEntity.noContent().build();
    }

}
