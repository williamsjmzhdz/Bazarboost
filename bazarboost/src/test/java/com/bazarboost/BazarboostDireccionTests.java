package com.bazarboost;

import com.bazarboost.model.entity.Direccion;
import com.bazarboost.model.entity.Usuario;
import com.bazarboost.repository.DireccionRepository;
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
public class BazarboostDireccionTests {

    private static final String ALUMNO = "FRANCISCO WILLIAMS JIMÉNEZ HERNÁNDEZ";

    @Autowired
    private DireccionRepository direccionRepository;

    @Test
    @Order(1)
    void pruebaEncontrarTodasLasDireccionesDeUsuarioTest() {
        System.out.println("=== INICIO PRUEBA: Encontrar todas las direcciones de un usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: encontrar todas las direcciones de un usuario");
        System.out.println("-----------------------------------------------");

        // Usuario de ejemplo
        Integer usuarioId = 1;

        // Buscar todas las direcciones de un usuario
        List<Direccion> direcciones = direccionRepository.findByUsuarioUsuarioId(usuarioId);
        System.out.println("Direcciones del usuario con ID " + usuarioId + ":");
        direcciones.forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Encontrar todas las direcciones de un usuario ===");
        System.out.println();
    }

    @Test
    @Order(2)
    void pruebaBuscarDireccionPorIdYUsuarioTest() {
        System.out.println("=== INICIO PRUEBA: Buscar dirección por ID y usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: buscar dirección por ID y usuario");
        System.out.println("-----------------------------------------------");

        // ID de dirección y usuario
        Integer direccionId = 1;
        Integer usuarioId = 1;
        System.out.println("Buscando dirección con ID: " + direccionId + " y usuario ID: " + usuarioId);

        Optional<Direccion> direccion = direccionRepository.findByDireccionIdAndUsuarioUsuarioId(direccionId, usuarioId);
        if (direccion.isPresent()) {
            System.out.println("Dirección encontrada: " + direccion.get());
        } else {
            System.out.println("Dirección con ID " + direccionId + " y usuario ID " + usuarioId + " no encontrada.");
        }

        System.out.println("=== FIN PRUEBA: Buscar dirección por ID y usuario ===");
        System.out.println();
    }

    @Test
    @Order(3)
    void pruebaCrearOEditarDireccionTest() {
        System.out.println("=== INICIO PRUEBA: Crear o editar una dirección ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: crear o editar una dirección");
        System.out.println("-----------------------------------------------");

        // Crear nueva dirección
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1); // Usuario de prueba
        Direccion nuevaDireccion = new Direccion();
        nuevaDireccion.setEstado("Puebla");
        nuevaDireccion.setCiudad("Puebla");
        nuevaDireccion.setColonia("Centro");
        nuevaDireccion.setCalle("Calle Principal");
        nuevaDireccion.setNumeroDomicilio(123);
        nuevaDireccion.setCodigoPostal("72000");
        nuevaDireccion.setUsuario(usuario);

        // Guardar la nueva dirección
        System.out.println("Dirección a insertar: " + nuevaDireccion);
        direccionRepository.save(nuevaDireccion);
        System.out.println("Nueva dirección creada con éxito.");

        // Mostrar lista actualizada de direcciones
        List<Direccion> direcciones = direccionRepository.findByUsuarioUsuarioId(usuario.getUsuarioId());
        System.out.println("Lista actualizada de direcciones:");
        direcciones.forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Crear o editar una dirección ===");
        System.out.println();
    }

    @Test
    @Order(4)
    void pruebaEliminarDireccionPorIdYUsuarioTest() {
        System.out.println("=== INICIO PRUEBA: Eliminar una dirección por ID y usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: eliminar una dirección por ID y usuario");
        System.out.println("-----------------------------------------------");

        // ID de dirección y usuario
        Integer direccionId = 1;
        Integer usuarioId = 1;

        // Mostrar lista original de direcciones
        List<Direccion> direccionesOriginales = direccionRepository.findByUsuarioUsuarioId(usuarioId);
        System.out.println("Lista original de direcciones:");
        direccionesOriginales.forEach(System.out::println);

        // Eliminar la dirección
        System.out.println("Eliminando dirección con ID: " + direccionId + " del usuario ID: " + usuarioId);
        direccionRepository.deleteByDireccionIdAndUsuarioUsuarioId(direccionId, usuarioId);
        System.out.println("Dirección eliminada con éxito.");

        // Verificar lista actualizada de direcciones
        List<Direccion> direccionesActualizadas = direccionRepository.findByUsuarioUsuarioId(usuarioId);
        System.out.println("Lista actualizada de direcciones:");
        direccionesActualizadas.forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Eliminar una dirección por ID y usuario ===");
        System.out.println();
    }
}
