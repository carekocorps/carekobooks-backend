package br.com.edu.ifce.maracanau.carekobooks.common.config;

import br.com.edu.ifce.maracanau.carekobooks.common.swagger.SwaggerUiCustomizer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Carekobooks",
                version = "v1"
        )
)
@SecurityScheme(
        name = "Bearer",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter the JWT token like this: {token}"
)
@Configuration
public class OpenAPIConfig {

    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(
            SwaggerUiConfigProperties swaggerUiConfig,
            SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            SwaggerWelcomeCommon swaggerWelcomeCommon,
            ObjectMapperProvider objectMapperProvider
    ) {
        return new SwaggerUiCustomizer(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
    }

}
