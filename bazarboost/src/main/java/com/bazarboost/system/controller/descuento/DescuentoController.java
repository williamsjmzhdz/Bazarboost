package com.bazarboost.system.controller.descuento;

import com.bazarboost.system.exception.DescuentoNoEncontradoException;
import com.bazarboost.system.model.Descuento;
import com.bazarboost.service.DescuentoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/descuentos")
public class DescuentoController {

    private static final Integer USUARIO_ID_TEMP = 1;

    @Autowired
    private DescuentoService descuentoService;

    @GetMapping
    public String mostrarListaDescuentos(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-descuentos";
    }

    @GetMapping("/crear")
    public String crearDescuento(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("modo", "crear");
        return "crear-editar-descuento";
    }

    @GetMapping("/editar/{descuentoId}")
    public String editarDescuento(Model model,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes,
                                  @PathVariable Integer descuentoId) {
        try {
            Descuento descuento = descuentoService.obtenerDescuentoPorIdYUsuarioId(
                    descuentoId,
                    USUARIO_ID_TEMP
            );

            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("modo", "editar");
            model.addAttribute("descuento", descuento);

            return "crear-editar-descuento";
        } catch (DescuentoNoEncontradoException ex) {
            // Usamos addAttribute en lugar de addFlashAttribute para que aparezca en la URL
            redirectAttributes.addAttribute("mensajeError",
                    "El descuento que intentas editar no te pertenece.");
            return "redirect:/descuentos";
        }
    }

}
