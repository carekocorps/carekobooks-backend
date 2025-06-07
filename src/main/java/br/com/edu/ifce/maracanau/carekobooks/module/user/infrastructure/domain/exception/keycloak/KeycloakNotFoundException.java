package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class KeycloakNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Keycloak User not found";

    public KeycloakNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
