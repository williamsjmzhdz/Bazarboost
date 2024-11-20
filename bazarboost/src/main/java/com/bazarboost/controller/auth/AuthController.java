package com.bazarboost.controller.auth;

import com.bazarboost.model.Usuario;
import com.bazarboost.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/inicio-sesion")
    public String inicioSesion() {
        return "inicio-sesion";
    }

}
