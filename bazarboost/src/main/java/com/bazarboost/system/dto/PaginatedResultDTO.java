package com.bazarboost.system.dto;

/**
 * Interfaz que define los métodos necesarios para objetos que contienen información de paginación.
 * Debe ser implementada por los DTOs que requieran manejar datos de paginación.
 */
public interface PaginatedResultDTO {
    void setPaginaActual(int paginaActual);
    void setTotalPaginas(int totalPaginas);
    void setTotalElementos(long totalElementos);
}