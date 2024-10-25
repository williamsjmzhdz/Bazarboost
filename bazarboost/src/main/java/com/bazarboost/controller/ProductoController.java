package com.bazarboost.controller;

import com.bazarboost.model.entity.Producto;
import com.bazarboost.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    public String mostrarListaProductos(Model model) {
        Pageable pageable = PageRequest.of(0, 9);
        Page<Producto> productos = productoService.obtenerProductos(pageable);
        model.addAttribute("productos", productos.getContent());
        return "lista-productos";
    }

    @GetMapping("/productos/busqueda")
    @ResponseBody
    public List<Producto> buscarProductos(@RequestParam("keyword") String keyword) {
        return productoService.buscarProductosPorNombre(keyword);
    }

}
