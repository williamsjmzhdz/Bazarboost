package com.bazarboost.dto;

import jakarta.validation.constraints.Max;
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

    @Min(value = 1, message = "El descuento es inválido o no existe.")
    private Integer descuentoId;

    @Min(value = 1, message = "El porcentaje de descuento no puede ser menor a 1%.")
    @Max(value = 100, message = "El porcentaje de descuento no puede ser mayor a 100%.")
    private Integer descuentoUnitarioPorcentaje;

}
