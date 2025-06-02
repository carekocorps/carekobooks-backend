package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookGenreRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookGenreRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidRequest_shouldPass() {
        // Arrange
        var request = BookGenreRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_withInvalidRequestByBlankName_shouldFail() {
        // Arrange
        var request = BookGenreRequestFactory.invalidRequestByBlankName();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("name")));
    }

    @Test
    void validate_withInvalidRequestByNameRegexMismatch_shouldFail() {
        // Arrange
        var request = BookGenreRequestFactory.invalidRequestByNameRegexMismatch();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("name")));
    }

    @Test
    void validate_withInvalidRequestByNameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookGenreRequestFactory.invalidRequestByNameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("name")));
    }

    @Test
    void validate_withInvalidRequestByBlankDisplayName_shouldFail() {
        // Arrange
        var request = BookGenreRequestFactory.invalidRequestByBlankDisplayName();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("displayName")));
    }

    @Test
    void validate_withInvalidRequestByDisplayNameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookGenreRequestFactory.invalidRequestByDisplayNameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("displayName")));
    }

    @Test
    void validate_withInvalidRequestByDescriptionExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookGenreRequestFactory.invalidRequestByDescriptionExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("description")));
    }

}
