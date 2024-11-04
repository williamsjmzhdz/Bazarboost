package com.bazarboost.service.impl;

import com.bazarboost.dto.DescuentoVendedorDTO;
import com.bazarboost.exception.*;
import com.bazarboost.model.Descuento;
import com.bazarboost.model.Usuario;
import com.bazarboost.repository.DescuentoRepository;
import com.bazarboost.repository.UsuarioRepository;
import com.bazarboost.service.DescuentoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio que maneja las operaciones relacionadas con los descuentos.
 */
@Service
@RequiredArgsConstructor
public class DescuentoServiceImpl implements DescuentoService {

    private final DescuentoRepository descuentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DescuentoVendedorDTO> obtenerDescuentosDTOPorUsuario(Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        List<Descuento> descuentos = descuentoRepository.findByUsuario(usuario);
        return descuentos.stream()
                .map(this::convertirADescuentoVendedorDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Descuento obtenerDescuentoPorIdYUsuarioId(Integer descuentoId, Integer usuarioId) {
        // Primero verificamos que el descuento existe
        Descuento descuento = descuentoRepository.findById(descuentoId)
                .orElseThrow(() -> new DescuentoNoEncontradoException(
                        "El descuento que intentas acceder no existe"));

        // Luego verificamos si pertenece al usuario
        if (!descuento.getUsuario().getUsuarioId().equals(usuarioId)) {
            throw new AccesoDenegadoException("No tienes permiso para acceder a este descuento");
        }

        return descuento;
    }

    @Override
    @Transactional
    public Descuento crearDescuento(Descuento descuento, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        validarPorcentajeDescuento(descuento.getPorcentaje());
        validarNombreUnicoPorUsuario(descuento.getNombre(), usuarioId, descuento.getDescuentoId());
        descuento.setUsuario(usuario);
        return descuentoRepository.save(descuento);
    }

    @Override
    @Transactional
    public Descuento actualizarDescuento(Integer descuentoId, Descuento descuentoActualizado, Integer usuarioId) {
        Descuento descuentoExistente = obtenerDescuentoPorIdYUsuarioId(descuentoId, usuarioId);
        validarPorcentajeDescuento(descuentoActualizado.getPorcentaje());
        validarNombreUnicoPorUsuario(descuentoActualizado.getNombre(), usuarioId, null);
        descuentoActualizado.setDescuentoId(descuentoId);
        descuentoActualizado.setUsuario(descuentoExistente.getUsuario());
        descuentoExistente.setNombre(descuentoActualizado.getNombre());
        descuentoExistente.setPorcentaje(descuentoActualizado.getPorcentaje());
        return descuentoRepository.save(descuentoExistente);
    }

    @Override
    @Transactional
    public void eliminarDescuento(Integer descuentoId, Integer usuarioId) {
        // Validamos que el descuento exista y pertenezca al usuario
        Descuento descuento = obtenerDescuentoPorIdYUsuarioId(descuentoId, usuarioId);
        descuentoRepository.delete(descuento);
    }

    // Métodos de validación

    private void validarPorcentajeDescuento(Integer porcentaje) {
        if (porcentaje == null) {
            throw new PorcentajeDescuentoInvalidoException("El porcentaje de descuento no puede estar vacío");
        }
        if (porcentaje < 1 || porcentaje > 100) {
            throw new PorcentajeDescuentoInvalidoException(
                    "El porcentaje de descuento debe estar entre 1 y 100");
        }
    }

    // Método de validación de nombre único
    private void validarNombreUnicoPorUsuario(String nombre, Integer usuarioId, Integer descuentoIdExcluido) {
        Optional<Descuento> descuentoExistente = descuentoRepository
                .findByNombreAndUsuarioUsuarioId(nombre, usuarioId);

        if (descuentoExistente.isPresent()) {
            Descuento descuento = descuentoExistente.get();
            // Si estamos actualizando, ignorar si el descuento encontrado es el mismo que estamos editando
            if (descuentoIdExcluido == null || !descuento.getDescuentoId().equals(descuentoIdExcluido)) {
                throw new NombreDescuentoDuplicadoException(
                        "Ya existe un descuento con el nombre '" + nombre + "' para este usuario");
            }
        }
    }

    private void validarUsuario(Integer usuarioId) {
        if (usuarioId == null) {
            System.out.println("aqui");
            throw new UsuarioNoEncontradoException("El ID del usuario no puede estar vacío");
        }
        if (!usuarioRepository.existsById(usuarioId)) {
            System.out.println("aqui 2");
            throw new UsuarioNoEncontradoException("El usuario con ID " + usuarioId + " no existe");
        }
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        validarUsuario(usuarioId);
        return usuarioRepository.getReferenceById(usuarioId); // Más eficiente que findById para este caso
    }

    // Métodos de conversión y mapeo

    private DescuentoVendedorDTO convertirADescuentoVendedorDTO(Descuento descuento) {
        return modelMapper.map(descuento, DescuentoVendedorDTO.class);
    }
}