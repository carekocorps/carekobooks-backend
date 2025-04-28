package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.book;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class BookNotContainingGenreException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book does not contain this genre";

    public BookNotContainingGenreException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
