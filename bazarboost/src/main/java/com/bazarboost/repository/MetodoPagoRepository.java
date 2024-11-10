package com.bazarboost.repository;

import com.bazarboost.model.MetodoPago;
import com.bazarboost.model.Usuario;
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

    /**
     * Busca un método de pago específico asociado a un usuario.
     *
     * @param metodoPagoId ID del método de pago que se desea buscar.
     * @param usuario Objeto usuario al cual debe estar asociado el método de pago.
     * @return Un Optional que contiene el método de pago si se encuentra, o vacío si no existe.
     */
    Optional<MetodoPago> findByMetodoPagoIdAndUsuario(Integer metodoPagoId, Usuario usuario);


    List<MetodoPago> findByUsuarioUsuarioId(Integer usuarioId);

    /**
     * Reduce el monto disponible de un método de pago en un monto específico.
     *
     * @param metodoPagoId ID del método de pago a actualizar.
     * @param monto        Monto a reducir del saldo disponible.
     */
    @Modifying
    @Query("UPDATE MetodoPago m SET m.monto = m.monto - :monto WHERE m.metodoPagoId = :metodoPagoId")
    void reducirMonto(@Param("metodoPagoId") Integer metodoPagoId, @Param("monto") Double monto);



}


