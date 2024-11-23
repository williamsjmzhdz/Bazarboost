package com.bazarboost.auth.controller;

import com.bazarboost.system.dto.UsuarioRegistroDTO;
import com.bazarboost.system.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Controller
public class RegistroController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") UsuarioRegistroDTO usuario,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        if (result.hasErrors()) {
            return "registro";
        }

        usuarioService.guardarUsuario(usuario);

        // Codificar el mensaje de éxito
        String encodedMessage = java.net.URLEncoder.encode("¡Registro exitoso! Ya puedes iniciar sesión.", StandardCharsets.UTF_8);
        return "redirect:/inicio-sesion?mensajeExito=" + encodedMessage;
    }
}
