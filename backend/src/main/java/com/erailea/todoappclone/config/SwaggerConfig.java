package com.erailea.todoappclone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI todoAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo App API")
                        .description("API for managing todo lists and notes")
                        .version("1.0"));
    }
}