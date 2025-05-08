package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookGenreNameConflictException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Genre with the same name already exists";

    public BookGenreNameConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
