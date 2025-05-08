package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.reply;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookThreadReplyModificationForbiddenException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "You are not allowed to modify this book thread reply";

    public BookThreadReplyModificationForbiddenException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
