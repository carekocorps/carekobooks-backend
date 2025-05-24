package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookGenreRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookGenreResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookGenreFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookGenreMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookGenreService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookGenreValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookGenreServiceTest {

    @Mock
    private BookGenreRepository bookGenreRepository;

    @Mock
    private BookGenreValidator bookGenreValidator;

    @Mock
    private BookGenreMapper bookGenreMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BookGenreService bookGenreService;

    @Test
    void find_withExistingGenre_shouldReturnValidResponse() {
        // Arrange
        var genre = BookGenreFactory.validGenre();
        var response = BookGenreResponseFactory.validResponse(genre);

        when(bookGenreRepository.findByName(any(String.class)))
                .thenReturn(Optional.of(genre));

        when(bookGenreMapper.toResponse(any(BookGenre.class)))
                .thenReturn(response);

        // Act
        var result = bookGenreService.find(genre.getName());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(result.get(), response);
        verify(bookGenreRepository).findByName(any(String.class));
    }

    @Test
    void find_withNonExistingGenre_shouldReturnEmpty() {
        // Arrange
        when(bookGenreRepository.findByName(any(String.class)))
                .thenReturn(Optional.empty());

        // Act
        var result = bookGenreService.find("genre");

        // Assert
        assertTrue(result.isEmpty());
        verify(bookGenreRepository).findByName(any(String.class));
    }

    @Test
    void create_withValidGenre_shouldReturnValidResponse() {
        // Arrange
        var request = BookGenreRequestFactory.validRequest();
        var genre = BookGenreFactory.validGenre(request);
        var response = BookGenreResponseFactory.validResponse(genre);

        when(bookGenreMapper.toModel(any(BookGenreRequest.class)))
                .thenReturn(genre);

        when(bookGenreRepository.save(any(BookGenre.class)))
                .thenReturn(genre);

        when(bookGenreMapper.toResponse(any(BookGenre.class)))
                .thenReturn(response);

        // Act
        var result = bookGenreService.create(request);

        // Assert
        verify(bookGenreValidator).validate(genre);
        assertEquals(response, result);
    }

    @Test
    void update_withNonExistingGenre_shouldFail() {
        // Arrange
        var name = "example";
        var request = BookGenreRequestFactory.validRequest();

        // Act && Assert
        assertThrows(BookGenreNotFoundException.class, () -> bookGenreService.update(name, request));
    }

}
