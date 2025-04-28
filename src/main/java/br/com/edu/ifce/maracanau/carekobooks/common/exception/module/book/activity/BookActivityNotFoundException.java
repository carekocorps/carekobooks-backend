package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.activity;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class BookActivityNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book activity not found";

    public BookActivityNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
