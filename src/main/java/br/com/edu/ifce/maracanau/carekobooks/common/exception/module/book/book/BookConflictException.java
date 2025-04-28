package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.book;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ConflictException;

public class BookConflictException extends ConflictException {

    private static final String DEFAULT_ERROR_MESSAGE = "A book with the same information already exists";

    public BookConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
