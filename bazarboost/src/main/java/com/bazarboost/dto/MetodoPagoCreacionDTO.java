package com.bazarboost.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class MetodoPagoCreacionDTO {

    @NotBlank(message = "El nombre del titular es obligatorio y no puede estar vacío o contener solo espacios en blanco")
    @Length(max = 120, message = "El nombre del titular no puede tener más de 120 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
            message = "El nombre del titular solo puede contener letras y espacios"
    )
    private String nombreTitular;

    @NotBlank(message = "El número de tarjeta es obligatorio y no puede estar vacío o contener solo espacios en blanco")
    @Length(max = 20, message = "El número de tarjeta no puede tener más de 20 caracteres")
    @Pattern(
            regexp = "^[0-9]+$",
            message = "El número de tarjeta solo puede contener dígitos"
    )
    private String numeroTarjeta;

    @NotNull(message = "La fecha de expiración es obligatoria.")
    @Future(message = "La fecha de expiración debe ser una fecha futura.")
    private LocalDate fechaExpiracion;

    @NotBlank(message = "El tipo de tarjeta es obligatorio.")
    @Pattern(regexp = "Crédito|Débito", message = "El tipo de tarjeta solo puede ser 'Crédito' o 'Débito'.")
    private String tipoTarjeta;

    @NotNull(message = "El monto es obligatorio.")
    @Min(value = 0, message = "El monto no puede ser negativo.")
    private Double monto;

}
