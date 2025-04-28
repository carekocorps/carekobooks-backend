package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "User not found";

    public UserNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
