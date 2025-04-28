package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.genre;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.ConflictException;

public class BookGenreNameConflictException extends ConflictException {

    private static final String DEFAULT_ERROR_MESSAGE = "Book Genre with the same name already exists";

    public BookGenreNameConflictException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
