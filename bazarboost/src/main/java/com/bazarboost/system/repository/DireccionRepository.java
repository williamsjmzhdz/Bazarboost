package com.bazarboost.system.repository;

import com.bazarboost.system.model.Direccion;
import com.bazarboost.system.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DireccionRepository extends CrudRepository<Direccion, Integer> {

    /**
     * Busca una dirección específica asociada a un usuario.
     *
     * @param direccionId ID de la direccion que se desea buscar.
     * @param usuario Objeto usuario al cual debe estar asociada la dirección.
     * @return Un Optional que contiene la dirección si se encuentra, o vacío si no existe.
     */
    Optional<Direccion> findByDireccionIdAndUsuario(Integer direccionId, Usuario usuario);

    /**
     * Recupera todas las direcciones asociadas a un usuario específico.
     *
     * @param usuarioId ID del usuario del cual se desean obtener las direcciones.
     * @return Lista de direcciones asociadas al usuario. Si el usuario no tiene direcciones,
     *         retorna una lista vacía.
     * @throws IllegalArgumentException si el ID es null.
     */
    List<Direccion> findByUsuarioUsuarioId(Integer usuarioId);

    /**
     * Recupera todas las direcciones asociadas a un usuario específico.
     *
     * @param usuario Objeto Usuario del cual se desean obtener las direcciones.
     * @return Lista de direcciones asociadas al usuario. Si el usuario no tiene direcciones,
     *         retorna una lista vacía.
     * @throws IllegalArgumentException si usuario es null.
     */
    List<Direccion> findByUsuario(Usuario usuario);

}

