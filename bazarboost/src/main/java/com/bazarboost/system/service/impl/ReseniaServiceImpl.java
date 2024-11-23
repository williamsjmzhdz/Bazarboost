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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
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
        // Buscar el producto asociado
        Integer productoId = reseniaDTO.getProductoId();
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID: " + productoId + " no encontrado"));

        // Buscar usuario asociado
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID: " + usuarioId + " no encontrado"));

        // Verificar si ya existe una reseña de este usuario para este producto
        if (reseniaRepository.existsByUsuarioAndProducto(usuario, producto)) {
            throw new ReseniaExistenteException("El usuario con ID " + usuarioId + " ya ha creado una reseña para el producto con ID " + productoId);
        }

        // Crear la entidad Resenia
        Resenia resenia = new Resenia();
        modelMapper.map(reseniaDTO, resenia);
        resenia.setFecha(LocalDateTime.now());
        resenia.setProducto(producto);
        resenia.setUsuario(usuario);

        // Guardar la reseña en la base de datos
        Resenia reseniaGuardada = reseniaRepository.save(resenia);

        // Retornar el DTO de respuesta
        return convertirAReseniaRespuestDTO(reseniaGuardada);
    }

    @Override
    @Transactional
    public ReseniaRespuestaDTO editarResenia(ReseniaEdicionDTO reseniaDTO, Integer usuarioId) {
        // Buscar la reseña existente
        Resenia resenia = reseniaRepository.findById(reseniaDTO.getReseniaId())
                .orElseThrow(() -> new ReseniaNoEncontradaException("Reseña con ID: " + reseniaDTO.getReseniaId() + " no encontrada"));

        // Verificar que la reseña le pertenece al usuario que intenta editarla
        if (!resenia.getUsuario().getUsuarioId().equals(usuarioId)) {
            throw new AccesoDenegadoException("Usuario con ID: " + usuarioId + " no tiene permiso para editar la reseña con ID: " + resenia.getReseniaId());
        }

        // Actualizar los campos de la reseña
        resenia.setComentario(reseniaDTO.getComentario());
        resenia.setCalificacion(reseniaDTO.getCalificacion());
        resenia.setFecha(LocalDateTime.now());

        // Guardar los cambios en la base de datos
        Resenia reseniaActualizada = reseniaRepository.save(resenia);

        // Retornar el DTO de respuesta con la información actualizada
        return convertirAReseniaRespuestDTO(reseniaActualizada);
    }

    @Override
    @Transactional
    public CalificacionPromedioDTO eliminarResenia(Integer reseniaId, Integer usuarioId) {
        // Buscar la reseña existente
        Resenia resenia = reseniaRepository.findById(reseniaId)
                .orElseThrow(() -> new ReseniaNoEncontradaException("Reseña con ID: " + reseniaId + " no encontrada"));

        // Verificar que la reseña pertenece al usuario que intenta eliminarla
        if (!resenia.getUsuario().getUsuarioId().equals(usuarioId)) {
            throw new AccesoDenegadoException("Usuario con ID: " + usuarioId + " no tiene permiso para eliminar la reseña con ID: " + resenia.getReseniaId());
        }

        // Guardar el producto asociado antes de eliminar la reseña
        Producto producto = resenia.getProducto();

        // Eliminar la reseña de la base de datos
        reseniaRepository.delete(resenia);

        // Retornar el DTO con la calificación promedio actualizada
        return new CalificacionPromedioDTO(calcularCalificacionPromedio(producto));
    }


    private ReseniaRespuestaDTO convertirAReseniaRespuestDTO(Resenia resenia) {
        ReseniaRespuestaDTO dto = new ReseniaRespuestaDTO();
        modelMapper.map(resenia, dto);
        dto.setAutor(resenia.getUsuario().getNombre());

        dto.setCalificacionPromedioActualizada(calcularCalificacionPromedio(resenia.getProducto()));

        return dto;
    }

    @Override
    public BigDecimal calcularCalificacionPromedio(Producto producto) {
        Double promedioCalificacion = reseniaRepository.obtenerCalificacionPromedio(producto.getProductoId());

        if (promedioCalificacion != null) {
            return BigDecimal.valueOf(promedioCalificacion)
                    .setScale(1, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
