package com.bazarboost.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public abstract class DireccionBaseDTO {

    @NotBlank(message = "El estado es obligatorio.")
    @Size(max = 40, message = "El estado no puede tener más de 40 caracteres.")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "El estado solo puede contener letras, espacios, guiones y apóstrofres."
    )
    private String estado;

    @NotBlank(message = "La ciudad es obligatoria.")
    @Size(max = 40, message = "La ciudad no puede tener más de 40 caracteres.")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "La ciudad solo puede contener letras, espacios, guiones y apóstrofres."
    )
    private String ciudad;

    @NotBlank(message = "La colonia es obligatoria.")
    @Size(max = 40, message = "La colonia no puede tener más de 40 caracteres.")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "La colonia solo puede contener letras, espacios, guiones y apóstrofres."
    )
    private String colonia;

    @NotBlank(message = "La calle es obligatoria.")
    @Size(max = 60, message = "La calle no puede tener más de 60 caracteres.")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "La calle solo puede contener letras, espacios, guiones y apóstrofres."
    )
    private String calle;

    @NotNull
    @Min(value = 0, message = "El número de domicilio no puede ser negativo.")
    private Integer numeroDomicilio;


    @NotBlank(message = "El código postal es obligatorio.")
    @Size(max = 5, message = "El código postal no puede tener más de 5 digitos.")
    @Pattern(
            regexp = "^\\d{5}$",
            message = "El código postal debe contener exactamente 5 dígitos."
    )
    private String codigoPostal;

}
