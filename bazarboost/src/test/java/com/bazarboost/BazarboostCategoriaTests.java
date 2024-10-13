package com.bazarboost;

import com.bazarboost.model.entity.Categoria;
import com.bazarboost.repository.CategoriaRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
// Define el orden de ejecución de los tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
public class BazarboostCategoriaTests {

    private final String ALUMNO = "FRANCISCO WILLIAMS JIMÉNEZ HERNÁNDEZ";

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @Order(1) // Se ejecuta primero
    void crearOEditarCategoriaTest() {
        System.out.println("=== INICIO PRUEBA: Crear o editar una categoría ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: crear o editar una categoría");
        System.out.println("-----------------------------------------------");

        // Crear una nueva categoría
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("Nueva Categoría");

        // Imprimir la categoría a crear
        System.out.println("Datos de la categoría a insertar:");
        System.out.println("- Nombre: " + nuevaCategoria.getNombre());

        // Guardar la nueva categoría
        categoriaRepository.save(nuevaCategoria);
        System.out.println("Categoría guardada con éxito.");

        // Lista actualizada de categorías
        List<Categoria> categorias = categoriaRepository.findAll();
        System.out.println("Lista actualizada de categorías (" + categorias.size() + "):");
        categorias.forEach(categoria ->
                System.out.println("- Categoría: " + categoria.getNombre() + " (ID: " + categoria.getCategoriaId() + ")"));

        System.out.println("=== FIN PRUEBA: Crear o editar una categoría ===");
        System.out.println();
    }

    @Test
    @Order(2) // Se ejecuta en segundo lugar
    void encontrarTodasLasCategoriasTest() {
        System.out.println("=== INICIO PRUEBA: Encontrar todas las categorías ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: encontrar todas las categorías");
        System.out.println("-----------------------------------------------");

        // Lista actual de categorías
        List<Categoria> categorias = categoriaRepository.findAll();
        System.out.println("Lista actual de categorías (" + categorias.size() + "):");
        if (categorias.isEmpty()) {
            System.out.println("No hay categorías disponibles.");
        } else {
            categorias.forEach(categoria ->
                    System.out.println("- Categoría: " + categoria.getNombre() + " (ID: " + categoria.getCategoriaId() + ")"));
        }

        System.out.println("=== FIN PRUEBA: Encontrar todas las categorías ===");
        System.out.println();
    }

    @Test
    @Order(3) // Se ejecuta en tercer lugar
    void buscarCategoriaPorIdTest() {
        System.out.println("=== INICIO PRUEBA: Buscar una categoría por ID ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: buscar una categoría por su ID");
        System.out.println("-----------------------------------------------");

        // ID de la categoría a buscar
        Integer categoriaId = 1;
        System.out.println("Buscando categoría con ID: " + categoriaId);

        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        if (categoria.isPresent()) {
            System.out.println("Categoría encontrada:");
            System.out.println("- Nombre: " + categoria.get().getNombre());
            System.out.println("- ID: " + categoria.get().getCategoriaId());
        } else {
            System.out.println("Categoría con ID " + categoriaId + " no encontrada.");
        }

        System.out.println("=== FIN PRUEBA: Buscar una categoría por ID ===");
        System.out.println();
    }

    @Test
    @Order(4) // Se ejecuta en cuarto lugar
    void eliminarCategoriaPorIdTest() {
        System.out.println("=== INICIO PRUEBA: Eliminar una categoría por ID ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: eliminar una categoría por su ID");
        System.out.println("-----------------------------------------------");

        // Mostrar lista original de categorías
        List<Categoria> categoriasOriginales = categoriaRepository.findAll();
        System.out.println("Lista original de categorías (" + categoriasOriginales.size() + "):");
        categoriasOriginales.forEach(categoria ->
                System.out.println("- Categoría: " + categoria.getNombre() + " (ID: " + categoria.getCategoriaId() + ")"));

        // ID de la categoría a eliminar
        Integer categoriaId = 1;
        System.out.println("Eliminando categoría con ID: " + categoriaId);

        // Eliminar la categoría
        categoriaRepository.deleteById(categoriaId);

        System.out.println("Verificando eliminación...");

        // Verificar si la categoría fue eliminada
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        if (categoria.isPresent()) {
            System.out.println("Categoría no ha sido eliminada.");
        } else {
            System.out.println("Categoría eliminada correctamente.");
        }

        // Mostrar lista de categorías después de la eliminación
        List<Categoria> categoriasActualizadas = categoriaRepository.findAll();
        System.out.println("Lista actualizada de categorías (" + categoriasActualizadas.size() + "):");
        categoriasActualizadas.forEach(categoriaActualizada ->
                System.out.println("- Categoría: " + categoriaActualizada.getNombre() + " (ID: " + categoriaActualizada.getCategoriaId() + ")"));

        System.out.println("=== FIN PRUEBA: Eliminar una categoría por ID ===");
        System.out.println();
    }

}
