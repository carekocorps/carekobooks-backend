package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookProgressRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BookProgressRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidRequest_shouldPass() {
        // Arrange
        var request = BookProgressRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_withInvalidRequestByBlankUsername_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByBlankUsername();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByUsernameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByUsernameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByUsernameRegexMismatch_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByUsernameRegexMismatch();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByBlankBookId_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByBlankBookId();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("bookId")));
    }

    @Test
    void validate_withInvalidRequestByScoreExceedingMax_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByScoreExceedingMax();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("score")));
    }

    @Test
    void validate_withInvalidRequestByNegativeScore_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByNegativeScore();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("score")));
    }

    @Test
    void validate_withInvalidRequestByNegativePageCount_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByNegativePageCount();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("pageCount")));
    }

    @Test
    void validate_withInvalidRequestByBlankStatus_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByBlankStatus();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("status")));
    }

    @Test
    void validate_withInvalidRequestByBlankIsFavorite_shouldFail() {
        // Arrange
        var request = BookProgressRequestFactory.invalidRequestByBlankIsFavorite();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("isFavorite")));
    }

}
