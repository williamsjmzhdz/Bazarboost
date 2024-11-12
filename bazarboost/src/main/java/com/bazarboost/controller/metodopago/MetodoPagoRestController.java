package com.bazarboost.controller.metodopago;

import com.bazarboost.dto.MetodoPagoCreacionDTO;
import com.bazarboost.dto.MetodoPagoDTO;
import com.bazarboost.service.MetodoPagoService;
import jakarta.validation.Valid;
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

}
