package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.request.UserSignUpRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSignUpRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidSignUpRequest_shouldSucceed() {
        // Arrange
        var request = UserSignUpRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_withInvalidSignUpRequestByBlankUsername_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByBlankUsername();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidSignUpRequestByUsernameBelowMinLength_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByUsernameBelowMinLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidSignUpRequestByUsernameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByUsernameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidSignUpRequestByUsernameRegexMismatch_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByUsernameRegexMismatch();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validate_withInvalidSignUpRequestByDisplayNameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByDisplayNameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("displayName")));
    }

    @Test
    void validate_withInvalidSignUpRequestByBlankEmail_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByBlankEmail();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validate_withInvalidSignUpRequestByEmailExceedingMaxLength_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByEmailExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validate_withInvalidSignUpRequestByEmailRegexMismatch_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByEmailRegexMismatch();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validate_withInvalidSignUpRequestByDescriptionExceedingMaxLength_shouldFail() {
        // Arrange
        var request = UserSignUpRequestFactory.invalidRequestByDescriptionExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("description")));
    }

}
