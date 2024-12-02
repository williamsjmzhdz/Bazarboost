package com.bazarboost.system.service.impl;

import com.bazarboost.shared.exception.*;
import com.bazarboost.system.dto.*;
import com.bazarboost.system.model.Rol;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.model.UsuarioRol;
import com.bazarboost.system.repository.RolRepository;
import com.bazarboost.system.repository.UsuarioRepository;
import com.bazarboost.system.repository.UsuarioRolRepository;
import com.bazarboost.system.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Integer usuarioId) {
        log.debug("Buscando usuario con ID: {}", usuarioId);
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", usuarioId);
                    return new UsuarioNoEncontradoException("Usuario con ID " + usuarioId + " no encontrado");
                });
    }

    @Override
    @Transactional
    public void guardarUsuario(UsuarioRegistroDTO usuarioDTO) {
        log.info("Iniciando registro de usuario: {}", usuarioDTO.getCorreoElectronico());

        if (usuarioRepository.findByCorreoElectronico(usuarioDTO.getCorreoElectronico()).isPresent()) {
            log.error("Correo electrónico ya registrado: {}", usuarioDTO.getCorreoElectronico());
            throw new CorreoElectronicoExistenteException(
                    "El correo electrónico " + usuarioDTO.getCorreoElectronico() + " ya está registrado");
        }

        Rol rolCliente = rolRepository.findByNombre("Cliente")
                .orElseThrow(() -> {
                    log.error("Rol Cliente no encontrado");
                    return new RolNoEncontradoException("Error en el sistema: No se encontró el rol Cliente");
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidoPaterno(usuarioDTO.getApellidoPaterno());
        usuario.setApellidoMaterno(usuarioDTO.getApellidoMaterno());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setCorreoElectronico(usuarioDTO.getCorreoElectronico());
        usuario.setContrasenia(passwordEncoder.encode(usuarioDTO.getContrasenia()));
        usuario = usuarioRepository.save(usuario);

        log.debug("Usuario guardado con ID: {}", usuario.getUsuarioId());

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rolCliente);
        usuarioRol.setFechaAsignacion(LocalDateTime.now());
        usuarioRolRepository.save(usuarioRol);

        log.info("Usuario registrado exitosamente: {}", usuario.getCorreoElectronico());
    }


    @Override
    @Transactional(readOnly = true)
    public UsuariosPaginadosDTO obtenerTodos(String keyword, Integer pagina, Integer tamanioPagina, Integer usuarioId) {
        log.debug("Iniciando obtención de todos los usuarios con: keyword = {}, pagina = {}, tamaño de página = {}.", keyword, pagina, tamanioPagina);

        Usuario usuario = obtenerUsuario(usuarioId);
        validarRolAdministrador(usuario);

        long totalRegistros = usuarioRepository.count();
        if (totalRegistros == 0) {
            log.debug("No se encontraron usuarios, se retorna una lista vacía.");
            return new UsuariosPaginadosDTO(Collections.emptyList(), pagina, 0, 0, true, true);
        }

        validarPaginacion(pagina, tamanioPagina, totalRegistros);

        return obtenerUsuariosPaginados(pagina, tamanioPagina, keyword);
    }

    @Override
    @Transactional
    public void actualizarRolVendedor(Integer usuarioId, Boolean esVendedor, Integer usuarioAdminId) {
        String nombreRol = "VENDEDOR";
        log.debug("Iniciando la actualización del rol {} para el usuario {} a: {}.", nombreRol, usuarioId, !esVendedor);

        Usuario usuarioAdmin = obtenerUsuarioConMensaje(usuarioAdminId,
                "No se encontró información del usuario administrador. Por favor, inicie sesión nuevamente.");
        validarRolAdministrador(usuarioAdmin);

        Usuario usuario = obtenerUsuarioConMensaje(usuarioId,
                "No se encontró información del usuario al que intentas modificar el rol.");

        Optional<Rol> rolOptional = rolRepository.findByNombre(nombreRol);
        if (rolOptional.isEmpty()) {
            log.error("No se encontró el rol {}.", nombreRol);
            throw new RolNoEncontradoException("No se encontró el rol " + nombreRol);
        }
        Rol rolVendedor = rolOptional.get();

        boolean tieneRolVendedor = usuarioRepository.tieneRol(usuarioId, nombreRol);
        if (esVendedor && tieneRolVendedor) {
            log.error("El usuario {} ya tiene el rol {}.", usuarioId, nombreRol);
            throw new AsignacionRolInvalidaException("El usuario ya tiene el rol de Vendedor asignado.");
        }
        if (!esVendedor && !tieneRolVendedor) {
            log.error("El usuario {} no tiene el rol de {} para quitárselo.", usuarioId, nombreRol);
            throw new AsignacionRolInvalidaException("El usuario no tiene el rol de Vendedor para quitar.");
        }

        if (esVendedor) {
            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setUsuario(usuario);
            usuarioRol.setRol(rolVendedor);
            usuarioRol.setFechaAsignacion(LocalDateTime.now());
            usuarioRolRepository.save(usuarioRol);
            log.debug("Se ha asignado el rol {} al usuario {} exitosamente.", nombreRol, usuarioId);
        } else {
            usuarioRolRepository.deleteByUsuarioAndRol(usuario, rolVendedor);
            log.debug("Se ha quitado el rol {} al usuario {} exitosamente,", nombreRol, usuarioId);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public PerfilUsuarioDTO obtenerPerfil(Integer usuarioId) {
        Usuario usuario = obtenerUsuarioConMensaje(usuarioId, "No se encontró información del perfíl de usuario.");
        return mapearAPerfilUsuarioDTO(usuario);
    }

    @Override
    @Transactional
    public void actualizar(Integer usuarioId, UsuarioActualizacionDTO request) {
        log.debug("Iniciando actualización del usuario {}.", usuarioId);
        Usuario usuario = obtenerUsuarioConMensaje(usuarioId,
                "No se encontró información del usuario a actualizar.");

        if (!usuario.getCorreoElectronico().equals(request.getCorreoElectronico())) {
            usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico())
                    .ifPresent(u -> {
                        log.error("El correo electrónico {} ya está registrado por otro usuario.", request.getCorreoElectronico());
                        throw new CorreoElectronicoExistenteException(
                                "El correo electrónico ya está registrado por otro usuario.");
                    });
        }

        if (!usuario.getTelefono().equals(request.getTelefono())) {
            usuarioRepository.findByTelefono(request.getTelefono())
                    .ifPresent(u -> {
                        log.error("El número de teléfono {} ya está registrado por otro usuario.", request.getTelefono());
                        throw new TelefonoExistenteException(
                                "El número telefónico ya está registrado por otro usuario.");
                    });
        }

        usuario.setNombre(request.getNombre());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setTelefono(request.getTelefono());
        usuario.setCorreoElectronico(request.getCorreoElectronico());

        if (request.getContrasenia() != null && !request.getContrasenia().isEmpty()) {
            if (!request.getContrasenia().equals(request.getConfirmacionContrasenia())) {
                log.error("La contraseña y su confirmación no coinciden para el usuario {}.", usuarioId);
                throw new ContraseniasNoCoincidentesException(
                        "La contraseña y su confirmación no coinciden.");
            }
            usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        }

        usuarioRepository.save(usuario);
        log.debug("Usuario {} actualizado exitosamente.", usuarioId);
    }

    @Override
    public UsuarioNombreDTO obtenerNombre(Integer usuarioId) {
        Usuario usuario = obtenerUsuarioConMensaje(usuarioId,
                "No se encontró información del usuario. Inicia sesión de nuevo y vuelve a intentarlo.");
        return modelMapper.map(usuario, UsuarioNombreDTO.class);
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        if (optionalUsuario.isEmpty()) {
            log.error("El usuario {} no fue encontrado.", usuarioId);
            throw new UsuarioNoEncontradoException("No se encontró información de su usuario. Inicie sesión nuevamente e inténtelo de nuevo.");
        }
        return optionalUsuario.get();
    }

    private void validarRolAdministrador(Usuario usuario) {
        if (!usuarioRepository.tieneRol(usuario.getUsuarioId(), "ADMINISTRADOR")) {
            log.error("El usuario {} no tiene el rol de ADMINISTRADOR.", usuario.getUsuarioId());
            throw new AccesoDenegadoException("No puedes acceder al panel de usuarios porque no tienes el rol de administrador.");
        }
    }

    private void validarPaginacion(Integer pagina, Integer tamanioPagina, long totalRegistros) {
        int maximoPaginas = (int) Math.ceil((double) totalRegistros / tamanioPagina);
        if (pagina < 0 || pagina >= maximoPaginas) {
            log.error("El número de página {} está fuera del rango: 0 - {}. El tamaño de página es {}.", pagina, maximoPaginas, tamanioPagina);
            throw new PaginaFueraDeRangoException("Número de página fuera de rango.");
        }
    }

    private UsuariosPaginadosDTO obtenerUsuariosPaginados(Integer pagina, Integer tamanioPagina, String keyword) {

        log.debug("Iniciando paginación de usuarios con: pagina = {}, tamaño de página = {}, keyword = {}.",
                pagina, tamanioPagina, keyword);

        Pageable pageable = PageRequest.of(pagina, tamanioPagina);
        Page<Usuario> usuariosPage = keyword != null && !keyword.trim().isEmpty()
                ? usuarioRepository.buscarUsuarios(keyword, pageable)
                : usuarioRepository.findAll(pageable);

        log.debug("Paginación de usuarios exitosa.");

        List<UsuarioDTO> usuariosDTO = usuariosPage.getContent()
                .stream()
                .map(this::mapearAUsuarioDTO)
                .toList();

        return new UsuariosPaginadosDTO(
                usuariosDTO,
                usuariosPage.getNumber(),
                usuariosPage.getTotalPages(),
                usuariosPage.getTotalElements(),
                usuariosPage.isFirst(),
                usuariosPage.isLast()
        );
    }

    private UsuarioDTO mapearAUsuarioDTO(Usuario usuario) {
        log.debug("Mapeando usuario {} a UsuarioDTO.", usuario.getUsuarioId());
        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        usuarioDTO.setEsVendedor(usuarioRepository.tieneRol(usuario.getUsuarioId(), "Vendedor"));
        return usuarioDTO;
    }

    private Usuario obtenerUsuarioConMensaje(Integer usuarioId, String mensajeError) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        if (optionalUsuario.isEmpty()) {
            log.error("El usuario {} no fue encontrado.", usuarioId);
            throw new UsuarioNoEncontradoException(mensajeError);
        }
        return optionalUsuario.get();
    }

    private PerfilUsuarioDTO mapearAPerfilUsuarioDTO(Usuario usuario) {
        log.debug("Mapeando usuario {} a PerfilUsuarioDTO.", usuario.getUsuarioId());
        return modelMapper.map(usuario, PerfilUsuarioDTO.class);
    }
}
