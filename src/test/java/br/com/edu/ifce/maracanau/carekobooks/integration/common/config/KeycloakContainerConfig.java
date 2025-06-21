package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.api.controller.uri.KeycloakUriFactory;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@TestConfiguration(proxyBeanMethods = false)
public class KeycloakContainerConfig {

    private static final String IMAGE_NAME = "quay.io/keycloak/keycloak:26.0.5";
    private static final String IMPORT_FILE = "keycloak/carekobooks-realm.json";

    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public KeycloakContainer keycloakContainer() {
        return new KeycloakContainer(IMAGE_NAME)
                .withFeaturesEnabled("update-email")
                .withRealmImportFile(IMPORT_FILE);
    }

    @Bean
    public JwtDecoder jwtDecoder(KeycloakContainer container) {
        return NimbusJwtDecoder
                .withJwkSetUri(KeycloakUriFactory.getJwtSetUri(realm, container.getAuthServerUrl()))
                .build();
    }

}
