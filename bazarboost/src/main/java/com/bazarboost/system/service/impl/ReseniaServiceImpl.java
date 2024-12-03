package com.bazarboost.system.service.impl;

import com.bazarboost.shared.exception.*;
import com.bazarboost.system.dto.CalificacionPromedioDTO;
import com.bazarboost.system.dto.ReseniaCreacionDTO;
import com.bazarboost.system.dto.ReseniaEdicionDTO;
import com.bazarboost.system.dto.ReseniaRespuestaDTO;
import com.bazarboost.system.model.Resenia;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.repository.ReseniaRepository;
import com.bazarboost.system.repository.ProductoRepository;
import com.bazarboost.system.repository.UsuarioRepository;
import com.bazarboost.system.service.ReseniaService;
import com.bazarboost.system.model.Producto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ReseniaServiceImpl implements ReseniaService {

    @Autowired
    private ReseniaRepository reseniaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ReseniaRespuestaDTO crearResenia(ReseniaCreacionDTO reseniaDTO, Integer usuarioId) {
        Integer productoId = reseniaDTO.getProductoId();
        log.debug("Iniciando creación de la reseña sobre el producto {} para el usuario {}.", productoId, usuarioId);

        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        if (productoOptional.isEmpty()) {
            log.debug("No se encontro el producto {}.", productoId);
            throw new ProductoNoEncontradoException("Producto con ID: " + productoId + " no encontrado");
        }
        Producto producto = productoOptional.get();

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isEmpty()) {
            log.debug("No se encontró el usuario {}.", usuarioId);
            throw new UsuarioNoEncontradoException("Usuario con ID: " + usuarioId + " no encontrado");
        }
        Usuario usuario = usuarioOptional.get();

        if (reseniaRepository.existsByUsuarioAndProducto(usuario, producto)) {
            log.debug("El usuario {} ya ha escrito una reseña para el producto {}.", usuarioId, productoId);
            throw new ReseniaExistenteException("El usuario con ID " + usuarioId + " ya ha creado una reseña para el producto con ID " + productoId);
        }

        Resenia resenia = new Resenia();
        modelMapper.map(reseniaDTO, resenia);
        resenia.setFecha(LocalDateTime.now());
        resenia.setProducto(producto);
        resenia.setUsuario(usuario);

        Resenia reseniaGuardada = reseniaRepository.save(resenia);
        log.debug("Reseeña {} creada con éxito.", reseniaGuardada.getReseniaId());

        return convertirAReseniaRespuestDTO(reseniaGuardada);
    }

    @Override
    @Transactional
    public ReseniaRespuestaDTO editarResenia(ReseniaEdicionDTO reseniaDTO, Integer usuarioId) {

        log.debug("Iniciando edición de la reseña {} para el usuario {}.", reseniaDTO.getReseniaId(), usuarioId);

        Optional<Resenia> reseniaOptional = reseniaRepository.findById(reseniaDTO.getReseniaId());
        if (reseniaOptional.isEmpty()) {
            log.debug("Reseña {} no encontrada.", reseniaDTO.getReseniaId());
            throw new ReseniaNoEncontradaException("Reseña con ID: " + reseniaDTO.getReseniaId() + " no encontrada");
        }
        Resenia resenia = reseniaOptional.get();

        if (!resenia.getUsuario().getUsuarioId().equals(usuarioId)) {
            log.debug("La reseña {} no le pertenece al usuario {}.", resenia.getReseniaId(), usuarioId);
            throw new AccesoDenegadoException("Usuario con ID: " + usuarioId + " no tiene permiso para editar la reseña con ID: " + resenia.getReseniaId());
        }

        resenia.setComentario(reseniaDTO.getComentario());
        resenia.setCalificacion(reseniaDTO.getCalificacion());
        resenia.setFecha(LocalDateTime.now());

        Resenia reseniaActualizada = reseniaRepository.save(resenia);
        log.debug("Reseña {} actualizada correctamente para el usuario {}.", reseniaActualizada.getReseniaId(), usuarioId);

        return convertirAReseniaRespuestDTO(reseniaActualizada);
    }

    @Override
    @Transactional
    public CalificacionPromedioDTO eliminarResenia(Integer reseniaId, Integer usuarioId) {

        log.debug("Iniciando eliminación de la reseña {} para el usuario {}.", reseniaId, usuarioId);

        Optional<Resenia> reseniaOptional = reseniaRepository.findById(reseniaId);
        if (reseniaOptional.isEmpty()) {
            log.debug("Reseña {} no encontrada.", reseniaId);
            throw new ReseniaNoEncontradaException("Reseña con ID: " + reseniaId + " no encontrada");
        }
        Resenia resenia = reseniaOptional.get();

        if (!resenia.getUsuario().getUsuarioId().equals(usuarioId)) {
            log.debug("La reseña {} no le pertenece al usuario {}.", reseniaId, usuarioId);
            throw new AccesoDenegadoException("Usuario con ID: " + usuarioId + " no tiene permiso para eliminar la reseña con ID: " + resenia.getReseniaId());
        }

        Producto producto = resenia.getProducto();
        reseniaRepository.delete(resenia);
        log.debug("Se eliminó la reseña {} para el usuario {} exitosamente.", reseniaId, usuarioId);

        return new CalificacionPromedioDTO(calcularCalificacionPromedio(producto));
    }


    private ReseniaRespuestaDTO convertirAReseniaRespuestDTO(Resenia resenia) {
        log.debug("Mapeando reseña {} a ReseniaRespuestaDTO.", resenia.getReseniaId());
        ReseniaRespuestaDTO dto = new ReseniaRespuestaDTO();
        modelMapper.map(resenia, dto);
        dto.setAutor(resenia.getUsuario().getNombre());
        dto.setCalificacionPromedioActualizada(calcularCalificacionPromedio(resenia.getProducto()));
        return dto;
    }

    @Override
    public BigDecimal calcularCalificacionPromedio(Producto producto) {
        log.debug("Iniciando el cálculo de la calificación promedio para el producto {}.", producto.getProductoId());

        Double promedioCalificacion = reseniaRepository.obtenerCalificacionPromedio(producto.getProductoId());

        if (promedioCalificacion != null) {
            return BigDecimal.valueOf(promedioCalificacion)
                    .setScale(1, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
