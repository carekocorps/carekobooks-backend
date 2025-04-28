package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class UserAlreadyFollowingException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "User is already following the target";

    public UserAlreadyFollowingException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
