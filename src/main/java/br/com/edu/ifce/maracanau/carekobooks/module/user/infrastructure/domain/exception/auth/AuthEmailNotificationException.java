package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class AuthEmailNotificationException extends RuntimeException {

    public AuthEmailNotificationException(String message) {
        super(message);
    }

}
