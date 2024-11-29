package com.bazarboost.system.controller.factura;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.CarritoPagoRespuestaDTO;
import com.bazarboost.system.dto.CarritoPagoSolicitudDTO;
import com.bazarboost.system.dto.DetalleFacturaDTO;
import com.bazarboost.system.dto.FacturasPaginadasDTO;
import com.bazarboost.system.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facturas")
public class FacturaRestController {

    private static final Integer TAMANO_PAGINA = 10;

    @Autowired
    private FacturaService facturaService;

    @PostMapping
    public ResponseEntity<CarritoPagoRespuestaDTO> procesarPago(
            @RequestBody @Valid CarritoPagoSolicitudDTO carritoPagoSolicitudDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        CarritoPagoRespuestaDTO carritoPagoRespuestaDTO = facturaService.procesarPago(carritoPagoSolicitudDTO, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoPagoRespuestaDTO);
    }

    @GetMapping
    public ResponseEntity<FacturasPaginadasDTO> listarFacturas(
            @RequestParam(defaultValue = "fecha") String ordenarPor,
            @RequestParam(defaultValue = "desc") String direccionOrden,
            @RequestParam(defaultValue = "0") Integer pagina,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        FacturasPaginadasDTO facturas = facturaService.obtenerFacturasPaginadasYOrdenadas(
                ordenarPor, direccionOrden, pagina, TAMANO_PAGINA, usuarioId);
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{facturaId}")
    public ResponseEntity<DetalleFacturaDTO> obtenerDetalleFactura(
            @PathVariable Integer facturaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        DetalleFacturaDTO detalleFactura = facturaService.obtenerDetalleFactura(facturaId, usuarioId);
        return ResponseEntity.ok(detalleFactura);
    }

}
