package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookProgressModificationForbiddenException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this book progress";

    public BookProgressModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
