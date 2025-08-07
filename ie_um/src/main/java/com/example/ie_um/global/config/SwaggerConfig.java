package com.example.ie_um.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class SwaggerConfig {
    @Value("${swagger.server.local-url}")
    private String localUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(servers());
    }

    private Info apiInfo() {
        return new Info()
                .title("ie_um API")
                .description("")
                .version("1.0.0");
    }

    private List<Server> servers() {
        return List.of(
                new Server()
                        .url(localUrl)
                        .description("Local development server")
        );
    }
}
