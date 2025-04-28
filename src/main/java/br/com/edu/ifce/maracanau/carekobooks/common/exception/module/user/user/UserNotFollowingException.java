package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class UserNotFollowingException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "User does not follow the target";

    public UserNotFollowingException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
