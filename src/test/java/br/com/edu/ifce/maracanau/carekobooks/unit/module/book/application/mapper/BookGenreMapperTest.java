package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookGenreRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookGenreFactory;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        assertNull(result);
    }

    @Test
    void toEntity_withValidGenreRequest_shouldReturnGenre() {
        // Arrange
        var genreRequest = BookGenreRequestFactory.validRequest();
        var genre = BookGenreFactory.validGenre(genreRequest);

        // Act
        var result = bookGenreMapper.toEntity(genreRequest);

        // Assert
        assertEquals(genre.getName(), result.getName());
        assertEquals(genre.getDisplayName(), result.getDisplayName());
        assertEquals(genre.getDescription(), result.getDescription());
    }

    @Test
    void toEntity_withNullGenreResponse_shouldReturnNullGenre() {
        // Arrange
        BookGenreResponse genreResponse = null;

        // Act
        var result = bookGenreMapper.toEntity(genreResponse);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_withValidGenreResponse_shouldReturnGenre() {
        // Arrange
        var genre = BookGenreFactory.validGenre();
        var genreResponse = BookGenreResponseFactory.validResponse(genre);

        // Act
        var result = bookGenreMapper.toEntity(genreResponse);

        // Assert
        assertEquals(genre.getId(), result.getId());
        assertEquals(genre.getName(), result.getName());
        assertEquals(genre.getDisplayName(), result.getDisplayName());
        assertEquals(genre.getDescription(), result.getDescription());
        assertEquals(genre.getCreatedAt(), result.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toEntity_withValidGenreNames_shouldReturnGenreList() {
        // Arrange
        var numGenres = Math.abs(new Random().nextInt(10));
        var genres = IntStream.range(0, numGenres).mapToObj(i -> BookGenreFactory.validGenre()).toList();
        var genreNames = genres.stream().map(BookGenre::getName).toList();

        when(bookGenreRepository.findAllByNameIn(genreNames))
                .thenReturn(genres);

        // Act
        var result = bookGenreMapper.toEntity(genreNames);

        // Assert
        assertNotNull(result);
        assertEquals(numGenres, result.size());
        assertAll(IntStream.range(0, numGenres).mapToObj(i -> () -> assertEquals(genres.get(i).getName(), result.get(i).getName())));
        verify(bookGenreRepository, times(1)).findAllByNameIn(genreNames);
    }

    @Test
    void toResponse_withNullGenre_shouldReturnNullGenreResponse() {
        // Arrange
        BookGenre genre = null;

        // Act
        var result = bookGenreMapper.toResponse(genre);

        // Assert
        assertNull(result);
    }

    @Test
    void toResponse_withValidGenre_shouldReturnGenreResponse() {
        // Arrange
        var genre = BookGenreFactory.validGenre();

        // Act
        var result = bookGenreMapper.toResponse(genre);

        // Assert
        assertEquals(genre.getId(), result.getId());
        assertEquals(genre.getName(), result.getName());
        assertEquals(genre.getDisplayName(), result.getDisplayName());
        assertEquals(genre.getDescription(), result.getDescription());
        assertEquals(genre.getCreatedAt(), result.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), result.getUpdatedAt());
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
        assertEquals(genre.getId(), newGenre.getId());
        assertEquals(genre.getName(), newGenre.getName());
        assertEquals(genre.getDisplayName(), newGenre.getDisplayName());
        assertEquals(genre.getDescription(), newGenre.getDescription());
        assertEquals(genre.getCreatedAt(), newGenre.getCreatedAt());
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
        assertEquals(genre.getId(), newGenre.getId());
        assertEquals(genreRequest.getName(), newGenre.getName());
        assertEquals(genreRequest.getDisplayName(), newGenre.getDisplayName());
        assertEquals(genreRequest.getDescription(), newGenre.getDescription());
        assertEquals(genre.getCreatedAt(), newGenre.getCreatedAt());
    }

}
