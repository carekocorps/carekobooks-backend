package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.web.util.UriComponentsBuilder;

@TestConfiguration(proxyBeanMethods = false)
public class KeycloakTestcontainerConfig {

    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public KeycloakContainer keycloak() {
        return new KeycloakContainer("quay.io/keycloak/keycloak:26.0.5")
                .withRealmImportFile("keycloak/carekobooks-realm.json");
    }

    @Bean
    public JwtDecoder jwtDecoder(KeycloakContainer container) {
        return NimbusJwtDecoder
                .withJwkSetUri(getJwtSetUri(container))
                .build();
    }

    @Bean
    public DynamicPropertyRegistrar keycloakProperties(KeycloakContainer container) {
        return properties -> {
            properties.add("keycloak.server-url", container::getAuthServerUrl);
            properties.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> getIssuerUri(container));
            properties.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> getJwtSetUri(container));
        };
    }

    private String getIssuerUri(KeycloakContainer container) {
        return UriComponentsBuilder
                .fromUriString(container.getAuthServerUrl())
                .pathSegment("realms", realm)
                .build()
                .toUriString();
    }

    private String getJwtSetUri(KeycloakContainer container) {
        return UriComponentsBuilder
                .fromUriString(container.getAuthServerUrl())
                .pathSegment("realms", realm, "protocol", "openid-connect", "certs")
                .build()
                .toUriString();
    }

}
