package com.bazarboost.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Entity
@Table(name = "Productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Integer productoId;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Length(min = 1, max = 40, message = "El nombre debe tener entre 1 y 40 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ'\\- ]+$",
            message = "El nombre del producto solo puede contener letras, espacios, guiones y apóstrofes"
    )
    @Column(name = "nombre")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Length(max = 255, message = "La descripción no puede superar los 255 caracteres")
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener un máximo de 10 dígitos enteros y 2 decimales")
    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    @NotNull(message = "La existencia es obligatoria")
    @Min(value = 0, message = "La existencia no puede ser negativa")
    @Column(name = "existencia")
    private Integer existencia;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "descuento_id")
    private Descuento descuento;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
