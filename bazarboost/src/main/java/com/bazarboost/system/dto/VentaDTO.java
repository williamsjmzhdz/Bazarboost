package com.bazarboost.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {
    private Integer ventaId;
    private LocalDateTime fecha;
    private String nombreCliente;
    private DetalleFacturaProductoDTO producto;
}
