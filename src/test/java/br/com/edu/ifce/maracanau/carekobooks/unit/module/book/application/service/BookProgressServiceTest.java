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
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

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
    void find_withNonExistingProgress_shouldReturnEmptyGenreResponse() {
        // Arrange
        var progressId = Math.abs(new Random().nextLong()) + 1;

        when(bookProgressRepository.findById(progressId))
                .thenReturn(Optional.empty());

        // Act
        var result = bookProgressService.find(progressId);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookProgressRepository, times(1)).findById(progressId);
        verify(bookProgressMapper, never()).toResponse(any(BookProgress.class));
    }

    @Test
    void find_withExistingProgress_shouldReturnProgressResponse() {
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
    void create_withValidProgressRequestAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progress = BookProgressFactory.validProgress();
            var request = BookProgressRequestFactory.validRequest(progress);

            when(bookProgressMapper.toEntity(request))
                    .thenReturn(progress);

            doNothing()
                    .when(bookProgressValidator)
                    .validate(progress);

            when(bookProgressRepository.save(progress))
                    .thenReturn(progress);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class))
                    .thenThrow(BookProgressModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookProgressModificationForbiddenException.class, () -> bookProgressService.create(request));
            verify(bookProgressMapper, times(1)).toEntity(request);
            verify(bookProgressValidator, times(1)).validate(progress);
            verify(bookProgressRepository, times(1)).save(progress);
            verify(bookProgressMapper, never()).toResponse(any(BookProgress.class));
            verify(bookActivityService, never()).create(any(BookProgressRequest.class));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class), times(1));
        }
    }

    @Test
    void create_withValidProgressRequestAndAuthorizedUser_shouldReturnProgressResponse() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progress = BookProgressFactory.validProgress();
            var request = BookProgressRequestFactory.validRequest(progress);
            var response = BookProgressResponseFactory.validResponse(progress);

            when(bookProgressMapper.toEntity(request))
                    .thenReturn(progress);

            doNothing()
                    .when(bookProgressValidator)
                    .validate(progress);

            when(bookProgressRepository.save(progress))
                    .thenReturn(progress);

            when(bookProgressMapper.toResponse(progress))
                    .thenReturn(response);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(response.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act
            var result = bookProgressService.create(request);

            // Assert
            assertNotNull(result);
            assertEquals(result, response);
            verify(bookProgressMapper, times(1)).toEntity(request);
            verify(bookProgressValidator, times(1)).validate(progress);
            verify(bookProgressRepository, times(1)).save(progress);
            verify(bookProgressMapper, times(1)).toResponse(progress);
            verify(bookActivityService, times(1)).create(request);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(response.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class), times(1));
        }
    }

    @Test
    void update_withNonExistingProgressAndValidUpdateRequest_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var activityId = Math.abs(new Random().nextLong()) + 1;
            var updateRequest = BookProgressRequestFactory.validRequest();

            // Act && Assert
            assertThrows(BookProgressNotFoundException.class, () -> bookProgressService.update(activityId, updateRequest));
            verify(bookProgressRepository, times(1)).findById(activityId);
            verify(bookProgressMapper, never()).updateEntity(any(BookProgress.class), any(BookProgressRequest.class));
            verify(bookProgressValidator, never()).validate(any(BookProgress.class));
            verify(bookProgressRepository, never()).save(any(BookProgress.class));
            verify(bookProgressMapper, never()).toResponse(any(BookProgress.class));
            verify(bookActivityService, never()).create(updateRequest);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
        }
    }

    @Test
    void update_withExistingProgressAndValidUpdateRequestAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progress = BookProgressFactory.validProgress();
            var progressId = progress.getId();
            var updateRequest = BookProgressRequestFactory.validRequest(progress);

            when(bookProgressRepository.findById(progressId))
                    .thenReturn(Optional.of(progress));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class))
                    .thenThrow(BookProgressModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookProgressModificationForbiddenException.class, () -> bookProgressService.update(progressId, updateRequest));
            verify(bookProgressRepository, times(1)).findById(progressId);
            verify(bookProgressMapper, never()).updateEntity(any(BookProgress.class), any(BookProgressRequest.class));
            verify(bookProgressValidator, never()).validate(any(BookProgress.class));
            verify(bookProgressRepository, never()).save(any(BookProgress.class));
            verify(bookProgressMapper, never()).toResponse(any(BookProgress.class));
            verify(bookActivityService, never()).create(updateRequest);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class), times(1));
        }
    }

    @Test
    void update_withExistingProgressAndValidUpdateRequestAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progress = BookProgressFactory.validProgress();
            var updateRequest = BookProgressRequestFactory.validRequest();

            var updatedProgress = BookProgressFactory.updatedProgress(progress, updateRequest);
            var updatedProgressResponse = BookProgressResponseFactory.validResponse(updatedProgress);

            when(bookProgressRepository.findById(progress.getId()))
                    .thenReturn(Optional.of(progress));

            doNothing().when(bookProgressMapper)
                    .updateEntity(progress, updateRequest);

            doNothing().when(bookProgressValidator)
                    .validate(progress);

            when(bookProgressRepository.save(progress))
                    .thenReturn(updatedProgress);

            when(bookProgressMapper.toResponse(updatedProgress))
                    .thenReturn(updatedProgressResponse);

            when(bookActivityService.create(updateRequest))
                    .thenReturn(new BookActivityResponse());

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act
            var result = bookProgressService.update(progress.getId(), updateRequest);

            // Assert
            assertNotNull(result);
            assertEquals(updatedProgressResponse, result);
            verify(bookProgressRepository, times(1)).findById(progress.getId());
            verify(bookProgressMapper, times(1)).updateEntity(progress, updateRequest);
            verify(bookProgressValidator, times(1)).validate(progress);
            verify(bookProgressRepository, times(1)).save(progress);
            verify(bookProgressMapper, times(1)).toResponse(updatedProgress);
            verify(bookActivityService, times(1)).create(updateRequest);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class), times(1));
        }
    }

    @Test
    void delete_withNonExistingProgress_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var id = 1L;

            when(bookProgressRepository.findById(id))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(BookProgressNotFoundException.class, () -> bookProgressService.delete(id));
            verify(bookProgressRepository, times(1)).findById(id);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
        }
    }

    @Test
    void delete_withExistingProgressAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progress = BookProgressFactory.validProgress();
            var progressId = progress.getId();

            when(bookProgressRepository.findById(progressId))
                    .thenReturn(Optional.of(progress));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class))
                    .thenThrow(BookProgressModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookProgressModificationForbiddenException.class, () -> bookProgressService.delete(progressId));
            verify(bookProgressRepository, times(1)).findById(progressId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class), times(1));
        }
    }

    @Test
    void delete_withExistingProgressAndAuthorizedUserAuthorized_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progress = BookProgressFactory.validProgress();
            var progressId = progress.getId();

            when(bookProgressRepository.findById(progressId))
                    .thenReturn(Optional.of(progress));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act
            bookProgressService.delete(progressId);

            // Arrange
            verify(bookProgressRepository, times(1)).findById(progressId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(progress.getUser().getKeycloakId(), BookProgressModificationForbiddenException.class), times(1));
        }
    }

}
