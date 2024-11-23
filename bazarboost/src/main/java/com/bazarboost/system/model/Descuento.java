package com.bazarboost.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */

@Entity
@Table(name = "Descuentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "descuento_id")
    private Integer descuentoId;

    @NotNull(message = "El porcentaje no puede estar vacío")
    @Min(value = 1, message = "El porcentaje no puede ser menor a 1%")
    @Max(value = 100, message = "El porcentaje no puede ser mayor a 100%")
    @Column(name = "porcentaje")
    private Integer porcentaje;

    @NotBlank(message = "El nombre no puede estar vacío o contener solo espacios en blanco")
    @Length(max = 40, message = "El nombre no puede exceder los 40 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "El nombre solo puede contener letras, espacios, guiones y apóstrofres."
    )
    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
