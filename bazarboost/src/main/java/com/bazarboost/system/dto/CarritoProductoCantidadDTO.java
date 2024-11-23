package com.bazarboost.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarritoProductoCantidadDTO {

    @NotNull(message = "El ID del producto en el carrito no puede ser vacío.")
    private Integer productoCarritoId;
    @NotNull(message = "El ID del producto no puede ser vacío.")
    private Integer productoId;
    @NotNull(message = "La cantidad no puede estar vacía")
    @Min(value = 1, message = "La cantidad no puede ser menor a 1.")
    private Integer cantidad;

}
