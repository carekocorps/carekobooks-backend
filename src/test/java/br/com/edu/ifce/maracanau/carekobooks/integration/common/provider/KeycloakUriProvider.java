package br.com.edu.ifce.maracanau.carekobooks.integration.common.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KeycloakUriProvider {

    @Value("${keycloak.realm}")
    private String realm;

    public String getIssuerUri(String authServerUri) {
        return UriComponentsBuilder
                .fromUriString(authServerUri)
                .pathSegment("realms", realm)
                .build()
                .toUriString();
    }

    public String getJwtSetUri(String authServerUri) {
        return UriComponentsBuilder
                .fromUriString(authServerUri)
                .pathSegment("realms", realm, "protocol", "openid-connect", "certs")
                .build()
                .toUriString();
    }

}
