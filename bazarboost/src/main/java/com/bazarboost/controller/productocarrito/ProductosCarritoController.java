package com.bazarboost.controller.productocarrito;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carrito")
public class ProductosCarritoController {

    @GetMapping
    public String mostrarCarritoCompras(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "carrito-compras";
    }

}
