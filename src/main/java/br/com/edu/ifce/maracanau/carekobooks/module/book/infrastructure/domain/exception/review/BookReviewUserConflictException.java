package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookReviewUserConflictException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "User has already reviewed this book";

    public BookReviewUserConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
