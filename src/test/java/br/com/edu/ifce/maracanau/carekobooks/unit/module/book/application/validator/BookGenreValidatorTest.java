package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookGenreValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreNameConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookGenreValidatorTest {

    @Mock
    private BookGenreRepository bookGenreRepository;

    @InjectMocks
    private BookGenreValidator bookGenreValidator;

    @Test
    void validate_withNewAndUniqueGenre_shouldSucceed() {
        // Arrange
        var genre = BookGenreFactory.validGenre();

        // Act && Assert
        assertThatCode(() -> bookGenreValidator.validate(genre)).doesNotThrowAnyException();
    }

    @Test
    void validate_withExistingGenreBeingModified_shouldSucceed() {
        // Arrange
        var existingGenre = BookGenreFactory.validGenre();
        var existingGenreModified = BookGenreFactory.validGenre(existingGenre.getId(), existingGenre.getName());

        when(bookGenreRepository.findByName(existingGenre.getName()))
                .thenReturn(Optional.of(existingGenre));

        // Act && Assert
        assertThatCode(() -> bookGenreValidator.validate(existingGenreModified)).doesNotThrowAnyException();
    }

    @Test
    void validate_withNewAndDuplicateGenre_shouldFail() {
        // Arrange
        var existingGenre = BookGenreFactory.validGenre();
        var newGenre = BookGenreFactory.validGenre(existingGenre.getName());

        when(bookGenreRepository.findByName(existingGenre.getName()))
                .thenReturn(Optional.of(existingGenre));

        // Act && Assert
        assertThatThrownBy(() -> bookGenreValidator.validate(newGenre)).isInstanceOf(BookGenreNameConflictException.class);
    }

}
