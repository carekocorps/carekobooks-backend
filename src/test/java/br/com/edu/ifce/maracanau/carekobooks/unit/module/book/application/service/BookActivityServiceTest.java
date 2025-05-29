package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookActivityResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.activity.subject.BookActivityNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookActivityService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookActivityValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.activity.BookActivityModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
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
    void find_withNonExistingActivity_shouldReturnEmpty() {
        // Arrange
        var id = 1L;

        when(bookActivityRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = bookActivityService.find(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookActivityRepository, times(1)).findById(id);
        verify(bookActivityMapper, never()).toResponse(any(BookActivity.class));
    }

    @Test
    void find_withExistingActivity_shouldReturnBookActivity() {
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
    void create_withValidActivity_shouldReturnValidResponse() {
        // Arrange
        var request = BookProgressRequestFactory.validRequest();
        var activity = BookActivityFactory.validActivity();
        var response = BookActivityResponseFactory.validResponse(activity);

        when(bookActivityMapper.toModel(request))
                .thenReturn(activity);

        doNothing()
                .when(bookActivityValidator)
                .validate(activity);

        when(bookActivityRepository.save(activity))
                .thenReturn(activity);

        when(bookActivityMapper.toResponse(activity))
                .thenReturn(response);

        doNothing()
                .when(bookActivityNotificationSubject)
                .notify(response);

        // Act
        var result = bookActivityService.create(request);

        // Assert
        assertEquals(response, result);
        verify(bookActivityMapper, times(1)).toModel(request);
        verify(bookActivityValidator, times(1)).validate(activity);
        verify(bookActivityRepository, times(1)).save(activity);
        verify(bookActivityMapper, times(1)).toResponse(activity);
        verify(bookActivityNotificationSubject, times(1)).notify(response);
    }

    @Test
    void delete_withExistingActivityAndUserIsUnauthorized_shouldFail() {
        // Arrange
        var activity = BookActivityFactory.validActivity();
        var activityId = activity.getId();

        when(bookActivityRepository.findById(activityId))
                .thenReturn(Optional.of(activity));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(activity.getUser().getUsername()))
                    .thenReturn(true);

            assertThrows(BookActivityModificationForbiddenException.class, () -> bookActivityService.delete(activityId));
        }

        verify(bookActivityRepository, times(1)).findById(activityId);
    }

    @Test
    void delete_withExistingReviewAndUserIsAuthorized_shouldPass() {
        // Arrange
        var activity = BookActivityFactory.validActivity();
        var activityId = activity.getId();

        when(bookActivityRepository.findById(activityId))
                .thenReturn(Optional.of(activity));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(activity.getUser().getUsername()))
                    .thenReturn(false);

            assertDoesNotThrow(() -> bookActivityService.delete(activityId));
        }

        verify(bookActivityRepository, times(1)).findById(activityId);
    }

}
