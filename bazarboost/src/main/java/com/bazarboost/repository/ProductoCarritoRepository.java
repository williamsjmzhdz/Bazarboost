package com.bazarboost.repository;

import com.bazarboost.model.ProductoCarrito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ProductoCarritoRepository extends CrudRepository<ProductoCarrito, Integer> {

    /**
     * Verifica si un producto específico está en el carrito de un usuario dado.
     *
     * @param productoId el ID del producto que se desea verificar.
     * @param usuarioId  el ID del usuario dueño del carrito.
     * @return true si el producto está en el carrito del usuario, false en caso contrario.
     */
    boolean existsByProductoProductoIdAndUsuarioUsuarioId(Integer productoId, Integer usuarioId);

}


