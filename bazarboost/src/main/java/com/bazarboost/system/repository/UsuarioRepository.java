package com.bazarboost.repository;

import com.bazarboost.system.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Verifica si un usuario tiene un rol específico.
     *
     * @param usuarioId ID del usuario a verificar
     * @param nombreRol Nombre del rol a buscar (ej: "CLIENTE", "VENDEDOR", "ADMIN")
     * @return true si el usuario tiene el rol especificado, false en caso contrario
     */
    @Query("SELECT COUNT(ur) > 0 FROM UsuarioRol ur " +
            "WHERE ur.usuario.usuarioId = :usuarioId " +
            "AND ur.rol.nombre = :nombreRol " +
            "AND ur.usuario.usuarioId IS NOT NULL " +
            "AND ur.rol.rolId IS NOT NULL")
    boolean tieneRol(@Param("usuarioId") Integer usuarioId, @Param("nombreRol") String nombreRol);

    /**
     * Busca un usuario por su correo electrónico.
     * Método usado por Spring Security para el proceso de autenticación.
     *
     * @param correoElectronico Correo electrónico del usuario a buscar
     * @return Optional conteniendo el usuario si existe, Optional.empty() si no existe
     */
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
}
