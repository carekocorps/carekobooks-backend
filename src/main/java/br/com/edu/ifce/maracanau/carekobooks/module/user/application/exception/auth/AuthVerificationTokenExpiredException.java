package br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthVerificationTokenExpiredException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Verification token is expired";

    public AuthVerificationTokenExpiredException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
