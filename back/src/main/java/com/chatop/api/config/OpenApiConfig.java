package com.chatop.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "ChaTop API",
    version = "1.0.0",
    description = "API REST du portail de location ChaTop : authentification JWT, locations et messages."),
  security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH))
@SecurityScheme(
  name = OpenApiConfig.BEARER_AUTH,
  type = SecuritySchemeType.HTTP,
  scheme = "bearer",
  bearerFormat = "JWT",
  description = "Token renvoyé par /api/auth/login ou /api/auth/register")
public class OpenApiConfig {

  public static final String BEARER_AUTH = "bearerAuth";
}
