package com.bazarboost.system.controller.factura;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/facturas")
@Slf4j
public class FacturaController {

    @GetMapping("/detalle/{id}")
    public String mostrarDetalleFactura(Model model, HttpServletRequest request, @PathVariable Integer id) {
        log.debug("Mostrando la plantilla del detalle de una factura.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "detalle-factura";
    }

    @GetMapping
    public String mostrarListaFacturas(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla de la lista de facturas.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-facturas";
    }

}
