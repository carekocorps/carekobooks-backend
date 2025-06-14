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
    void validate_withValidGenreRequest_shouldSucceed() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.validRequest();

        // Act
        var result = validator.validate(genreRequest);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_withInvalidGenreRequestByBlankName_shouldFail() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.invalidRequestByBlankName();

        // Act
        var result = validator.validate(genreRequest);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("name")));
    }

    @Test
    void validate_withInvalidGenreRequestByNameRegexMismatch_shouldFail() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.invalidRequestByNameRegexMismatch();

        // Act
        var result = validator.validate(genreRequest);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("name")));
    }

    @Test
    void validate_withInvalidGenreRequestByNameExceedingMaxLength_shouldFail() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.invalidRequestByNameExceedingMaxLength();

        // Act
        var result = validator.validate(genreRequest);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("name")));
    }

    @Test
    void validate_withInvalidGenreRequestByBlankDisplayName_shouldFail() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.invalidRequestByBlankDisplayName();

        // Act
        var result = validator.validate(genreRequest);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("displayName")));
    }

    @Test
    void validate_withInvalidGenreRequestByDisplayNameExceedingMaxLength_shouldFail() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.invalidRequestByDisplayNameExceedingMaxLength();

        // Act
        var result = validator.validate(genreRequest);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("displayName")));
    }

    @Test
    void validate_withInvalidGenreRequestByDescriptionExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookGenreRequestFactory.invalidRequestByDescriptionExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("description")));
    }

}
