package com.bazarboost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public abstract class CategoriaBaseDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 40, message = "El nombre no puede tener más de 40 caracteres.")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "El nombre solo puede contener letras, espacios, guiones y apóstrofres."
    )
    private String nombre;

}
