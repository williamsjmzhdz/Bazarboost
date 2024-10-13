package com.bazarboost;

import com.bazarboost.model.entity.Factura;
import com.bazarboost.model.entity.Usuario;
import com.bazarboost.model.entity.MetodoPago;
import com.bazarboost.model.entity.Direccion;
import com.bazarboost.repository.FacturaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql({"/schema.sql", "/data.sql"})
public class BazarboostFacturaTests {

    private static final String ALUMNO = "FRANCISCO WILLIAMS JIMÉNEZ HERNÁNDEZ";

    @Autowired
    private FacturaRepository facturaRepository;

    @Test
    @Order(1)
    void pruebaObtenerFacturasPorUsuarioConPaginacionTest() {
        System.out.println("=== INICIO PRUEBA: Obtener facturas por usuario con paginación ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: obtener facturas por usuario con paginación");
        System.out.println("-----------------------------------------------");

        // Usuario de ejemplo
        Integer usuarioId = 1;
        Pageable pageable = PageRequest.of(0, 5); // Página 0, 5 facturas por página

        // Obtener y mostrar facturas de la primera página
        Page<Factura> facturasPage1 = facturaRepository.findByUsuarioUsuarioId(usuarioId, pageable);
        System.out.println("Primera página de facturas del usuario con ID " + usuarioId + ":");
        facturasPage1.getContent().forEach(System.out::println);

        // Obtener y mostrar facturas de la segunda página
        pageable = PageRequest.of(1, 5); // Página 1, 5 facturas por página
        Page<Factura> facturasPage2 = facturaRepository.findByUsuarioUsuarioId(usuarioId, pageable);
        System.out.println("Segunda página de facturas del usuario con ID " + usuarioId + ":");
        facturasPage2.getContent().forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Obtener facturas por usuario con paginación ===");
        System.out.println();
    }

    @Test
    @Order(2)
    void pruebaOrdenarFacturasPorMontoDescConPaginacionTest() {
        System.out.println("=== INICIO PRUEBA: Ordenar facturas por monto descendente con paginación ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: ordenar facturas por monto total descendente con paginación");
        System.out.println("-----------------------------------------------");

        // Usuario de ejemplo
        Integer usuarioId = 1;
        Pageable pageable = PageRequest.of(0, 5); // Página 0, 5 facturas por página

        // Obtener y mostrar la primera página de facturas ordenadas de mayor a menor
        Page<Factura> facturasPage1 = facturaRepository.findFacturasByUsuarioIdOrderByTotalDesc(usuarioId, pageable);
        System.out.println("Primera página de facturas ordenadas de mayor a menor por monto:");
        facturasPage1.getContent().forEach(System.out::println);

        // Obtener y mostrar la segunda página de facturas
        pageable = PageRequest.of(1, 5); // Página 1, 5 facturas por página
        Page<Factura> facturasPage2 = facturaRepository.findFacturasByUsuarioIdOrderByTotalDesc(usuarioId, pageable);
        System.out.println("Segunda página de facturas ordenadas de mayor a menor por monto:");
        facturasPage2.getContent().forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Ordenar facturas por monto descendente con paginación ===");
        System.out.println();
    }

    @Test
    @Order(3)
    void pruebaOrdenarFacturasPorMontoAscConPaginacionTest() {
        System.out.println("=== INICIO PRUEBA: Ordenar facturas por monto ascendente con paginación ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: ordenar facturas por monto total ascendente con paginación");
        System.out.println("-----------------------------------------------");

        // Usuario de ejemplo
        Integer usuarioId = 1;
        Pageable pageable = PageRequest.of(0, 5); // Página 0, 5 facturas por página

        // Obtener y mostrar la primera página de facturas ordenadas de menor a mayor
        Page<Factura> facturasPage1 = facturaRepository.findFacturasByUsuarioIdOrderByTotalAsc(usuarioId, pageable);
        System.out.println("Primera página de facturas ordenadas de menor a mayor por monto:");
        facturasPage1.getContent().forEach(System.out::println);

        // Obtener y mostrar la segunda página de facturas
        pageable = PageRequest.of(1, 5); // Página 1, 5 facturas por página
        Page<Factura> facturasPage2 = facturaRepository.findFacturasByUsuarioIdOrderByTotalAsc(usuarioId, pageable);
        System.out.println("Segunda página de facturas ordenadas de menor a mayor por monto:");
        facturasPage2.getContent().forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Ordenar facturas por monto ascendente con paginación ===");
        System.out.println();
    }

    @Test
    @Order(4)
    void pruebaCrearOEditarFacturaTest() {
        System.out.println("=== INICIO PRUEBA: Crear o editar una factura ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Prueba: crear o editar una factura");
        System.out.println("-----------------------------------------------");

        // Crear una nueva factura
        Factura nuevaFactura = new Factura();
        nuevaFactura.setSubtotal(100.00);
        nuevaFactura.setTotal(110.00);
        nuevaFactura.setPorcentajeImpuestos(10);
        nuevaFactura.setFecha(LocalDateTime.now());

        // Asignar entidades relacionadas
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(1); // Usuario de prueba
        nuevaFactura.setUsuario(usuario);

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setMetodoPagoId(1); // Método de pago de prueba
        nuevaFactura.setMetodoPago(metodoPago);

        Direccion direccion = new Direccion();
        direccion.setDireccionId(2); // Dirección de prueba
        nuevaFactura.setDireccion(direccion);

        // Guardar la nueva factura
        System.out.println("Factura a insertar: " + nuevaFactura);
        facturaRepository.save(nuevaFactura);
        System.out.println("Nueva factura creada con éxito.");

        // Mostrar lista actualizada de facturas, paginadas (primera y segunda página)
        Pageable pageable = PageRequest.of(0, 5); // Página 0, 5 facturas por página
        Page<Factura> facturasPage1 = facturaRepository.findByUsuarioUsuarioId(1, pageable);
        System.out.println("Primera página de la lista actualizada de facturas:");
        facturasPage1.getContent().forEach(System.out::println);

        pageable = PageRequest.of(1, 5); // Página 1, 5 facturas por página
        Page<Factura> facturasPage2 = facturaRepository.findByUsuarioUsuarioId(1, pageable);
        System.out.println("Segunda página de la lista actualizada de facturas:");
        facturasPage2.getContent().forEach(System.out::println);

        pageable = PageRequest.of(2, 5); // Página 2, 5 facturas por página
        Page<Factura> facturasPage3 = facturaRepository.findByUsuarioUsuarioId(1, pageable);
        System.out.println("Tercera página de la lista actualizada de facturas:");
        facturasPage3.getContent().forEach(System.out::println);

        System.out.println("=== FIN PRUEBA: Crear o editar una factura ===");
        System.out.println();
    }
}
