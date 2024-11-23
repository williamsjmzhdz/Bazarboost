package com.bazarboost.system.controller.resenia;

import com.bazarboost.system.dto.CalificacionPromedioDTO;
import com.bazarboost.system.dto.ReseniaCreacionDTO;
import com.bazarboost.system.dto.ReseniaEdicionDTO;
import com.bazarboost.system.dto.ReseniaRespuestaDTO;
import com.bazarboost.service.ReseniaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/resenias")
public class ReseniaRestController {

    @Autowired
    private ReseniaService reseniaService;

    private static final Integer USUARIO_ID_TEMPORAL = 1;

    // Endpoint para crear una nueva reseña
    @PostMapping
    public ResponseEntity<ReseniaRespuestaDTO> crearResenia(@Valid @RequestBody ReseniaCreacionDTO reseniaDTO) {
        ReseniaRespuestaDTO respuesta = reseniaService.crearResenia(reseniaDTO, USUARIO_ID_TEMPORAL);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    // Endpoint para editar una reseña existente
    @PutMapping("/{reseniaId}")
    public ResponseEntity<ReseniaRespuestaDTO> editarResenia(
            @PathVariable Integer reseniaId,
            @Valid @RequestBody ReseniaEdicionDTO reseniaDTO
    ) {
        reseniaDTO.setReseniaId(reseniaId); // Configura el ID para su uso en el servicio
        ReseniaRespuestaDTO respuesta = reseniaService.editarResenia(reseniaDTO, USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(respuesta);
    }

    // Endpoint para eliminar una reseña existente
    @DeleteMapping("/{reseniaId}")
    public ResponseEntity<CalificacionPromedioDTO> eliminarResenia(@PathVariable Integer reseniaId) {
        CalificacionPromedioDTO calificacionPromedioDTO = reseniaService.eliminarResenia(reseniaId, USUARIO_ID_TEMPORAL);
        return ResponseEntity.ok(calificacionPromedioDTO);
    }

}


