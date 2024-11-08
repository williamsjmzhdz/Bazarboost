package com.bazarboost.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoPagoDTO {

    @NotNull(message = "El producto seleccionado no es válido, intenta nuevamente.")
    @Min(value = 1, message = "El producto seleccionado no es válido, intenta nuevamente.")
    private Integer productoId;

    // Solo para mensajes personalizados
    private String nombreProducto;

    @NotNull(message = "Por favor, ingresa una cantidad para el producto '{nombreProducto}'.")
    @Min(value = 1, message = "La cantidad para el producto '{nombreProducto}' debe ser al menos 1.")
    private Integer cantidad;

}
