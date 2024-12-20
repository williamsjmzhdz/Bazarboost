package com.bazarboost.system.controller.metodopago;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/metodos-pago")
@Slf4j
public class MetodoPagoController {

    @GetMapping
    public String mostrarListaMetodosPago(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla de la lista de métodos de pago.");
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-metodos-pago";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla del formulario de creación de métodos de pago.");
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("modo", "crear");
        return "crear-editar-metodo-pago";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(Model model, HttpServletRequest request) {
        log.debug("Mostrando la plantilla del formulario de edición de métodos de pago.");
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("modo", "editar");
        return "crear-editar-metodo-pago";
    }

}
