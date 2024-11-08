package com.bazarboost.repository;

import com.bazarboost.model.Direccion;
import com.bazarboost.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface DireccionRepository extends CrudRepository<Direccion, Integer> {

    /**
     * Busca una dirección específica asociada a un usuario.
     *
     * @param direccionId ID de la direccion que se desea buscar.
     * @param usuario Objeto usuario al cual debe estar asociada la dirección.
     * @return Un Optional que contiene la dirección si se encuentra, o vacío si no existe.
     */
    Optional<Direccion> findByDireccionIdAndUsuario(Integer direccionId, Usuario usuario);

}

