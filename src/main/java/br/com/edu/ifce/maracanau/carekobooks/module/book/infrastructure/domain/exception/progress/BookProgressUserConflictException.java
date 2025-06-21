package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookProgressUserConflictException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "User already has a progress record for this book";

    public BookProgressUserConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
