package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookActivityValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class BookActivityValidatorTest {

    private BookActivityValidator bookActivityValidator;

    @BeforeEach
    void setUp() {
        bookActivityValidator = new BookActivityValidator();
    }

    @Test
    void validate_withValidActivity_shouldSucceed() {
        // Arrange
        var activity = BookActivityFactory.validActivity();

        // Act && Assert
        assertThatCode(() -> bookActivityValidator.validate(activity)).doesNotThrowAnyException();
    }

    @Test
    void validate_withInvalidActivityByEmptyUser_shouldFail() {
        // Arrange
        var activity = BookActivityFactory.invalidActivityByEmptyUser();

        // Act && Assert
        assertThatThrownBy(() -> bookActivityValidator.validate(activity)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void validate_withInvalidActivityByEmptyBook_shouldFail() {
        // Arrange
        var progress = BookActivityFactory.invalidActivityByEmptyBook();

        // Act && Assert
        assertThatThrownBy(() -> bookActivityValidator.validate(progress)).isInstanceOf(BookNotFoundException.class);
    }

}
