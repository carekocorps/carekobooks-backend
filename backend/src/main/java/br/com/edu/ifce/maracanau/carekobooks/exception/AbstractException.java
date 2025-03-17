package br.com.edu.ifce.maracanau.carekobooks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class AbstractException extends RuntimeException {

    public AbstractException(String message) {
        super(message);
    }

}
