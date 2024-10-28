package com.bazarboost.controller.producto;

import com.bazarboost.dto.ProductoVendedorDTO;
import com.bazarboost.dto.ProductosPaginadosDTO;
import com.bazarboost.model.Producto;
import com.bazarboost.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoRestController {

    private static final Integer VENDEDOR_ID_TEMPORAL = 1;

    @Autowired
    private ProductoService productoService;

    @Value("${app.imagenes.directorio}")
    private String directorioImagenes;

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
    @GetMapping("/imagenes/{nombreImagen}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreImagen) throws IOException {
        Path rutaImagen = Paths.get(directorioImagenes).resolve(nombreImagen);
        Resource recurso = new UrlResource(rutaImagen.toUri());

        // Determina el tipo de contenido dinámicamente
        String contentType = Files.probeContentType(rutaImagen);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Usa un tipo genérico si no se puede determinar
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreImagen + "\"")
                .body(recurso);
    }

    @GetMapping("/mis-productos")
    @ResponseBody
    public List<ProductoVendedorDTO> obtenerMisProductos() {
        return productoService.obtenerProductosPorVendedor(VENDEDOR_ID_TEMPORAL);
    }

}
