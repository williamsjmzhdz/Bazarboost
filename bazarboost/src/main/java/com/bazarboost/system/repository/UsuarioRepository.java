package com.bazarboost.system.repository;

import com.bazarboost.system.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Busca usuarios aplicando filtros en múltiples campos y retorna los resultados paginados.
     * La búsqueda es case-insensitive y utiliza coincidencia parcial (LIKE %keyword%).
     *
     * @param keyword Término de búsqueda que se aplicará a todos los campos especificados
     * @param pageable Objeto de paginación que especifica el número de página y tamaño
     * @return Page<Usuario> Página de usuarios que coinciden con los criterios de búsqueda
     */
    @Query("SELECT DISTINCT u FROM Usuario u WHERE " +
            "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.apellidoPaterno) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.apellidoMaterno) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.correoElectronico) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.telefono) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Usuario> buscarUsuarios(@Param("keyword") String keyword, Pageable pageable);
}
