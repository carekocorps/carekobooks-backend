package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakUriProvider;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@TestConfiguration(proxyBeanMethods = false)
public class KeycloakContainerConfig {

    @Autowired
    private KeycloakUriProvider keycloakUriProvider;

    @Bean
    public KeycloakContainer keycloak() {
        return new KeycloakContainer("quay.io/keycloak/keycloak:26.0.5")
                .withRealmImportFile("keycloak/carekobooks-realm.json");
    }

    @Bean
    public JwtDecoder jwtDecoder(KeycloakContainer container) {
        return NimbusJwtDecoder
                .withJwkSetUri(keycloakUriProvider.getJwtSetUri(container.getAuthServerUrl()))
                .build();
    }

}
