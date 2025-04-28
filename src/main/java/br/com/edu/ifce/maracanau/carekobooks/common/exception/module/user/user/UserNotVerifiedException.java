package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class UserNotVerifiedException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "User is not verified";

    public UserNotVerifiedException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public UserNotVerifiedException(String message) {
        super(message);
    }

}
