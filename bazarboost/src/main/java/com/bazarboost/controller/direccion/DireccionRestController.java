package com.bazarboost.controller.direccion;

import com.bazarboost.dto.DireccionCreacionDTO;
import com.bazarboost.dto.DireccionDTO;
import com.bazarboost.dto.DireccionEdicionDTO;
import com.bazarboost.service.DireccionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/direcciones")
public class DireccionRestController {

    private static final Integer USUARIO_ID = 1;

    @Autowired
    private DireccionService direccionService;

    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody @Valid DireccionCreacionDTO direccionCreacionDTO) {
        direccionService.crear(direccionCreacionDTO, USUARIO_ID);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerTodas() {
        List<DireccionDTO> direcciones = direccionService.obtenerTodas(USUARIO_ID);
        direcciones.forEach(System.out::println);
        return ResponseEntity.ok(direcciones);
    }

    @GetMapping("/{direccionId}/edicion")
    public ResponseEntity<DireccionEdicionDTO> obtenerDatosEdicion(@PathVariable Integer direccionId) {
        DireccionEdicionDTO direccion = direccionService.obtenerDatosEdicion(direccionId, USUARIO_ID);
        return ResponseEntity.ok(direccion);
    }

    @PutMapping
    public ResponseEntity<Void> actualizar(
            @RequestBody @Valid DireccionEdicionDTO dto
    ) {
        direccionService.actualizar(dto, USUARIO_ID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{direccionId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @NotNull(message = "El ID de la dirección es requerido")
            @Min(value = 1, message = "El ID del método de pago debe ser un número positivo")
            Integer direccionId) {

        direccionService.eliminar(direccionId, USUARIO_ID);
        return ResponseEntity.noContent().build();
    }

}
