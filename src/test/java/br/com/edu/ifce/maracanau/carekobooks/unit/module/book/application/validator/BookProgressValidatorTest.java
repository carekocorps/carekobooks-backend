package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressExceedingPageCountLimitException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookProgressValidatorTest {

    private BookProgressValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BookProgressValidator();
    }

    @Test
    void validate_withValidProgress_shouldPass() {
        // Arrange
        var progress = BookProgressFactory.validProgress();

        // Act && Assert
        assertDoesNotThrow(() -> validator.validate(progress));
    }

    @Test
    void validate_withValidProgressWithNullPageCount_shouldPass() {
        // Arrange
        var progress = BookProgressFactory.validProgressWithNullPageCount();

        // Act && Assert
        assertDoesNotThrow(() -> validator.validate(progress));
    }

    @Test
    void validate_withInvalidProgressByEmptyUser_shouldFail() {
        // Arrange
        var progress = BookProgressFactory.invalidProgressByEmptyUser();

        // Act && Assert
        assertThrows(UserNotFoundException.class, () -> validator.validate(progress));
    }

    @Test
    void validate_withInvalidProgressByEmptyBook_shouldFail() {
        // Arrange
        var progress = BookProgressFactory.invalidProgressByEmptyBook();

        // Act && Assert
        assertThrows(BookNotFoundException.class, () -> validator.validate(progress));
    }

    @Test
    void validate_withInvalidProgressByExceedingPageCount_shouldFail() {
        // Arrange
        var progress = BookProgressFactory.invalidProgressByExceedingPageCount();

        // Act && Assert
        assertThrows(BookProgressExceedingPageCountLimitException.class, () -> validator.validate(progress));
    }

}
