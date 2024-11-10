package com.bazarboost.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoPagoDTO {

    @NotNull(message = "El producto seleccionado no es válido, intenta nuevamente.")
    @Min(value = 1, message = "El producto seleccionado no es válido, intenta nuevamente.")
    private Integer productoId;

    @NotNull(message = "Por favor, ingresa una cantidad para el producto.")
    @Min(value = 1, message = "La cantidad para el producto debe ser al menos 1.")
    private Integer cantidad;

}
