package com.perfulandia.cl.Perfulandia.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        return new OpenAPI()
                    .info(new Info()
                    .title("API Inventario")
                    .version("2.0.0")
                    .description("Documentacion de API Inventario de Perfulandia"));

        
    }

}
