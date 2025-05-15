package br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ImageUploadException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Failed to upload a image from the server";

    public ImageUploadException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ImageUploadException(String message) {
        super(message);
    }

}
