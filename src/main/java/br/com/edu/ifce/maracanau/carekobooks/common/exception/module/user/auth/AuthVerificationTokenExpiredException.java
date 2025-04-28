package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.auth;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class AuthVerificationTokenExpiredException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "Verification token is expired";

    public AuthVerificationTokenExpiredException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
