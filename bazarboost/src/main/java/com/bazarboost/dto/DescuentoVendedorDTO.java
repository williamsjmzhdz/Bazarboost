package com.bazarboost.dto;

import lombok.Data;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Data
public class DescuentoVendedorDTO {

    private Integer descuentoId;
    private Integer porcentaje;
    private String nombre;

}
