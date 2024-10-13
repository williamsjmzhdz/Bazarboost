package com.bazarboost.repository;

import com.bazarboost.model.entity.MetodoPago;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface MetodoPagoRepository extends CrudRepository<MetodoPago, Integer> {

    // Encontrar todos los métodos de pago de un usuario
    @Query("SELECT mp FROM MetodoPago mp WHERE mp.usuario.usuarioId = :usuarioId")
    List<MetodoPago> findByUsuarioUsuarioId(@Param("usuarioId") Integer usuarioId);

    // Buscar un método de pago por su ID y validar que pertenece al usuario
    @Query("SELECT mp FROM MetodoPago mp WHERE mp.metodoPagoId = :metodoPagoId AND mp.usuario.usuarioId = :usuarioId")
    Optional<MetodoPago> findByMetodoPagoIdAndUsuarioUsuarioId(@Param("metodoPagoId") Integer metodoPagoId,
                                                               @Param("usuarioId") Integer usuarioId);

    // Eliminar un método de pago de un usuario específico
    @Transactional
    @Modifying
    @Query("DELETE FROM MetodoPago mp WHERE mp.metodoPagoId = :metodoPagoId AND mp.usuario.usuarioId = :usuarioId")
    void deleteByMetodoPagoIdAndUsuarioUsuarioId(@Param("metodoPagoId") Integer metodoPagoId,
                                                 @Param("usuarioId") Integer usuarioId);


    // Verificar fondos suficientes en el método de pago
    @Query("SELECT CASE WHEN mp.monto >= :total THEN true ELSE false END " +
            "FROM MetodoPago mp WHERE mp.metodoPagoId = :metodoPagoId AND mp.usuario.usuarioId = :usuarioId")
    boolean verifySufficientFunds(@Param("metodoPagoId") Integer metodoPagoId,
                                  @Param("usuarioId") Integer usuarioId,
                                  @Param("total") Double total);

    // Verificar si la tarjeta de crédito ha expirado
    @Query("SELECT CASE WHEN mp.fechaExpiracion > CURRENT_DATE THEN true ELSE false END " +
            "FROM MetodoPago mp WHERE mp.metodoPagoId = :metodoPagoId AND mp.usuario.usuarioId = :usuarioId")
    boolean verifyExpirationCard(@Param("metodoPagoId") Integer metodoPagoId,
                                 @Param("usuarioId") Integer usuarioId);
}


