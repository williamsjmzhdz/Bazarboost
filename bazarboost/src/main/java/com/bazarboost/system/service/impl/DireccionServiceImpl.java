package com.bazarboost.system.service.impl;

import com.bazarboost.system.dto.DireccionCreacionDTO;
import com.bazarboost.system.dto.DireccionDTO;
import com.bazarboost.system.dto.DireccionEdicionDTO;
import com.bazarboost.shared.exception.DireccionNoEncontradaException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.system.model.Direccion;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.repository.DireccionRepository;
import com.bazarboost.system.repository.UsuarioRepository;
import com.bazarboost.system.service.DireccionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DireccionServiceImpl implements DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Void crear(DireccionCreacionDTO dto, Integer usuarioId) {
        log.debug("Creando dirección para el usuario {}.", usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        Direccion direccion = modelMapper.map(dto, Direccion.class);
        direccion.setUsuario(usuario);
        direccionRepository.save(direccion);
        log.debug("Dirección creada con éxito para el usuario {}.", usuario);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionDTO> obtenerTodas(Integer usuarioId) {
        log.debug("Obteniendo todas las direcciones para el usuario {}.", usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        return direccionRepository.findByUsuario(usuario)
                .stream()
                .map(direccion -> modelMapper.map(direccion, DireccionDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DireccionEdicionDTO obtenerDatosEdicion(Integer direccionId, Integer usuarioId) {
        log.debug("Obteniendo datos de edición de la direccion {} para el usuario {}.", direccionId, usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        Direccion direccion = obtenerDireccion(direccionId, usuario);
        return modelMapper.map(
                direccion,
                DireccionEdicionDTO.class
        );
    }

    @Override
    @Transactional
    public Void actualizar(DireccionEdicionDTO dto, Integer usuarioId) {
        log.debug("Actualizando la dirección {} para el usuario {}.", dto.getDireccionId(), usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        Direccion direccion = obtenerDireccion(dto.getDireccionId(), usuario);
        modelMapper.map(dto, direccion);
        direccionRepository.save(direccion);
        log.debug("Dirección {} actualizada con éxito para el usuario {}.", direccion.getDireccionId(), usuario.getUsuarioId());
        return null;
    }

    @Override
    @Transactional
    public void eliminar(Integer direccionId, Integer usuarioId) {
        log.debug("Eliminando dirección {} para el usuario {}.", direccionId, usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        Direccion direccion = obtenerDireccion(direccionId, usuario);
        direccionRepository.delete(direccion);
        log.debug("Dirección {} eliminado con éxito para el usuario {}.", direccionId, usuarioId);
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        log.debug("Buscando usuario con ID {}.", usuarioId);
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.debug("Usuario {} no encontrado.", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado");
                });
    }

    private Direccion obtenerDireccion(Integer direccionId, Usuario usuario) {
        Optional<Direccion> direccionOptional = direccionRepository.findByDireccionIdAndUsuario(direccionId, usuario);
        if (direccionOptional.isEmpty()) {
            log.debug("No se econtró la dirección {} para el usuario {}.", direccionId, usuario.getUsuarioId());
            throw  new DireccionNoEncontradaException(String.format(
                    "No se encontró una dirección con ID %d para el usuario con ID %d",
                    direccionId,
                    usuario.getUsuarioId()
            ));
        }
        return direccionOptional.get();

    }
}
