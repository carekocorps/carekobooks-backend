package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookGenreRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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
    void toModel_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookGenreRequestFactory.validRequest();
        var genre = BookGenreFactory.validGenre(request);

        // Act
        var result = bookGenreMapper.toModel(request);

        // Assert
        assertEquals(genre.getName(), result.getName());
        assertEquals(genre.getDisplayName(), result.getDisplayName());
        assertEquals(genre.getDescription(), result.getDescription());
    }

    @Test
    void toModel_withValidResponse_shouldReturnValidModel() {
        // Arrange
        var genre = BookGenreFactory.validGenre();
        var response = BookGenreResponseFactory.validResponse(genre);

        // Act
        var result = bookGenreMapper.toModel(response);

        // Assert
        assertEquals(genre.getId(), result.getId());
        assertEquals(genre.getName(), result.getName());
        assertEquals(genre.getDisplayName(), result.getDisplayName());
        assertEquals(genre.getDescription(), result.getDescription());
        assertEquals(genre.getCreatedAt(), result.getCreatedAt());
        assertEquals(genre.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidModel() {
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
    void updateModel_withValidModelAndValidRequest_shouldUpdateModel() {
        // Arrange
        var genre = BookGenreFactory.validGenre();
        var newGenre = SerializationUtils.clone(genre);
        var request = BookGenreRequestFactory.validRequest();

        // Act
        bookGenreMapper.updateModel(newGenre, request);

        // Assert
        assertEquals(genre.getId(), newGenre.getId());
        assertEquals(request.getName(), newGenre.getName());
        assertEquals(request.getDisplayName(), newGenre.getDisplayName());
        assertEquals(request.getDescription(), newGenre.getDescription());
        assertEquals(genre.getCreatedAt(), newGenre.getCreatedAt());
    }

    @Test
    void toModel_withValidGenreNames_shouldReturnValidModels() {
        // Arrange
        var size = 10;
        var genres = IntStream.range(0, size).mapToObj(i -> BookGenreFactory.validGenre()).toList();
        var genreNames = genres.stream().map(BookGenre::getName).toList();

        when(bookGenreRepository.findAllByNameIn(genreNames))
                .thenReturn(genres);

        // Act
        var result = bookGenreMapper.toModel(genreNames);

        // Assert
        assertNotNull(result);
        assertEquals(size, result.size());
        assertAll(IntStream.range(0, size).mapToObj(i -> () -> assertEquals(genres.get(i).getName(), result.get(i).getName())));
        verify(bookGenreRepository, times(1)).findAllByNameIn(genreNames);
    }

    @Test
    void toModel_withDuplicateGenreNames_shouldReturnNonDuplicateValidModels() {
        // Arrange
        var size = 10;
        var genre = BookGenreFactory.validGenre();
        var genres = IntStream.range(0, size).mapToObj(i -> genre).toList();
        var uniqueGenres = genres
                .stream()
                .collect(Collectors.toMap(BookGenre::getName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();

        var genreNames = genres.stream().map(BookGenre::getName).toList();

        when(bookGenreRepository.findAllByNameIn(genreNames))
                .thenReturn(uniqueGenres);

        // Act
        var result = bookGenreMapper.toModel(genreNames);

        // Assert
        assertNotNull(result);
        assertNotEquals(genreNames.size(), result.size());
        assertEquals(uniqueGenres.size(), result.size());
        verify(bookGenreRepository, times(1)).findAllByNameIn(genreNames);
    }

    @Test
    void toModel_withInvalidGenreNames_shouldReturnNonInvalidModels() {
        // Arrange
        var size = 10;
        var genres = IntStream.range(0, size).mapToObj(i -> BookGenreFactory.validGenre("genre_" + i)).toList();
        var uniqueGenres = genres
                .stream()
                .collect(Collectors.toMap(BookGenre::getName, Function.identity(), (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();

        var genreNames = IntStream.range(0, size + 1).mapToObj(i -> "genre_" + i).toList();

        when(bookGenreRepository.findAllByNameIn(genreNames))
                .thenReturn(uniqueGenres);

        // Act
        var result = bookGenreMapper.toModel(genreNames);

        // Assert
        assertNotNull(result);
        assertNotEquals(genreNames.size(), result.size());
        verify(bookGenreRepository, times(1)).findAllByNameIn(genreNames);
    }

    @Test
    void toModel_withEmptyGenreNames_shouldReturnEmptyList() {
        // Arrange
        var uniqueGenres = List.<BookGenre>of();
        var genreNames = List.<String>of();

        when (bookGenreRepository.findAllByNameIn(genreNames))
                .thenReturn(uniqueGenres);

        // Act
        var result = bookGenreMapper.toModel(genreNames);

        // Assert
        assertTrue(result.isEmpty());
    }

}
