package com.bazarboost.controller.descuento;

import com.bazarboost.dto.DescuentoVendedorDTO;
import com.bazarboost.service.DescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/descuentos")
public class DescuentoRestController {

    private static final Integer VENDEDOR_ID_TEMPORAL = 1;

    @Autowired
    private DescuentoService descuentoService;

    @GetMapping("/mis-descuentos")
    @ResponseBody
    private List<DescuentoVendedorDTO> mostrarMisDescuentos() {
        return descuentoService.obtenerDescuentosDTOPorUsuario(VENDEDOR_ID_TEMPORAL);
    }

}
