package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookUpdateRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class BookUpdateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidUpdateRequest_shouldSucceed() {
        // Arrange
        var request = BookUpdateRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void validate_withInvalidUpdateRequestByBlankTitle_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByBlankTitle();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("title"));
    }

    @Test
    void validate_withInvalidUpdateRequestByTitleExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByTitleExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("title"));
    }

    @Test
    void validate_withInvalidUpdateRequestBySynopsisExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestBySynopsisExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("synopsis"));
    }

    @Test
    void validate_withInvalidUpdateRequestByBlankAuthorName_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByBlankAuthorName();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("authorName"));
    }

    @Test
    void validate_withInvalidUpdateRequestByAuthorNameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByAuthorNameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("authorName"));
    }

    @Test
    void validate_withInvalidUpdateRequestByBlankPublisherName_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByBlankPublisherName();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("publisherName"));
    }

    @Test
    void validate_withInvalidUpdateRequestByPublisherNameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByPublisherNameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("publisherName"));
    }

    @Test
    void validate_withInvalidUpdateRequestByInvalidPublishedAt_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByInvalidPublishedAt();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("publishedAt"));
    }

    @Test
    void validate_withInvalidUpdateRequestByNullPageCount_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByNullPageCount();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("pageCount"));
    }

    @Test
    void validate_withInvalidUpdateRequestByNegativePageCount_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByNegativePageCount();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("pageCount"));
    }

    @Test
    void validate_withInvalidUpdateRequestByNullRetainCurrentImage_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByNullRetainCurrentImage();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().contains("retainCurrentImage"));
    }

    @Test
    void validate_withInvalidUpdateRequestByBlankGenre_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByBlankGenre();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().contains("genres"));
    }

    @Test
    void validate_withInvalidUpdateRequestByGenreExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByGenreExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().contains("genres"));
    }

    @Test
    void validate_withInvalidUpdateRequestByTooManyGenres_shouldFail() {
        // Arrange
        var request = BookUpdateRequestFactory.invalidRequestByTooManyGenres();

        // Act
        var result = validator.validate(request);

        // Assert
        assertThat(result).anyMatch(x -> x.getPropertyPath().toString().equals("genres"));
    }

}
