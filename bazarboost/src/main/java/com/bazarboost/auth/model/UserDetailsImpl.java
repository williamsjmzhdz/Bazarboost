package com.bazarboost.auth.model;

import com.bazarboost.system.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private final Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles().stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRol().getNombre().toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getContrasenia();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreoElectronico();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

