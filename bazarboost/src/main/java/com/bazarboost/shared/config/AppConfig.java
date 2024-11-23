package com.bazarboost.system.config;

import com.bazarboost.system.util.ProductoUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ProductoUtility productoUtility() {
        return new ProductoUtility();
    }

}
