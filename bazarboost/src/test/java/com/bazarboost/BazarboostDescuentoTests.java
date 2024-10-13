package com.bazarboost;

import com.bazarboost.model.entity.Descuento;
import com.bazarboost.model.entity.Usuario;
import com.bazarboost.repository.DescuentoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql({"/schema.sql", "/data.sql"})
public class BazarboostDescuentoTests {

    private static final String ALUMNO = "FRANCISCO WILLIAMS JIMÉNEZ HERNÁNDEZ";

    @Autowired
    private DescuentoRepository descuentoRepository;

    @Test
    @Order(1)
    void pruebaBuscarTodosLosDescuentosDeUnUsuarioTest() {
        System.out.println("=== INICIO PRUEBA: Buscar todos los descuentos de un usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: buscar todos los descuentos de un usuario");
        System.out.println("-----------------------------------------------");

        // Usuario de ejemplo
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1); // Usuario de prueba

        // Buscar descuentos del usuario
        List<Descuento> descuentos = descuentoRepository.findByUsuario(usuario);
        System.out.println("Descuentos del usuario con ID " + usuario.getUsuarioId() + ":");
        descuentos.forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Buscar todos los descuentos de un usuario ===");
        System.out.println();
    }

    @Test
    @Order(2)
    void pruebaBuscarDescuentoPorIdYUsuarioTest() {
        System.out.println("=== INICIO PRUEBA: Buscar descuento por ID y usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: buscar descuento por ID y usuario");
        System.out.println("-----------------------------------------------");

        // ID de descuento y usuario
        Integer descuentoId = 1;
        Integer usuarioId = 1;
        System.out.println("Buscando descuento con ID: " + descuentoId + " y usuario ID: " + usuarioId);

        Optional<Descuento> descuento = descuentoRepository.findByDescuentoIdAndUsuarioUsuarioId(descuentoId, usuarioId);
        if (descuento.isPresent()) {
            System.out.println("Descuento encontrado: " + descuento.get());
        } else {
            System.out.println("Descuento con ID " + descuentoId + " y usuario ID " + usuarioId + " no encontrado.");
        }

        System.out.println("=== FIN PRUEBA: Buscar descuento por ID y usuario ===");
        System.out.println();
    }

    @Test
    @Order(3)
    void pruebaCrearOEditarDescuentoTest() {
        System.out.println("=== INICIO PRUEBA: Crear o editar un descuento ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: crear o editar un descuento");
        System.out.println("-----------------------------------------------");

        // Crear nuevo descuento
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1); // Usuario de prueba
        Descuento nuevoDescuento = new Descuento();
        nuevoDescuento.setNombre("Descuento Especial");
        nuevoDescuento.setPorcentaje(20);
        nuevoDescuento.setUsuario(usuario);

        // Guardar el nuevo descuento
        System.out.println("Descuento a insertar: " + nuevoDescuento);
        descuentoRepository.save(nuevoDescuento);
        System.out.println("Nuevo descuento creado con éxito.");

        // Mostrar lista actualizada de descuentos
        List<Descuento> descuentos = descuentoRepository.findByUsuario(usuario);
        System.out.println("Lista actualizada de descuentos:");
        descuentos.forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Crear o editar un descuento ===");
        System.out.println();
    }

    @Test
    @Order(4)
    void pruebaEliminarDescuentoPorIdYUsuarioTest() {
        System.out.println("=== INICIO PRUEBA: Eliminar un descuento por ID y usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: eliminar un descuento por ID y usuario");
        System.out.println("-----------------------------------------------");

        // ID de descuento y usuario
        Integer descuentoId = 1;
        Integer usuarioId = 1;

        // Mostrar lista original de descuentos
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(usuarioId);
        List<Descuento> descuentosOriginales = descuentoRepository.findByUsuario(usuario);
        System.out.println("Lista original de descuentos:");
        descuentosOriginales.forEach(System.out::println);

        // Eliminar el descuento
        System.out.println("Eliminando descuento con ID: " + descuentoId + " del usuario ID: " + usuarioId);
        descuentoRepository.deleteByDescuentoIdAndUsuarioUsuarioId(descuentoId, usuarioId);
        System.out.println("Descuento eliminado con éxito.");

        // Verificar lista actualizada de descuentos
        List<Descuento> descuentosActualizados = descuentoRepository.findByUsuario(usuario);
        System.out.println("Lista actualizada de descuentos:");
        descuentosActualizados.forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Eliminar un descuento por ID y usuario ===");
        System.out.println();
    }
}
