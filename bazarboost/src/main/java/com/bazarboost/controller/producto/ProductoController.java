package com.bazarboost.controller.producto;

import com.bazarboost.model.Producto;
import com.bazarboost.service.CategoriaService;
import com.bazarboost.service.DescuentoService;
import com.bazarboost.service.ProductoService;
import com.bazarboost.service.UsuarioService;
import com.bazarboost.util.ProductoUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
@RequestMapping("/productos")
public class ProductoController {


    private static final Integer VENDEDOR_ID_TEMPORAL = 1;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private DescuentoService descuentoService;

    @Autowired
    private ProductoUtility productoUtility;

    /* ============================= RENDERIZADO DE PLANTILLAS ============================= */

    @GetMapping
    public String mostrarListaProductos(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-productos";
    }


    @GetMapping("/vendedor")
    public String mostrarListaProductosVendedor(Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-productos-vendedor";
    }

    @GetMapping("/vendedor/crear")
    public String mostrarFormularioCrearProducto(Model model, HttpServletRequest request) {
        model.addAttribute("modo", "crear");
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
        model.addAttribute("descuentos", descuentoService.obtenerDescuentosPorUsuario(VENDEDOR_ID_TEMPORAL));
        model.addAttribute("requestURI", request.getRequestURI());
        return "crear-editar-producto";
    }

    /* ============================= OPERACIONES DE PRODUCTO ============================= */

    @PostMapping("/crear")
    public String crearProducto(
            @Valid @ModelAttribute("producto") Producto producto,
            BindingResult resultado,
            @RequestParam("categoriaId") Integer categoriaId,
            @RequestParam(value = "descuentoId", required = false) Integer descuentoId,
            @RequestParam("imagenArchivo") MultipartFile imagenArchivo,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            Model model
    ) throws IOException {

        // Validación de imagenArchivo para que no esté en blanco
        if (imagenArchivo.isEmpty() || imagenArchivo.getOriginalFilename() == null || imagenArchivo.getOriginalFilename().isBlank()) {
            resultado.rejectValue("imagenUrl", "NotBlank", "La imagen no puede estar en blanco.");
        } else if (imagenArchivo.getOriginalFilename().length() > 255) {
            // Validación de longitud del nombre del archivo
            resultado.rejectValue("imagenUrl", "Size", "El nombre de la imagen no puede exceder los 255 caracteres.");
        }

        if (resultado.hasErrors()) {
            model.addAttribute("modo", "crear");
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
            model.addAttribute("descuentos", descuentoService.obtenerDescuentosPorUsuario(VENDEDOR_ID_TEMPORAL));
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("errores", resultado.getAllErrors());
            return "crear-editar-producto";
        }

        producto.setUsuario(usuarioService.obtenerUsuarioPorId(VENDEDOR_ID_TEMPORAL));
        producto.setCategoria(categoriaService.obtenerCategoriaPorId(categoriaId));
        producto.setDescuento(descuentoService.obtenerDescuentoPorIdYUsuario(descuentoId, VENDEDOR_ID_TEMPORAL));
        productoUtility.guardarImagenProducto(producto, imagenArchivo);
        productoService.guardarProducto(producto, VENDEDOR_ID_TEMPORAL);
        redirectAttributes.addFlashAttribute("mensajeExito", "¡Producto '" + producto.getNombre() + "' creado exitosamente!");
        return "redirect:/productos/vendedor";
    }

}
