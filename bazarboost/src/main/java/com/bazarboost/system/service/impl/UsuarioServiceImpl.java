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
import java.util.UUID;

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

    @Autowired
    private ModelMapper modelMapper;

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

    @Override
    @Transactional(readOnly = true)
    public UsuariosPaginadosDTO obtenerTodos(String keyword, Integer pagina, Integer tamanioPagina, Integer usuarioId) {
        Usuario usuario = obtenerUsuario(usuarioId);
        validarRolAdministrador(usuario);

        long totalRegistros = usuarioRepository.count();
        if (totalRegistros == 0) {
            return new UsuariosPaginadosDTO(Collections.emptyList(), pagina, 0, 0, true, true);
        }
        validarPaginacion(pagina, tamanioPagina, totalRegistros);

        return obtenerUsuariosPaginados(pagina, tamanioPagina, keyword);
    }

    @Override
    @Transactional
    public void actualizarRolVendedor(Integer usuarioId, Boolean esVendedor, Integer usuarioAdminId) {
        String nombreRol = "VENDEDOR";

        // Obtener y validar usuario administrador
        Usuario usuarioAdmin = obtenerUsuarioConMensaje(usuarioAdminId,
                "No se encontró información del usuario administrador. Por favor, inicie sesión nuevamente.");
        validarRolAdministrador(usuarioAdmin);

        // Obtener usuario a modificar
        Usuario usuario = obtenerUsuarioConMensaje(usuarioId,
                "No se encontró información del usuario al que intentas modificar el rol.");


        // Obtener rol vendedor
        Rol rolVendedor = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RolNoEncontradoException("No se encontró el rol " + nombreRol));

        // Verificar si ya tiene el rol
        boolean tieneRolVendedor = usuarioRepository.tieneRol(usuarioId, "VENDEDOR");

        if (esVendedor && tieneRolVendedor) {
            throw new AsignacionRolInvalidaException("El usuario ya tiene el rol de Vendedor asignado.");
        }

        if (!esVendedor && !tieneRolVendedor) {
            throw new AsignacionRolInvalidaException("El usuario no tiene el rol de Vendedor para quitar.");
        }

        if (esVendedor) {
            // Crear y guardar nueva relación usuario-rol
            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setUsuario(usuario);
            usuarioRol.setRol(rolVendedor);
            usuarioRol.setFechaAsignacion(LocalDateTime.now());
            usuarioRolRepository.save(usuarioRol);
        } else {
            // Eliminar la relación usuario-rol
            usuarioRolRepository.deleteByUsuarioAndRol(usuario, rolVendedor);
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
        Usuario usuario = obtenerUsuarioConMensaje(usuarioId,
                "No se encontró información del usuario a actualizar.");

        // Validar correo y teléfono duplicados si cambiaron
        if (!usuario.getCorreoElectronico().equals(request.getCorreoElectronico())) {
            usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico())
                    .ifPresent(u -> {
                        throw new CorreoElectronicoExistenteException(
                                "El correo electrónico ya está registrado por otro usuario.");
                    });
        }

        if (!usuario.getTelefono().equals(request.getTelefono())) {
            usuarioRepository.findByTelefono(request.getTelefono())
                    .ifPresent(u -> {
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
                throw new ContraseniasNoCoincidentesException(
                        "La contraseña y su confirmación no coinciden.");
            }
            usuario.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        }

        usuarioRepository.save(usuario);
    }

    private Usuario obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró información de su usuario. Inicie sesión nuevamente e inténtelo de nuevo."));
    }

    private void validarRolAdministrador(Usuario usuario) {
        if (!usuarioRepository.tieneRol(usuario.getUsuarioId(), "ADMINISTRADOR")) {
            throw new AccesoDenegadoException("No puedes acceder al panel de usuarios porque no tienes el rol de administrador.");
        }
    }

    private void validarPaginacion(Integer pagina, Integer tamanioPagina, long totalRegistros) {
        int maximoPaginas = (int) Math.ceil((double) totalRegistros / tamanioPagina);
        if (pagina < 0 || pagina >= maximoPaginas) {
            throw new PaginaFueraDeRangoException("Número de página fuera de rango.");
        }
    }

    private UsuariosPaginadosDTO obtenerUsuariosPaginados(Integer pagina, Integer tamanioPagina, String keyword) {
        Pageable pageable = PageRequest.of(pagina, tamanioPagina);

        // Obtener la página de usuarios
        Page<Usuario> usuariosPage = keyword != null && !keyword.trim().isEmpty()
                ? usuarioRepository.buscarUsuarios(keyword, pageable)
                : usuarioRepository.findAll(pageable);

        // Convertir usuarios a DTOs
        List<UsuarioDTO> usuariosDTO = usuariosPage.getContent()
                .stream()
                .map(this::mapearAUsuarioDTO)
                .toList();

        // Crear y retornar el DTO con la información de paginación
        return new UsuariosPaginadosDTO(
                usuariosDTO,                    // lista de usuarios
                usuariosPage.getNumber(),       // página actual (0-based)
                usuariosPage.getTotalPages(),   // total de páginas
                usuariosPage.getTotalElements(),// total de elementos
                usuariosPage.isFirst(),         // si es la primera página
                usuariosPage.isLast()           // si es la última página
        );
    }

    private UsuarioDTO mapearAUsuarioDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        usuarioDTO.setEsVendedor(usuarioRepository.tieneRol(usuario.getUsuarioId(), "Vendedor"));
        return usuarioDTO;
    }

    private Usuario obtenerUsuarioConMensaje(Integer usuarioId, String mensajeError) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(mensajeError));
    }

    private PerfilUsuarioDTO mapearAPerfilUsuarioDTO(Usuario usuario) {
        return modelMapper.map(usuario, PerfilUsuarioDTO.class);
    }
}
