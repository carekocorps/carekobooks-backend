package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookProgressRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class BookProgressRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidProgressRequest_shouldSucceed() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.validRequest();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void validate_withInvalidProgressRequestByBlankUsername_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByBlankUsername();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidProgressRequestByUsernameExceedingMaxLength_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByUsernameExceedingMaxLength();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidProgressRequestByUsernameRegexMismatch_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByUsernameRegexMismatch();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidProgressRequestByBlankBookId_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByBlankBookId();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("bookId"));
    }

    @Test
    void validate_withInvalidProgressRequestByScoreExceedingMax_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByScoreExceedingMax();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("score"));
    }

    @Test
    void validate_withInvalidProgressRequestByNegativeScore_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByNegativeScore();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("score"));
    }

    @Test
    void validate_withInvalidProgressRequestByNegativePageCount_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByNegativePageCount();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("pageCount"));
    }

    @Test
    void validate_withInvalidProgressRequestByBlankStatus_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByBlankStatus();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("status"));
    }

    @Test
    void validate_withInvalidProgressRequestByBlankIsFavorite_shouldFail() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.invalidRequestByBlankIsFavorite();

        // Act
        var result = validator.validate(progressRequest);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("isFavorite"));
    }

}
