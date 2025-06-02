package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookReviewRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookReviewRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidRequest_shouldPass() {
        // Arrange
        var request = BookReviewRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_withInvalidRequestByBlankTitle_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByBlankTitle();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("title")));
    }

    @Test
    void validate_withInvalidRequestByTitleExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByTitleExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("title")));
    }

    @Test
    void validate_withInvalidRequestByBlankContent_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByBlankContent();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("content")));
    }

    @Test
    void validate_withInvalidRequestByContentExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByContentExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("content")));
    }

    @Test
    void validate_withInvalidRequestByNullScore_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByNullScore();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("score")));
    }

    @Test
    void validate_withInvalidRequestByNegativeScore_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByNegativeScore();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("score")));
    }

    @Test
    void validate_withInvalidRequestByScoreExceedingMaxValue_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByScoreExceedingMaxValue();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("score")));
    }

    @Test
    void validate_withInvalidRequestByBlankUsername_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByBlankUsername();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByUsernameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByUsernameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByUsernameRegexMismatch_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByUsernameRegexMismatch();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByBlankBookId_shouldFail() {
        // Arrange
        var request = BookReviewRequestFactory.invalidRequestByBlankBookId();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("bookId")));
    }

}
