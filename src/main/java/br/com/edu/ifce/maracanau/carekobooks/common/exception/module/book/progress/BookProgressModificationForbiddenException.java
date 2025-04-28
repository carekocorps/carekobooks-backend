package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.progress;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ForbiddenException;

public class BookProgressModificationForbiddenException extends ForbiddenException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this book progress";

    public BookProgressModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
