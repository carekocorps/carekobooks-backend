package br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ImageRetrievalException extends RuntimeException {

    public ImageRetrievalException(String message) {
        super(message);
    }

}
