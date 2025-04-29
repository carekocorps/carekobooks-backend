package br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

}
