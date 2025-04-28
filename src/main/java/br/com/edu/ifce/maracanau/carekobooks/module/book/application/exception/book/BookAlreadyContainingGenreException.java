package br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookAlreadyContainingGenreException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book already contains this genre";

    public BookAlreadyContainingGenreException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
