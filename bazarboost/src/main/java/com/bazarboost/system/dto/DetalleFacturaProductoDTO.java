package com.bazarboost.system.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleFacturaProductoDTO {

    private String nombre;
    private BigDecimal precioUnitario;
    private Integer descuentoUnitarioPorcentaje;
    private BigDecimal descuentoUnitarioValor;
    private Integer cantidad;
    private BigDecimal totalSinDescuento;
    private BigDecimal descuentoTotal;
    private BigDecimal totalFinal;

}
