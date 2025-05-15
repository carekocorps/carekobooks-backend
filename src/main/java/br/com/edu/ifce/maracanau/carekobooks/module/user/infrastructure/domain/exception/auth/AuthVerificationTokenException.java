package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthVerificationTokenException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Invalid verification token";

    public AuthVerificationTokenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
