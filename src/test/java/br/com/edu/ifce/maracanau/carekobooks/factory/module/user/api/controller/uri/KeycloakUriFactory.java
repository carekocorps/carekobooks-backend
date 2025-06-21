package br.com.edu.ifce.maracanau.carekobooks.factory.module.user.api.controller.uri;

import org.springframework.web.util.UriComponentsBuilder;

public class KeycloakUriFactory {

    private KeycloakUriFactory() {
    }

    public static String getIssuerUri(String realm, String authServerUrl) {
        return UriComponentsBuilder
                .fromUriString(authServerUrl)
                .pathSegment("realms", realm)
                .build()
                .toUriString();
    }

    public static String getJwtSetUri(String realm, String authServerUrl) {
        return UriComponentsBuilder
                .fromUriString(authServerUrl)
                .pathSegment("realms", realm, "protocol", "openid-connect", "certs")
                .build()
                .toUriString();
    }

}
