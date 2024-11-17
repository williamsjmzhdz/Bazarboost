package com.bazarboost.repository;

import com.bazarboost.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT COUNT(ur) > 0 FROM UsuarioRol ur " +
            "WHERE ur.usuario.usuarioId = :usuarioId " +
            "AND ur.rol.nombre = :nombreRol " +
            "AND ur.usuario.usuarioId IS NOT NULL " +
            "AND ur.rol.rolId IS NOT NULL")
    boolean tieneRol(@Param("usuarioId") Integer usuarioId, @Param("nombreRol") String nombreRol);

}
