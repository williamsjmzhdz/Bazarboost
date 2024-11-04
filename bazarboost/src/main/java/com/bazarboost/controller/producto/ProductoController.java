package com.bazarboost.controller.producto;

import com.bazarboost.exception.AccesoDenegadoException;
import com.bazarboost.exception.ProductoNoEncontradoException;
import com.bazarboost.exception.UsuarioNoEncontradoException;
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
        model.addAttribute("descuentos", descuentoService.obtenerDescuentosDTOPorUsuario(VENDEDOR_ID_TEMPORAL));
        model.addAttribute("requestURI", request.getRequestURI());
        return "crear-editar-producto";
    }

    @GetMapping("/vendedor/editar/{productoId}")
    public String mostrarFormularioCrearProducto(
            @PathVariable Integer productoId,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            model.addAttribute("modo", "editar");
            model.addAttribute("producto", productoService.obtenerProductoPorId(productoId, VENDEDOR_ID_TEMPORAL ));
            model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
            model.addAttribute("descuentos", descuentoService.obtenerDescuentosDTOPorUsuario(VENDEDOR_ID_TEMPORAL));
            model.addAttribute("requestURI", request.getRequestURI());
            return "crear-editar-producto";
        } catch (ProductoNoEncontradoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "El producto que intentas editar no existe.");
            return "redirect:/productos/vendedor";
        } catch (UsuarioNoEncontradoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al editar el producto: usuario no encontrado.");
            return "redirect:/productos/vendedor";
        } catch (AccesoDenegadoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/productos/vendedor";
        }

    }

    @GetMapping("/detalle-producto/{id}")
    public String mostrarDestalleProducto(Model model, HttpServletRequest request, @PathVariable Integer id) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "detalle-producto";
    }

    /* ============================= OPERACIONES DE PRODUCTO ============================= */

    @PostMapping("/guardar")
    public String guardarProducto(
            @Valid @ModelAttribute("producto") Producto producto,
            BindingResult resultado,
            @RequestParam("categoriaId") Integer categoriaId,
            @RequestParam(value = "descuentoId", required = false) Integer descuentoId,
            @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            Model model
    ) throws IOException {
        boolean esEdicion = producto.getProductoId() != null;

        // Validación de imagen
        if (!esEdicion && (imagenArchivo == null || imagenArchivo.isEmpty() ||
                imagenArchivo.getOriginalFilename() == null ||
                imagenArchivo.getOriginalFilename().isBlank())) {
            resultado.rejectValue("imagenUrl", "NotBlank", "La imagen no puede estar en blanco.");
        } else if (imagenArchivo != null && !imagenArchivo.isEmpty() &&
                imagenArchivo.getOriginalFilename().length() > 255) {
            resultado.rejectValue("imagenUrl", "Size",
                    "El nombre de la imagen no puede exceder los 255 caracteres.");
        }

        if (resultado.hasErrors()) {
            model.addAttribute("modo", esEdicion ? "editar" : "crear");
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
            model.addAttribute("descuentos",
                    descuentoService.obtenerDescuentosDTOPorUsuario(VENDEDOR_ID_TEMPORAL));
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("errores", resultado.getAllErrors());
            return "crear-editar-producto";
        }

        try {
            // Si es edición, verificar que el producto pertenezca al vendedor
            if (esEdicion) {
                productoService.obtenerProductoPorId(producto.getProductoId(), VENDEDOR_ID_TEMPORAL);
            }

            // Configurar las relaciones
            producto.setUsuario(usuarioService.obtenerUsuarioPorId(VENDEDOR_ID_TEMPORAL));
            producto.setCategoria(categoriaService.obtenerCategoriaPorId(categoriaId));
            producto.setDescuento(descuentoService.obtenerDescuentoPorIdYUsuarioId(
                    descuentoId, VENDEDOR_ID_TEMPORAL));

            // Manejar la imagen solo si se subió una nueva
            if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
                productoUtility.guardarImagenProducto(producto, imagenArchivo);
            }

            // Guardar el producto
            productoService.guardarProducto(producto, VENDEDOR_ID_TEMPORAL);

            // Mensaje de éxito
            String mensaje = esEdicion
                    ? "¡Producto '" + producto.getNombre() + "' actualizado exitosamente!"
                    : "¡Producto '" + producto.getNombre() + "' creado exitosamente!";
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);

            return "redirect:/productos/vendedor";

        } catch (ProductoNoEncontradoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "El producto que intentas guardar no existe.");
            return "redirect:/productos/vendedor";
        } catch (UsuarioNoEncontradoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar el producto: usuario no encontrado.");
            return "redirect:/productos/vendedor";
        } catch (AccesoDenegadoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/productos/vendedor";
        }
    }

    @PostMapping("/vendedor/eliminar/{productoId}")
    public String eliminarProducto(
            @PathVariable Integer productoId,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {
        try {
            Producto producto = productoService.eliminarProductoPorId(productoId, VENDEDOR_ID_TEMPORAL);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Producto '" + producto.getNombre() + "' eliminado exitosamente!");
            return "redirect:/productos/vendedor";
        } catch (ProductoNoEncontradoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "El producto que intentas eliminar no existe.");
            return "redirect:/productos/vendedor";
        } catch (UsuarioNoEncontradoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar el producto: usuario no encontrado.");
            return "redirect:/productos/vendedor";
        } catch (AccesoDenegadoException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/productos/vendedor";
        }
    }

}
