package com.app.hotelsaas.catin.web.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()

                .info(new Info()
                        .title("Hotel SaaS API")
                        .description("API para gestión de hoteles multi-tenant")
                        .version("1.0.0"))

                .servers(List.of(
                        new Server().url("https://hotel.bryandev.online")
                                .description("Servidor en producción por tunel")
                ))

                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))

                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
