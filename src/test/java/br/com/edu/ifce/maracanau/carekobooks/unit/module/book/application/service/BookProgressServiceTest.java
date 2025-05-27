package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookProgressResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookActivityService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookProgressService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookProgressServiceTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookActivityService bookActivityService;

    @Mock
    private BookProgressRepository bookProgressRepository;

    @Mock
    private BookProgressValidator bookProgressValidator;

    @Mock
    private BookProgressMapper bookProgressMapper;

    @InjectMocks
    private BookProgressService bookProgressService;

    @Test
    void find_withNonExistingProgress_shouldReturnEmpty() {
        // Arrange
        var id = 1L;

        when(bookProgressRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = bookProgressService.find(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookProgressRepository, times(1)).findById(id);
        verify(bookProgressMapper, never()).toResponse(any(BookProgress.class));
    }

    @Test
    void find_withExistingProgress_shouldReturnValidResponse() {
        // Arrange
        var progress = BookProgressFactory.validProgress();
        var response = BookProgressResponseFactory.validResponse(progress);

        when(bookProgressRepository.findById(progress.getId()))
                .thenReturn(Optional.of(progress));

        when(bookProgressMapper.toResponse(progress))
                .thenReturn(response);

        // Act
        var result = bookProgressService.find(progress.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(result.get(), response);
        verify(bookProgressRepository, times(1)).findById(progress.getId());
        verify(bookProgressMapper, times(1)).toResponse(progress);
    }

    @Test
    void create_withValidProgressAndUserUnauthorized_shouldFail() {
        // Arrange
        var progress = BookProgressFactory.validProgress();
        var request = BookProgressRequestFactory.validRequest(progress);
        var response = BookProgressResponseFactory.validResponse(progress);

        when(bookProgressMapper.toModel(request))
                .thenReturn(progress);

        doNothing()
                .when(bookProgressValidator)
                .validate(progress);

        when(bookProgressRepository.save(progress))
                .thenReturn(progress);

        when(bookProgressMapper.toResponse(progress))
                .thenReturn(response);

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(request.getUsername()))
                    .thenReturn(true);

            assertThrows(BookProgressModificationForbiddenException.class, () -> bookProgressService.create(request));
        }

        verify(bookProgressMapper, times(1)).toModel(request);
        verify(bookProgressValidator, times(1)).validate(progress);
        verify(bookProgressRepository, times(1)).save(progress);
        verify(bookProgressMapper, times(1)).toResponse(progress);
        verify(bookActivityService, never()).create(any(BookProgressRequest.class));
        verify(bookProgressRepository, never()).calculateUserAverageScoreByBookId(any(Long.class));
        verify(bookService, never()).changeUserAverageScore(any(Long.class), any(Double.class));
    }

    @Test
    void create_withValidProgressAndUserAuthorizedAndNullScore_shouldReturnValidResponse() {
        // Arrange
        var progress = BookProgressFactory.validProgress(null);
        var request = BookProgressRequestFactory.validRequest(progress);
        var response = BookProgressResponseFactory.validResponse(progress);

        when(bookProgressMapper.toModel(request))
                .thenReturn(progress);

        doNothing()
                .when(bookProgressValidator)
                .validate(progress);

        when(bookProgressRepository.save(progress))
                .thenReturn(progress);

        when(bookProgressMapper.toResponse(progress))
                .thenReturn(response);

        when(bookActivityService.create(request))
                .thenReturn(new BookActivityResponse());

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(request.getUsername()))
                    .thenReturn(false);

            assertDoesNotThrow(() -> bookProgressService.create(request));
        }

        verify(bookProgressMapper, times(1)).toModel(request);
        verify(bookProgressValidator, times(1)).validate(progress);
        verify(bookProgressRepository, times(1)).save(progress);
        verify(bookProgressMapper, times(1)).toResponse(progress);
        verify(bookActivityService, times(1)).create(request);
        verify(bookProgressRepository, never()).calculateUserAverageScoreByBookId(any(Long.class));
        verify(bookService, never()).changeUserAverageScore(any(Long.class), any(Double.class));
    }

}
