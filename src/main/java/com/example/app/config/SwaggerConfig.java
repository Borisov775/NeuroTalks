package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.GroupedOpenApi;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NeuroTalks API")
                        .version("1.0.0")
                        .description("API documentation for NeuroTalks - A social networking platform for blog posts and user profiles")
                        .contact(new Contact()
                                .name("NeuroTalks Team")
                                .url("https://github.com/Borisov775/NeuroTalks")
                                .email("support@neurotalks.io")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("neurotalks")
                .packagesToScan("com.example.app.controllers")
                .build();
    }
}
