package com.bazarboost.system.service.impl;

import com.bazarboost.shared.exception.*;
import com.bazarboost.system.dto.DescuentoVendedorDTO;
import com.bazarboost.system.model.Descuento;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.repository.DescuentoRepository;
import com.bazarboost.system.repository.UsuarioRepository;
import com.bazarboost.system.service.DescuentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DescuentoServiceImpl implements DescuentoService {

    private final DescuentoRepository descuentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DescuentoVendedorDTO> obtenerDescuentosDTOPorUsuario(Integer usuarioId) {
        log.debug("Obteniendo descuentos DTO para el usuario {}.", usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        List<Descuento> descuentos = descuentoRepository.findByUsuario(usuario);
        return descuentos.stream()
                .map(this::convertirADescuentoVendedorDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Descuento obtenerDescuentoPorIdYUsuarioId(Integer descuentoId, Integer usuarioId) {
        log.debug("Obteniendo descuento con ID {} para el usuario {}.", descuentoId, usuarioId);
        Descuento descuento = descuentoRepository.findById(descuentoId)
                .orElseThrow(() -> {
                    log.debug("Descuento {} no encontrado.", descuentoId);
                    return new DescuentoNoEncontradoException("El descuento que intentas acceder no existe");
                });
        if (!descuento.getUsuario().getUsuarioId().equals(usuarioId)) {
            log.debug("Acceso denegado al descuento {} para el usuario {}.", descuentoId, usuarioId);
            throw new AccesoDenegadoException("No tienes permiso para acceder a este descuento");
        }
        return descuento;
    }

    @Override
    @Transactional
    public Descuento crearDescuento(Descuento descuento, Integer usuarioId) {
        log.debug("Creando descuento para el usuario {}.", usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        validarPorcentajeDescuento(descuento.getPorcentaje());
        validarNombreUnicoPorUsuario(descuento.getNombre(), usuarioId, descuento.getDescuentoId());
        descuento.setUsuario(usuario);
        Descuento savedDescuento = descuentoRepository.save(descuento);
        log.debug("Descuento creado con éxito con ID {} para el usuario {}.", savedDescuento.getDescuentoId(), usuarioId);
        return savedDescuento;
    }

    @Override
    @Transactional
    public Descuento actualizarDescuento(Integer descuentoId, Descuento descuentoActualizado, Integer usuarioId) {
        log.debug("Actualizando descuento con ID {} para el usuario {}.", descuentoId, usuarioId);
        Descuento descuentoExistente = obtenerDescuentoPorIdYUsuarioId(descuentoId, usuarioId);
        validarPorcentajeDescuento(descuentoActualizado.getPorcentaje());
        validarNombreUnicoPorUsuario(descuentoActualizado.getNombre(), usuarioId, descuentoId);
        descuentoActualizado.setDescuentoId(descuentoId);
        descuentoActualizado.setUsuario(descuentoExistente.getUsuario());
        descuentoExistente.setNombre(descuentoActualizado.getNombre());
        descuentoExistente.setPorcentaje(descuentoActualizado.getPorcentaje());
        Descuento savedDescuento = descuentoRepository.save(descuentoExistente);
        log.debug("Descuento con ID {} actualizado con éxito para el usuario {}.", descuentoId, usuarioId);
        return savedDescuento;
    }

    @Override
    @Transactional
    public void eliminarDescuento(Integer descuentoId, Integer usuarioId) {
        log.debug("Eliminando descuento con ID {} para el usuario {}.", descuentoId, usuarioId);
        Descuento descuento = obtenerDescuentoPorIdYUsuarioId(descuentoId, usuarioId);
        descuentoRepository.delete(descuento);
        log.debug("Descuento con ID {} eliminado con éxito para el usuario {}.", descuentoId, usuarioId);
    }

    private void validarPorcentajeDescuento(Integer porcentaje) {
        log.debug("Validando porcentaje de descuento: {}.", porcentaje);
        if (porcentaje == null) {
            log.debug("Porcentaje de descuento es nulo.");
            throw new PorcentajeDescuentoInvalidoException("El porcentaje de descuento no puede estar vacío");
        }
        if (porcentaje < 1 || porcentaje > 100) {
            log.debug("Porcentaje de descuento fuera de rango: {}.", porcentaje);
            throw new PorcentajeDescuentoInvalidoException("El porcentaje de descuento debe estar entre 1 y 100");
        }
    }

    private void validarNombreUnicoPorUsuario(String nombre, Integer usuarioId, Integer descuentoIdExcluido) {
        log.debug("Validando nombre único para el descuento '{}' del usuario {}.", nombre, usuarioId);
        Optional<Descuento> descuentoExistente = descuentoRepository.findByNombreAndUsuarioUsuarioId(nombre, usuarioId);
        if (descuentoExistente.isPresent()) {
            Descuento descuento = descuentoExistente.get();
            if (descuentoIdExcluido == null || !descuento.getDescuentoId().equals(descuentoIdExcluido)) {
                log.debug("Nombre de descuento duplicado encontrado: '{}' para el usuario {}.", nombre, usuarioId);
                throw new NombreDescuentoDuplicadoException("Ya existe un descuento con el nombre '" + nombre + "' para este usuario");
            }
        }
    }

    private void validarUsuario(Integer usuarioId) {
        log.debug("Validando usuario con ID {}.", usuarioId);
        if (usuarioId == null) {
            log.debug("ID de usuario es nulo.");
            throw new UsuarioNoEncontradoException("El ID del usuario no puede estar vacío");
        }
        if (!usuarioRepository.existsById(usuarioId)) {
            log.debug("Usuario con ID {} no existe.", usuarioId);
            throw new UsuarioNoEncontradoException("El usuario con ID " + usuarioId + " no existe");
        }
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        log.debug("Obteniendo usuario con ID {}.", usuarioId);
        validarUsuario(usuarioId);
        return usuarioRepository.getReferenceById(usuarioId);
    }

    private DescuentoVendedorDTO convertirADescuentoVendedorDTO(Descuento descuento) {
        return modelMapper.map(descuento, DescuentoVendedorDTO.class);
    }
}
