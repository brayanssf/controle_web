package com.unincor.presenca.controle_web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration

@OpenAPIDefinition(
    
        info = @Info(
                title = "Sistema de Controle de Eventos API",
                version = "1.0",
                description = "API para gerenciamento de eventos, participantes, presenças e relatórios",
                contact = @Contact(
                        name = "Equipe de Desenvolvimento",
                        email = "contato@controle.com"
                )
        )
)

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)

public class SwaggerConfig {

}
