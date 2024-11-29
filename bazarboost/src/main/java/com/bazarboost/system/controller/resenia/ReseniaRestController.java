package com.bazarboost.system.controller.resenia;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.system.dto.CalificacionPromedioDTO;
import com.bazarboost.system.dto.ReseniaCreacionDTO;
import com.bazarboost.system.dto.ReseniaEdicionDTO;
import com.bazarboost.system.dto.ReseniaRespuestaDTO;
import com.bazarboost.system.service.ReseniaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/resenias")
public class ReseniaRestController {

    @Autowired
    private ReseniaService reseniaService;

    @PostMapping
    public ResponseEntity<ReseniaRespuestaDTO> crearResenia(
            @Valid @RequestBody ReseniaCreacionDTO reseniaDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        ReseniaRespuestaDTO respuesta = reseniaService.crearResenia(reseniaDTO, usuarioId);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @PutMapping("/{reseniaId}")
    public ResponseEntity<ReseniaRespuestaDTO> editarResenia(
            @PathVariable Integer reseniaId,
            @Valid @RequestBody ReseniaEdicionDTO reseniaDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reseniaDTO.setReseniaId(reseniaId);
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        ReseniaRespuestaDTO respuesta = reseniaService.editarResenia(reseniaDTO, usuarioId);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{reseniaId}")
    public ResponseEntity<CalificacionPromedioDTO> eliminarResenia(
            @PathVariable Integer reseniaId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer usuarioId = userDetails.getUsuario().getUsuarioId();
        CalificacionPromedioDTO calificacionPromedioDTO = reseniaService.eliminarResenia(reseniaId, usuarioId);
        return ResponseEntity.ok(calificacionPromedioDTO);
    }

}

