package br.com.edu.ifce.maracanau.carekobooks.integration.common.contributor;

import br.com.edu.ifce.maracanau.carekobooks.integration.common.provider.KeycloakUriProvider;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;

@Component
public class KeycloakPropertyContributor implements BasePropertyContributor {

    @Autowired(required = false)
    private KeycloakContainer keycloak;

    @Autowired
    private KeycloakUriProvider keycloakUriProvider;

    @Override
    public void contribute(DynamicPropertyRegistry registry) {
        if (keycloak != null && keycloak.isRunning()) {
            registry.add("keycloak.server-url", keycloak::getAuthServerUrl);
            registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloakUriProvider.getIssuerUri(keycloak.getAuthServerUrl()));
            registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> keycloakUriProvider.getJwtSetUri(keycloak.getAuthServerUrl()));
        }
    }

}
