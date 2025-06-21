package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookThreadValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookThreadValidatorTest {

    private BookThreadValidator bookThreadValidator;

    @BeforeEach
    void setUp() {
        bookThreadValidator = new BookThreadValidator();
    }

    @Test
    void validate_withValidThread_shouldPass() {
        // Arrange
        var thread = BookThreadFactory.validThread();

       // Act && Assert
        assertDoesNotThrow(() -> bookThreadValidator.validate(thread));
    }

    @Test
    void validate_withInvalidThreadByEmptyUser_shouldFail() {
        // Arrange
        var thread = BookThreadFactory.invalidReviewByEmptyUser();

        // Act && Assert
        assertThrows(UserNotFoundException.class, () -> bookThreadValidator.validate(thread));
    }

    @Test
    void validate_withInvalidThreadByEmptyBook_shouldFail() {
        // Arrange
        var thread = BookThreadFactory.invalidReviewByEmptyBook();

        // Act && Assert
        assertThrows(BookNotFoundException.class, () -> bookThreadValidator.validate(thread));
    }

}
