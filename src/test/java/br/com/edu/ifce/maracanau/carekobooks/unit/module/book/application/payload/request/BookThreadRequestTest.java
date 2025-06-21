package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookThreadRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class BookThreadRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidRequest_shouldSucceed() {
        // Arrange
        var request = BookThreadRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void validate_withInvalidRequestByBlankTitle_shouldFail() {
        var request = BookThreadRequestFactory.invalidRequestByBlankTitle();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("title"));
    }

    @Test
    void validate_withInvalidRequestByTitleExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookThreadRequestFactory.invalidRequestByTitleExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("title"));
    }

    @Test
    void validate_withInvalidRequestByBlankDescription_shouldFail() {
        // Arrange
        var request = BookThreadRequestFactory.invalidRequestByBlankDescription();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("description"));
    }

    @Test
    void validate_withInvalidRequestByDescriptionExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookThreadRequestFactory.invalidRequestByDescriptionExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("description"));
    }

    @Test
    void validate_withInvalidRequestByBlankUsername_shouldFail() {
        // Arrange
        var request = BookThreadRequestFactory.invalidRequestByBlankUsername();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidRequestByUsernameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookThreadRequestFactory.invalidRequestByUsernameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidRequestByUsernameRegexMismatch_shouldFail() {
        // Arrange
        var request = BookThreadRequestFactory.invalidRequestByUsernameRegexMismatch();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidRequestByBlankBookId_shouldFail() {
        // Arrange
        var request = BookThreadRequestFactory.invalidRequestByBlankBookId();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("bookId"));
    }

}
