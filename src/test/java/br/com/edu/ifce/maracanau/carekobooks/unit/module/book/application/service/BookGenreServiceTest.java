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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bookGenreService, "entityManager", entityManager);
    }

    @Test
    void find_withNonExistingGenre_shouldReturnEmpty() {
        // Arrange
        var name = "genre";

        when(bookGenreRepository.findByName(name))
                .thenReturn(Optional.empty());

        // Act
        var result = bookGenreService.find(name);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookGenreRepository, times(1)).findByName(name);
        verify(bookGenreMapper, never()).toResponse(any(BookGenre.class));
    }

    @Test
    void find_withExistingGenre_shouldReturnValidResponse() {
        // Arrange
        var genre = BookGenreFactory.validGenre();
        var response = BookGenreResponseFactory.validResponse(genre);

        when(bookGenreRepository.findByName(genre.getName()))
                .thenReturn(Optional.of(genre));

        when(bookGenreMapper.toResponse(genre))
                .thenReturn(response);

        // Act
        var result = bookGenreService.find(genre.getName());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(result.get(), response);
        verify(bookGenreRepository, times(1)).findByName(genre.getName());
        verify(bookGenreMapper, times(1)).toResponse(genre);
    }

    @Test
    void create_withValidGenre_shouldReturnValidResponse() {
        // Arrange
        var request = BookGenreRequestFactory.validRequest();
        var genre = BookGenreFactory.validGenre(request);
        var response = BookGenreResponseFactory.validResponse(genre);

        when(bookGenreMapper.toModel(request))
                .thenReturn(genre);

        doNothing()
                .when(bookGenreValidator)
                .validate(genre);

        when(bookGenreRepository.save(genre))
                .thenReturn(genre);

        when(bookGenreMapper.toResponse(genre))
                .thenReturn(response);

        // Act
        var result = bookGenreService.create(request);

        // Assert
        assertEquals(response, result);
        verify(bookGenreMapper, times(1)).toModel(request);
        verify(bookGenreValidator, times(1)).validate(genre);
        verify(bookGenreRepository, times(1)).save(genre);
        verify(bookGenreMapper, times(1)).toResponse(genre);
    }

    @Test
    void update_withNonExistingGenre_shouldFail() {
        // Arrange
        var name = "genre";
        var request = BookGenreRequestFactory.validRequest();

        // Act && Assert
        assertThrows(BookGenreNotFoundException.class, () -> bookGenreService.update(name, request));
        verify(bookGenreRepository, times(1)).findByName(name);
        verify(entityManager, never()).detach(any(BookGenre.class));
        verify(bookGenreMapper, never()).updateModel(any(BookGenre.class), any(BookGenreRequest.class));
        verify(bookGenreValidator, never()).validate(any(BookGenre.class));
        verify(entityManager, never()).merge(any(BookGenre.class));
        verify(bookGenreRepository, never()).save(any(BookGenre.class));
        verify(bookGenreMapper, never()).toResponse(any(BookGenre.class));
    }

    @Test
    void update_withExistingGenre_shouldPass() {
        // Arrange
        var genre = BookGenreFactory.validGenre();
        var request = BookGenreRequestFactory.validRequest();
        var updatedGenre = BookGenreFactory.updatedGenre(genre, request);
        var updatedGenreResponse = BookGenreResponseFactory.validResponse(updatedGenre);

        when(bookGenreRepository.findByName(genre.getName()))
                .thenReturn(Optional.of(genre));

        doNothing()
                .when(entityManager)
                .detach(genre);

        doNothing()
                .when(bookGenreMapper)
                .updateModel(genre, request);

        doNothing()
                .when(bookGenreValidator)
                .validate(genre);

        when(entityManager.merge(genre))
                .thenReturn(updatedGenre);

        when(bookGenreRepository.save(updatedGenre))
                .thenReturn(updatedGenre);

        when(bookGenreMapper.toResponse(updatedGenre))
                .thenReturn(updatedGenreResponse);

        // Act && Assert
        assertDoesNotThrow(() -> bookGenreService.update(genre.getName(), request));
        verify(bookGenreRepository, times(1)).findByName(genre.getName());
        verify(entityManager, times(1)).detach(genre);
        verify(bookGenreMapper, times(1)).updateModel(genre, request);
        verify(bookGenreValidator, times(1)).validate(genre);
        verify(entityManager, times(1)).merge(genre);
        verify(bookGenreRepository, times(1)).save(updatedGenre);
        verify(bookGenreMapper, times(1)).toResponse(updatedGenre);
    }

    @Test
    void delete_withNonExistingGenre_shouldFail() {
        // Arrange
        var name = "genre";

        when(bookGenreRepository.existsByName(name))
                .thenReturn(false);

        // Act && Assert
        assertThrows(BookGenreNotFoundException.class, () -> bookGenreService.delete(name));
        verify(bookGenreRepository, times(1)).existsByName(name);
        verify(bookGenreRepository, never()).deleteByName(any(String.class));
    }

    @Test
    void delete_withExistingGenre_shouldPass() {
        // Arrange
        var genre = BookGenreFactory.validGenre();

        when(bookGenreRepository.existsByName(genre.getName()))
                .thenReturn(true);

        // Act && Assert
        assertDoesNotThrow(() -> bookGenreService.delete(genre.getName()));
        verify(bookGenreRepository, times(1)).existsByName(genre.getName());
        verify(bookGenreRepository, times(1)).deleteByName(genre.getName());
    }

}
