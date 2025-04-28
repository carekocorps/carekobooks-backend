package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.thread.reply;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ForbiddenException;

public class BookThreadReplyModificationForbiddenException extends ForbiddenException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this book thread reply";

    public BookThreadReplyModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
