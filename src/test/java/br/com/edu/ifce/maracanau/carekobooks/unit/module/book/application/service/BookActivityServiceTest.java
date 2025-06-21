package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookActivityResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.subject.BookActivityNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookActivityService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookActivityValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.activity.BookActivityModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookActivityServiceTest {

    @Mock
    private BookActivityRepository bookActivityRepository;

    @Mock
    private BookActivityMapper bookActivityMapper;

    @Mock
    private BookActivityValidator bookActivityValidator;

    @Mock
    private BookActivityNotificationSubject bookActivityNotificationSubject;

    @InjectMocks
    private BookActivityService bookActivityService;

    @Test
    void find_withNonExistingActivity_shouldReturnEmptyActivityResponse() {
        // Arrange
        var activityId = Math.abs(new Random().nextLong()) + 1;

        when(bookActivityRepository.findById(activityId))
                .thenReturn(Optional.empty());

        // Act
        var result = bookActivityService.find(activityId);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookActivityRepository, times(1)).findById(activityId);
        verify(bookActivityMapper, never()).toResponse(any(BookActivity.class));
    }

    @Test
    void find_withExistingActivity_shouldReturnActivityResponse() {
        // Arrange
        var activity = BookActivityFactory.validActivity();
        var response = BookActivityResponseFactory.validResponse(activity);

        when(bookActivityRepository.findById(activity.getId()))
                .thenReturn(Optional.of(activity));

        when(bookActivityMapper.toResponse(activity))
                .thenReturn(response);

        // Act
        var result = bookActivityService.find(activity.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(response, result.get());
        verify(bookActivityRepository, times(1)).findById(activity.getId());
        verify(bookActivityMapper, times(1)).toResponse(activity);
    }

    @Test
    void create_withValidProgressRequestAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookProgressRequestFactory.validRequest();
            var activity = BookActivityFactory.validActivity();

            when(bookActivityMapper.toEntity(request))
                    .thenReturn(activity);

            doNothing()
                    .when(bookActivityValidator)
                    .validate(activity);

            when(bookActivityRepository.save(activity))
                    .thenReturn(activity);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(activity.getUser().getKeycloakId(), BookActivityModificationForbiddenException.class))
                    .thenThrow(BookActivityModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookActivityModificationForbiddenException.class, () -> bookActivityService.create(request));
            verify(bookActivityMapper, times(1)).toEntity(request);
            verify(bookActivityValidator, times(1)).validate(activity);
            verify(bookActivityRepository, times(1)).save(activity);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(activity.getUser().getKeycloakId(), BookActivityModificationForbiddenException.class), times(1));
            verify(bookActivityMapper, never()).toResponse(any(BookActivity.class));
            verify(bookActivityNotificationSubject, never()).notify(any(BookActivityResponse.class));
        }
    }

    @Test
    void create_withValidProgressRequestAndAuthorizedUser_shouldReturnActivityResponse() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookProgressRequestFactory.validRequest();
            var activity = BookActivityFactory.validActivity(request);
            var response = BookActivityResponseFactory.validResponse(activity);

            when(bookActivityMapper.toEntity(request))
                    .thenReturn(activity);

            doNothing()
                    .when(bookActivityValidator)
                    .validate(activity);

            when(bookActivityRepository.save(activity))
                    .thenReturn(activity);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(activity.getUser().getKeycloakId(), BookActivityModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            when(bookActivityMapper.toResponse(activity))
                    .thenReturn(response);

            doNothing()
                    .when(bookActivityNotificationSubject)
                    .notify(response);

            // Act
            var result = bookActivityService.create(request);

            // Assert
            assertEquals(response, result);
            verify(bookActivityMapper, times(1)).toEntity(request);
            verify(bookActivityValidator, times(1)).validate(activity);
            verify(bookActivityRepository, times(1)).save(activity);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(activity.getUser().getKeycloakId(), BookActivityModificationForbiddenException.class), times(1));
            verify(bookActivityMapper, times(1)).toResponse(activity);
            verify(bookActivityNotificationSubject, times(1)).notify(response);
        }
    }

    @Test
    void delete_withExistingActivityAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var activity = BookActivityFactory.validActivity();
            var activityId = activity.getId();

            when(bookActivityRepository.findById(activityId))
                    .thenReturn(Optional.of(activity));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(activity.getUser().getKeycloakId(), BookActivityModificationForbiddenException.class))
                    .thenThrow(BookActivityModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookActivityModificationForbiddenException.class, () -> bookActivityService.delete(activityId));
            verify(bookActivityRepository, times(1)).findById(activityId);
        }
    }

    @Test
    void delete_withExistingActivityAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var activity = BookActivityFactory.validActivity();
            var activityId = activity.getId();

            when(bookActivityRepository.findById(activityId))
                    .thenReturn(Optional.of(activity));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(activity.getUser().getKeycloakId(), BookActivityModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act && Assert
            assertDoesNotThrow(() -> bookActivityService.delete(activityId));
            verify(bookActivityRepository, times(1)).findById(activityId);
        }
    }

}
