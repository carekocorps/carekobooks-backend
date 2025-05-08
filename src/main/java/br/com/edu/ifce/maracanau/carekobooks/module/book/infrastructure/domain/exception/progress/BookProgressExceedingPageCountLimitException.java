package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookProgressExceedingPageCountLimitException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Pages read cannot exceed total pages of the book";

    public BookProgressExceedingPageCountLimitException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
