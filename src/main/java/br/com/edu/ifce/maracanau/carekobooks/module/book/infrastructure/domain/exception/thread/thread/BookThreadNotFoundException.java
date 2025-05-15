package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookThreadNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Thread not found";

    public BookThreadNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
