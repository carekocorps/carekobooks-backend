package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.activity;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ForbiddenException;

public class BookActivityModificationForbiddenException extends ForbiddenException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this book activity";

    public BookActivityModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
