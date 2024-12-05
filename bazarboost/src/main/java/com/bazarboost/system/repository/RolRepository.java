package com.bazarboost.system.repository;

import com.bazarboost.system.model.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RolRepository extends CrudRepository<Rol, Integer> {

    /**
     * Encuentra un usuario por su nombre
     *
     * @param nombre nombre del usuario a buscar.
     * @return Objeto optional con el usuario si lo encuentra, Optional.empty() si no lo encuentra
     */
    Optional<Rol> findByNombre(String nombre);

}
