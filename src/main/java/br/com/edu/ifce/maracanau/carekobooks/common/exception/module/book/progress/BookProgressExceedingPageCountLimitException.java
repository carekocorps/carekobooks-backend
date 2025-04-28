package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.book.progress;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class BookProgressExceedingPageCountLimitException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "Pages read cannot exceed total pages of the book";

    public BookProgressExceedingPageCountLimitException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
