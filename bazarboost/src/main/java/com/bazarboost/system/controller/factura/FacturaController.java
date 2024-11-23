package com.bazarboost.system.controller.factura;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @GetMapping("/detalle/{id}")
    public String mostrarDetalleFactura(Model model, HttpServletRequest request, @PathVariable Integer id) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "detalle-factura";
    }

    @GetMapping
    public String mostrarListaFacturas(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-facturas";
    }

}
