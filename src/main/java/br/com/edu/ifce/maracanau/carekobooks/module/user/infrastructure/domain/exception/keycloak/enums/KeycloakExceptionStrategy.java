package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.enums;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.keycloak.*;
import org.apache.http.HttpStatus;

import java.util.function.Supplier;

public enum KeycloakExceptionStrategy {

    BAD_REQUEST(HttpStatus.SC_BAD_REQUEST, KeycloakBadRequestException::new),
    FORBIDDEN(HttpStatus.SC_FORBIDDEN, KeycloakForbiddenException::new),
    NOT_FOUND(HttpStatus.SC_NOT_FOUND, KeycloakNotFoundException::new),
    CONFLICT(HttpStatus.SC_CONFLICT, KeycloakConflictException::new);

    private final int statusCode;
    private final Supplier<RuntimeException> supplier;

    KeycloakExceptionStrategy(int statusCode, Supplier<RuntimeException> supplier) {
        this.statusCode = statusCode;
        this.supplier = supplier;
    }

    public RuntimeException toException() {
        return supplier.get();
    }

    public static RuntimeException of(int statusCode) {
        for (var strategy : values()) {
            if (strategy.statusCode == statusCode) {
                return strategy.toException();
            }
        }

        return new KeycloakBadGatewayException();
    }

}
