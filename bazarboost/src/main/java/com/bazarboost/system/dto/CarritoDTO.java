package com.bazarboost.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CarritoDTO {

    private List<CarritoProductoDTO> carritoProductoDTOS;
    private List<CarritoDireccionDTO> carritoDireccionDTOS;
    private List<CarritoMetodoPagoDTO> carritoMetodoPagoDTOS;

}
