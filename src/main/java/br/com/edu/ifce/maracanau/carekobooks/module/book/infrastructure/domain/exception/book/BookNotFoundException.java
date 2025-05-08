package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book not found";

    public BookNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
