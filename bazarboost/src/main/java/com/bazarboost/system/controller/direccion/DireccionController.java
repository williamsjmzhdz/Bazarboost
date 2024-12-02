package com.bazarboost.system.controller.direccion;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/direcciones")
@Slf4j
public class DireccionController {

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla del formulario de creación de direcciones.");
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("modo", "crear");
        return "crear-editar-direccion";
    }

    @GetMapping
    public String mostrarListaDirecciones(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla de la lista de direcciones.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-direcciones";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla del formulario de edición de direcciones.");
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("modo", "editar");
        return "crear-editar-direccion";
    }

}
