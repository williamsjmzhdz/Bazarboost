package com.bazarboost.system.service.impl;

import com.bazarboost.system.dto.CategoriaCreacionDTO;
import com.bazarboost.system.dto.CategoriaEdicionDTO;
import com.bazarboost.shared.exception.AccesoDenegadoException;
import com.bazarboost.shared.exception.CategoriaNoEncontradaException;
import com.bazarboost.shared.exception.NombreCategoriaDuplicadoException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.system.model.Categoria;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.repository.CategoriaRepository;
import com.bazarboost.system.repository.UsuarioRepository;
import com.bazarboost.system.service.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Void crear(CategoriaCreacionDTO dto, Integer usuarioId) {
        log.debug("Creando categoría con nombre '{}' para el usuario {}.", dto.getNombre(), usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        verificarRol(usuario);
        verificarNombreCategoria(dto.getNombre(), null);
        Categoria categoria = modelMapper.map(dto, Categoria.class);
        categoriaRepository.save(categoria);
        log.debug("Categoría '{}' creada con éxito.", categoria.getNombre());
        return null;
    }

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        log.debug("Obteniendo todas las categorías.");
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerCategoriaPorId(Integer id) {
        log.debug("Obteniendo categoría con ID {}.", id);
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaEdicionDTO obtenerDatosEdicion(Integer categoriaId) {
        log.debug("Obteniendo datos de edición para la categoría con ID {}.", categoriaId);
        Categoria categoria = obtenerCategoria(categoriaId);
        return modelMapper.map(categoria, CategoriaEdicionDTO.class);
    }

    @Override
    public List<Categoria> obtenerTodas() {
        log.debug("Obteniendo todas las categorías.");
        return categoriaRepository.findAll();
    }

    @Override
    @Transactional
    public Void actualizar(CategoriaEdicionDTO dto, Integer usuarioId) {
        log.debug("Actualizando categoría con ID {} para el usuario {}.", dto.getCategoriaId(), usuarioId);
        verificarNombreCategoria(dto.getNombre(), dto.getCategoriaId());
        Usuario usuario = obtenerUsuario(usuarioId);
        verificarRol(usuario);
        Categoria categoria = obtenerCategoria(dto.getCategoriaId());
        modelMapper.map(dto, categoria);
        categoriaRepository.save(categoria);
        log.debug("Categoría con ID {} actualizada con éxito.", categoria.getCategoriaId());
        return null;
    }

    @Override
    @Transactional
    public void eliminar(Integer categoriaId, Integer usuarioId) {
        log.debug("Eliminando categoría con ID {} para el usuario {}.", categoriaId, usuarioId);
        Usuario usuario = obtenerUsuario(usuarioId);
        verificarRol(usuario);
        Categoria categoria = obtenerCategoria(categoriaId);
        categoriaRepository.delete(categoria);
        log.debug("Categoría con ID {} eliminada con éxito.", categoriaId);
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        log.debug("Obteniendo usuario con ID {}.", usuarioId);
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.debug("Usuario con ID {} no encontrado.", usuarioId);
                    return new UsuarioNoEncontradoException("No se encontró información del usuario. Por favor, reinicia sesión e intenta nuevamente.");
                });
    }

    private void verificarRol(Usuario usuario) {
        log.debug("Verificando rol del usuario con ID {}.", usuario.getUsuarioId());
        if (!usuarioRepository.tieneRol(usuario.getUsuarioId(), "Administrador")) {
            log.debug("Acceso denegado para el usuario con ID {}.", usuario.getUsuarioId());
            throw new AccesoDenegadoException("No tienes permisos para crear, editar o eliminar una categoria.");
        }
    }

    private void verificarNombreCategoria(String nombre, Integer categoriaIdExcluido) {
        log.debug("Verificando si el nombre de categoría '{}' es único.", nombre);
        boolean existeNombre = categoriaIdExcluido == null
                ? categoriaRepository.existsByNombre(nombre)
                : categoriaRepository.existsByNombreAndCategoriaIdNot(nombre, categoriaIdExcluido);
        if (existeNombre) {
            log.debug("Nombre de categoría duplicado: '{}'.", nombre);
            throw new NombreCategoriaDuplicadoException(
                    String.format("Ya existe una categoría con el nombre: %s", nombre)
            );
        }
    }

    private Categoria obtenerCategoria(Integer categoriaId) {
        log.debug("Obteniendo categoría con ID {}.", categoriaId);
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> {
                    log.debug("Categoría con ID {} no encontrada.", categoriaId);
                    return new CategoriaNoEncontradaException("No se encontró la categoría.");
                });
    }
}
