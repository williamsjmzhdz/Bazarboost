package com.bazarboost;

import com.bazarboost.model.Usuario;
import com.bazarboost.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
class BazarBoostTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        System.out.println("FRANCISCO WILLIAMS JIMÉNEZ HERNÁNDEZ");
        System.out.println("Cargar esquema y datos");

        // Encriptar contraseñas
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            if ("password123".equals(usuario.getContrasenia())) {
                usuario.setContrasenia(passwordEncoder.encode("password123"));
                usuarioRepository.save(usuario);
            }
        }
        System.out.println("Contraseñas encriptadas exitosamente");
    }
}
