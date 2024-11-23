package com.bazarboost.system.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */

@Entity
@Table(name = "UsuariosRoles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_rol_id")
    private Integer usuarioRolId;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
}
