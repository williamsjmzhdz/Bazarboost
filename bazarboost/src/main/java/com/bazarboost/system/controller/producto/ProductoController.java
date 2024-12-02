package com.bazarboost.system.controller.producto;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.shared.exception.AccesoDenegadoException;
import com.bazarboost.shared.exception.ProductoNoEncontradoException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.system.model.Producto;
import com.bazarboost.system.service.CategoriaService;
import com.bazarboost.system.service.DescuentoService;
import com.bazarboost.system.service.ProductoService;
import com.bazarboost.system.service.UsuarioService;
import com.bazarboost.shared.util.ProductoUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

/**
 *
 * NOTA IMPORTANTE: Las operaciones Create, Update y Delete de un producto están hechas con MVC y no con REST como
 * el resto de la aplicación por motivos del entregable final del módulo 9.
 *
 */
@Controller
@RequestMapping("/productos")
@Slf4j
public class ProductoController {

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
        log.debug("Mostrando la plantilla de la lista de productos");
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-productos";
    }


    @GetMapping("/vendedor")
    public String mostrarListaProductosVendedor(
            Model model,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Mostrando la plantilla de la lista de productos del vendedor {}", usuarioId);
        model.addAttribute("requestURI", request.getRequestURI());
        return "lista-productos-vendedor";
    }

    @GetMapping("/vendedor/crear")
    public String mostrarFormularioCrearProducto(
            Model model,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Mostrando la plantilla del formulario de creación de producto para vendedor {}", usuarioId);
        model.addAttribute("modo", "crear");
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
        model.addAttribute("descuentos", descuentoService.obtenerDescuentosDTOPorUsuario(usuarioId));
        model.addAttribute("requestURI", request.getRequestURI());
        return "crear-editar-producto";
    }

    @GetMapping("/vendedor/editar/{productoId}")
    public String mostrarFormularioEditarProducto(
            @PathVariable Integer productoId,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Mostrando la plantilla del formulario de edición para producto {} del vendedor {}", productoId, usuarioId);
        try {
            model.addAttribute("modo", "editar");
            model.addAttribute("producto", productoService.obtenerProductoPorId(productoId, usuarioId));
            model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
            model.addAttribute("descuentos", descuentoService.obtenerDescuentosDTOPorUsuario(usuarioId));
            model.addAttribute("requestURI", request.getRequestURI());
            return "crear-editar-producto";
        } catch (ProductoNoEncontradoException ex) {
            log.error("Error al mostrar formulario de edición: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", "El producto que intentas editar no existe.");
            return "redirect:/productos/vendedor";
        } catch (UsuarioNoEncontradoException ex) {
            log.error("Error al mostrar formulario de edición: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", "Error al editar el producto: usuario no encontrado.");
            return "redirect:/productos/vendedor";
        } catch (AccesoDenegadoException ex) {
            log.error("Error al mostrar formulario de edición: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/productos/vendedor";
        }
    }

    @GetMapping("/detalle-producto/{id}")
    public String mostrarDetalleProducto(Model model, HttpServletRequest request, @PathVariable Integer id) {
        log.debug("Mostrando la plantilla del detalle del producto: {}", id);
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
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        boolean esEdicion = producto.getProductoId() != null;

        if (!esEdicion && (imagenArchivo == null || imagenArchivo.isEmpty() ||
                imagenArchivo.getOriginalFilename() == null ||
                imagenArchivo.getOriginalFilename().isBlank())) {
            resultado.rejectValue("imagenUrl", "NotBlank", "La imagen no puede estar en blanco.");
        } else if (imagenArchivo != null && !imagenArchivo.isEmpty() &&
                imagenArchivo.getOriginalFilename().length() > 255) {
            resultado.rejectValue("imagenUrl", "Size",
                    "El nombre de la imagen no puede exceder los 255 caracteres.");
        }

        log.info("{}ando producto por vendedor {}",
                esEdicion ? "Edit" : "Cre", usuarioId);

        if (resultado.hasErrors()) {
            model.addAttribute("modo", esEdicion ? "editar" : "crear");
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
            model.addAttribute("descuentos",
                    descuentoService.obtenerDescuentosDTOPorUsuario(usuarioId));
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("errores", resultado.getAllErrors());
            log.warn("Errores de validación al guardar producto");
            return "crear-editar-producto";
        }

        try {
            if (esEdicion) {
                productoService.obtenerProductoPorId(producto.getProductoId(), usuarioId);
            }

            producto.setUsuario(usuarioService.obtenerUsuarioPorId(usuarioId));
            producto.setCategoria(categoriaService.obtenerCategoriaPorId(categoriaId));
            if (descuentoId != null && descuentoId != -1) {
                producto.setDescuento(descuentoService.obtenerDescuentoPorIdYUsuarioId(
                        descuentoId, usuarioId));
            } else {
                producto.setDescuento(null);
            }

            if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
                productoUtility.guardarImagenProducto(producto, imagenArchivo);
            }

            productoService.guardarProducto(producto, usuarioId);

            String mensaje = esEdicion
                    ? "¡Producto '" + producto.getNombre() + "' actualizado exitosamente!"
                    : "¡Producto '" + producto.getNombre() + "' creado exitosamente!";
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);

            log.info("Producto {} exitosamente {}",
                    producto.getNombre(), esEdicion ? "actualizado" : "creado");

            return "redirect:/productos/vendedor";

        } catch (ProductoNoEncontradoException ex) {
            log.error("Error al guardar producto: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", "El producto que intentas guardar no existe.");
            return "redirect:/productos/vendedor";
        } catch (UsuarioNoEncontradoException ex) {
            log.error("Error al guardar producto: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar el producto: usuario no encontrado.");
            return "redirect:/productos/vendedor";
        } catch (AccesoDenegadoException ex) {
            log.error("Error al guardar producto: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/productos/vendedor";
        }
    }

    @PostMapping("/vendedor/eliminar/{productoId}")
    public String eliminarProducto(
            @PathVariable Integer productoId,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.info("Eliminando producto {} por vendedor {}", productoId, usuarioId);
        try {
            System.out.println("Controlador 1");
            Producto producto = productoService.eliminarProductoPorId(productoId, usuarioId);
            System.out.println("Controlador 2");
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Producto '" + producto.getNombre() + "' eliminado exitosamente!");
            log.info("Producto {} eliminado exitosamente", producto.getNombre());
            return "redirect:/productos/vendedor";
        } catch (ProductoNoEncontradoException ex) {
            log.error("Error al eliminar producto: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", "El producto que intentas eliminar no existe.");
            return "redirect:/productos/vendedor";
        } catch (UsuarioNoEncontradoException ex) {
            log.error("Error al eliminar producto: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar el producto: usuario no encontrado.");
            return "redirect:/productos/vendedor";
        } catch (AccesoDenegadoException ex) {
            log.error("Error al eliminar producto: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/productos/vendedor";
        }
    }

}
