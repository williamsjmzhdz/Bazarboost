package com.bazarboost;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
class BazarBoostTests {

    @Test
    void contextLoads() {
        System.out.println("FRANCISCO WILLIAMS JIMÉNEZ HERNÁNDEZ");
        System.out.println("Cargar esquema y datos");
    }

}
