package com.bazarboost.service.impl;

import com.bazarboost.model.Usuario;
import com.bazarboost.repository.UsuarioRepository;
import com.bazarboost.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario obtenerUsuarioPorId(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId).orElse(null);
    }

}
