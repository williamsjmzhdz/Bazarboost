package com.bazarboost.repository;

import com.bazarboost.model.entity.MetodoPago;
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
    List<MetodoPago> findByUsuarioUsuarioId(Integer usuarioId);

    // Buscar un método de pago por su ID y validar que pertenece al usuario
    Optional<MetodoPago> findByMetodoPagoIdAndUsuarioUsuarioId(Integer metodoPagoId, Integer usuarioId);

    // Eliminar un método de pago de un usuario específico
    @Transactional
    void deleteByMetodoPagoIdAndUsuarioUsuarioId(Integer metodoPagoId, Integer usuarioId);

    // Crear o editar un método de pago (método heredado de CrudRepository)
    // save() ya está heredado de CrudRepository.

    @Query("SELECT CASE WHEN mp.monto >= :total THEN true ELSE false END " +
            "FROM MetodoPago mp WHERE mp.metodoPagoId = :metodoPagoId AND mp.usuario.usuarioId = :usuarioId")
    boolean verifySufficientFunds(@Param("metodoPagoId") Integer metodoPagoId,
                                       @Param("usuarioId") Integer usuarioId,
                                       @Param("total") Double total);

    @Query("SELECT CASE WHEN mp.fechaExpiracion > CURRENT_DATE THEN true ELSE false END " +
            "FROM MetodoPago mp WHERE mp.metodoPagoId = :metodoPagoId AND mp.usuario.usuarioId = :usuarioId")
    boolean verifyExpirationCard(@Param("metodoPagoId") Integer metodoPagoId,
                                 @Param("usuarioId") Integer usuarioId);


}

