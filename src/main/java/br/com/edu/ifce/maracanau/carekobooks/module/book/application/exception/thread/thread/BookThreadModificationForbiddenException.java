package br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.thread.thread;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookThreadModificationForbiddenException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this book thread";

    public BookThreadModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
