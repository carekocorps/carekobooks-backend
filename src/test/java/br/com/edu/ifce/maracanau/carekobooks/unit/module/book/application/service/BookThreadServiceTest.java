package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookThreadRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookThreadResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.thread.subject.BookThreadNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookThreadService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookThreadValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
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
    void create_withValidThread_shouldReturnValidResponse() {
        // Arrange
        var request = BookThreadRequestFactory.validRequest();
        var thread = BookThreadFactory.validThread(request);
        var response = BookThreadResponseFactory.validResponse(thread);

        when(bookThreadMapper.toModel(request))
                .thenReturn(thread);

        doNothing()
                .when(bookThreadValidator)
                .validate(thread);

        when(bookThreadRepository.save(thread))
                .thenReturn(thread);

        when(bookThreadMapper.toResponse(thread))
                .thenReturn(response);

        doNothing()
                .when(bookThreadNotificationSubject)
                .notify(response);

        // Act
        var result = bookThreadService.create(request);

        // Assert
        assertEquals(response, result);
        verify(bookThreadMapper, times(1)).toModel(request);
        verify(bookThreadValidator, times(1)).validate(thread);
        verify(bookThreadRepository, times(1)).save(thread);
        verify(bookThreadMapper, times(1)).toResponse(thread);
        verify(bookThreadNotificationSubject, times(1)).notify(response);
    }

    @Test
    void update_withNonExistingThread_shouldFail() {
        // Arrange
        var id = 1L;
        var request = BookThreadRequestFactory.validRequest();

        // Act && Assert
        assertThrows(BookThreadNotFoundException.class, () -> bookThreadService.update(id, request));
        verify(bookThreadRepository, times(1)).findById(id);
        verify(bookThreadMapper, never()).updateModel(any(BookThread.class), any(BookThreadRequest.class));
        verify(bookThreadValidator, never()).validate(any(BookThread.class));
        verify(bookThreadRepository, never()).save(any(BookThread.class));
        verify(bookThreadMapper, never()).toResponse(any(BookThread.class));
    }

    @Test
    void update_withExistingThread_shouldPass() {
        // Arrange
        var request = BookThreadRequestFactory.validRequest();
        var thread = BookThreadFactory.validThread(request);
        var updatedThread = BookThreadFactory.updatedThread(thread, request);
        var updatedThreadResponse = BookThreadResponseFactory.validResponse(updatedThread);

        when(bookThreadRepository.findById(thread.getId()))
                .thenReturn(Optional.of(thread));

        doNothing()
                .when(bookThreadMapper)
                .updateModel(thread, request);

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
        verify(bookThreadMapper, times(1)).updateModel(thread, request);
        verify(bookThreadValidator, times(1)).validate(thread);
        verify(bookThreadRepository, times(1)).save(thread);
        verify(bookThreadMapper, times(1)).toResponse(updatedThread);
    }

    @Test
    void delete_withExistingThreadAndUserIsUnauthorized_shouldFail() {
        // Arrange
        var thread = BookThreadFactory.validThread();
        var threadId = thread.getId();

        when(bookThreadRepository.findById(threadId))
                .thenReturn(Optional.of(thread));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(thread.getUser().getUsername()))
                    .thenReturn(true);

            assertThrows(BookThreadModificationForbiddenException.class, () -> bookThreadService.delete(threadId));
        }

        verify(bookThreadRepository, times(1)).findById(threadId);
    }

    @Test
    void delete_withExistingThreadAndUserIsAuthorized_shouldPass() {
        // Arrange
        var thread = BookThreadFactory.validThread();
        var threadId = thread.getId();

        when(bookThreadRepository.findById(threadId))
                .thenReturn(Optional.of(thread));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(thread.getUser().getUsername()))
                    .thenReturn(false);

            assertDoesNotThrow(() -> bookThreadService.delete(threadId));
        }

        verify(bookThreadRepository, times(1)).findById(threadId);
    }

}
