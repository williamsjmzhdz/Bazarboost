package com.bazarboost.system.controller.venta;

import com.bazarboost.system.dto.VentasPaginadasDTO;
import com.bazarboost.system.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ventas")
public class VentaRestController {

    private static final Integer USUARIO_ID_TEMPORAL = 1;
    private static final Integer TAMANO_PAGINA = 10;

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public ResponseEntity<VentasPaginadasDTO> listarVentas(
            @RequestParam(defaultValue = "fecha") String ordenarPor,
            @RequestParam(defaultValue = "desc") String direccionOrden,
            @RequestParam(defaultValue = "0") Integer pagina
    ) {
        VentasPaginadasDTO ventas = facturaService.obtenerVentasPaginadasYOrdenadas(
                ordenarPor, direccionOrden, pagina, TAMANO_PAGINA, USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(ventas);
    }
}
