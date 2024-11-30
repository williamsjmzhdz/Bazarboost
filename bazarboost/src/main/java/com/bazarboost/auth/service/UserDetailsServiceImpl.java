package com.bazarboost.auth.service;

import com.bazarboost.auth.model.UserDetailsImpl;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("Intentando autenticar usuario: {}", email);
        try {
            Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
                    .orElseThrow(() -> {
                        log.error("Usuario no encontrado: {}", email);
                        return new UsernameNotFoundException("Usuario no encontrado");
                    });
            log.debug("Usuario autenticado exitosamente: {}", email);
            return new UserDetailsImpl(usuario);
        } catch (Exception e) {
            log.error("Error durante la autenticación: {}", e.getMessage());
            throw e;
        }
    }
}

