package com.bazarboost.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosPaginadosDTO {
    private List<UsuarioDTO> usuarios;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;
    private boolean esPrimera;
    private boolean esUltima;
}
