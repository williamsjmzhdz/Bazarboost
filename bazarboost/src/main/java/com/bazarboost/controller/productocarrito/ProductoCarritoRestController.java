package com.bazarboost.controller.productocarrito;

import com.bazarboost.dto.RespuestaCarritoDTO;
import com.bazarboost.dto.SolicitudCarritoDTO;
import com.bazarboost.service.ProductoCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/producto-carrito")
public class ProductoCarritoRestController {

    private static final Integer USUARIO_ID_TEMPORAL = 1;

    @Autowired
    private ProductoCarritoService productoCarritoService;

    @PostMapping("/actualizar")
    public ResponseEntity<RespuestaCarritoDTO> actualizarCarrito(@RequestBody SolicitudCarritoDTO solicitudCarritoDTO) {
        RespuestaCarritoDTO respuestaCarritoDTO = productoCarritoService.actualizarCarrito(solicitudCarritoDTO, USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(respuestaCarritoDTO);
    }

}
