package com.bazarboost.controller.descuento;

import com.bazarboost.dto.DescuentoVendedorDTO;
import com.bazarboost.model.Descuento;
import com.bazarboost.model.Usuario;
import com.bazarboost.service.DescuentoService;
import com.bazarboost.service.UsuarioService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/descuentos")
public class DescuentoRestController {

    private static final Integer VENDEDOR_ID_TEMPORAL = 13;

    @Autowired
    private DescuentoService descuentoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/mis-descuentos")
    @ResponseBody
    private ResponseEntity<List<DescuentoVendedorDTO>> mostrarMisDescuentos() {
        return ResponseEntity.ok().body(descuentoService.obtenerDescuentosDTOPorUsuario(VENDEDOR_ID_TEMPORAL));
    }

    @PostMapping("/crear")
    private ResponseEntity<Void> crearDescuento(@Valid @RequestBody Descuento descuento) throws URISyntaxException {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(VENDEDOR_ID_TEMPORAL);
        descuento.setUsuario(usuario);
        descuentoService.crearDescuento(descuento);
        URI location = new URI("/descuentos");
        return ResponseEntity.created(location).build(); // Devuelve solo el encabezado Location sin cuerpo
    }


}
