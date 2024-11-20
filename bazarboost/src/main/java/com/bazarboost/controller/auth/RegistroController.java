package com.bazarboost.controller.auth;

import com.bazarboost.dto.UsuarioRegistroDTO;
import com.bazarboost.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                            RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors()) {
            return "registro";
        }

        usuarioService.guardarUsuario(usuario);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "¡Registro exitoso! Ya puedes iniciar sesión.");

        return "redirect:/inicio-sesion";
    }
}
