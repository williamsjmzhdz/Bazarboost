package com.bazarboost.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Controller
public class InicioController {

    @GetMapping("/")
    public String redireccionarAProductos() {
        return "redirect:/productos";
    }
}
