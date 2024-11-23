package com.bazarboost.system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReseniaRespuestaDTO {
    private String comentario;
    private Integer calificacion;
    private LocalDateTime fecha;
    private Integer reseniaId;
    private String autor;
    private BigDecimal calificacionPromedioActualizada;
}
