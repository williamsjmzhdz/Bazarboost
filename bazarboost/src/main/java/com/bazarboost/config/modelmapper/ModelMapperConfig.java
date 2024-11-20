package com.bazarboost.config.modelmapper;

import com.bazarboost.dto.CarritoProductoDTO;
import com.bazarboost.model.ProductoCarrito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Desactivar el mapeo implícito
        modelMapper.getConfiguration().setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // Configurar explícitamente el mapeo de ProductoCarrito a CarritoProductoDTO
        modelMapper.createTypeMap(ProductoCarrito.class, CarritoProductoDTO.class)
                .addMappings(mapper -> {
                    // Mapear explícitamente productoId desde producto.productoId
                    mapper.map(src -> src.getProducto().getProductoId(), CarritoProductoDTO::setProductoId);
                    // Agregar otros mapeos específicos si son necesarios
                    mapper.map(src -> src.getProducto().getNombre(), CarritoProductoDTO::setNombre);
                    mapper.map(src -> src.getProducto().getPrecio(), CarritoProductoDTO::setPrecio);
                    mapper.map(ProductoCarrito::getCantidad, CarritoProductoDTO::setCantidad);
                });

        return modelMapper;
    }
}
