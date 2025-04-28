package br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookConflictException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "A book with the same information already exists";

    public BookConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
