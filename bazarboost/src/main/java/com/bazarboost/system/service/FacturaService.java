package com.bazarboost.system.service;

import com.bazarboost.shared.exception.AccesoDenegadoException;
import com.bazarboost.shared.exception.FacturaNoEncontradaException;
import com.bazarboost.shared.exception.RolNoEncontradoException;
import com.bazarboost.shared.exception.UsuarioNoEncontradoException;
import com.bazarboost.shared.exception.OrdenNoValidoException;
import com.bazarboost.shared.exception.PaginaFueraDeRangoException;
import com.bazarboost.system.dto.*;

public interface FacturaService {

    /**
     * Procesa el pago del carrito para el usuario especificado.
     *
     * @param carritoPagoSolicitudDTO con toda la información para procesar el pago
     * @param usuarioId el ID del usuario dueño del carrito
     * @return El {@link CarritoPagoRespuestaDTO} con el ID de la factura generada
     */
    CarritoPagoRespuestaDTO procesarPago(CarritoPagoSolicitudDTO carritoPagoSolicitudDTO, Integer usuarioId);

    /**
     * Obtiene una página de facturas ordenadas y paginadas según los criterios indicados.
     *
     * @param ordenarPor     Campo por el cual ordenar las facturas, puede ser "fecha" o "total".
     * @param direccionOrden Dirección del orden, "asc" para ascendente o "desc" para descendente.
     * @param pagina         Número de página a obtener (comenzando desde 0).
     * @param tamanoPagina   Tamaño de la página de resultados.
     * @param usuarioId      ID del usuario propietario de las facturas.
     * @return Objeto que contiene las facturas en la página solicitada e información de paginación.
     * @throws UsuarioNoEncontradoException si el usuario no existe en el sistema.
     * @throws AccesoDenegadoException si el usuario no tiene el rol de Cliente.
     * @throws OrdenNoValidoException si los criterios de ordenamiento son inválidos.
     * @throws PaginaFueraDeRangoException si la página especificada está fuera de rango.
     * @throws
     */
    FacturasPaginadasDTO obtenerFacturasPaginadasYOrdenadas(String ordenarPor, String direccionOrden, Integer pagina, Integer tamanoPagina,
                                                            Integer usuarioId);

    /**
     * Obtiene el detalle completo de una factura específica.
     *
     * @param facturaId ID de la factura a consultar.
     * @param usuarioId ID del usuario que solicita la factura.
     * @return DTO con toda la información detallada de la factura.
     * @throws UsuarioNoEncontradoException si el usuario no existe en el sistema.
     * @throws AccesoDenegadoException si el usuario no tiene el rol Cliente.
     * @throws FacturaNoEncontradaException si la factura no existe en el sistema.
     * @throws AccesoDenegadoException si la factura no pertenece al usuario especificado.
     */
    DetalleFacturaDTO obtenerDetalleFactura(Integer facturaId, Integer usuarioId);

    /**
     * Obtiene una página de ventas ordenadas y paginadas según los criterios indicados.
     * Solo muestra las ventas de los productos del vendedor.
     *
     * @param ordenarPor     Campo por el cual ordenar las ventas, puede ser "fecha" o "total".
     * @param direccionOrden Dirección del orden, "asc" para ascendente o "desc" para descendente.
     * @param pagina         Número de página a obtener (comenzando desde 0).
     * @param tamanoPagina   Tamaño de la página de resultados.
     * @param vendedorId     ID del vendedor propietario de los productos vendidos.
     * @return Objeto que contiene las ventas en la página solicitada e información de paginación.
     * @throws UsuarioNoEncontradoException si el usuario no existe en el sistema.
     * @throws AccesoDenegadoException si el usuario no tiene el rol de Vendedor.
     * @throws OrdenNoValidoException si los criterios de ordenamiento son inválidos.
     * @throws PaginaFueraDeRangoException si la página especificada está fuera de rango.
     */
    VentasPaginadasDTO obtenerVentasPaginadasYOrdenadas(String ordenarPor, String direccionOrden, Integer pagina, Integer tamanoPagina,
                                                        Integer vendedorId);
}
