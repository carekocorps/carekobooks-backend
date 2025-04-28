package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.review;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ForbiddenException;

public class BookReviewModificationForbiddenException extends ForbiddenException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this book review";

    public BookReviewModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
