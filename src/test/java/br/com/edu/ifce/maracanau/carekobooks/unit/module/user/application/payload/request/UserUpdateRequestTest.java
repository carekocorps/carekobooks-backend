package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.request.UserUpdateRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class UserUpdateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidUpdateRequest_shouldSucceed() {
        // Arrange
        var request = UserUpdateRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void validate_withInvalidUpdateRequestByBlankUsername_shouldFail() {
        // Arrange
        var request = UserUpdateRequestFactory.invalidRequestByBlankUsername();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidUpdateRequestByUsernameBelowMinLength_shouldFail() {
        // Arrange
        var request = UserUpdateRequestFactory.invalidRequestByUsernameBelowMinLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidUpdateRequestByUsernameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = UserUpdateRequestFactory.invalidRequestByUsernameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidUpdateRequestByUsernameRegexMismatch_shouldFail() {
        // Arrange
        var request = UserUpdateRequestFactory.invalidRequestByUsernameRegexMismatch();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("username"));
    }

    @Test
    void validate_withInvalidUpdateRequestByDisplayNameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = UserUpdateRequestFactory.invalidRequestByDisplayNameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("displayName"));
    }

    @Test
    void validate_withInvalidUpdateRequestByDescriptionExceedingMaxLength_shouldFail() {
        // Arrange
        var request = UserUpdateRequestFactory.invalidRequestByDescriptionExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("description"));
    }

    @Test
    void validate_withInvalidUpdateRequestByNullRetainCurrentImage_shouldFail() {
        // Arrange
        var request = UserUpdateRequestFactory.invalidRequestByNullRetainCurrentImage();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("retainCurrentImage"));
    }

}
