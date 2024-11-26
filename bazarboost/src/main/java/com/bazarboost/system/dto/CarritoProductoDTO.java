package com.bazarboost.system.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarritoProductoDTO {

    private Integer productoCarritoId;
    private Integer productoId;
    private Integer descuentoId;
    private String nombre;
    private BigDecimal precio;
    private Integer descuentoUnitarioPorcentaje;
    private BigDecimal descuentoUnitarioValor;
    private Integer cantidad;
    private BigDecimal totalSinDescuento;
    private BigDecimal descuentoTotal;
    private BigDecimal totalFinal;

}