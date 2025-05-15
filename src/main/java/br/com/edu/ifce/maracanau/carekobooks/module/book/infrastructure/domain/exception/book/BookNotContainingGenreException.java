package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotContainingGenreException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book does not contain this genre";

    public BookNotContainingGenreException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
