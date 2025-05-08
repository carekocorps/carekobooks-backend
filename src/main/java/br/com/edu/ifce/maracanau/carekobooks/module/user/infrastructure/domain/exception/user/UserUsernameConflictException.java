package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserUsernameConflictException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "A user with the same username already exists";

    public UserUsernameConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
