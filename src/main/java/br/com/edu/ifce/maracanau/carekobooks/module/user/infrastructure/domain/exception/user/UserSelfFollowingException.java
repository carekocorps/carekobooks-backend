package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserSelfFollowingException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "A user cannot follow/unfollow themselves";

    public UserSelfFollowingException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
