package com.bazarboost.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VentasPaginadasDTO {
    private List<VentaDTO> ventas;
    private Integer paginaActual;
    private Integer totalPaginas;
    private Long totalElementos;
    private Boolean esPrimeraPagina;
    private Boolean esUltimaPagina;
}
