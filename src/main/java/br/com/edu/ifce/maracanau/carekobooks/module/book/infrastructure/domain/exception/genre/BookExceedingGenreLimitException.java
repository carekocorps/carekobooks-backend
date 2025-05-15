package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookExceedingGenreLimitException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "A book can have at most 5 genres";

    public BookExceedingGenreLimitException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
