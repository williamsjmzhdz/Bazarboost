package com.bazarboost.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 40, message = "El nombre no puede tener más de 40 caracteres.")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ' ]+$",
            message = "El nombre solo puede contener letras, espacios y apóstrofes."
    )
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio.")
    @Size(max = 40, message = "El apellido paterno no puede tener más de 40 caracteres.")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ' ]+$",
            message = "El apellido paterno solo puede contener letras, espacios y apóstrofes."
    )
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio.")
    @Size(max = 40, message = "El apellido materno no puede tener más de 40 caracteres.")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ' ]+$",
            message = "El apellido materno solo puede contener letras, espacios y apóstrofes."
    )
    private String apellidoMaterno;

    @NotBlank(message = "El número de teléfono es obligatorio.")
    @Size(min = 10, max = 10, message = "El número de teléfono debe tener exactamente 10 dígitos.")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "El número de teléfono solo puede contener 10 dígitos numéricos."
    )
    private String telefono;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Size(max = 80, message = "El correo electrónico no puede tener más de 80 caracteres.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, max = 60, message = "La contraseña debe tener entre 8 y 60 caracteres.")
    private String contrasenia;


}
