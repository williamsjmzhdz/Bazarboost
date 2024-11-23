package com.bazarboost.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DireccionEdicionDTO extends DireccionBaseDTO {
    @NotNull(message = "No se ha podido identificar la dirección a modificar.")
    @Min(value = 0, message = "La dirección seleccionada no es válida.")
    private Integer direccionId;
}
