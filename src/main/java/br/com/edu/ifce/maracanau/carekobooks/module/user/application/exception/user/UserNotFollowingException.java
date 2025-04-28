package br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFollowingException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "User does not follow the target";

    public UserNotFollowingException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
