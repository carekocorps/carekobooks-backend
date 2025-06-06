package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class KeycloakBadGatewayException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred while communicating with Keycloak";

    public KeycloakBadGatewayException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
