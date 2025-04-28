package br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.activity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookActivityNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book activity not found";

    public BookActivityNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
