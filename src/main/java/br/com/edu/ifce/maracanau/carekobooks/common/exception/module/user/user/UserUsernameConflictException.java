package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ConflictException;

public class UserUsernameConflictException extends ConflictException {

    private static final String DEFAULT_ERROR_MESSAGE = "A user with the same username already exists";

    public UserUsernameConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
