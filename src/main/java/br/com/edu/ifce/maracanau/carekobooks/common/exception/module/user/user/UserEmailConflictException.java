package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ConflictException;

public class UserEmailConflictException extends ConflictException {

    private static final String DEFAULT_ERROR_MESSAGE = "A user with the same email already exists";

    public UserEmailConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
