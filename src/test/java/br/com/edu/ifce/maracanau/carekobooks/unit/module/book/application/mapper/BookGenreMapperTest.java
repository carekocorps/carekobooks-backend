package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookGenreRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookGenreMapperTest {

    private BookGenreRepository bookGenreRepository;
    private BookGenreMapper bookGenreMapper;

    @BeforeEach
    void setUp() {
        bookGenreMapper = Mappers.getMapper(BookGenreMapper.class);
        bookGenreRepository = mock(BookGenreRepository.class);
        bookGenreMapper.setBookGenreRepository(bookGenreRepository);
    }

    @Test
    void toModel_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookGenreRequestFactory.validRequest();

        // Act
        var result = bookGenreMapper.toModel(request);

        // Assert
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDisplayName(), result.getDisplayName());
        assertEquals(request.getDescription(), result.getDescription());
    }

    @Test
    void toModel_withValidResponse_shouldReturnValidModel() {
        // Arrange
        var response = BookGenreResponseFactory.validResponse();

        // Act
        var result = bookGenreMapper.toModel(response);

        // Assert
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getDisplayName(), result.getDisplayName());
        assertEquals(response.getDescription(), result.getDescription());
        assertEquals(response.getCreatedAt(), result.getCreatedAt());
        assertEquals(response.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidResponse() {
        // Arrange
        var genre = BookGenreFactory.validGenre();

        // Act
        var result = bookGenreMapper.toResponse(genre);

        // Assert
        assertEquals(result.getId(), genre.getId());
        assertEquals(result.getName(), genre.getName());
        assertEquals(result.getDisplayName(), genre.getDisplayName());
        assertEquals(result.getDescription(), genre.getDescription());
        assertEquals(result.getCreatedAt(), genre.getCreatedAt());
        assertEquals(result.getUpdatedAt(), genre.getUpdatedAt());
    }

    @Test
    void updateModel_withValidModelAndValidRequest_shouldReturnValidUpdatedModel() {
        // Arrange
        var genre = BookGenreFactory.validGenre();
        var request = BookGenreRequestFactory.validRequest();

        // Act
        var result = new BookGenre();
        BeanUtils.copyProperties(genre, result);
        bookGenreMapper.updateModel(result, request);

        // Assert
        assertEquals(genre.getId(), result.getId());
        assertEquals(genre.getBooks(), result.getBooks());
        assertEquals(genre.getCreatedAt(), result.getCreatedAt());

        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDisplayName(), result.getDisplayName());
        assertEquals(request.getDescription(), result.getDescription());
    }

    @Test
    void toModel_withValidGenreNames_shouldReturnValidModels() {
        // Arrange
        var names = List.of("genre1", "genre2");
        var genre1 = BookGenreFactory.validGenre(names.getFirst());
        var genre2 = BookGenreFactory.validGenre(names.get(1));

        when(bookGenreRepository.findAllByNameIn(names))
                .thenReturn(List.of(genre1, genre2));

        // Act
        var result = bookGenreMapper.toModel(names);

        // Assert
        assertEquals(2, result.size());
        assertEquals(names.getFirst(), result.getFirst().getName());
        assertEquals(names.get(1), result.get(1).getName());
    }

}
