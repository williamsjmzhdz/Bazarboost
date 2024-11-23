package com.bazarboost.shared.config;

import com.bazarboost.shared.util.ProductoUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ProductoUtility productoUtility() {
        return new ProductoUtility();
    }

}
