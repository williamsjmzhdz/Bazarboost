package com.bazarboost.dto;

import lombok.Data;

import java.time.LocalDateTime;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Data
public class ReseniaDTO {
    private Integer reseniaId;
    private String comentario;
    private Integer calificacion;
    private LocalDateTime fecha;
    private UsuarioReseniaDTO usuario;
}
