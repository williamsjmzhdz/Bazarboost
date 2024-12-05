package com.bazarboost.system.repository;

import com.bazarboost.system.model.Factura;
import com.bazarboost.system.model.ProductoFactura;
import com.bazarboost.system.model.Rol;
import com.bazarboost.system.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductoFacturaRepository extends JpaRepository<ProductoFactura, Integer> {

    /**
     * Encuentra los productos asociados a una factura.
     *
     * @param factura Objeto factura sobre el que se requieren los detalles.
     * @return Lista de productos asociados a la factura.
     */
    List<ProductoFactura> findByFactura(Factura factura);

    /**
     * Encuentra todos los productos vendidos por un vendedor específico.
     *
     * @param vendedor Vendedor cuyos productos vendidos se desean buscar.
     * @param pageable Objeto que define la paginación y ordenamiento de los resultados.
     * @return Página de productos vendidos del vendedor.
     */
    @Query("SELECT pf FROM ProductoFactura pf " +
            "WHERE pf.producto.usuario = :vendedor")
    Page<ProductoFactura> findByProductoUsuario(@Param("vendedor") Usuario vendedor, Pageable pageable);

    /**
     * Cuenta el total de productos vendidos por un vendedor específico.
     *
     * @param vendedor Vendedor cuyos productos vendidos se desean contar.
     * @return Número total de productos vendidos del vendedor.
     */
    @Query("SELECT COUNT(pf) FROM ProductoFactura pf " +
            "WHERE pf.producto.usuario = :vendedor")
    long countByProductoUsuario(@Param("vendedor") Usuario vendedor);

}