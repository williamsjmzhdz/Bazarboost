package com.bazarboost.controller;

import com.bazarboost.dto.ProductoVendedorDTO;
import com.bazarboost.dto.ProductosPaginadosDTO;
import com.bazarboost.model.Categoria;
import com.bazarboost.model.Descuento;
import com.bazarboost.model.Producto;
import com.bazarboost.model.Usuario;
import com.bazarboost.service.CategoriaService;
import com.bazarboost.service.DescuentoService;
import com.bazarboost.service.ProductoService;
import com.bazarboost.service.UsuarioService;
import com.bazarboost.util.ProductoUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.internal.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private static final Integer VENDEDOR_ID_TEMPORAL = 1;

    @Autowired
    private ProductoUtility productoUtility;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private DescuentoService descuentoService;

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
            @ModelAttribute Producto producto,
            @RequestParam("categoriaId") Integer categoriaId,
            @RequestParam(value = "descuentoId", required = false) Integer descuentoId,
            @RequestParam("imagenArchivo") MultipartFile imagenArchivo,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        producto.setUsuario(usuarioService.obtenerUsuarioPorId(VENDEDOR_ID_TEMPORAL));
        producto.setCategoria(categoriaService.obtenerCategoriaPorId(categoriaId));
        producto.setDescuento(descuentoService.obtenerDescuentoPorIdYUsuario(descuentoId, VENDEDOR_ID_TEMPORAL));
        productoUtility.guardarImagenProducto(producto, imagenArchivo);
        productoService.guardarProducto(producto, VENDEDOR_ID_TEMPORAL);
        redirectAttributes.addFlashAttribute("mensajeExito", "¡Producto creado exitosamente!");
        return "redirect:/productos/vendedor";
    }

    /* ============================= SERVICIOS REST ============================= */

    @GetMapping("/filtrados")
    @ResponseBody
    public ProductosPaginadosDTO buscarProductosConFiltros(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "orden", required = false) String orden,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, 9);
        Page<Producto> productosPaginados = productoService.buscarProductosConFiltros(keyword, categoria, orden, pageable);

        return new ProductosPaginadosDTO(
                productosPaginados.getContent(),
                productosPaginados.getNumber(),
                productosPaginados.getTotalPages(),
                productosPaginados.getTotalElements()
        );
    }

    @GetMapping("/mis-productos")
    @ResponseBody
    public List<ProductoVendedorDTO> obtenerMisProductos() {
        return productoService.obtenerProductosPorVendedor(VENDEDOR_ID_TEMPORAL);
    }
}
