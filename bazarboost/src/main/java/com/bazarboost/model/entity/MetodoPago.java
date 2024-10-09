package com.bazarboost.model.entity;

import com.bazarboost.model.entity.auxiliar.TipoTarjeta;
import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */

@Entity
@Table(name = "MetodosPago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodo_pago_id")
    private Integer metodoPagoId;

    @Column(name = "nombre_titular")
    private String nombreTitular;

    @Column(name = "numero_tarjeta")
    private String numeroTarjeta;

    @Column(name = "fecha_expiracion")
    private YearMonth fechaExpiracion;

    @Column(name = "tipo_tarjeta")
    @Enumerated(EnumType.STRING)
    private TipoTarjeta tipoTarjeta;

    @Column(name = "monto")
    private Double monto;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}

