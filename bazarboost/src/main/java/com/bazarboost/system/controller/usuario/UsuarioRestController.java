package com.bazarboost.system.controller.usuario;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.PerfilUsuarioDTO;
import com.bazarboost.system.dto.UsuarioActualizacionDTO;
import com.bazarboost.system.dto.UsuarioNombreDTO;
import com.bazarboost.system.dto.UsuariosPaginadosDTO;
import com.bazarboost.system.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@Slf4j
public class UsuarioRestController {

    private static final Integer TAMANIO_PAGINA = 10;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<UsuariosPaginadosDTO> obtenerTodos(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer pagina,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.debug("Buscando usuarios con keyword '{}', página {}", keyword, pagina);
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        UsuariosPaginadosDTO usuarios = usuarioService.obtenerTodos(keyword, pagina, TAMANIO_PAGINA, usuarioId);
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{usuarioId}/rol-vendedor")
    public ResponseEntity<Void> actualizarRolVendedor(
            @PathVariable Integer usuarioId,
            @RequestBody Map<String, Boolean> request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boolean esVendedor = request.get("esVendedor");
        log.info("Actualizando rol vendedor del usuario {} a {}", usuarioId, esVendedor);
        Integer usuarioActualizadorId = userDetails.getUsuario().getUsuarioId();
        usuarioService.actualizarRolVendedor(usuarioId, esVendedor, usuarioActualizadorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/perfil")
    public ResponseEntity<PerfilUsuarioDTO> obtenerPerfil(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo perfil del usuario {}", usuarioId);
        return ResponseEntity.ok(usuarioService.obtenerPerfil(usuarioId));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizar(@RequestBody @Valid UsuarioActualizacionDTO request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.info("Actualizando perfil del usuario {}", usuarioId);
        usuarioService.actualizar(usuarioId, request);
        return ResponseEntity.ok("Perfil actualizado correctamente");
    }

    @GetMapping("/obtenerNombre")
    public ResponseEntity<UsuarioNombreDTO> obtenerNombre(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo nombre del usuario {}", usuarioId);
        UsuarioNombreDTO dto = usuarioService.obtenerNombre(usuarioId);
        return ResponseEntity.ok(dto);
    }
}