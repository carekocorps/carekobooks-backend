package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookThreadReplyValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookThreadReplyValidatorTest {

    private BookThreadReplyValidator bookThreadReplyValidator;

    @BeforeEach
    void setUp() {
        bookThreadReplyValidator = new BookThreadReplyValidator();
    }

    @Test
    void validate_withValidThread_shouldPass() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();

        // Act && Assert
        assertDoesNotThrow(() -> bookThreadReplyValidator.validate(reply));
    }

    @Test
    void validate_withInvalidThreadByEmptyUser_shouldFail() {
        // Arrange
        var reply = BookThreadReplyFactory.invalidReviewByEmptyUser();

        // Act && Assert
        assertThrows(UserNotFoundException.class, () -> bookThreadReplyValidator.validate(reply));
    }

    @Test
    void validate_withInvalidThreadByEmptyThread_shouldFail() {
        // Arrange
        var reply = BookThreadReplyFactory.invalidReviewByEmptyThread();

        // Act && Assert
        assertThrows(BookThreadNotFoundException.class, () -> bookThreadReplyValidator.validate(reply));
    }

}
