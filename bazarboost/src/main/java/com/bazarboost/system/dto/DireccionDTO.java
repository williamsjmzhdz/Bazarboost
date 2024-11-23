package com.bazarboost.system.dto;

import lombok.Data;

@Data
public class DireccionDTO {

    private Integer direccionId;
    private String estado;
    private String ciudad;
    private String colonia;
    private String calle;
    private Integer numeroDomicilio;
    private String codigoPostal;

}
