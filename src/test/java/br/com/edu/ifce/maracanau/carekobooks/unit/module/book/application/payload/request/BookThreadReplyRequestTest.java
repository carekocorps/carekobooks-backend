package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookThreadReplyRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class BookThreadReplyRequestTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp(){
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidRequest_shouldSucceed() {
        // Arrange
        var request = BookThreadReplyRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void validate_withInvalidRequestByBlankContent_shouldFail() {
        // Arrange
        var request = BookThreadReplyRequestFactory.invalidRequestByBlankContent();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("content"));
    }

    @Test
    void validate_withInvalidRequestByContentExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookThreadReplyRequestFactory.invalidRequestByContentExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("content"));
    }

    @Test
    void validate_withInvalidRequestByBlankUsername_shouldFail() {
        // Arrange
        var request = BookThreadReplyRequestFactory.invalidRequestByBlankUsername();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidRequestByUsernameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookThreadReplyRequestFactory.invalidRequestByUsernameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidRequestByUsernameRegexMismatch_shouldFail() {
        // Arrange
        var request = BookThreadReplyRequestFactory.invalidRequestByUsernameRegexMismatch();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidRequestByBlankThreadId_shouldFail() {
        // Arrange
        var request = BookThreadReplyRequestFactory.invalidRequestByBlankThreadId();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("threadId"));
    }
    
}
