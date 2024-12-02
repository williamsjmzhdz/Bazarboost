package com.bazarboost.system.controller.venta;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ventas")
@Slf4j
public class VentaController {

    @GetMapping
    public String mostrarListaVentas(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla de la lista de ventas.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-ventas";
    }

}
