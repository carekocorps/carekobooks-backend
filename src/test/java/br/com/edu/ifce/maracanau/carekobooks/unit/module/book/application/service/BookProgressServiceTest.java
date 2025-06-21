package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookProgressResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
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
        assertThat(result).isEmpty();
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
        assertThat(result)
                .isPresent()
                .contains(response);

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
            assertThatThrownBy(() -> bookProgressService.create(request)).isInstanceOf(BookProgressModificationForbiddenException.class);
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
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(response);

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
            assertThatThrownBy(() -> bookProgressService.update(activityId, updateRequest)).isInstanceOf(BookProgressNotFoundException.class);
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
            assertThatThrownBy(() -> bookProgressService.update(progressId, updateRequest)).isInstanceOf(BookProgressModificationForbiddenException.class);
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
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(updatedProgressResponse);

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
    void changeAsFavorite_withNonExistingProgress_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progressId = Math.abs(new Random().nextLong()) + 1;
            var isFavorite = new Random().nextBoolean();

            when(bookProgressRepository.findById(progressId))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThatThrownBy(() -> bookProgressService.changeAsFavorite(progressId, isFavorite)).isInstanceOf(BookProgressNotFoundException.class);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(bookProgressRepository, never()).changeAsFavoriteById(any(Long.class), any(Boolean.class));
        }
    }

    @Test
    void changeAsFavorite_withExistingProgressAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progress = BookProgressFactory.validProgress();
            var progressId = progress.getId();

            var userKeycloakId = progress.getUser().getKeycloakId();
            var isFavorite = new Random().nextBoolean();

            when(bookProgressRepository.findById(progressId))
                    .thenReturn(Optional.of(progress));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(userKeycloakId, BookProgressModificationForbiddenException.class))
                    .thenThrow(BookProgressModificationForbiddenException.class);

            // Act && Assert
            assertThatThrownBy(() -> bookProgressService.changeAsFavorite(progressId, isFavorite)).isInstanceOf(BookProgressModificationForbiddenException.class);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(userKeycloakId, BookProgressModificationForbiddenException.class), times(1));
            verify(bookProgressRepository, never()).changeAsFavoriteById(any(Long.class), any(Boolean.class));
        }
    }

    @Test
    void changeAsFavorite_withExistingProgressAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progress = BookProgressFactory.validProgress();
            var progressId = progress.getId();

            var userKeycloakId = progress.getUser().getKeycloakId();
            var isFavorite = new Random().nextBoolean();

            when(bookProgressRepository.findById(progressId))
                    .thenReturn(Optional.of(progress));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(userKeycloakId, BookProgressModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(bookProgressRepository)
                    .changeAsFavoriteById(progressId, isFavorite);

            // Act && Assert
            assertThatCode(() -> bookProgressService.changeAsFavorite(progressId, isFavorite)).doesNotThrowAnyException();
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(userKeycloakId, BookProgressModificationForbiddenException.class), times(1));
            verify(bookProgressRepository, times(1)).changeAsFavoriteById(progressId, isFavorite);
        }
    }

    @Test
    void delete_withNonExistingProgress_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var progressId = Math.abs(new Random().nextLong()) + 1;

            when(bookProgressRepository.findById(progressId))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThatThrownBy(() -> bookProgressService.delete(progressId)).isInstanceOf(BookProgressNotFoundException.class);
            verify(bookProgressRepository, times(1)).findById(progressId);
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
            assertThatThrownBy(() -> bookProgressService.delete(progressId)).isInstanceOf(BookProgressModificationForbiddenException.class);
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
