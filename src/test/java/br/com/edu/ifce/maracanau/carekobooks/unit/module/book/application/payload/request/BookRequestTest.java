package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookGenreRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookRequestFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void validate_withValidRequest_shouldSucceed() {
        // Arrange
        var request = BookGenreRequestFactory.validRequest();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_withInvalidRequestByBlankTitle_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByBlankTitle();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("title")));
    }

    @Test
    void validate_withInvalidRequestByTitleExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByTitleExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("title")));
    }

    @Test
    void validate_withInvalidRequestBySynopsisExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestBySynopsisExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("synopsis")));
    }

    @Test
    void validate_withInvalidRequestByBlankAuthorName_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByBlankAuthorName();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("authorName")));
    }

    @Test
    void validate_withInvalidRequestByAuthorNameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByAuthorNameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("authorName")));
    }

    @Test
    void validate_withInvalidRequestByBlankPublisherName_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByBlankPublisherName();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("publisherName")));
    }

    @Test
    void validate_withInvalidRequestByPublisherNameExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByPublisherNameExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("publisherName")));
    }

    @Test
    void validate_withInvalidRequestByInvalidPublishedAt_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByInvalidPublishedAt();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("publishedAt")));
    }

    @Test
    void validate_withInvalidRequestByNullPageCount_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByNullPageCount();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("pageCount")));
    }

    @Test
    void validate_withInvalidRequestByNegativePageCount_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByNegativePageCount();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("pageCount")));
    }

    @Test
    void validate_withInvalidRequestByBlankGenre_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByBlankGenre();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().contains("genres")));
    }

    @Test
    void validate_withInvalidRequestByGenreExceedingMaxLength_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByGenreExceedingMaxLength();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().contains("genres")));
    }

    @Test
    void validate_withInvalidRequestByTooManyGenres_shouldFail() {
        // Arrange
        var request = BookRequestFactory.invalidRequestByTooManyGenres();

        // Act
        var result = validator.validate(request);

        // Assert
        assertTrue(result.stream().anyMatch(x -> x.getPropertyPath().toString().equals("genres")));
    }

}
