package com.bazarboost.system.controller.usuario;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {
    @GetMapping
    public String mostrarListaUsuarios(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla de lista de usuarios.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-usuarios";
    }

    @GetMapping("/perfil")
    public String mostrarPerfilUsuario(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla de perfíl de usuario.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "perfil-usuario";
    }
}
