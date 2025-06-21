package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;
import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressExceedingPageCountLimitException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressUserConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookProgressValidatorTest {

    @Mock
    private BookProgressRepository bookProgressRepository;

    @InjectMocks
    private BookProgressValidator bookProgressValidator;

    @Test
    void validate_withValidProgress_shouldPass() {
        // Arrange
        var progress = BookProgressFactory.validProgress();

        // Act && Assert
        assertDoesNotThrow(() -> bookProgressValidator.validate(progress));
    }

    @Test
    void validate_withValidProgressWithNullPageCount_shouldPass() {
        // Arrange
        var progress = BookProgressFactory.validProgressWithNullPageCount();

        // Act && Assert
        assertDoesNotThrow(() -> bookProgressValidator.validate(progress));
    }

    @Test
    void validate_withInvalidProgressByEmptyUser_shouldFail() {
        // Arrange
        var progress = BookProgressFactory.invalidProgressByEmptyUser();

        // Act && Assert
        assertThrows(UserNotFoundException.class, () -> bookProgressValidator.validate(progress));
    }

    @Test
    void validate_withInvalidProgressByEmptyBook_shouldFail() {
        // Arrange
        var progress = BookProgressFactory.invalidProgressByEmptyBook();

        // Act && Assert
        assertThrows(BookNotFoundException.class, () -> bookProgressValidator.validate(progress));
    }

    @Test
    void validate_withInvalidProgressByExceedingPageCount_shouldFail() {
        // Arrange
        var progress = BookProgressFactory.invalidProgressByExceedingPageCount();

        // Act && Assert
        assertThrows(BookProgressExceedingPageCountLimitException.class, () -> bookProgressValidator.validate(progress));
    }

    @Test
    void validate_withInvalidProgressByUserDuplicatedProgress_shouldFail() {
        // Arrange
        var existingProgress = BookProgressFactory.validProgress();
        var progress = BookProgressFactory.validProgress();
        progress.setUser(existingProgress.getUser());
        progress.setBook(existingProgress.getBook());
        progress.setPageCount(Math.min(progress.getPageCount(), progress.getBook().getPageCount()));

        when(bookProgressRepository.findAll(ArgumentMatchers.<Specification<BookProgress>>any()))
                .thenReturn(List.of(existingProgress));

        // Act && Assert
        assertThrows(BookProgressUserConflictException.class, () -> bookProgressValidator.validate(progress));
    }

}
