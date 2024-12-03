package com.bazarboost.system.service.impl;

import com.bazarboost.system.dto.*;
import com.bazarboost.shared.exception.*;
import com.bazarboost.system.model.*;
import com.bazarboost.system.repository.*;
import com.bazarboost.system.service.MetodoPagoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class MetodoPagoServiceImpl implements MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPagoDTO> obtenerTodos(Integer usuarioId) {
        log.debug("Obteniendo todos los métodos de pago para el usuario {}.", usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        List<MetodoPagoDTO> metodoPagos = metodoPagoRepository.findByUsuario(usuario)
                .stream()
                .map(this::convertirAMetodoPagoDTO)
                .toList();
        log.debug("Se encontraron {} métodos de pago para el usuario {}.", metodoPagos.size(), usuarioId);
        return metodoPagos;
    }

    @Override
    @Transactional
    public Void crear(MetodoPagoCreacionDTO dto, Integer usuarioId) {
        log.debug("Creando nuevo método de pago para el usuario {}.", usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);

        if (metodoPagoRepository.existsByNumeroTarjeta(dto.getNumeroTarjeta())) {
            log.debug("Intento de crear método de pago con número de tarjeta duplicado.");
            throw new NumeroTarjetaDuplicadoException("El número de tarjeta ya está registrado");
        }

        MetodoPago metodoPago = new MetodoPago();
        mapearDatosComunes(dto, metodoPago);
        metodoPago.setUsuario(usuario);

        metodoPagoRepository.save(metodoPago);
        log.debug("Método de pago creado exitosamente para el usuario {}.", usuarioId);
        return null;
    }

    @Override
    @Transactional
    public Void actualizar(MetodoPagoEdicionDTO dto, Integer usuarioId) {
        log.debug("Actualizando método de pago {} para el usuario {}.", dto.getMetodoPagoId(), usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        MetodoPago metodoPago = obtenerMetodoPago(dto.getMetodoPagoId(), usuario);

        if (!metodoPago.getNumeroTarjeta().equals(dto.getNumeroTarjeta()) &&
                metodoPagoRepository.existsByNumeroTarjeta(dto.getNumeroTarjeta())) {
            log.debug("Intento de actualizar método de pago con número de tarjeta duplicado.");
            throw new NumeroTarjetaDuplicadoException("El número de tarjeta ya está registrado");
        }

        mapearDatosComunes(dto, metodoPago);
        metodoPagoRepository.save(metodoPago);
        log.debug("Método de pago {} actualizado exitosamente.", dto.getMetodoPagoId());
        return null;
    }

    @Override
    @Transactional
    public void eliminar(Integer metodoPagoId, Integer usuarioId) {
        log.debug("Eliminando método de pago {} del usuario {}.", metodoPagoId, usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        MetodoPago metodoPago = obtenerMetodoPago(metodoPagoId, usuario);
        metodoPagoRepository.delete(metodoPago);
        log.debug("Método de pago {} eliminado exitosamente.", metodoPagoId);
    }

    @Override
    @Transactional(readOnly = true)
    public MetodoPagoEdicionDTO obtenerDatosEdicion(Integer metodoPagoId, Integer usuarioId) {
        log.debug("Obteniendo datos de edición para el método de pago {} del usuario {}.", metodoPagoId, usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        MetodoPago metodoPago = obtenerMetodoPago(metodoPagoId, usuario);
        return convertirAMetodoPagoEdicionDTO(metodoPago);
    }

    private void mapearDatosComunes(MetodoPagoBaseDTO dto, MetodoPago metodoPago) {
        log.debug("Mapeando datos comunes del DTO al modelo de método de pago.");
        metodoPago.setNombreTitular(dto.getNombreTitular());
        metodoPago.setNumeroTarjeta(dto.getNumeroTarjeta());
        metodoPago.setFechaExpiracion(dto.getFechaExpiracion());
        metodoPago.setTipoTarjeta(dto.getTipoTarjeta());
        metodoPago.setMonto(dto.getMonto().doubleValue());
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        log.debug("Buscando usuario con ID {}.", usuarioId);
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.debug("Usuario {} no encontrado.", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado");
                });
    }

    private MetodoPago obtenerMetodoPago(Integer metodoPagoId, Usuario usuario) {
        log.debug("Buscando método de pago {} para el usuario {}.", metodoPagoId, usuario.getUsuarioId());
        return metodoPagoRepository.findByMetodoPagoIdAndUsuario(metodoPagoId, usuario)
                .orElseThrow(() -> {
                    log.debug("Método de pago {} no encontrado para el usuario {}.", metodoPagoId, usuario.getUsuarioId());
                    return new MetodoPagoNoEncontradoException(String.format(
                            "No se encontró un método de pago con ID %d para el usuario con ID %d",
                            metodoPagoId, usuario.getUsuarioId()));
                });
    }

    private MetodoPagoDTO convertirAMetodoPagoDTO(MetodoPago metodoPago) {
        log.debug("Convirtiendo método de pago {} a DTO.", metodoPago.getMetodoPagoId());
        MetodoPagoDTO metodoPagoDTO = new MetodoPagoDTO();
        metodoPagoDTO.setMetodoPagoId(metodoPago.getMetodoPagoId());
        metodoPagoDTO.setNombreTitular(metodoPago.getNombreTitular());
        metodoPagoDTO.setTerminacion(metodoPago.getNumeroTarjeta().substring(metodoPago.getNumeroTarjeta().length() - 4));
        metodoPagoDTO.setFechaExpiracion(metodoPago.getFechaExpiracion().format(DateTimeFormatter.ofPattern("MM/yyyy")));
        metodoPagoDTO.setTipo(metodoPago.getTipoTarjeta().name());

        Double montoOriginal = metodoPago.getMonto();
        BigDecimal montoFormateado = new BigDecimal(montoOriginal)
                .setScale(2, RoundingMode.HALF_UP);
        metodoPagoDTO.setMonto(montoFormateado.doubleValue());

        return metodoPagoDTO;
    }

    private MetodoPagoEdicionDTO convertirAMetodoPagoEdicionDTO(MetodoPago metodoPago) {
        log.debug("Convirtiendo método de pago {} a DTO de edición.", metodoPago.getMetodoPagoId());
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