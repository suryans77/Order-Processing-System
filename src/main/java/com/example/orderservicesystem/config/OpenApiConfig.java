package com.example.orderservicesystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Event-Driven Order Processing API",
                version = "1.0",
                description = "Order lifecycle with idempotency, async events, failures & compensation"
        )
)
public class OpenApiConfig {
}
