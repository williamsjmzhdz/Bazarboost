package com.bazarboost.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Entity
@Table(name = "ProductosCarrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_carrito_id")
    private Integer productoCarritoId;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "total")
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}
