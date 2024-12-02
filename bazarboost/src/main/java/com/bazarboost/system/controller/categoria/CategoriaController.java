package com.bazarboost.system.controller.categoria;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categorias")
@Slf4j
public class CategoriaController {

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla del formulario de creación de categorías.");
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("modo", "crear");
        return "crear-editar-categoria";
    }

    @GetMapping
    public String mostrarListaCategorias(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla de la lista de categorías.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-categorias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla del formulario de edición de categorías.");
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("modo", "editar");
        return "crear-editar-categoria";
    }

}
