package com.bazarboost; // Reemplaza con el paquete correspondiente

import com.bazarboost.model.entity.MetodoPago;
import com.bazarboost.model.entity.Usuario;
import com.bazarboost.model.entity.auxiliar.TipoTarjeta;
import com.bazarboost.repository.MetodoPagoRepository;
import com.bazarboost.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/*
 * Alumno: Francisco Williams Jiménez Hernández
 * Proyecto: Bazarboost
 * */
@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
@TestMethodOrder(OrderAnnotation.class)
public class BazarboostMetodoPagoTests {

    private static final String ALUMNO = "FRANCISCO WILLIAMS JIMÉNEZ HERNÁNDEZ";

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @Order(1)
    void testFindByUsuarioUsuarioId() {
        System.out.println("=== INICIO PRUEBA: Obtener métodos de pago por usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Descripción: Esta prueba verifica que se pueden obtener correctamente los métodos de pago asociados a un usuario específico.");
        System.out.println("---------------------------------------------------------");

        Integer usuarioId = 1;
        System.out.println("ID de usuario utilizado para la prueba: " + usuarioId);

        // Obtener los métodos de pago del usuario
        List<MetodoPago> metodosPago = metodoPagoRepository.findByUsuarioUsuarioId(usuarioId);

        // Verificar que la lista no es nula
        Assertions.assertNotNull(metodosPago, "La lista de métodos de pago no debería ser nula.");

        // Imprimir los métodos de pago obtenidos
        System.out.println("Se obtuvieron " + metodosPago.size() + " método(s) de pago para el usuario con ID " + usuarioId + ":");
        for (MetodoPago mp : metodosPago) {
            System.out.println("- Método de Pago ID: " + mp.getMetodoPagoId());
            System.out.println("  - Titular: " + mp.getNombreTitular());
            System.out.println("  - Número de tarjeta: " + mp.getNumeroTarjeta());
            System.out.println("  - Fecha de expiración: " + mp.getFechaExpiracion());
            System.out.println("  - Tipo de tarjeta: " + mp.getTipoTarjeta());
            System.out.println("  - Monto disponible: $" + mp.getMonto());
            System.out.println();
        }

        System.out.println("=== FIN PRUEBA: Obtener métodos de pago por usuario ===");
        System.out.println();
    }


    @Test
    @Order(2)
    void testFindByMetodoPagoIdAndUsuarioUsuarioId() {
        System.out.println("=== INICIO PRUEBA: Obtener método de pago por ID y usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Descripción: Esta prueba verifica que se puede obtener un método de pago específico para un usuario dado, utilizando su ID de método de pago y ID de usuario.");
        System.out.println("-------------------------------------------------------------");

        Integer metodoPagoId = 5;
        Integer usuarioId = 5;

        System.out.println("ID de método de pago utilizado para la prueba: " + metodoPagoId);
        System.out.println("ID de usuario utilizado para la prueba: " + usuarioId);

        // Obtener el método de pago
        Optional<MetodoPago> optionalMetodoPago = metodoPagoRepository.findByMetodoPagoIdAndUsuarioUsuarioId(metodoPagoId, usuarioId);

        // Verificar si el método de pago fue encontrado
        if (optionalMetodoPago.isPresent()) {
            MetodoPago mp = optionalMetodoPago.get();
            System.out.println("Método de Pago encontrado:");
            System.out.println("- ID: " + mp.getMetodoPagoId());
            System.out.println("- Titular: " + mp.getNombreTitular());
            System.out.println("- Número de tarjeta: " + mp.getNumeroTarjeta());
            System.out.println("- Fecha de expiración: " + mp.getFechaExpiracion());
            System.out.println("- Tipo de tarjeta: " + mp.getTipoTarjeta());
            System.out.println("- Monto disponible: $" + mp.getMonto());
        } else {
            System.out.println("No se encontró el método de pago con ID " + metodoPagoId + " para el usuario con ID " + usuarioId);
        }

        System.out.println("=== FIN PRUEBA: Obtener método de pago por ID y usuario ===");
        System.out.println();
    }


    @Test
    @Order(3)
    void testDeleteByMetodoPagoIdAndUsuarioUsuarioId() {
        System.out.println("=== INICIO PRUEBA: Eliminar método de pago por ID y usuario ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Descripción: Esta prueba verifica que se puede eliminar un método de pago específico asociado a un usuario determinado.");
        System.out.println("---------------------------------------------------------------------------------------------");

        Integer usuarioId = 5;
        System.out.println("ID de usuario utilizado para la prueba: " + usuarioId);

        // Obtener el usuario
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            // Obtener y mostrar métodos de pago actuales
            List<MetodoPago> metodosPagoAntes = metodoPagoRepository.findByUsuarioUsuarioId(usuarioId);
            System.out.println("Métodos de pago actuales antes de la creación del nuevo método (" + metodosPagoAntes.size() + "):");
            for (MetodoPago mp : metodosPagoAntes) {
                System.out.println("- ID: " + mp.getMetodoPagoId() + ", Titular: " + mp.getNombreTitular());
            }
            System.out.println();

            // Crear un nuevo método de pago
            MetodoPago nuevoMetodoPago = new MetodoPago();
            nuevoMetodoPago.setNombreTitular(usuario.getNombre() + " " + usuario.getApellidoPaterno());
            nuevoMetodoPago.setNumeroTarjeta("4999999999999999");
            nuevoMetodoPago.setFechaExpiracion(LocalDate.now().plusYears(1));
            nuevoMetodoPago.setTipoTarjeta(TipoTarjeta.Débito);
            nuevoMetodoPago.setMonto(1000.00);
            nuevoMetodoPago.setUsuario(usuario);

            // Guardar el nuevo método de pago
            MetodoPago savedMetodoPago = metodoPagoRepository.save(nuevoMetodoPago);
            Integer metodoPagoId = savedMetodoPago.getMetodoPagoId();

            System.out.println("Nuevo método de pago creado:");
            System.out.println("- ID: " + metodoPagoId);
            System.out.println("- Titular: " + savedMetodoPago.getNombreTitular());
            System.out.println("- Número de tarjeta: " + savedMetodoPago.getNumeroTarjeta());
            System.out.println("- Fecha de expiración: " + savedMetodoPago.getFechaExpiracion());
            System.out.println("- Tipo de tarjeta: " + savedMetodoPago.getTipoTarjeta());
            System.out.println("- Monto disponible: $" + savedMetodoPago.getMonto());
            System.out.println();

            // Mostrar métodos de pago después de la creación
            List<MetodoPago> metodosPagoDespuesCreacion = metodoPagoRepository.findByUsuarioUsuarioId(usuarioId);
            System.out.println("Métodos de pago después de la creación del nuevo método (" + metodosPagoDespuesCreacion.size() + "):");
            for (MetodoPago mp : metodosPagoDespuesCreacion) {
                System.out.println("- ID: " + mp.getMetodoPagoId() + ", Titular: " + mp.getNombreTitular());
            }
            System.out.println();

            // Eliminar el método de pago
            System.out.println("Eliminando el método de pago recién creado...");
            metodoPagoRepository.deleteByMetodoPagoIdAndUsuarioUsuarioId(metodoPagoId, usuarioId);
            System.out.println("Método de pago con ID " + metodoPagoId + " eliminado.");
            System.out.println();

            // Mostrar métodos de pago después de la eliminación
            List<MetodoPago> metodosPagoDespuesEliminacion = metodoPagoRepository.findByUsuarioUsuarioId(usuarioId);
            System.out.println("Métodos de pago después de la eliminación del método recién creado (" + metodosPagoDespuesEliminacion.size() + "):");
            for (MetodoPago mp : metodosPagoDespuesEliminacion) {
                System.out.println("- ID: " + mp.getMetodoPagoId() + ", Titular: " + mp.getNombreTitular());
            }
            System.out.println();

            // Verificar que se eliminó
            Optional<MetodoPago> optionalDeletedMetodoPago = metodoPagoRepository.findById(metodoPagoId);
            Assertions.assertFalse(optionalDeletedMetodoPago.isPresent(), "El método de pago debería haber sido eliminado.");

            System.out.println("=== FIN PRUEBA: Eliminar método de pago por ID y usuario ===");
            System.out.println();
        } else {
            System.out.println("No se encontró un usuario con ID: " + usuarioId);
        }
    }



    @Test
    @Order(4)
    void testVerifySufficientFunds() {
        System.out.println("=== INICIO PRUEBA: Verificar fondos suficientes en método de pago ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Descripción: Esta prueba verifica si un método de pago tiene fondos suficientes para cubrir un monto específico.");
        System.out.println("-----------------------------------------------------------------------------");

        Integer metodoPagoId = 1;
        Integer usuarioId = 1;

        System.out.println("ID de método de pago utilizado para la prueba: " + metodoPagoId);
        System.out.println("ID de usuario utilizado para la prueba: " + usuarioId);

        // Obtener el método de pago y mostrar el monto disponible
        Optional<MetodoPago> optionalMetodoPago = metodoPagoRepository.findByMetodoPagoIdAndUsuarioUsuarioId(metodoPagoId, usuarioId);
        Assertions.assertTrue(optionalMetodoPago.isPresent(), "El método de pago debería existir.");
        MetodoPago metodoPago = optionalMetodoPago.get();
        System.out.println("Monto disponible en el método de pago: $" + metodoPago.getMonto());
        System.out.println();

        // Caso con fondos suficientes
        Double totalSuficiente = 900.00;
        System.out.println("Verificando si hay fondos suficientes para un total de $" + totalSuficiente + "...");
        boolean fondosSuficientes = metodoPagoRepository.verifySufficientFunds(metodoPagoId, usuarioId, totalSuficiente);
        System.out.println("Resultado: " + (fondosSuficientes ? "Fondos suficientes" : "Fondos insuficientes"));
        Assertions.assertTrue(fondosSuficientes, "Debería haber fondos suficientes para el monto especificado.");
        System.out.println();

        // Caso con fondos insuficientes
        Double totalInsuficiente = 1100.00;
        System.out.println("Verificando si hay fondos suficientes para un total de $" + totalInsuficiente + "...");
        boolean fondosInsuficientes = metodoPagoRepository.verifySufficientFunds(metodoPagoId, usuarioId, totalInsuficiente);
        System.out.println("Resultado: " + (fondosInsuficientes ? "Fondos suficientes" : "Fondos insuficientes"));
        Assertions.assertFalse(fondosInsuficientes, "No debería haber fondos suficientes para el monto especificado.");

        System.out.println("=== FIN PRUEBA: Verificar fondos suficientes en método de pago ===");
        System.out.println();
    }



    @Test
    @Order(5)
    void testVerifyExpirationCard() {
        System.out.println("=== INICIO PRUEBA: Verificar expiración de tarjeta de crédito ===");
        System.out.println("Alumno: " + ALUMNO);
        System.out.println("Descripción: Esta prueba verifica si una tarjeta de crédito asociada a un método de pago ha expirado.");
        System.out.println("--------------------------------------------------------------------------------------------");

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();
        System.out.println("Fecha actual: " + fechaActual);
        System.out.println();

        // Tarjeta no expirada
        Integer metodoPagoIdValido = 1;
        Integer usuarioIdValido = 1;

        // Obtener el método de pago y su fecha de expiración
        Optional<MetodoPago> optionalMetodoPagoValido = metodoPagoRepository.findByMetodoPagoIdAndUsuarioUsuarioId(metodoPagoIdValido, usuarioIdValido);
        Assertions.assertTrue(optionalMetodoPagoValido.isPresent(), "El método de pago debería existir.");
        MetodoPago metodoPagoValido = optionalMetodoPagoValido.get();
        LocalDate fechaExpiracionValida = metodoPagoValido.getFechaExpiracion();

        System.out.println("Método de pago ID: " + metodoPagoIdValido + ", Usuario ID: " + usuarioIdValido);
        System.out.println("Fecha de expiración de la tarjeta: " + fechaExpiracionValida);
        System.out.println("Verificando si la tarjeta ha expirado...");
        boolean tarjetaValida = metodoPagoRepository.verifyExpirationCard(metodoPagoIdValido, usuarioIdValido);
        System.out.println("Resultado: " + (tarjetaValida ? "La tarjeta está vigente" : "La tarjeta ha expirado"));
        Assertions.assertTrue(tarjetaValida, "La tarjeta debería estar vigente.");
        System.out.println();

        // Tarjeta expirada
        Integer metodoPagoIdExpirado = 3;
        Integer usuarioIdExpirado = 3;

        // Obtener el método de pago y su fecha de expiración
        Optional<MetodoPago> optionalMetodoPagoExpirado = metodoPagoRepository.findByMetodoPagoIdAndUsuarioUsuarioId(metodoPagoIdExpirado, usuarioIdExpirado);
        Assertions.assertTrue(optionalMetodoPagoExpirado.isPresent(), "El método de pago debería existir.");
        MetodoPago metodoPagoExpirado = optionalMetodoPagoExpirado.get();
        LocalDate fechaExpiracionExpirada = metodoPagoExpirado.getFechaExpiracion();

        System.out.println("Método de pago ID: " + metodoPagoIdExpirado + ", Usuario ID: " + usuarioIdExpirado);
        System.out.println("Fecha de expiración de la tarjeta: " + fechaExpiracionExpirada);
        System.out.println("Verificando si la tarjeta ha expirado...");
        boolean tarjetaExpirada = metodoPagoRepository.verifyExpirationCard(metodoPagoIdExpirado, usuarioIdExpirado);
        System.out.println("Resultado: " + (tarjetaExpirada ? "La tarjeta está vigente" : "La tarjeta ha expirado"));
        Assertions.assertFalse(tarjetaExpirada, "La tarjeta debería haber expirado.");

        System.out.println();
        System.out.println("=== FIN PRUEBA: Verificar expiración de tarjeta de crédito ===");
        System.out.println();
    }

}
