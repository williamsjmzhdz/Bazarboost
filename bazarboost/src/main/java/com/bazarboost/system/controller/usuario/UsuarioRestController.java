package com.bazarboost.system.controller.usuario;

import com.bazarboost.system.dto.PerfilUsuarioDTO;
import com.bazarboost.system.dto.UsuarioActualizacionDTO;
import com.bazarboost.system.dto.UsuariosPaginadosDTO;
import com.bazarboost.system.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    private static final Integer USUARIO_ID_TEMPORAL = 1;
    private static final Integer TAMANIO_PAGINA = 10;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<UsuariosPaginadosDTO> obtenerTodos(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer pagina
    ) {
        UsuariosPaginadosDTO usuarios = usuarioService.obtenerTodos(keyword, pagina, TAMANIO_PAGINA, USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{usuarioId}/rol-vendedor")
    public ResponseEntity<Void> actualizarRolVendedor(
            @PathVariable Integer usuarioId,
            @RequestBody Map<String, Boolean> request) {
        boolean esVendedor = request.get("esVendedor");
        usuarioService.actualizarRolVendedor(usuarioId, esVendedor, USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/perfil")
    public ResponseEntity<PerfilUsuarioDTO> obtenerPerfil() {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(USUARIO_ID_TEMPORAL));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizar(@RequestBody @Valid UsuarioActualizacionDTO request) {
        usuarioService.actualizar(USUARIO_ID_TEMPORAL, request);
        return ResponseEntity.ok("Perfil actualizado correctamente");
    }

}
