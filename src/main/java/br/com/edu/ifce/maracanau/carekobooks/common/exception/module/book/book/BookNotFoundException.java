package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.book;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class BookNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book not found";

    public BookNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
