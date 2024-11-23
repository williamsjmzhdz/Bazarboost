package com.bazarboost.system.dto;

import com.bazarboost.system.model.auxiliar.TipoTarjeta;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public abstract class MetodoPagoBaseDTO {
    @NotBlank(message = "El nombre del titular es obligatorio")
    @Size(max = 120, message = "El nombre del titular no puede tener más de 120 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "El nombre del titular solo puede contener letras, espacios, guiones y apóstrofes"
    )
    private String nombreTitular;

    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Pattern(
            regexp = "^[0-9]{13,19}$",
            message = "El número de tarjeta debe tener entre 13 y 19 dígitos"
    )
    private String numeroTarjeta;

    @NotNull(message = "La fecha de expiración es obligatoria")
    @Future(message = "La fecha de expiración debe ser una fecha futura")
    private LocalDate fechaExpiracion;

    @NotNull(message = "El tipo de tarjeta es obligatorio")
    private TipoTarjeta tipoTarjeta;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    @Digits(integer = 8, fraction = 2, message = "El monto debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal monto;
}
