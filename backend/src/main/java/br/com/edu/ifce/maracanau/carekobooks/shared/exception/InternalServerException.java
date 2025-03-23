package br.com.edu.ifce.maracanau.carekobooks.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends BaseException {

    public InternalServerException(String message) {
        super(message);
    }

}
