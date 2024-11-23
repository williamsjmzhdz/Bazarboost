package com.bazarboost.repository;

import com.bazarboost.system.model.MetodoPago;
import com.bazarboost.system.model.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface MetodoPagoRepository extends CrudRepository<MetodoPago, Integer> {

    /**
     * Busca un método de pago específico asociado a un usuario.
     *
     * @param metodoPagoId ID del método de pago que se desea buscar.
     * @param usuario Objeto usuario al cual debe estar asociado el método de pago.
     * @return Un Optional que contiene el método de pago si se encuentra, o vacío si no existe.
     */
    Optional<MetodoPago> findByMetodoPagoIdAndUsuario(Integer metodoPagoId, Usuario usuario);

    /**
     * Recupera todos los métodos de pago asociados a un usuario específico, utilizando su ID.
     *
     * @param usuarioId ID del usuario del cual se desean obtener los métodos de pago.
     * @return Lista de métodos de pago asociados al usuario. Si el usuario no tiene métodos de pago,
     *         retorna una lista vacía.
     * @throws IllegalArgumentException si usuarioId es null.
     */
    List<MetodoPago> findByUsuarioUsuarioId(Integer usuarioId);

    /**
     * Recupera todos los métodos de pago asociados a un usuario específico, utilizando el objeto Usuario.
     *
     * @param usuario Objeto Usuario del cual se desean obtener los métodos de pago.
     * @return Lista de métodos de pago asociados al usuario. Si el usuario no tiene métodos de pago,
     *         retorna una lista vacía.
     * @throws IllegalArgumentException si usuario es null.
     */
    List<MetodoPago> findByUsuario(Usuario usuario);

    /**
     * Reduce el monto disponible de un método de pago en un monto específico.
     *
     * @param metodoPagoId ID del método de pago a actualizar.
     * @param monto        Monto a reducir del saldo disponible.
     */
    @Modifying
    @Query("UPDATE MetodoPago m SET m.monto = m.monto - :monto WHERE m.metodoPagoId = :metodoPagoId")
    void reducirMonto(@Param("metodoPagoId") Integer metodoPagoId, @Param("monto") Double monto);

    /**
     * Verifica si existe un método de pago con el mismo número de tarjeta.
     *
     * @param numeroTarjeta Número de tarjeta a verificar.
     * @return true si el número de tarjeta ya existe, false si no.
     */
    boolean existsByNumeroTarjeta(String numeroTarjeta);
}
