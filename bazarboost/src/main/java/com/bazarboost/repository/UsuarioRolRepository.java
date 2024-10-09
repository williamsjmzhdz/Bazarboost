package com.bazarboost.repository;

import com.bazarboost.model.entity.UsuarioRol;
import org.springframework.data.repository.CrudRepository;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface UsuarioRolRepository extends CrudRepository<UsuarioRol, Integer> {

    // Eliminar por usuarioId y rolId
    void deleteByUsuarioUsuarioIdAndRolRolId(Integer usuarioId, Integer rolId);

    // Para asignar un nuevo rol, se utiliza el método save() heradado de CrudRepository

}
