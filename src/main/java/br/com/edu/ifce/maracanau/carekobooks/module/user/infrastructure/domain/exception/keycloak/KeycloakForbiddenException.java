package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class KeycloakForbiddenException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "The current Keycloak context does not have permission";

    public KeycloakForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
