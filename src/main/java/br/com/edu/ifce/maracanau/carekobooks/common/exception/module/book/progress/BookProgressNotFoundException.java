package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.progress;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class BookProgressNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Progress not found";

    public BookProgressNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
