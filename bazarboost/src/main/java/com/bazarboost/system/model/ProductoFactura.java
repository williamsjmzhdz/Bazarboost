package com.bazarboost.system.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */

@Entity
@Table(name = "ProductosFacturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_factura_id")
    private Integer productoFacturaId;

    @Column(name = "producto_nombre")
    private String productoNombre;

    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "total")
    private Double total;

    @Column(name = "descuento_unitario_porcentaje")
    private Integer descuentoUnitarioPorcentaje;

    @Column(name = "descuento_unitario_valor")
    private BigDecimal descuentoUnitarioValor;

    @Column(name = "descuento_nombre")
    private String descuentoNombre;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}
