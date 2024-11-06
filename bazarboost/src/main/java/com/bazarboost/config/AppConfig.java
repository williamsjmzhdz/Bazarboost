package com.bazarboost.config;

import com.bazarboost.util.ProductoUtility;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ProductoUtility productoUtility() {
        return new ProductoUtility();
    }

}
