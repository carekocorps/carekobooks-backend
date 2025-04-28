package br.com.edu.ifce.maracanau.carekobooks.common.exception.module.image;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.http.NotFoundException;

public class ImageNotFoundException extends NotFoundException {

    private static final String DEFAULT_ERROR_MESSAGE = "Image not found";

    public ImageNotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
