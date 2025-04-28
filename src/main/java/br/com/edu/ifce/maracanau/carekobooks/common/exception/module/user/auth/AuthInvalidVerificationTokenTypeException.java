package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.auth;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class AuthInvalidVerificationTokenTypeException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "Invalid verification token type";

    public AuthInvalidVerificationTokenTypeException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
