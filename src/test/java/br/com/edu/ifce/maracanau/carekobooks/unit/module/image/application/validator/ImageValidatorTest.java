package br.com.edu.ifce.maracanau.carekobooks.unit.module.image.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.validator.ImageValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageContentTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class ImageValidatorTest {

    private ImageValidator imageValidator;

    @BeforeEach
    void setUp() {
        imageValidator = new ImageValidator();
    }

    @Test
    void validate_withInvalidContentType_shouldFail() {
        // Arrange
        var invalidImage = ImageFactory.invalidImageByContentType();

        // Act & Assert
        assertThrows(ImageContentTypeException.class, () -> imageValidator.validate(invalidImage));
    }

    @Test
    void validate_withValidContentType_shouldPass() {
        // Arrange
        var jpegImage = ImageFactory.validJpegImage();
        var pngImage = ImageFactory.validPngImage();

        // Act & Assert
        assertDoesNotThrow(() -> imageValidator.validate(jpegImage));
        assertDoesNotThrow(() -> imageValidator.validate(pngImage));
    }

}
