package com.bazarboost.system.controller.producto;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.ProductoDetalladoDTO;
import com.bazarboost.system.dto.ProductoVendedorDTO;
import com.bazarboost.system.dto.ProductosPaginadosDTO;
import com.bazarboost.system.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Slf4j
public class ProductoRestController {

    private static final Integer TAMANIO_PAGINA_RESENIAS = 10;

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
            @RequestParam(value = "page", defaultValue = "0") int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.debug("Buscando productos: keyword='{}', categoria='{}', orden='{}', página={}",
                keyword, categoria, orden, page);
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        return productoService.buscarProductosConFiltros(keyword, categoria, orden, page, usuarioId);
    }

    @GetMapping("/imagenes/{nombreImagen}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreImagen) throws IOException {
        log.debug("Obteniendo imagen: {}", nombreImagen);
        Path rutaImagen = Paths.get(directorioImagenes).resolve(nombreImagen);
        try {
            Resource recurso = new UrlResource(rutaImagen.toUri());

            String contentType = Files.probeContentType(rutaImagen);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                log.warn("No se pudo determinar el tipo de contenido para {}", nombreImagen);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreImagen + "\"")
                    .body(recurso);
        } catch (IOException e) {
            log.error("Error al obtener imagen {}: {}", nombreImagen, e.getMessage());
            throw e;
        }

    }

    @GetMapping("/mis-productos")
    @ResponseBody
    public List<ProductoVendedorDTO> obtenerMisProductos(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo productos del vendedor {}", usuarioId);
        return productoService.obtenerProductosPorVendedor(usuarioId);
    }

    @GetMapping("/detalle-producto/{id}")
    @ResponseBody
    public ResponseEntity<ProductoDetalladoDTO> obtenerProductoDetalle(
            @PathVariable Integer id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        log.debug("Obteniendo detalle del producto {} para usuario {}", id, usuarioId);
        Pageable pageable = PageRequest.of(page, TAMANIO_PAGINA_RESENIAS);
        ProductoDetalladoDTO detalleDTO = productoService.obtenerProductoDetalle(id, usuarioId, pageable);
        return ResponseEntity.ok().body(detalleDTO);
    }

}
