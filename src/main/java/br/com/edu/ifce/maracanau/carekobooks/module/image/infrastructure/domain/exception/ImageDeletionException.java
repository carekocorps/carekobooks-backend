package br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ImageDeletionException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Failed to delete a image from the server";

    public ImageDeletionException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ImageDeletionException(String message) {
        super(message);
    }

}
