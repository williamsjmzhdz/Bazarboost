package com.bazarboost.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FacturasPaginadasDTO {

    private List<FacturaDTO> facturas;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;
    private boolean esPrimera;
    private boolean esUltima;

}
