package com.bazarboost.system.repository;

import com.bazarboost.system.model.Producto;
import com.bazarboost.system.model.Resenia;
import com.bazarboost.system.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
public interface ReseniaRepository extends JpaRepository<Resenia, Integer> {

    /**
     * Recupera una reseña específica realizada por un usuario en un producto determinado.
     *
     * @param productoId ID del producto en el cual buscar la reseña.
     * @param usuarioId  ID del usuario que realizó la reseña.
     * @return Un Optional que contiene la reseña si existe; de lo contrario, un Optional vacío.
     */
    @Query("SELECT r FROM Resenia r WHERE r.producto.productoId = :productoId AND r.usuario.usuarioId = :usuarioId")
    Optional<Resenia> findByProductoIdAndUsuarioId(@Param("productoId") Integer productoId, @Param("usuarioId") Integer usuarioId);

    /**
     * Recupera todas las reseñas de un producto, excluyendo la reseña del usuario especificado.
     * Los resultados están paginados para facilitar la visualización en la interfaz.
     *
     * @param productoId ID del producto en el cual buscar las reseñas.
     * @param usuarioId  ID del usuario cuyas reseñas se deben excluir de los resultados.
     * @param pageable   Objeto de paginación que define el tamaño de la página y el número de página.
     * @return Una página de reseñas, excluyendo la del usuario especificado, ordenadas según el Pageable.
     */
    @Query("SELECT r FROM Resenia r WHERE r.producto.productoId = :productoId AND r.usuario.usuarioId <> :usuarioId")
    Page<Resenia> findByProductoIdAndUsuarioIdNot(@Param("productoId") Integer productoId, @Param("usuarioId") Integer usuarioId, Pageable pageable);

    /**
     * Obtiene la calificación promedio de las reseñas para un producto específico.
     *
     * @param productoId ID del producto para el cual se calcula la calificación promedio.
     * @return La calificación promedio como Double, o null si no hay reseñas.
     */
    @Query("SELECT AVG(r.calificacion) FROM Resenia r WHERE r.producto.productoId = :productoId")
    Double obtenerCalificacionPromedio(@Param("productoId") Integer productoId);

    /**
     * Verifica si existe una reseña en la base de datos para una combinación específica de usuario y producto.
     *
     * @param usuario el usuario que creó la reseña.
     * @param producto el producto al que pertenece la reseña.
     * @return true si ya existe una reseña para el usuario y producto especificados, false en caso contrario.
     */
    boolean existsByUsuarioAndProducto(Usuario usuario, Producto producto);


}


