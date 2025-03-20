package br.com.edu.ifce.maracanau.carekobooks.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Carekobooks",
                description = "Book management API",
                version = "v1"
        )
)
@Configuration
public class OpenAPIConfig {
}
