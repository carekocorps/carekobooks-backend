package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookGenreRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookGenreMapperTest {

    @Mock
    private BookGenreRepository bookGenreRepository;

    @InjectMocks
    private BookGenreMapper bookGenreMapper = Mappers.getMapper(BookGenreMapper.class);

    @Test
    void toEntity_withNullGenreRequest_shouldReturnNullGenre() {
        // Arrange
        BookGenreRequest genreRequest = null;

        // Act
        var result = bookGenreMapper.toEntity(genreRequest);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void toEntity_withValidGenreRequest_shouldReturnGenre() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.validRequest();
        var genre = BookGenreFactory.validGenre(genreRequest);

        // Act
        var result = bookGenreMapper.toEntity(genreRequest);

        // Assert
        assertThat(genre.getName()).isEqualTo(result.getName());
        assertThat(genre.getDisplayName()).isEqualTo(result.getDisplayName());
        assertThat(genre.getDescription()).isEqualTo(result.getDescription());
    }

    @Test
    void toEntity_withNullGenreResponse_shouldReturnNullGenre() {
        // Arrange
        BookGenreResponse genreResponse = null;

        // Act
        var result = bookGenreMapper.toEntity(genreResponse);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void toEntity_withValidGenreResponse_shouldReturnGenre() {
        // Arrange
        var genre = BookGenreFactory.validGenre();
        var genreResponse = BookGenreResponseFactory.validResponse(genre);

        // Act
        var result = bookGenreMapper.toEntity(genreResponse);

        // Assert
        assertThat(result.getId()).isEqualTo(genre.getId());
        assertThat(result.getName()).isEqualTo(genre.getName());
        assertThat(result.getDisplayName()).isEqualTo(genre.getDisplayName());
        assertThat(result.getDescription()).isEqualTo(genre.getDescription());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(genre.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(genre.getUpdatedAt());
    }

    @Test
    void toEntity_withValidGenreNames_shouldReturnGenreList() {
        // Arrange
        var numGenres = Math.abs(new Random().nextInt(10));
        var genres = IntStream.range(0, numGenres).mapToObj(x -> BookGenreFactory.validGenre()).toList();
        var genreNames = genres.stream().map(BookGenre::getName).toList();

        when(bookGenreRepository.findAllByNameIn(genreNames))
                .thenReturn(genres);

        // Act
        var result = bookGenreMapper.toEntity(genreNames);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(numGenres)
                .extracting(BookGenre::getName)
                .containsExactlyElementsOf(genres.stream().map(BookGenre::getName).toList());

        verify(bookGenreRepository, times(1)).findAllByNameIn(genreNames);
    }

    @Test
    void toResponse_withNullGenre_shouldReturnNullGenreResponse() {
        // Arrange
        BookGenre genre = null;

        // Act
        var result = bookGenreMapper.toResponse(genre);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void toResponse_withValidGenre_shouldReturnGenreResponse() {
        // Arrange
        var genre = BookGenreFactory.validGenre();

        // Act
        var result = bookGenreMapper.toResponse(genre);

        // Assert
        assertThat(result.getId()).isEqualTo(genre.getId());
        assertThat(result.getName()).isEqualTo(genre.getName());
        assertThat(result.getDisplayName()).isEqualTo(genre.getDisplayName());
        assertThat(result.getDescription()).isEqualTo(genre.getDescription());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(genre.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(genre.getUpdatedAt());
    }

    @Test
    void updateEntity_withValidGenreAndNullGenreRequest_shouldPreserveGenre() {
        // Arrange
        BookGenreRequest genreRequest = null;
        var genre = BookGenreFactory.validGenre();
        var newGenre = SerializationUtils.clone(genre);

        // Act
        bookGenreMapper.updateEntity(genre, genreRequest);

        // Assert
        assertThat(newGenre.getId()).isEqualTo(genre.getId());
        assertThat(newGenre.getName()).isEqualTo(genre.getName());
        assertThat(newGenre.getDisplayName()).isEqualTo(genre.getDisplayName());
        assertThat(newGenre.getDescription()).isEqualTo(genre.getDescription());
        assertThat(newGenre.getCreatedAt()).isEqualToIgnoringNanos(genre.getCreatedAt());
    }

    @Test
    void updateEntity_withValidGenreAndValidGenreRequest_shouldUpdateGenre() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.validRequest();
        var genre = BookGenreFactory.validGenre();
        var newGenre = SerializationUtils.clone(genre);

        // Act
        bookGenreMapper.updateEntity(newGenre, genreRequest);

        // Assert
        assertThat(newGenre.getId()).isEqualTo(genre.getId());
        assertThat(newGenre.getName()).isEqualTo(genreRequest.getName());
        assertThat(newGenre.getDisplayName()).isEqualTo(genreRequest.getDisplayName());
        assertThat(newGenre.getDescription()).isEqualTo(genreRequest.getDescription());
        assertThat(newGenre.getCreatedAt()).isEqualToIgnoringNanos(genre.getCreatedAt());
    }

}
