package br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.exception.ImageContentTypeException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageValidator implements BaseValidator<Image> {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png");

    public void validate(Image image) {
        if (isContentTypeInvalid(image)) {
            throw new ImageContentTypeException();
        }
    }

    private boolean isContentTypeInvalid(Image image) {
        return !ALLOWED_CONTENT_TYPES.contains(image.getContentType());
    }

}
