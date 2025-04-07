package br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageValidator {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png");

    public void validate(Image image) {
        if (isContentTypeInvalid(image)) {
            throw new BadRequestException("Invalid content type");
        }
    }

    private boolean isContentTypeInvalid(Image image) {
        return !ALLOWED_CONTENT_TYPES.contains(image.getContentType());
    }

}
