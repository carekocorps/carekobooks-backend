package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ForbiddenException;

public class UserModificationForbiddenException extends ForbiddenException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this user";

    public UserModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
