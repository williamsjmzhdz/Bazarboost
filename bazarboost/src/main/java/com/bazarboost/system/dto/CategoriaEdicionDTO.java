package com.bazarboost.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoriaEdicionDTO extends CategoriaBaseDTO {

    @NotNull(message = "No se ha podido identificar la categoria a modificar.")
    @Min(value = 0, message = "La categoria a actualizar no es válida.")
    private Integer categoriaId;

}
