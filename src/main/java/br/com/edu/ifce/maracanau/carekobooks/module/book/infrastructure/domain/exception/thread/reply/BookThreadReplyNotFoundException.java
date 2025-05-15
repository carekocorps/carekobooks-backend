package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.reply;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookThreadReplyNotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Thread Reply not found";

    public BookThreadReplyNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
