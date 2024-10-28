package com.bazarboost.dto;

import com.bazarboost.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Data
@AllArgsConstructor
public class ProductosPaginadosDTO {
    private List<Producto> productos;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;
}

