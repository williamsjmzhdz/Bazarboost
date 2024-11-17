package com.bazarboost.controller.metodopago;

import com.bazarboost.dto.MetodoPagoCreacionDTO;
import com.bazarboost.dto.MetodoPagoDTO;
import com.bazarboost.dto.MetodoPagoEdicionDTO;
import com.bazarboost.service.MetodoPagoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
public class MetodoPagoRestController {

    private static final Integer USUARIO_ID = 1;

    @Autowired
    private MetodoPagoService metodoPagoService;

    @GetMapping("/{metodoPagoId}/edicion")
    public ResponseEntity<MetodoPagoEdicionDTO> obtenerDatosEdicion(@PathVariable Integer metodoPagoId) {
        MetodoPagoEdicionDTO metodoPago = metodoPagoService.obtenerDatosEdicion(metodoPagoId, USUARIO_ID);
        return ResponseEntity.ok(metodoPago);
    }

    @GetMapping
    public ResponseEntity<List<MetodoPagoDTO>> obtenerTodos() {
        List<MetodoPagoDTO> metodosPago = metodoPagoService.obtenerTodos(USUARIO_ID);
        return ResponseEntity.ok(metodosPago);
    }

    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody @Valid MetodoPagoCreacionDTO metodoPagoCreacionDTO) {
        metodoPagoService.crear(metodoPagoCreacionDTO, USUARIO_ID);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> actualizar(@RequestBody @Valid MetodoPagoEdicionDTO dto) {
        metodoPagoService.actualizar(dto, USUARIO_ID);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{metodoPagoId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable
            @NotNull(message = "El ID del método de pago es requerido")
            @Min(value = 1, message = "El ID del método de pago debe ser un número positivo")
            Integer metodoPagoId) {

        metodoPagoService.eliminar(metodoPagoId, USUARIO_ID);
        return ResponseEntity.noContent().build();
    }

}
