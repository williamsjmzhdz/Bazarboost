package com.bazarboost.repository;

import com.bazarboost.model.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface RolRepository extends CrudRepository<Rol, Integer> {

    // Buscar un rol por su nombre
    Optional<Rol> findByNombre(String nombre);

}
