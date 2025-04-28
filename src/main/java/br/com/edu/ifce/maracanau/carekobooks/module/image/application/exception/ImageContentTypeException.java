package br.com.edu.ifce.maracanau.carekobooks.module.image.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageContentTypeException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Invalid image content type";

    public ImageContentTypeException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
