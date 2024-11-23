package com.bazarboost.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DetalleFacturaDTO {

    private Integer facturaId;
    private LocalDateTime fechaEmision;
    private BigDecimal totalFactura;
    private List<DetalleFacturaProductoDTO> productos;

}
