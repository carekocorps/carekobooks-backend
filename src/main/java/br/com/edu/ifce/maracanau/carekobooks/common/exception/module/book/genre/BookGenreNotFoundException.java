package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.genre;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class BookGenreNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Genre not found";

    public BookGenreNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
