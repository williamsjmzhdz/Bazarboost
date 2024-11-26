package com.bazarboost.system.controller.usuario;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @GetMapping
    public String mostrarListaUsuarios(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-usuarios";
    }
}
