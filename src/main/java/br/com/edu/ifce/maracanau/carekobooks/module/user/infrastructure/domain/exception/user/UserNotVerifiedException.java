package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotVerifiedException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "User is not verified";

    public UserNotVerifiedException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public UserNotVerifiedException(String message) {
        super(message);
    }

}
