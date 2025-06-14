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
    void find_withNonExistingReply_shouldReturnEmptyReplyResponse() {
        // Arrange
        var replyId = Math.abs(new Random().nextLong()) + 1;

        when(bookThreadReplyRepository.findById(replyId))
                .thenReturn(Optional.empty());

        // Act
        var result = bookThreadReplyService.find(replyId);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookThreadReplyRepository, times(1)).findById(replyId);
        verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
    }

    @Test
    void find_withExistingReply_shouldReturnThreadResponse() {
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
    void create_withValidReplyRequestAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookThreadReplyRequestFactory.validRequest();
            var reply = BookThreadReplyFactory.validReply(request);

            when(bookThreadReplyMapper.toEntity(request))
                    .thenReturn(reply);

            doNothing()
                    .when(bookThreadReplyValidator)
                    .validate(reply);

            when(bookThreadReplyRepository.save(reply))
                    .thenReturn(reply);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class))
                    .thenThrow(BookThreadReplyModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookThreadReplyModificationForbiddenException.class, () -> bookThreadReplyService.create(request));
            verify(bookThreadReplyMapper, times(1)).toEntity(request);
            verify(bookThreadReplyValidator, times(1)).validate(reply);
            verify(bookThreadReplyRepository, times(1)).save(reply);
            mockedStatic.verify((() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class)), times(1));
            verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
        }
    }

    @Test
    void create_withValidReplyRequestAndAuthorizedUser_shouldReturnReplyResponse() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookThreadReplyRequestFactory.validRequest();
            var reply = BookThreadReplyFactory.validReply(request);
            var response = BookThreadReplyResponseFactory.validResponse(reply);

            when(bookThreadReplyMapper.toEntity(request))
                    .thenReturn(reply);

            doNothing()
                    .when(bookThreadReplyValidator)
                    .validate(reply);

            when(bookThreadReplyRepository.save(reply))
                    .thenReturn(reply);

            when(bookThreadReplyMapper.toResponse(reply))
                    .thenReturn(response);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(bookThreadReplyNotificationSubject)
                    .notify(response);

            // Act
            var result = bookThreadReplyService.create(request);

            // Assert
            assertNotNull(result);
            assertEquals(response, result);
            verify(bookThreadReplyMapper, times(1)).toEntity(request);
            verify(bookThreadReplyValidator, times(1)).validate(reply);
            verify(bookThreadReplyRepository, times(1)).save(reply);
            verify(bookThreadReplyMapper, times(1)).toResponse(reply);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class), times(1));
            verify(bookThreadReplyNotificationSubject, times(1)).notify(response);
        }
    }

    @Test
    void createChild_withNonExistingParent_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var parentId = Math.abs(new Random().nextLong()) + 1;
            var request = BookThreadReplyRequestFactory.validRequest();

            when(bookThreadReplyRepository.findById(parentId))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(BookThreadReplyNotFoundException.class, () -> bookThreadReplyService.createChild(parentId, request));
            verify(bookThreadReplyRepository, times(1)).findById(parentId);
            verify(bookThreadReplyMapper, never()).toEntity(any(BookThreadReplyRequest.class));
            verify(bookThreadReplyValidator, never()).validate(any(BookThreadReply.class));
            verify(bookThreadReplyRepository, never()).save(any(BookThreadReply.class));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
            verify(bookThreadReplyNotificationSubject, never()).notify(any(BookThreadReplyResponse.class));
        }
    }

    @Test
    void createChild_withExistingParentAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var parent = BookThreadReplyFactory.validReply();
            var parentId = parent.getId();

            var child = BookThreadReplyFactory.validReply();
            var childRequest = BookThreadReplyRequestFactory.validRequest(child);

            when(bookThreadReplyRepository.findById(parent.getId()))
                    .thenReturn(Optional.of(parent));

            when(bookThreadReplyMapper.toEntity(childRequest))
                    .thenReturn(child);

            doNothing()
                    .when(bookThreadReplyValidator)
                    .validate(child);

            when(bookThreadReplyRepository.save(child))
                    .thenReturn(child);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(child.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class))
                    .thenThrow(BookThreadReplyModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookThreadReplyModificationForbiddenException.class, () -> bookThreadReplyService.createChild(parentId, childRequest));
            verify(bookThreadReplyRepository, times(1)).findById(parentId);
            verify(bookThreadReplyMapper, times(1)).toEntity(childRequest);
            verify(bookThreadReplyValidator, times(1)).validate(child);
            verify(bookThreadReplyRepository, times(1)).save(child);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(child.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class),  times(1));
            verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
            verify(bookThreadReplyNotificationSubject, never()).notify(any(BookThreadReplyResponse.class));
        }
    }

    @Test
    void createChild_withExistingParentAndAuthorizedUser_shouldReturnReplyResponse() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var parent = BookThreadReplyFactory.validReply();
            var child = BookThreadReplyFactory.validReply();
            var childRequest = BookThreadReplyRequestFactory.validRequest(child);
            var childResponse = BookThreadReplyResponseFactory.validResponse(child);

            when(bookThreadReplyRepository.findById(parent.getId()))
                    .thenReturn(Optional.of(parent));

            when(bookThreadReplyMapper.toEntity(childRequest))
                    .thenReturn(child);

            doNothing()
                    .when(bookThreadReplyValidator)
                    .validate(child);

            when(bookThreadReplyRepository.save(child))
                    .thenReturn(child);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(child.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            when(bookThreadReplyMapper.toResponse(child))
                    .thenReturn(childResponse);

            doNothing()
                    .when(bookThreadReplyNotificationSubject)
                    .notify(childResponse);

            // Act
            var result = bookThreadReplyService.createChild(parent.getId(), childRequest);

            // Assert
            assertNotNull(result);
            assertEquals(childResponse, result);
            verify(bookThreadReplyRepository, times(1)).findById(parent.getId());
            verify(bookThreadReplyMapper, times(1)).toEntity(childRequest);
            verify(bookThreadReplyValidator, times(1)).validate(child);
            verify(bookThreadReplyRepository, times(1)).save(child);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(child.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class),  times(1));
            verify(bookThreadReplyMapper, times(1)).toResponse(child);
            verify(bookThreadReplyNotificationSubject, times(1)).notify(childResponse);
        }
    }

    @Test
    void update_withNonExistingReplyAndValidUpdateRequest_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var replyId = Math.abs(new Random().nextLong()) + 1;
            var updateRequest = BookThreadReplyRequestFactory.validRequest();

            // Act && Assert
            assertThrows(BookThreadReplyNotFoundException.class, () -> bookThreadReplyService.update(replyId, updateRequest));
            verify(bookThreadReplyRepository, times(1)).findById(replyId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(bookThreadReplyMapper, never()).updateEntity(any(BookThreadReply.class), any(BookThreadReplyRequest.class));
            verify(bookThreadReplyValidator, never()).validate(any(BookThreadReply.class));
            verify(bookThreadReplyRepository, never()).save(any(BookThreadReply.class));
            verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
        }
    }

    @Test
    void update_withExistingReplyAndValidUpdateRequestAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            var reply = BookThreadReplyFactory.validReply();
            var replyId = reply.getId();
            var updateRequest = BookThreadReplyRequestFactory.validRequest();

            when(bookThreadReplyRepository.findById(replyId))
                    .thenReturn(Optional.of(reply));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class))
                    .thenThrow(BookThreadReplyModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookThreadReplyModificationForbiddenException.class, () -> bookThreadReplyService.update(replyId, updateRequest));
            verify(bookThreadReplyRepository, times(1)).findById(replyId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class), times(1));
            verify(bookThreadReplyMapper, never()).updateEntity(any(BookThreadReply.class), any(BookThreadReplyRequest.class));
            verify(bookThreadReplyValidator, never()).validate(any(BookThreadReply.class));
            verify(bookThreadReplyRepository, never()).save(any(BookThreadReply.class));
            verify(bookThreadReplyMapper, never()).toResponse(any(BookThreadReply.class));
        }
    }

    @Test
    void update_withExistingReplyAndValidUpdateRequestAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            var reply = BookThreadReplyFactory.validReply();
            var updateRequest = BookThreadReplyRequestFactory.validRequest();

            var updatedReply = BookThreadReplyFactory.updatedReply(reply, updateRequest);
            var updatedReplyResponse = BookThreadReplyResponseFactory.validResponse(reply);

            when(bookThreadReplyRepository.findById(reply.getId()))
                    .thenReturn(Optional.of(reply));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(bookThreadReplyMapper)
                    .updateEntity(reply, updateRequest);

            doNothing()
                    .when(bookThreadReplyValidator)
                    .validate(reply);

            when(bookThreadReplyRepository.save(reply))
                    .thenReturn(updatedReply);

            when(bookThreadReplyMapper.toResponse(updatedReply))
                    .thenReturn(updatedReplyResponse);

            // Act
            var result = bookThreadReplyService.update(reply.getId(), updateRequest);

            // Assert
            assertNotNull(result);
            assertEquals(updatedReplyResponse, result);
            verify(bookThreadReplyRepository, times(1)).findById(reply.getId());
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class), times(1));
            verify(bookThreadReplyMapper, times(1)).updateEntity(reply, updateRequest);
            verify(bookThreadReplyValidator, times(1)).validate(reply);
            verify(bookThreadReplyRepository, times(1)).save(reply);
            verify(bookThreadReplyMapper, times(1)).toResponse(updatedReply);
        }
    }

    @Test
    void delete_withExistingReplyAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var reply = BookThreadReplyFactory.validReply();
            var replyId = reply.getId();

            when(bookThreadReplyRepository.findById(replyId))
                    .thenReturn(Optional.of(reply));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class))
                    .thenThrow(BookThreadReplyModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookThreadReplyModificationForbiddenException.class, () -> bookThreadReplyService.delete(replyId));
            verify(bookThreadReplyRepository, times(1)).findById(replyId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class), times(1));
        }
    }

    @Test
    void delete_withExistingReviewAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var reply = BookThreadReplyFactory.validReply();
            var replyId = reply.getId();

            when(bookThreadReplyRepository.findById(replyId))
                    .thenReturn(Optional.of(reply));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act && Assert
            assertDoesNotThrow(() -> bookThreadReplyService.delete(replyId));
            verify(bookThreadReplyRepository, times(1)).findById(replyId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(reply.getUser().getKeycloakId(), BookThreadReplyModificationForbiddenException.class), times(1));
        }
    }

}
