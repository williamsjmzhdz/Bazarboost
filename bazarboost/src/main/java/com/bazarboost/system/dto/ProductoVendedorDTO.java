package com.bazarboost.system.dto;

import lombok.Data;

import java.math.BigDecimal;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Data
public class ProductoVendedorDTO {

    private Integer productoId;
    private String nombre;
    private BigDecimal precio;
    private Integer existencia;
    private Integer descuentoPorcentaje;
    private BigDecimal descuentoValor;

}
