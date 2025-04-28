package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.review;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ConflictException;

public class BookReviewUserConflictException extends ConflictException {

    private static final String DEFAULT_ERROR_MESSAGE = "User has already reviewed this book";

    public BookReviewUserConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
