package com.bazarboost.repository;

import com.bazarboost.model.Producto;
import com.bazarboost.model.ProductoCarrito;
import com.bazarboost.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

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

    /**
     * Busca un producto en el carrito de un usuario específico.
     *
     * @param usuario el usuario que tiene el carrito.
     * @param producto el producto a buscar en el carrito.
     * @return un Optional con el ProductoCarrito si existe.
     */
    Optional<ProductoCarrito> findByUsuarioAndProducto(Usuario usuario, Producto producto);

    /**
     * Calcula el total de productos en el carrito de un usuario.
     *
     * @param usuarioId el ID del usuario.
     * @return el número total de productos en el carrito
     */
    @Query("SELECT SUM(pc.cantidad) FROM ProductoCarrito pc WHERE pc.usuario.id = :usuarioId")
    Integer totalProductosEnCarrito(Integer usuarioId);

}


