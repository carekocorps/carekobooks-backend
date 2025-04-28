package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.genre;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class BookGenreInvalidException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "The number of genres in the request does not match the available genres";

    public BookGenreInvalidException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
