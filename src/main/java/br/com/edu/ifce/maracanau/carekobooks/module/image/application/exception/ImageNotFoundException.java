package br.com.edu.ifce.maracanau.carekobooks.module.image.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Image not found";

    public ImageNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
