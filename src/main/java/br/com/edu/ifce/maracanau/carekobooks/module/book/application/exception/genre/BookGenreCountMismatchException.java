package br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.genre;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookGenreCountMismatchException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "The number of genres in the request does not match the available genres";

    public BookGenreCountMismatchException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
