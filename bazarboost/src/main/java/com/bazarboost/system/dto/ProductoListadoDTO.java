package com.bazarboost.system.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoListadoDTO {
    private Integer productoId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagenUrl;
    private Integer porcentajeDescuento;
    private String nombreDescuento;
    private BigDecimal precioFinalConDescuento;
    private BigDecimal calificacionPromedio;
    private boolean estaEnCarrito;
    private boolean esProductoPropio;
}

