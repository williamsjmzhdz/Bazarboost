package com.bazarboost.repository;

import com.bazarboost.model.Producto;
import com.bazarboost.model.ProductoCarrito;
import com.bazarboost.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio que gestiona las operaciones de persistencia para la entidad ProductoCarrito.
 * Proporciona métodos para buscar, guardar y eliminar productos del carrito.
 */
@Repository
public interface ProductoCarritoRepository extends CrudRepository<ProductoCarrito, Integer> {

    /**
     * Verifica si un producto específico está en el carrito de un usuario dado.
     *
     * @param productoId ID del producto que se desea verificar
     * @param usuarioId ID del usuario dueño del carrito
     * @return true si el producto está en el carrito del usuario, false en caso contrario
     */
    boolean existsByProductoProductoIdAndUsuarioUsuarioId(Integer productoId, Integer usuarioId);

    /**
     * Busca un producto específico en el carrito de un usuario.
     *
     * @param usuario usuario propietario del carrito
     * @param producto producto a buscar en el carrito
     * @return Optional<ProductoCarrito> que contiene el producto del carrito si existe
     */
    Optional<ProductoCarrito> findByUsuarioAndProducto(Usuario usuario, Producto producto);

    /**
     * Calcula el total de productos en el carrito de un usuario.
     * La cantidad se calcula sumando el campo 'cantidad' de todos los productos en el carrito.
     *
     * @param usuarioId ID del usuario propietario del carrito
     * @return Integer con la suma total de productos. Null si el carrito está vacío
     */
    @Query("SELECT SUM(pc.cantidad) FROM ProductoCarrito pc WHERE pc.usuario.usuarioId = :usuarioId")
    Integer totalProductosEnCarrito(Integer usuarioId);

    /**
     * Obtiene los productos en el carrito del usuario especificado.
     *
     * @param usuarioId ID del usuario propietario del carrito
     * @return List<ProductoCarrito> con todos los productos en el carrito del propietario
     */
    List<ProductoCarrito> findByUsuarioUsuarioId(Integer usuarioId);


    /**
     * Calcula el precio total de todos los productos en el carrito de un usuario específico.
     *
     * @param usuario El usuario cuyo carrito se desea calcular.
     * @return El precio total del carrito como BigDecimal.
     */
    @Query("SELECT SUM(pc.total) FROM ProductoCarrito pc WHERE pc.usuario = :usuario")
    BigDecimal obtenerPrecioTotalCarrito(@Param("usuario") Usuario usuario);

}
