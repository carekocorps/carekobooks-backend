package br.com.edu.ifce.maracanau.carekobooks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

}
