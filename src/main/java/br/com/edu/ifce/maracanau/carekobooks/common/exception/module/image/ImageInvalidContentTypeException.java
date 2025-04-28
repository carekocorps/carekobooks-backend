package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.image;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.BadRequestException;

public class ImageInvalidContentTypeException extends BadRequestException {

    private static final String DEFAULT_ERROR_MESSAGE = "Invalid image content type";

    public ImageInvalidContentTypeException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
