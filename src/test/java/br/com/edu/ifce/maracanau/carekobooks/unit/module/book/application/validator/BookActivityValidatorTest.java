package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookActivityValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookActivityValidatorTest {

    private BookActivityValidator bookActivityValidator;

    @BeforeEach
    void setUp() {
        bookActivityValidator = new BookActivityValidator();
    }

    @Test
    void validate_withValidActivity_shouldPass() {
        // Arrange
        var activity = BookActivityFactory.validActivity();

        // Act && Assert
        assertDoesNotThrow(() -> bookActivityValidator.validate(activity));
    }

    @Test
    void validate_withInvalidActivityByEmptyUser_shouldFail() {
        // Arrange
        var activity = BookActivityFactory.invalidActivityByEmptyUser();

        // Act && Assert
        assertThrows(UserNotFoundException.class, () -> bookActivityValidator.validate(activity));
    }

    @Test
    void validate_withInvalidActivityByEmptyBook_shouldFail() {
        // Arrange
        var progress = BookActivityFactory.invalidActivityByEmptyBook();

        // Act && Assert
        assertThrows(BookNotFoundException.class, () -> bookActivityValidator.validate(progress));
    }

}
