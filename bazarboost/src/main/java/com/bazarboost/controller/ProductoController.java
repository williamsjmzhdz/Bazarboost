package com.bazarboost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductoController {

    @GetMapping("/productos")
    public String mostrarListaProductos(Model model) {
        return "lista-productos";
    }

}
