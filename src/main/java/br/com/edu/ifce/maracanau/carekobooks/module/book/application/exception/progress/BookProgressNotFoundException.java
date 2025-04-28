package br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.progress;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookProgressNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Progress not found";

    public BookProgressNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
