package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.thread.thread;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ForbiddenException;

public class BookThreadModificationForbiddenException extends ForbiddenException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this book thread";

    public BookThreadModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
