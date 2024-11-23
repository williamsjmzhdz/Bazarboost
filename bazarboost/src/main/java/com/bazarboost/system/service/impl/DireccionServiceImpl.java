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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
        Usuario usuario = obtenerUsuario(usuarioId);
        Direccion direccion = modelMapper.map(dto, Direccion.class);
        direccion.setUsuario(usuario);
        direccionRepository.save(direccion);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionDTO> obtenerTodas(Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        return direccionRepository.findByUsuario(usuario)
                .stream()
                .map(direccion -> modelMapper.map(direccion, DireccionDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DireccionEdicionDTO obtenerDatosEdicion(Integer direccionId, Integer usuarioId) {
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
        Usuario usuario = obtenerUsuario(usuarioId);
        Direccion direccion = obtenerDireccion(dto.getDireccionId(), usuario);
        modelMapper.map(dto, direccion);
        direccionRepository.save(direccion);
        return null;
    }

    @Override
    @Transactional
    public void eliminar(Integer direccionId, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        Direccion direccion = obtenerDireccion(direccionId, usuario);
        direccionRepository.delete(direccion);
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
    }

    private Direccion obtenerDireccion(Integer direccionId, Usuario usuario) {
        return direccionRepository.findByDireccionIdAndUsuario(direccionId, usuario)
                .orElseThrow(() -> new DireccionNoEncontradaException(String.format(
                        "No se encontró una dirección con ID %d para el usuario con ID %d",
                        direccionId,
                        usuario.getUsuarioId()
                )));
    }
}
