package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookProgressResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookActivityService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookProgressService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressNotFoundException;
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

        when(bookProgressMapper.toModel(request))
                .thenReturn(progress);

        doNothing()
                .when(bookProgressValidator)
                .validate(progress);

        when(bookProgressRepository.save(progress))
                .thenReturn(progress);

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
        verify(bookProgressMapper, never()).toResponse(any(BookProgress.class));
        verify(bookActivityService, never()).create(any(BookProgressRequest.class));
    }

    @Test
    void create_withValidProgressAndUserAuthorized_shouldReturnValidResponse() {
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
                    .thenReturn(false);

            assertDoesNotThrow(() -> bookProgressService.create(request));
        }

        verify(bookProgressMapper, times(1)).toModel(request);
        verify(bookProgressValidator, times(1)).validate(progress);
        verify(bookProgressRepository, times(1)).save(progress);
        verify(bookProgressMapper, times(1)).toResponse(progress);
        verify(bookActivityService, times(1)).create(request);
    }

    @Test
    void update_withNonExisting_Progress_shouldFail() {
        // Arrange
        var id = 1L;
        var request = BookProgressRequestFactory.validRequest();

        // Act && Assert
        assertThrows(BookProgressNotFoundException.class, () -> bookProgressService.update(id, request));
        verify(bookProgressRepository, times(1)).findById(id);
        verify(bookProgressMapper, never()).updateModel(any(BookProgress.class), any(BookProgressRequest.class));
        verify(bookProgressValidator, never()).validate(any(BookProgress.class));
        verify(bookProgressRepository, never()).save(any(BookProgress.class));
        verify(bookProgressMapper, never()).toResponse(any(BookProgress.class));
        verify(bookActivityService, never()).create(request);
    }

    @Test
    void update_withExistingProgress_shouldPass() {
        // Arrange
        var review = BookProgressFactory.validProgress();
        var request = BookProgressRequestFactory.validRequest();
        var updatedProgress = BookProgressFactory.updatedProgress(review, request);
        var updatedProgressResponse = BookProgressResponseFactory.validResponse(updatedProgress);

        when(bookProgressRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));

        doNothing()
                .when(bookProgressMapper)
                .updateModel(review, request);

        doNothing()
                .when(bookProgressValidator)
                .validate(review);

        when(bookProgressRepository.save(review))
                .thenReturn(updatedProgress);

        when(bookProgressMapper.toResponse(updatedProgress))
                .thenReturn(updatedProgressResponse);

        when(bookActivityService.create(request))
                .thenReturn(new BookActivityResponse());

        // Act
        var result = bookProgressService.update(review.getId(), request);

        // Assert
        assertEquals(updatedProgressResponse, result);
        verify(bookProgressRepository, times(1)).findById(review.getId());
        verify(bookProgressMapper, times(1)).updateModel(review, request);
        verify(bookProgressValidator, times(1)).validate(review);
        verify(bookProgressRepository, times(1)).save(review);
        verify(bookProgressMapper, times(1)).toResponse(updatedProgress);
        verify(bookActivityService, times(1)).create(request);
    }

    @Test
    void delete_withExistingReviewAndUserIsUnauthorized_shouldFail() {
        // Arrange
        var progress = BookProgressFactory.validProgress();
        var progressId = progress.getId();

        when(bookProgressRepository.findById(progressId))
                .thenReturn(Optional.of(progress));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(progress.getUser().getUsername()))
                    .thenReturn(true);

            assertThrows(BookProgressModificationForbiddenException.class, () -> bookProgressService.delete(progressId));
        }

        verify(bookProgressRepository, times(1)).findById(progressId);
    }

    @Test
    void delete_withExistingReviewAndUserIsAuthorized_shouldPass() {
        // Arrange
        var progress = BookProgressFactory.validProgress();
        var progressId = progress.getId();

        when(bookProgressRepository.findById(progressId))
                .thenReturn(Optional.of(progress));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(progress.getUser().getUsername()))
                    .thenReturn(false);

            assertDoesNotThrow(() -> bookProgressService.delete(progressId));
        }

        verify(bookProgressRepository, times(1)).findById(progressId);
    }

}
