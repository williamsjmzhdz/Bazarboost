package com.bazarboost.service.impl;

import com.bazarboost.dto.DescuentoVendedorDTO;
import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.model.Descuento;
import com.bazarboost.model.Usuario;
import com.bazarboost.repository.DescuentoRepository;
import com.bazarboost.repository.UsuarioRepository;
import com.bazarboost.service.DescuentoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Service
public class DescuentoServiceImpl implements DescuentoService {

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Descuento> obtenerDescuentosPorUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
        return descuentoRepository.findByUsuario(usuario);
    }

    @Override
    public List<DescuentoVendedorDTO> obtenerDescuentosDTOPorUsuario(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));

        List<Descuento> descuentos = descuentoRepository.findByUsuario(usuario);

        return descuentos.stream()
                .map(this::convertirADescuentoVendedorDTO)
                .toList();
    }

    @Override
    public Descuento obtenerDescuentoPorIdYUsuario(Integer id, Integer usuarioId) {
        return descuentoRepository.findByDescuentoIdAndUsuarioUsuarioId(id, usuarioId).orElse(null);
    }

    @Override
    public Descuento crearDescuento(Descuento descuento) {
        return descuentoRepository.save(descuento);
    }

    private DescuentoVendedorDTO convertirADescuentoVendedorDTO(Descuento descuento) {
        return modelMapper.map(descuento, DescuentoVendedorDTO.class);
    }

}
