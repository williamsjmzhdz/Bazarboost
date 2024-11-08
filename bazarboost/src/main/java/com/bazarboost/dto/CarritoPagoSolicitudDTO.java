package com.bazarboost.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CarritoPagoSolicitudDTO {

    @NotNull(message = "Por favor, agrega productos al carrito para continuar con el pago")
    @Valid
    private List<ProductoPagoDTO> productos;
    @NotNull(message = "Selecciona un método de pago para continuar")
    private Integer metodoPagoId;
    @NotNull(message = "Especifica una dirección de envío para tu pedido")
    private Integer direccionId;

}
