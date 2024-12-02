package com.bazarboost.system.controller.productocarrito;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carrito")
@Slf4j
public class ProductosCarritoController {

    @GetMapping
    public String mostrarCarritoCompras(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla del carrito de compras.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "carrito-compras";
    }

}
