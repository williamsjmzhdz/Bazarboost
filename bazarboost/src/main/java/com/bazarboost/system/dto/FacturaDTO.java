package com.bazarboost.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FacturaDTO {

    private Integer facturaId;
    private LocalDateTime fecha;
    private Double monto;

}
