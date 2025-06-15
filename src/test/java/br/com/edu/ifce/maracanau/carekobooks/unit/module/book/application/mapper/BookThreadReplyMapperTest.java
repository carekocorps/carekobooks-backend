package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookThreadReplyRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookThreadResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadReplyMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookThreadReplyMapperTest {

    @Mock
    private BookThreadMapper bookThreadMapper = Mappers.getMapper(BookThreadMapper.class);

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private BookThreadReplyMapper bookThreadReplyMapper = Mappers.getMapper(BookThreadReplyMapper.class);

    @Test
    void toEntity_withNullReplyRequest_shouldReturnNullReplyResponse() {
        // Arrange
        BookThreadReplyRequest replyRequest = null;

        // Act
        var result = bookThreadReplyMapper.toEntity(replyRequest);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookThreadMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void toEntity_withValidReplyRequest_shouldReturnReplyResponse() {
        // Arrange
        var replyRequest = BookThreadReplyRequestFactory.validRequest();
        var reply = BookThreadReplyFactory.validReply(replyRequest);

        when(userMapper.toEntity(replyRequest.getUsername()))
                .thenReturn(reply.getUser());

        when(bookThreadMapper.toEntity(replyRequest.getThreadId()))
                .thenReturn(reply.getThread());

        // Act
        var result = bookThreadReplyMapper.toEntity(replyRequest);

        // Assert
        assertNotNull(result);
        assertEquals(reply.getContent(), result.getContent());
        assertEquals(reply.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(reply.getThread().getId(), result.getThread().getId());
        assertEquals(reply.getIsContainingChildren(), result.getIsContainingChildren());
        verify(userMapper, times(1)).toEntity(replyRequest.getUsername());
        verify(bookThreadMapper, times(1)).toEntity(replyRequest.getThreadId());
    }

    @Test
    void toResponse_withNullReply_shouldReturnNullReplyResponse() {
        // Arrange
        BookThreadReply reply = null;

        // Act
        var result = bookThreadReplyMapper.toResponse(reply);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toSimplifiedResponse(any(User.class));
        verify(bookThreadMapper, never()).toResponse(any(BookThread.class));
    }

    @Test
    void toResponse_withValidReply_shouldReturnReplyResponse() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();

        when(userMapper.toSimplifiedResponse(reply.getUser()))
                .thenReturn(SimplifiedUserResponseFactory.validResponse(reply.getUser()));

        when(bookThreadMapper.toResponse(reply.getThread()))
                .thenReturn(BookThreadResponseFactory.validResponse(reply.getThread()));

        // Act
        var result = bookThreadReplyMapper.toResponse(reply);

        // Assert
        assertNotNull(result);
        assertEquals(reply.getId(), result.getId());
        assertEquals(reply.getContent(), result.getContent());
        assertEquals(reply.getUser().getId(), result.getUser().getId());
        assertEquals(reply.getThread().getId(), result.getThread().getId());
        assertEquals(reply.getIsContainingChildren(), result.getIsContainingChildren());
        assertEquals(reply.getCreatedAt(), result.getCreatedAt());
        assertEquals(reply.getUpdatedAt(), result.getUpdatedAt());
        verify(userMapper, times(1)).toSimplifiedResponse(reply.getUser());
        verify(bookThreadMapper, times(1)).toResponse(reply.getThread());
    }

    @Test
    void updateEntity_withValidReplyAndNullReplyRequest_shouldPreserveReply() {
        // Arrange
        BookThreadReplyRequest replyRequest = null;
        var reply = BookThreadReplyFactory.validReply();
        var newReply = SerializationUtils.clone(reply);

        // Act
        bookThreadReplyMapper.updateEntity(newReply, replyRequest);

        // Assert
        assertEquals(reply.getId(), newReply.getId());
        assertEquals(reply.getContent(), newReply.getContent());
        assertEquals(reply.getUser().getUsername(), newReply.getUser().getUsername());
        assertEquals(reply.getThread().getId(), newReply.getThread().getId());
        assertEquals(reply.getIsContainingChildren(), newReply.getIsContainingChildren());
        assertEquals(reply.getCreatedAt(), newReply.getCreatedAt());
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookThreadMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void updateEntity_withValidReplyAndValidReplyRequest_shouldUpdateReply() {
        // Arrange
        var replyRequest = BookThreadReplyRequestFactory.validRequest();
        var reply = BookThreadReplyFactory.validReply();
        var newReply = SerializationUtils.clone(reply);

        when(userMapper.toEntity(replyRequest.getUsername()))
                .thenReturn(UserFactory.validUser(replyRequest.getUsername()));

        when(bookThreadMapper.toEntity(replyRequest.getThreadId()))
                .thenReturn(BookThreadFactory.validThread(replyRequest.getThreadId()));

        // Act
        bookThreadReplyMapper.updateEntity(newReply, replyRequest);

        // Assert
        assertEquals(reply.getId(), newReply.getId());
        assertEquals(replyRequest.getContent(), newReply.getContent());
        assertEquals(replyRequest.getUsername(), newReply.getUser().getUsername());
        assertEquals(replyRequest.getThreadId(), newReply.getThread().getId());
        assertEquals(reply.getIsContainingChildren(), newReply.getIsContainingChildren());
        assertEquals(reply.getCreatedAt(), newReply.getCreatedAt());
        verify(userMapper, times(1)).toEntity(replyRequest.getUsername());
        verify(bookThreadMapper, times(1)).toEntity(replyRequest.getThreadId());
    }

}
