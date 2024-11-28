package com.bazarboost.auth.controller;

import com.bazarboost.shared.exception.CorreoElectronicoExistenteException;
import com.bazarboost.shared.exception.RolNoEncontradoException;
import com.bazarboost.shared.exception.TelefonoExistenteException;
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
import java.net.URLEncoder;
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
                            Model model) {
        if (result.hasErrors()) {
            return "registro";
        }

        try {
            usuarioService.guardarUsuario(usuario);
            return "redirect:/inicio-sesion?mensajeExito=" +
                    URLEncoder.encode("¡Registro exitoso!", StandardCharsets.UTF_8);
        } catch (CorreoElectronicoExistenteException | TelefonoExistenteException | RolNoEncontradoException e) {
            model.addAttribute("mensajeError", e.getMessage());
            return "registro";
        }
    }
}
