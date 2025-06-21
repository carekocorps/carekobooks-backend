package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookExceedingGenreLimitException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.shaded.org.apache.commons.lang3.SerializationUtils;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookValidatorTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookValidator bookValidator;

    @Test
    void validate_withValidBook_shouldSucceed() {
        // Arrange
        var book = BookFactory.validBook();

        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any()))
                .thenReturn(List.of(book));

        // Act && Assert
        assertDoesNotThrow(() -> bookValidator.validate(book));
        verify(bookRepository, times(1)).findAll(ArgumentMatchers.<Specification<Book>>any());
    }

    @Test
    void validate_withNullBookGenres_shouldSucceed() {
        // Arrange
        var book = BookFactory.validBookWithNullGenres();

        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any()))
                .thenReturn(List.of(book));

        // Act && Assert
        assertDoesNotThrow(() -> bookValidator.validate(book));
        verify(bookRepository, times(1)).findAll(ArgumentMatchers.<Specification<Book>>any());
    }

    @Test
    void validate_withInvalidBookByDuplicatedBook_shouldFail() {
        // Arrange
        var existingBook = BookFactory.validBook();
        var book = SerializationUtils.clone(existingBook);
        book.setId(Math.abs(new Random().nextLong()) + 1);

        when(bookRepository.findAll(ArgumentMatchers.<Specification<Book>>any()))
                .thenReturn(List.of(existingBook, book));

        // Act && Assert
        assertThrows(BookConflictException.class, () -> bookValidator.validate(book));
        verify(bookRepository, times(1)).findAll(ArgumentMatchers.<Specification<Book>>any());
    }

    @Test
    void validate_withInvalidBookByExceedingGenreLimit_shouldFail() {
        // Arrange
        var book = BookFactory.invalidBookByExceedingGenreLimit();

        // Act && Assert
        assertThrows(BookExceedingGenreLimitException.class, () -> bookValidator.validate(book));
        assertNotNull(book.getGenres());
        assertTrue(book.getGenres().size() > 5);
    }

}
