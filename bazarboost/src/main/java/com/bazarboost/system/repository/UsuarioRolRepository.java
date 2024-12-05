package com.bazarboost.system.repository;

import com.bazarboost.system.model.Rol;
import com.bazarboost.system.model.Usuario;
import com.bazarboost.system.model.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Integer> {

    /**
     * Elimina un registro con base en el usuario y el rol
     * @param usuario Objeto usuario al que se le va quitar el rol.
     * @param rol Objeto rol que se le quitará al usuario especificado.
     */
    void deleteByUsuarioAndRol(Usuario usuario, Rol rol);
}
