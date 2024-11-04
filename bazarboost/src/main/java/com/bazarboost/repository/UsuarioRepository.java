package com.bazarboost.repository;

import com.bazarboost.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /*
    // Obtener todos los usuarios con un indicador de si tienen un rol
    @Query("SELECT u, CASE WHEN (ur.rol.rolId = :rolId) THEN true ELSE false END " +
            "FROM Usuario u " +
            "LEFT JOIN UsuarioRol ur ON u.usuarioId = ur.usuario.usuarioId AND ur.rol.rolId = :rolId")
    List<Object[]> getUsersWithRole(@Param("rolId") int rolId);
     */
}
