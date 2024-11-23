package com.bazarboost.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class ProductoPagoDTO {

    @NotNull(message = "El producto seleccionado no es válido, intenta nuevamente.")
    @Min(value = 1, message = "El producto seleccionado no es válido, intenta nuevamente.")
    private Integer productoId;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Length(min = 1, max = 40, message = "El nombre debe tener entre 1 y 40 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "El nombre del producto solo puede contener letras, espacios, guiones y apóstrofes"
    )
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener un máximo de 10 dígitos enteros y 2 decimales")
    private BigDecimal precioUnitario;

    @NotNull(message = "Por favor, ingresa una cantidad para el producto.")
    @Min(value = 1, message = "La cantidad para el producto debe ser al menos 1.")
    private Integer cantidad;

    @Min(value = 1, message = "El descuento es inválido o no existe.")
    private Integer descuentoId;

    @Min(value = 1, message = "El porcentaje de descuento no puede ser menor a 1%.")
    @Max(value = 100, message = "El porcentaje de descuento no puede ser mayor a 100%.")
    private Integer descuentoUnitarioPorcentaje;

}
