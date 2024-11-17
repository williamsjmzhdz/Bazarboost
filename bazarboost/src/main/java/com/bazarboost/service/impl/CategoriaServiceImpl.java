package com.bazarboost.service.impl;

import com.bazarboost.dto.CategoriaCreacionDTO;
import com.bazarboost.dto.CategoriaEdicionDTO;
import com.bazarboost.exception.AccesoDenegadoException;
import com.bazarboost.exception.CategoriaNoEncontradaException;
import com.bazarboost.exception.NombreCategoriaDuplicadoException;
import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.model.Categoria;
import com.bazarboost.model.Direccion;
import com.bazarboost.model.Usuario;
import com.bazarboost.repository.CategoriaRepository;
import com.bazarboost.repository.UsuarioRepository;
import com.bazarboost.service.CategoriaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * Autor: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@Service
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
        Usuario usuario = obtenerUsuario(usuarioId);
        verificarRol(usuario);
        verificarNombreCategoria(dto.getNombre(), null);
        Categoria categoria = modelMapper.map(dto, Categoria.class);
        categoriaRepository.save(categoria);
        return null;
    }

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }


    @Override
    @Transactional(readOnly = true)
    public CategoriaEdicionDTO obtenerDatosEdicion(Integer categoriaId) {
        Categoria categoria = obtenerCategoria(categoriaId);
        return modelMapper.map(
                categoria,
                CategoriaEdicionDTO.class
        );
    }

    @Override
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    @Transactional
    public Void actualizar(CategoriaEdicionDTO dto, Integer usuarioId) {
        verificarNombreCategoria(dto.getNombre(), dto.getCategoriaId());
        Usuario usuario = obtenerUsuario(usuarioId);
        verificarRol(usuario);
        Categoria categoria = obtenerCategoria(dto.getCategoriaId());
        modelMapper.map(dto, categoria);
        categoriaRepository.save(categoria);
        return null;
    }

    @Override
    @Transactional
    public void eliminar(Integer categoriaId, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        verificarRol(usuario);
        Categoria categoria = obtenerCategoria(categoriaId);
        categoriaRepository.delete(categoria);
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró información del usuario. Por favor, reinicia sesión e intenta nuevamente."));
    }

    private void verificarRol(Usuario usuario) {
        if (!usuarioRepository.tieneRol(usuario.getUsuarioId(), "Administrador")) {
            throw new AccesoDenegadoException("No tienes permisos para crear, editar o eliminar una categoria.");
        }
    }

    private void verificarNombreCategoria(String nombre, Integer categoriaIdExcluido) {
        boolean existeNombre = categoriaIdExcluido == null
                ? categoriaRepository.existsByNombre(nombre)
                : categoriaRepository.existsByNombreAndCategoriaIdNot(nombre, categoriaIdExcluido);

        if (existeNombre) {
            throw new NombreCategoriaDuplicadoException(
                    String.format("Ya existe una categoría con el nombre: %s", nombre)
            );
        }
    }

    private Categoria obtenerCategoria(Integer categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("No se encontró la categoría."));
    }

}
