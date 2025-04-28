package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.auth;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class AuthInvalidVerificationTokenException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "Invalid verification token";

    public AuthInvalidVerificationTokenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
