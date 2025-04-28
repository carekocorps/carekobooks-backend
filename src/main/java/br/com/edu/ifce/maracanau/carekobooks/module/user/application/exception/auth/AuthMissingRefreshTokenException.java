package br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthMissingRefreshTokenException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Refresh token cookie is missing";

    public AuthMissingRefreshTokenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
