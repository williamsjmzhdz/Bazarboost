package com.bazarboost.system.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReseniaEliminacionDTO {
    @NotNull(message = "El ID de la reseña es obligatorio")
    private Integer reseniaId;
}

