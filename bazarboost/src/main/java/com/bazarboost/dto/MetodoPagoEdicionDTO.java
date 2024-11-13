package com.bazarboost.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MetodoPagoEdicionDTO extends MetodoPagoBaseDTO {
    @NotNull(message = "No se ha podido identificar el método de pago a modificar")
    @Min(value = 0, message = "El método de pago seleccionado no es válido")
    private Integer metodoPagoId;
}
