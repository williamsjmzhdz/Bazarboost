package com.bazarboost.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DetalleFacturaDTO {

    private Integer facturaId;
    private LocalDateTime fechaEmision;
    private BigDecimal totalFactura;
    private List<DetalleFacturaProductoDTO> productos;

}
