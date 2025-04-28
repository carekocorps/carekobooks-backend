package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class UserAlreadyVerifiedException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "User is already verified";

    public UserAlreadyVerifiedException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
