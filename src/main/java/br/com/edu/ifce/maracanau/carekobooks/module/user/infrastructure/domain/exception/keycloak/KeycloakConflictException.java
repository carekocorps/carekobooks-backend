package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class KeycloakConflictException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "A user with the given email or username already exists";

    public KeycloakConflictException() {
        super(KeycloakConflictException.DEFAULT_ERROR_MESSAGE);
    }

}
