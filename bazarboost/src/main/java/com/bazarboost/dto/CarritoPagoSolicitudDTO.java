package com.bazarboost.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CarritoPagoSolicitudDTO {

    @NotNull(message = "Por favor, agrega productos al carrito para continuar con el pago")
    @Valid
    private List<ProductoPagoDTO> productos;
    @NotNull(message = "Selecciona un método de pago para continuar")
    @Min(value = 1, message = "El método de pago ingresado es inválido.")
    private Integer metodoPagoId;
    @NotNull(message = "Especifica una dirección de envío para tu pedido")
    @Min(value = 1, message = "La dirección ingresada es inválida.")
    private Integer direccionId;

}
