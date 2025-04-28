package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.thread.thread;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class BookThreadNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Thread not found";

    public BookThreadNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
