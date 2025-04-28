package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.book;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class BookAlreadyContainingGenreException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book already contains this genre";

    public BookAlreadyContainingGenreException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
