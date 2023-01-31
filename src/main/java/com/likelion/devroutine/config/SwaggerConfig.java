package com.likelion.devroutine.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @Info(title = "Devroutine APIs",
            description = "Devroutine API 명세서입니다.",
        version = "v1.0.0"))

@Configuration
public class SwaggerConfig{

    @Bean
    public GroupedOpenApi devroutineOpenApi() {

        String paths[] = {"/api/**"};

        return GroupedOpenApi.builder()
                .group("devroutine v1.0.0")
                .pathsToMatch(paths)
                .build();
    }
}
