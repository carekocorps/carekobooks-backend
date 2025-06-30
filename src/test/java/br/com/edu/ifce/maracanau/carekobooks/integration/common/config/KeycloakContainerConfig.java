package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.api.controller.uri.KeycloakUriFactory;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.testcontainers.containers.Network;

@Import({
        ContainerNetworkConfig.class,
        DynamicPropertyRegistrarConfig.class
})
@TestConfiguration(proxyBeanMethods = false)
public class KeycloakContainerConfig {

    private static final String DOCKER_IMAGE = "quay.io/keycloak/keycloak:26.2.5";
    private static final String REALM_IMPORT_FILE = "keycloak/carekobooks-test-realm.json";
    private static final String[] REALM_ENABLED_FEATURES = {"update-email"};

    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public KeycloakContainer keycloakContainer(Network network) {
        return new KeycloakContainer(DOCKER_IMAGE)
                .withFeaturesEnabled(REALM_ENABLED_FEATURES)
                .withRealmImportFile(REALM_IMPORT_FILE)
                .withNetwork(network);
    }

    @Bean
    public JwtDecoder jwtDecoder(KeycloakContainer container) {
        return NimbusJwtDecoder
                .withJwkSetUri(KeycloakUriFactory.getJwtSetUri(realm, container.getAuthServerUrl()))
                .build();
    }

}
