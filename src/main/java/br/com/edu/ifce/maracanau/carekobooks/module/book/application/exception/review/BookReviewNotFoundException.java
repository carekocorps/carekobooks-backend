package br.com.edu.ifce.maracanau.carekobooks.module.book.application.exception.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookReviewNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Review not found";

    public BookReviewNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
