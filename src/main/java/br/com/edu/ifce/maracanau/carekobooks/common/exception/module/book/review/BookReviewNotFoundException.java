package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.review;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class BookReviewNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Review not found";

    public BookReviewNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
