package com.bazarboost.auth.controller;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/inicio-sesion")
    public String inicioSesion() {
        log.debug("Mostrando página de inicio de sesión");
        return "inicio-sesion";
    }

    @PostMapping("/login")
    public String login(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Login exitoso para usuario: {}", userDetails.getUsername());
        return "redirect:/productos";
    }

}
