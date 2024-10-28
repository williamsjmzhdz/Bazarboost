package com.bazarboost.dto;

import lombok.Data;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Data
public class ProductoVendedorDTO {

    private Integer productoId;
    private String nombre;
    private Double precio;
    private Integer existencia;
    private Integer descuentoPorcentaje;
    private Double descuentoValor;

}
