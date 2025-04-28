package br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.genre;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookGenreNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Genre not found";

    public BookGenreNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
