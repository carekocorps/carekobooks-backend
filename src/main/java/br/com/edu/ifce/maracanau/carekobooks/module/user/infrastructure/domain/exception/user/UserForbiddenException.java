package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserForbiddenException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to access this user resource";

    public UserForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
