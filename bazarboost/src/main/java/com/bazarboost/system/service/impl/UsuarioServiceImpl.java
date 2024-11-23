package com.bazarboost.system.service.impl;

import com.bazarboost.system.dto.UsuarioRegistroDTO;
import com.bazarboost.shared.exception.CorreoElectronicoExistenteException;
import com.bazarboost.shared.exception.RolNoEncontradoException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.system.model.Rol;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.model.UsuarioRol;
import com.bazarboost.system.repository.RolRepository;
import com.bazarboost.system.repository.UsuarioRepository;
import com.bazarboost.system.repository.UsuarioRolRepository;
import com.bazarboost.system.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado"));
    }

    @Override
    @Transactional
    public void guardarUsuario(UsuarioRegistroDTO usuarioDTO) {
        // Verificar si existe el correo
        if (usuarioRepository.findByCorreoElectronico(usuarioDTO.getCorreoElectronico()).isPresent()) {
            throw new CorreoElectronicoExistenteException(
                    "El correo electrónico " + usuarioDTO.getCorreoElectronico() + " ya está registrado");
        }

        // Buscar rol Cliente
        Rol rolCliente = rolRepository.findByNombre("Cliente")
                .orElseThrow(() -> new RolNoEncontradoException(
                        "Error en el sistema: No se encontró el rol Cliente"));

        // Crear y guardar usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidoPaterno(usuarioDTO.getApellidoPaterno());
        usuario.setApellidoMaterno(usuarioDTO.getApellidoMaterno());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setCorreoElectronico(usuarioDTO.getCorreoElectronico());
        usuario.setContrasenia(passwordEncoder.encode(usuarioDTO.getContrasenia()));

        usuario = usuarioRepository.save(usuario);

        // Crear y guardar relación usuario-rol
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rolCliente);
        usuarioRol.setFechaAsignacion(LocalDateTime.now());

        usuarioRolRepository.save(usuarioRol);
    }
}
