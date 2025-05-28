package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookThreadReplyRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookThreadReplyResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadReplyMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.notification.thread.reply.subject.BookThreadReplyNotificationSubject;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookThreadReplyService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookThreadReplyValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.reply.BookThreadReplyModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.reply.BookThreadReplyNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadReplyRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookThreadReplyServiceTest {

    @Mock
    private BookThreadReplyRepository bookThreadReplyRepository;

    @Mock
    private BookThreadReplyValidator bookThreadReplyValidator;

    @Mock
    private BookThreadReplyMapper bookThreadReplyMapper;

    @Mock
    private BookThreadReplyNotificationSubject bookThreadReplyNotificationSubject;

    @InjectMocks
    private BookThreadReplyService bookThreadReplyService;

    @Test
    void find_withNonExistingReply_shouldReturnEmpty() {
        // Arrange
        var id = 1L;

        when(bookThreadReplyRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = bookThreadReplyService.find(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookThreadReplyRepository, times(1)).findById(id);
        verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
    }

    @Test
    void find_withExistingReply_shouldReturnValidResponse() {
        // Arrange
        var thread = BookThreadReplyFactory.validReply();
        var response = BookThreadReplyResponseFactory.validResponse(thread);

        when(bookThreadReplyRepository.findById(thread.getId()))
                .thenReturn(Optional.of(thread));

        when(bookThreadReplyMapper.toResponse(thread))
                .thenReturn(response);

        // Act
        var result = bookThreadReplyService.find(thread.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(response, result.get());
        verify(bookThreadReplyRepository, times(1)).findById(thread.getId());
        verify(bookThreadReplyMapper, times(1)).toResponse(thread);
    }

    @Test
    void create_withValidReply_shouldReturnValidResponse() {
        // Arrange
        var request = BookThreadReplyRequestFactory.validRequest();
        var reply = BookThreadReplyFactory.validReply(request);
        var response = BookThreadReplyResponseFactory.validResponse(reply);

        when(bookThreadReplyMapper.toModel(request))
                .thenReturn(reply);

        doNothing()
                .when(bookThreadReplyValidator)
                .validate(reply);

        when(bookThreadReplyRepository.save(reply))
                .thenReturn(reply);

        when(bookThreadReplyMapper.toResponse(reply))
                .thenReturn(response);

        doNothing()
                .when(bookThreadReplyNotificationSubject)
                .notify(response);

        // Act
        var result = bookThreadReplyService.create(request);

        // Assert
        assertEquals(response, result);
        verify(bookThreadReplyMapper, times(1)).toModel(request);
        verify(bookThreadReplyValidator, times(1)).validate(reply);
        verify(bookThreadReplyRepository, times(1)).save(reply);
        verify(bookThreadReplyMapper, times(1)).toResponse(reply);
        verify(bookThreadReplyNotificationSubject, times(1)).notify(response);
    }

    @Test
    void update_withNonExistingReply_shouldFail() {
        // Arrange
        var id = 1L;
        var request = BookThreadReplyRequestFactory.validRequest();

        // Act && Assert
        assertThrows(BookThreadReplyNotFoundException.class, () -> bookThreadReplyService.update(id, request));
        verify(bookThreadReplyRepository, times(1)).findById(id);
        verify(bookThreadReplyMapper, never()).updateModel(any(BookThreadReply.class), any(BookThreadReplyRequest.class));
        verify(bookThreadReplyValidator, never()).validate(any(BookThreadReply.class));
        verify(bookThreadReplyRepository, never()).save(any(BookThreadReply.class));
        verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
    }

    @Test
    void update_withExistingReply_shouldPass() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();
        var request = BookThreadReplyRequestFactory.validRequest();
        var updatedReply = BookThreadReplyFactory.updatedReply(reply, request);
        var updatedReplyResponse = BookThreadReplyResponseFactory.validResponse(reply);

        when(bookThreadReplyRepository.findById(reply.getId()))
                .thenReturn(Optional.of(reply));

        doNothing()
                .when(bookThreadReplyMapper)
                .updateModel(reply, request);

        doNothing()
                .when(bookThreadReplyValidator)
                .validate(reply);

        when(bookThreadReplyRepository.save(reply))
                .thenReturn(updatedReply);

        when(bookThreadReplyMapper.toResponse(updatedReply))
                .thenReturn(updatedReplyResponse);

        // Act
        var result = bookThreadReplyService.update(reply.getId(), request);

        // Assert
        assertEquals(updatedReplyResponse, result);
        verify(bookThreadReplyRepository, times(1)).findById(reply.getId());
        verify(bookThreadReplyMapper, times(1)).updateModel(reply, request);
        verify(bookThreadReplyValidator, times(1)).validate(reply);
        verify(bookThreadReplyRepository, times(1)).save(reply);
        verify(bookThreadReplyMapper, times(1)).toResponse(updatedReply);
    }

    @Test
    void createChild_withNonExistingParent_shouldFail() {
        // Arrange
        var id = 1L;
        var request = BookThreadReplyRequestFactory.validRequest();

        when(bookThreadReplyRepository.findById(id))
            .thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(BookThreadReplyNotFoundException.class, () -> bookThreadReplyService.createChild(id, request));
        verify(bookThreadReplyRepository, times(1)).findById(id);
        verify(bookThreadReplyMapper, never()).toModel(any(BookThreadReplyRequest.class));
        verify(bookThreadReplyValidator, never()).validate(any(BookThreadReply.class));
        verify(bookThreadReplyRepository, never()).save(any(BookThreadReply.class));
        verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
        verify(bookThreadReplyNotificationSubject, never()).notify(any(BookThreadReplyResponse.class));
    }

    @Test
    void createChild_withExistingParent_shouldReturnValidResponse() {
        // Arrange
        var parent = BookThreadReplyFactory.validReply();
        var child = BookThreadReplyFactory.validReply();
        var childRequest = BookThreadReplyRequestFactory.validRequest(child);
        var childResponse = BookThreadReplyResponseFactory.validResponse(child);

        when(bookThreadReplyRepository.findById(parent.getId()))
                .thenReturn(Optional.of(parent));

        when(bookThreadReplyMapper.toModel(childRequest))
                .thenReturn(child);

        doNothing()
                .when(bookThreadReplyValidator)
                .validate(child);

        when(bookThreadReplyRepository.save(child))
                .thenReturn(child);

        when(bookThreadReplyMapper.toResponse(child))
                .thenReturn(childResponse);

        doNothing()
                .when(bookThreadReplyNotificationSubject)
                .notify(childResponse);

        // Act
        var result = bookThreadReplyService.createChild(parent.getId(), childRequest);

        // Assert
        assertEquals(childResponse, result);
        verify(bookThreadReplyRepository, times(1)).findById(parent.getId());
        verify(bookThreadReplyMapper, times(1)).toModel(childRequest);
        verify(bookThreadReplyValidator, times(1)).validate(child);
        verify(bookThreadReplyRepository, times(1)).save(child);
        verify(bookThreadReplyMapper, times(1)).toResponse(child);
        verify(bookThreadReplyNotificationSubject, times(1)).notify(childResponse);
    }

    @Test
    void delete_withExistingReplyAndUserIsUnauthorized_shouldFail() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();
        var replyId = reply.getId();

        when(bookThreadReplyRepository.findById(replyId))
                .thenReturn(Optional.of(reply));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(reply.getUser().getUsername()))
                    .thenReturn(true);

            assertThrows(BookThreadReplyModificationForbiddenException.class, () -> bookThreadReplyService.delete(replyId));
        }

        verify(bookThreadReplyRepository, times(1)).findById(replyId);
    }

    @Test
    void delete_withExistingReviewAndUserIsAuthorized_shouldPass() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();
        var replyId = reply.getId();

        when(bookThreadReplyRepository.findById(replyId))
                .thenReturn(Optional.of(reply));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(reply.getUser().getUsername()))
                    .thenReturn(false);

            assertDoesNotThrow(() -> bookThreadReplyService.delete(replyId));
        }

        verify(bookThreadReplyRepository, times(1)).findById(replyId);
    }

}
