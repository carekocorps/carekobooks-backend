package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.genre;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class BookExceedingGenreLimitException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "A book can have at most 5 genres";

    public BookExceedingGenreLimitException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
