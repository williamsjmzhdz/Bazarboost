package com.bazarboost.system.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReseniaCreacionDTO {
    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación debe ser al menos 1")
    @Max(value = 5, message = "La calificación no puede ser mayor a 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(max = 255, message = "El comentario no puede tener más de 255 caracteres")
    private String comentario;

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer productoId;
}

