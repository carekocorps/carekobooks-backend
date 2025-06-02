package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookThreadReplyRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BookThreadReplyRequestTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp(){
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidRequest_shouldPass() {
        // Arrange
        var request = BookThreadReplyRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_withInvalidRequestByBlankContent_shouldFail() {
        var request = BookThreadReplyRequestFactory.invalidRequestByBlankContent();

        var result = validator.validate(request);

        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("content")));
    }

    @Test
    void validate_withInvalidRequestByContentExceedingMaxLength_shouldFail() {
        var request = BookThreadReplyRequestFactory.invalidRequestByContentExceedingMaxLength();

        var result = validator.validate(request);

        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("content")));
    }

    @Test
    void validate_withInvalidRequestByBlankUsername_shouldFail() {
        var request = BookThreadReplyRequestFactory.invalidRequestByBlankUsername();

        var result = validator.validate(request);

        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByUsernameExceedingMaxLength_shouldFail() {
        var request = BookThreadReplyRequestFactory.invalidRequestByUsernameExceedingMaxLength();

        var result = validator.validate(request);

        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByUsernameRegexMismatch_shouldFail() {
        var request = BookThreadReplyRequestFactory.invalidRequestByUsernameRegexMismatch();

        var result = validator.validate(request);

        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidRequestByBlankThreadId_shouldFail() {
        var request = BookThreadReplyRequestFactory.invalidRequestByBlankThreadId();

        var result = validator.validate(request);

        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("threadId")));
    }
    
}
