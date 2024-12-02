package com.bazarboost.system.controller.descuento;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.shared.exception.DescuentoNoEncontradoException;
import com.bazarboost.system.model.Descuento;
import com.bazarboost.system.service.DescuentoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/descuentos")
@Slf4j
public class DescuentoController {

    @Autowired
    private DescuentoService descuentoService;

    @GetMapping
    public String mostrarListaDescuentos(
            Model model,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.debug("Mostrando la plantilla de lista de descuentos.");
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        model.addAttribute("descuentos", descuentoService.obtenerDescuentosDTOPorUsuario(usuarioId));
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-descuentos";
    }

    @GetMapping("/crear")
    public String crearDescuento(
            Model model,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.debug("Mostrando la plantilla del formulario de creación de descuentos.");
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("modo", "crear");
        model.addAttribute("descuento", new Descuento());
        return "crear-editar-descuento";
    }

    @GetMapping("/editar/{descuentoId}")
    public String editarDescuento(
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @PathVariable Integer descuentoId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.debug("Mostrando la plantilla del formulario de edición de descuentos.");
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        try {
            Descuento descuento = descuentoService.obtenerDescuentoPorIdYUsuarioId(descuentoId, usuarioId);
            model.addAttribute("descuento", descuento);
            model.addAttribute("modo", "editar");
            model.addAttribute("requestURI", request.getRequestURI());
            return "crear-editar-descuento";
        } catch (DescuentoNoEncontradoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "El descuento que intentas editar no te pertenece o no existe.");
            return "redirect:/descuentos";
        }
    }
}
