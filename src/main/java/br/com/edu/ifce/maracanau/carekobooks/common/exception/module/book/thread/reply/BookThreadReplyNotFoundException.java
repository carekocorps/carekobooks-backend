package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.thread.reply;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class BookThreadReplyNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Thread Reply not found";

    public BookThreadReplyNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
