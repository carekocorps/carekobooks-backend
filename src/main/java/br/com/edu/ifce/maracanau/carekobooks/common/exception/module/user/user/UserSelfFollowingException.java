package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.user.user;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class UserSelfFollowingException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "A user cannot follow/unfollow themselves";

    public UserSelfFollowingException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
