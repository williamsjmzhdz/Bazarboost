package com.bazarboost.system.repository;

import com.bazarboost.system.model.Factura;
import com.bazarboost.system.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 */
public interface FacturaRepository extends PagingAndSortingRepository<Factura, Integer>, CrudRepository<Factura, Integer> {

    /**
     * Encuentra todas las facturas asociadas a un usuario específico con soporte de paginación.
     *
     * @param usuario El usuario cuyas facturas se desean recuperar.
     * @param pageable Objeto que define la paginación y ordenamiento de los resultados.
     * @return Página de facturas correspondientes al usuario especificado.
     */
    Page<Factura> findByUsuario(Usuario usuario, Pageable pageable);

    /**
     * Cuenta la cantidad total de facturas asociadas a un usuario específico.
     *
     * @param usuario El usuario cuyas facturas se desean contar.
     * @return El número total de facturas para el usuario especificado.
     */
    long countByUsuario(Usuario usuario);
}