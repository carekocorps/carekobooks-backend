package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookThreadReplyValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookThreadReplyValidatorTest {

    private BookThreadReplyValidator bookThreadReplyValidator;

    @BeforeEach
    void setUp() {
        bookThreadReplyValidator = new BookThreadReplyValidator();
    }

    @Test
    void validate_withValidThread_shouldSucceed() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();

        // Act && Assert
        assertThatCode(() -> bookThreadReplyValidator.validate(reply)).doesNotThrowAnyException();
    }

    @Test
    void validate_withInvalidThreadByEmptyUser_shouldFail() {
        // Arrange
        var reply = BookThreadReplyFactory.invalidReviewByEmptyUser();

        // Act && Assert
        assertThatThrownBy(() -> bookThreadReplyValidator.validate(reply)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void validate_withInvalidThreadByEmptyThread_shouldFail() {
        // Arrange
        var reply = BookThreadReplyFactory.invalidReviewByEmptyThread();

        // Act && Assert
        assertThatThrownBy(() -> bookThreadReplyValidator.validate(reply)).isInstanceOf(BookThreadNotFoundException.class);
    }

}
