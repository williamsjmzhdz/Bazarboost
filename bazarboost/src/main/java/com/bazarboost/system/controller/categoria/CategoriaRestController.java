package com.bazarboost.system.controller.categoria;

import com.bazarboost.system.dto.CategoriaCreacionDTO;
import com.bazarboost.system.dto.CategoriaEdicionDTO;
import com.bazarboost.system.model.Categoria;
import com.bazarboost.system.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    private static final Integer USUARIO_ID = 1;

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody @Valid CategoriaCreacionDTO categoriaCreacionDTO) {
        categoriaService.crear(categoriaCreacionDTO, USUARIO_ID);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
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
            @RequestBody @Valid CategoriaEdicionDTO dto
    ) {
        categoriaService.actualizar(dto, USUARIO_ID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer categoriaId) {
        categoriaService.eliminar(categoriaId, USUARIO_ID);
        return ResponseEntity.noContent().build();
    }

}
