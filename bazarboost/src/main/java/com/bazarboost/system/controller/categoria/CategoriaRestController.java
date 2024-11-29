package com.bazarboost.system.controller.categoria;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.CategoriaCreacionDTO;
import com.bazarboost.system.dto.CategoriaEdicionDTO;
import com.bazarboost.system.model.Categoria;
import com.bazarboost.system.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Void> crear(
            @RequestBody @Valid CategoriaCreacionDTO categoriaCreacionDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoriaService.crear(categoriaCreacionDTO, userDetails.getUsuario().getUsuarioId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{categoriaId}/edicion")
    public ResponseEntity<CategoriaEdicionDTO> obtenerDatosEdicion(@PathVariable Integer categoriaId) {
        CategoriaEdicionDTO categoria = categoriaService.obtenerDatosEdicion(categoriaId);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodas() {
        List<Categoria> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categorias);
    }

    @PutMapping
    public ResponseEntity<Void> actualizar(
            @RequestBody @Valid CategoriaEdicionDTO dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoriaService.actualizar(dto, userDetails.getUsuario().getUsuarioId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer categoriaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoriaService.eliminar(categoriaId, userDetails.getUsuario().getUsuarioId());
        return ResponseEntity.noContent().build();
    }

}
