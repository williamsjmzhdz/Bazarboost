package com.bazarboost.service.impl;

import com.bazarboost.exception.UsuarioNoEncontradoException;
import com.bazarboost.model.UserDetailsImpl;
import com.bazarboost.model.Usuario;
import com.bazarboost.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        return new UserDetailsImpl(usuario);
    }
}
