package com.bazarboost.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Data
public class ProductoDetalladoDTO implements PaginatedResultDTO {
    private Integer productoId;
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
    private boolean esProductoPropio;
    private boolean estaEnCarrito;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;

}

