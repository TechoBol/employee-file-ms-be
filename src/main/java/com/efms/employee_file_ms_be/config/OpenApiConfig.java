package com.efms.employee_file_ms_be.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Empleados")
                        .version("1.0.0")
                        .description("Documentación Swagger generada automáticamente para el CRUD de Employee")
                );
    }
}
