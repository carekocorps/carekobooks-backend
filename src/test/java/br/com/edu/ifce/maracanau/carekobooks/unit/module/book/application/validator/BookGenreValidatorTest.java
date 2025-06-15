package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookGenreValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreNameConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookGenreValidatorTest {

    @Mock
    private BookGenreRepository bookGenreRepository;

    @InjectMocks
    private BookGenreValidator bookGenreValidator;

    @Test
    void validate_withNewAndUniqueGenre_shouldPass() {
        // Arrange
        var genre = BookGenreFactory.validGenre();

        // Act && Assert
        assertDoesNotThrow(() -> bookGenreValidator.validate(genre));
    }

    @Test
    void validate_withExistingGenreBeingModified_shouldPass() {
        // Arrange
        var existingGenre = BookGenreFactory.validGenre();
        var existingGenreModified = BookGenreFactory.validGenre(existingGenre.getId(), existingGenre.getName());

        when(bookGenreRepository.findByName(existingGenre.getName()))
                .thenReturn(Optional.of(existingGenre));

        // Act && Assert
        assertDoesNotThrow(() -> bookGenreValidator.validate(existingGenreModified));
    }

    @Test
    void validate_withNewAndDuplicateGenre_shouldFail() {
        // Arrange
        var existingGenre = BookGenreFactory.validGenre();
        var newGenre = BookGenreFactory.validGenre(existingGenre.getName());

        when(bookGenreRepository.findByName(existingGenre.getName()))
                .thenReturn(Optional.of(existingGenre));

        // Act && Assert
        assertThrows(BookGenreNameConflictException.class, () -> bookGenreValidator.validate(newGenre));
    }

}
