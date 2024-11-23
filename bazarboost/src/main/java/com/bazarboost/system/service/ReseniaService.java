package com.bazarboost.system.service;

import com.bazarboost.system.dto.CalificacionPromedioDTO;
import com.bazarboost.system.dto.ReseniaCreacionDTO;
import com.bazarboost.system.dto.ReseniaEdicionDTO;
import com.bazarboost.system.dto.ReseniaRespuestaDTO;
import com.bazarboost.shared.exception.AccesoDenegadoException;
import com.bazarboost.shared.exception.ProductoNoEncontradoException;
import com.bazarboost.shared.exception.ReseniaNoEncontradaException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.system.model.Producto;

import java.math.BigDecimal;


public interface ReseniaService {

    /**
     * Crea una nueva reseña asociada a un producto y un usuario.
     * Verifica que el producto y el usuario existan, y que el usuario
     * no haya reseñado previamente el producto.
     *
     * @param reseniaDTO datos de la reseña (calificación, comentario, productoId).
     * @param usuarioId ID del usuario que crea la reseña.
     * @return ReseniaRespuestaDTO con la información de la reseña creada.
     * @throws ProductoNoEncontradoException si el producto no existe.
     * @throws UsuarioNoEncontradoException si el usuario no existe.
     */
    ReseniaRespuestaDTO crearResenia(ReseniaCreacionDTO reseniaDTO, Integer usuarioId);

    /**
     * Actualiza una reseña existente en la base de datos, asegurando que la reseña
     * pertenece al usuario autenticado que intenta realizar la actualización.
     *
     * @param reseniaDTO el objeto DTO que contiene los datos de la reseña a actualizar,
     *                   incluyendo el ID de la reseña, el nuevo comentario y la nueva calificación.
     * @param usuarioId el ID del usuario autenticado que realiza la solicitud, usado para verificar
     *                  la propiedad de la reseña.
     * @return un objeto ReseniaRespuestaDTO con la información actualizada de la reseña.
     * @throws ReseniaNoEncontradaException si la reseña con el ID especificado no existe.
     * @throws AccesoDenegadoException si el usuario autenticado no es el propietario de la reseña.
     */
    ReseniaRespuestaDTO editarResenia(ReseniaEdicionDTO reseniaDTO, Integer usuarioId);


    /**
     * Elimina una reseña existente, verificando que el usuario autenticado sea el propietario
     * de la reseña antes de proceder. Al eliminar una reseña, se actualiza la calificación promedio
     * del producto asociado.
     *
     * @param reseniaId el ID de la reseña a eliminar.
     * @param usuarioId el ID del usuario autenticado que realiza la solicitud, usado para verificar
     *                  la propiedad de la reseña.
     * @return un objeto CalificacionPromedioDTO con la calificación promedio actualizada del producto.
     * @throws ReseniaNoEncontradaException si la reseña con el ID especificado no existe.
     * @throws AccesoDenegadoException si el usuario autenticado no es el propietario de la reseña.
     */
    CalificacionPromedioDTO eliminarResenia(Integer reseniaId, Integer usuarioId);


    /**
     * Calcula la calificación promedio de un producto específico en base a las reseñas asociadas.
     *
     * Este método consulta las reseñas del producto proporcionado y calcula el promedio de
     * las calificaciones. Si el producto no tiene reseñas, retorna un valor de 0.
     *
     * @param producto El producto para el cual se desea calcular la calificación promedio.
     * @return Un BigDecimal con la calificación promedio redondeada a un decimal; si no hay
     *         reseñas, retorna BigDecimal.ZERO.
     */
    BigDecimal calcularCalificacionPromedio(Producto producto);

}
