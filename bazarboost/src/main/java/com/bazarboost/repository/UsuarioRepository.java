package com.bazarboost.repository;

import com.bazarboost.model.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    // Obtener todos los usuarios con un indicador de si tienen un rol
    @Query("SELECT u, CASE WHEN (ur.rol.rolId = :rolId) THEN true ELSE false END " +
            "FROM Usuario u " +
            "LEFT JOIN UsuarioRol ur ON u.usuarioId = ur.usuario.usuarioId AND ur.rol.rolId = :rolId")
    List<Object[]> getUsersWithRole(@Param("rolId") int rolId);

}
