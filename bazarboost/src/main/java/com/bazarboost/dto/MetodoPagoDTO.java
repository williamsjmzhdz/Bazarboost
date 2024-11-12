package com.bazarboost.dto;

import lombok.Data;

@Data
public class MetodoPagoDTO {

    private Integer metodoPagoId;
    private String nombreTitular;
    private String terminacion;
    private String fechaExpiracion;
    private String tipo;
    private Double monto;

}
