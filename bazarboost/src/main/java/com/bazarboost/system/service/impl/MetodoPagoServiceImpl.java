package com.bazarboost.service.impl;

import com.bazarboost.dto.MetodoPagoBaseDTO;
import com.bazarboost.dto.MetodoPagoCreacionDTO;
import com.bazarboost.dto.MetodoPagoDTO;
import com.bazarboost.dto.MetodoPagoEdicionDTO;
import com.bazarboost.exception.MetodoPagoNoEncontradoException;
import com.bazarboost.exception.NumeroTarjetaDuplicadoException;
import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.model.MetodoPago;
import com.bazarboost.model.Usuario;
import com.bazarboost.repository.MetodoPagoRepository;
import com.bazarboost.repository.UsuarioRepository;
import com.bazarboost.service.MetodoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MetodoPagoServiceImpl implements MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPagoDTO> obtenerTodos(Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        return metodoPagoRepository.findByUsuario(usuario).stream().map(this::convertirAMetodoPagoDTO).toList();
    }

    @Override
    @Transactional
    public Void crear(MetodoPagoCreacionDTO dto, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);

        if (metodoPagoRepository.existsByNumeroTarjeta(dto.getNumeroTarjeta())) {
            throw new NumeroTarjetaDuplicadoException("El número de tarjeta ya está registrado");
        }

        MetodoPago metodoPago = new MetodoPago();
        mapearDatosComunes(dto, metodoPago);
        metodoPago.setUsuario(usuario);

        metodoPagoRepository.save(metodoPago);
        return null;
    }

    @Override
    @Transactional
    public Void actualizar(MetodoPagoEdicionDTO dto, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        MetodoPago metodoPago = obtenerMetodoPago(dto.getMetodoPagoId(), usuario);

        // Verificar duplicado solo si cambió el número de tarjeta
        if (!metodoPago.getNumeroTarjeta().equals(dto.getNumeroTarjeta()) &&
                metodoPagoRepository.existsByNumeroTarjeta(dto.getNumeroTarjeta())) {
            throw new NumeroTarjetaDuplicadoException("El número de tarjeta ya está registrado");
        }

        mapearDatosComunes(dto, metodoPago);
        metodoPagoRepository.save(metodoPago);
        return null;
    }

    @Override
    @Transactional
    public void eliminar(Integer metodoPagoId, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        MetodoPago metodoPago = obtenerMetodoPago(metodoPagoId, usuario);
        metodoPagoRepository.delete(metodoPago);
    }

    @Override
    @Transactional(readOnly = true)
    public MetodoPagoEdicionDTO obtenerDatosEdicion(Integer metodoPagoId, Integer usuarioId) {

        Usuario usuario = obtenerUsuario(usuarioId);
        MetodoPago metodoPago = obtenerMetodoPago(metodoPagoId, usuario);

        return convertirAMetodoPagoEdicionDTO(metodoPago);
    }

    private void mapearDatosComunes(MetodoPagoBaseDTO dto, MetodoPago metodoPago) {
        metodoPago.setNombreTitular(dto.getNombreTitular());
        metodoPago.setNumeroTarjeta(dto.getNumeroTarjeta());
        metodoPago.setFechaExpiracion(dto.getFechaExpiracion());
        metodoPago.setTipoTarjeta(dto.getTipoTarjeta());
        metodoPago.setMonto(dto.getMonto().doubleValue());
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
    }

    private MetodoPago obtenerMetodoPago(Integer metodoPagoId, Usuario usuario) {
        return metodoPagoRepository.findByMetodoPagoIdAndUsuario(metodoPagoId, usuario)
                .orElseThrow(() -> new MetodoPagoNoEncontradoException(String.format(
                        "No se encontró un método de pago con ID %d para el usuario con ID %d", metodoPagoId, usuario.getUsuarioId())));
    }

    private MetodoPagoDTO convertirAMetodoPagoDTO(MetodoPago metodoPago) {
        MetodoPagoDTO metodoPagoDTO = new MetodoPagoDTO();
        metodoPagoDTO.setMetodoPagoId(metodoPago.getMetodoPagoId());
        metodoPagoDTO.setNombreTitular(metodoPago.getNombreTitular()); // Corregido: usaba DTO en lugar de entidad
        metodoPagoDTO.setTerminacion(metodoPago.getNumeroTarjeta().substring(metodoPago.getNumeroTarjeta().length() - 4));
        metodoPagoDTO.setFechaExpiracion(metodoPago.getFechaExpiracion().format(DateTimeFormatter.ofPattern("MM/yyyy")));
        metodoPagoDTO.setTipo(metodoPago.getTipoTarjeta().name());

        // Formateo del monto usando BigDecimal
        Double montoOriginal = metodoPago.getMonto();
        BigDecimal montoFormateado = new BigDecimal(montoOriginal)
                .setScale(2, RoundingMode.HALF_UP);
        metodoPagoDTO.setMonto(montoFormateado.doubleValue());

        return metodoPagoDTO;
    }

    private MetodoPagoEdicionDTO convertirAMetodoPagoEdicionDTO(MetodoPago metodoPago) {
        MetodoPagoEdicionDTO metodoPagoEdicionDTO = new MetodoPagoEdicionDTO();

        metodoPagoEdicionDTO.setMetodoPagoId(metodoPago.getMetodoPagoId());
        metodoPagoEdicionDTO.setNombreTitular(metodoPago.getNombreTitular());
        metodoPagoEdicionDTO.setNumeroTarjeta(metodoPago.getNumeroTarjeta());
        metodoPagoEdicionDTO.setFechaExpiracion(metodoPago.getFechaExpiracion());
        metodoPagoEdicionDTO.setTipoTarjeta(metodoPago.getTipoTarjeta());
        metodoPagoEdicionDTO.setMonto(BigDecimal.valueOf(metodoPago.getMonto()));

        return metodoPagoEdicionDTO;
    }
}
