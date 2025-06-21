package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookThreadRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookThreadResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject.BookThreadNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookThreadService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookThreadValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookThreadServiceTest {

    @Mock
    private BookThreadRepository bookThreadRepository;

    @Mock
    private BookThreadValidator bookThreadValidator;

    @Mock
    private BookThreadMapper bookThreadMapper;

    @Mock
    private BookThreadNotificationSubject bookThreadNotificationSubject;

    @InjectMocks
    private BookThreadService bookThreadService;

    @Test
    void find_withNonExistingThread_shouldReturnEmpty() {
        // Arrange
        var id = 1L;

        when(bookThreadRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = bookThreadService.find(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookThreadRepository, times(1)).findById(id);
        verify(bookThreadMapper, never()).toResponse(any(BookThread.class));
    }

    @Test
    void find_withExistingThread_shouldReturnValidResponse() {
        // Arrange
        var thread = BookThreadFactory.validThread();
        var response = BookThreadResponseFactory.validResponse(thread);

        when(bookThreadRepository.findById(thread.getId()))
                .thenReturn(Optional.of(thread));

        when(bookThreadMapper.toResponse(thread))
                .thenReturn(response);

        // Act
        var result = bookThreadService.find(thread.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(response, result.get());
        verify(bookThreadRepository, times(1)).findById(thread.getId());
        verify(bookThreadMapper, times(1)).toResponse(thread);
    }

    @Test
    void create_withValidThreadAndUserIsUnauthorized_shouldFail() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookThreadRequestFactory.validRequest();
            var thread = BookThreadFactory.validThread(request);

            when(bookThreadMapper.toEntity(request))
                    .thenReturn(thread);

            doNothing()
                    .when(bookThreadValidator)
                    .validate(thread);

            when(bookThreadRepository.save(thread))
                    .thenReturn(thread);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class))
                    .thenThrow(BookThreadModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookThreadModificationForbiddenException.class, () -> bookThreadService.create(request));
            verify(bookThreadMapper, times(1)).toEntity(request);
            verify(bookThreadValidator, times(1)).validate(thread);
            verify(bookThreadRepository, times(1)).save(thread);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class), times(1));
            verify(bookThreadMapper, never()).toResponse(any(BookThread.class));
            verify(bookThreadNotificationSubject, never()).notify(any(BookThreadResponse.class));
        }
    }

    @Test
    void create_withValidThreadAndUserIsAuthorized_shouldReturnValidResponse() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookThreadRequestFactory.validRequest();
            var thread = BookThreadFactory.validThread(request);
            var response = BookThreadResponseFactory.validResponse(thread);

            when(bookThreadMapper.toEntity(request))
                    .thenReturn(thread);

            doNothing()
                    .when(bookThreadValidator)
                    .validate(thread);

            when(bookThreadRepository.save(thread))
                    .thenReturn(thread);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            when(bookThreadMapper.toResponse(thread))
                    .thenReturn(response);

            doNothing()
                    .when(bookThreadNotificationSubject)
                    .notify(response);

            // Act
            var result = bookThreadService.create(request);

            // Assert
            assertEquals(response, result);
            verify(bookThreadMapper, times(1)).toEntity(request);
            verify(bookThreadValidator, times(1)).validate(thread);
            verify(bookThreadRepository, times(1)).save(thread);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class), times(1));
            verify(bookThreadMapper, times(1)).toResponse(thread);
            verify(bookThreadNotificationSubject, times(1)).notify(response);
        }
    }

    @Test
    void update_withNonExistingThread_shouldFail() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var id = 1L;
            var request = BookThreadRequestFactory.validRequest();

            // Act && Assert
            assertThrows(BookThreadNotFoundException.class, () -> bookThreadService.update(id, request));
            verify(bookThreadRepository, times(1)).findById(id);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(bookThreadMapper, never()).updateEntity(any(BookThread.class), any(BookThreadRequest.class));
            verify(bookThreadValidator, never()).validate(any(BookThread.class));
            verify(bookThreadRepository, never()).save(any(BookThread.class));
            verify(bookThreadMapper, never()).toResponse(any(BookThread.class));
        }
    }

    @Test
    void update_withExistingThreadAndUserIsUnauthorized_shouldFail() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookThreadRequestFactory.validRequest();
            var thread = BookThreadFactory.validThread(request);
            var threadId = thread.getId();

            when(bookThreadRepository.findById(threadId))
                    .thenReturn(Optional.of(thread));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class))
                    .thenThrow(BookThreadModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookThreadModificationForbiddenException.class, () -> bookThreadService.update(threadId, request));
            verify(bookThreadRepository, times(1)).findById(threadId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class), times(1));
            verify(bookThreadMapper, never()).updateEntity(any(BookThread.class), any(BookThreadRequest.class));
            verify(bookThreadValidator, never()).validate(any(BookThread.class));
            verify(bookThreadRepository, never()).save(any(BookThread.class));
            verify(bookThreadMapper, never()).toResponse(any(BookThread.class));
        }
    }

    @Test
    void update_withExistingThreadAndUserIsAuthorized_shouldPass() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookThreadRequestFactory.validRequest();
            var thread = BookThreadFactory.validThread(request);
            var updatedThread = BookThreadFactory.updatedThread(thread, request);
            var updatedThreadResponse = BookThreadResponseFactory.validResponse(updatedThread);

            when(bookThreadRepository.findById(thread.getId()))
                    .thenReturn(Optional.of(thread));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(bookThreadMapper)
                    .updateEntity(thread, request);

            doNothing()
                    .when(bookThreadValidator)
                    .validate(thread);

            when(bookThreadRepository.save(thread))
                    .thenReturn(updatedThread);

            when(bookThreadMapper.toResponse(updatedThread))
                    .thenReturn(updatedThreadResponse);

            // Act
            var result = bookThreadService.update(thread.getId(), request);

            // Assert
            assertEquals(updatedThreadResponse, result);
            verify(bookThreadRepository, times(1)).findById(thread.getId());
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class), times(1));
            verify(bookThreadMapper, times(1)).updateEntity(thread, request);
            verify(bookThreadValidator, times(1)).validate(thread);
            verify(bookThreadRepository, times(1)).save(thread);
            verify(bookThreadMapper, times(1)).toResponse(updatedThread);
        }
    }

    @Test
    void delete_withExistingThreadAndUserIsUnauthorized_shouldFail() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var thread = BookThreadFactory.validThread();
            var threadId = thread.getId();

            when(bookThreadRepository.findById(threadId))
                    .thenReturn(Optional.of(thread));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class))
                    .thenThrow(BookThreadModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookThreadModificationForbiddenException.class, () -> bookThreadService.delete(threadId));
            verify(bookThreadRepository, times(1)).findById(threadId);
        }
    }

    @Test
    void delete_withExistingThreadAndUserIsAuthorized_shouldPass() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var thread = BookThreadFactory.validThread();
            var threadId = thread.getId();

            when(bookThreadRepository.findById(threadId))
                    .thenReturn(Optional.of(thread));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(thread.getUser().getKeycloakId(), BookThreadModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act && Assert
            assertDoesNotThrow(() -> bookThreadService.delete(threadId));
            verify(bookThreadRepository, times(1)).findById(threadId);
        }
    }

}
