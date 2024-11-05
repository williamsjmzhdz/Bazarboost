package com.bazarboost.dto;

import lombok.Data;

@Data
public class CarritoMetodoPagoDTO {

    private Integer metodoPagoId;
    private String tipo;
    private String terminacion;
    private String fechaExpiracion;

}
