package br.com.edu.ifce.maracanau.carekobooks.integration.common.contributor;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.api.controller.uri.KeycloakUriFactory;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;

@Component
public class KeycloakPropertyContributor implements BasePropertyContributor {

    @Autowired(required = false)
    private KeycloakContainer keycloakContainer;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public void contribute(DynamicPropertyRegistry registry) {
        if (keycloakContainer != null && keycloakContainer.isRunning()) {
            var authServerUrl = keycloakContainer.getAuthServerUrl();
            registry.add("keycloak.server-url", () -> authServerUrl);
            registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> KeycloakUriFactory.getIssuerUri(realm, authServerUrl));
            registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> KeycloakUriFactory.getJwtSetUri(realm, authServerUrl));
        }
    }

}
