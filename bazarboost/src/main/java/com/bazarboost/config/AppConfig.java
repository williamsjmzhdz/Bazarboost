package com.bazarboost.config;

import com.bazarboost.model.Producto;
import com.bazarboost.util.ProductoUtility;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ProductoUtility productoUtility() {
        return new ProductoUtility();
    }

}
