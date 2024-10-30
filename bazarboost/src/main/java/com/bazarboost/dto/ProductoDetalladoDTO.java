package com.bazarboost.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Data
public class ProductoDetalladoDTO {
    private String imagenUrl;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal precioConDescuento;
    private Integer existencia;
    private String nombreCategoria;
    private BigDecimal calificacionPromedio;
    private DescuentoDTO descuento;
    private ReseniaDTO miResenia;
    private List<ReseniaDTO> reseniasAdicionales;
}

