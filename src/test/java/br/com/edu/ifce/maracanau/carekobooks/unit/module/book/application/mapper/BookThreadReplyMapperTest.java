package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookThreadReplyRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookThreadResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response.simplified.SimplifiedUserResponseFactory;
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
    void toEntity_withNullRequest_shouldReturnNull() {
        // Arrange
        BookThreadReplyRequest request = null;

        // Act
        var result = bookThreadReplyMapper.toEntity(request);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookThreadMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void toEntity_withValidRequest_shouldReturnValidEntity() {
        // Arrange
        var request = BookThreadReplyRequestFactory.validRequest();
        var reply = BookThreadReplyFactory.validReply(request);

        when(userMapper.toEntity(request.getUsername()))
                .thenReturn(reply.getUser());

        when(bookThreadMapper.toEntity(request.getThreadId()))
                .thenReturn(reply.getThread());

        // Act
        var result = bookThreadReplyMapper.toEntity(request);

        // Assert
        assertEquals(reply.getContent(), result.getContent());
        assertEquals(reply.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(reply.getThread().getId(), result.getThread().getId());
        assertEquals(reply.getIsContainingChildren(), result.getIsContainingChildren());
        verify(userMapper, times(1)).toEntity(request.getUsername());
        verify(bookThreadMapper, times(1)).toEntity(request.getThreadId());
    }

    @Test
    void toResponse_withNullEntity_shouldReturnNull() {
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
    void toResponse_withValidEntity_shouldReturnValidResponse() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();

        when(userMapper.toSimplifiedResponse(reply.getUser()))
                .thenReturn(SimplifiedUserResponseFactory.validResponse(reply.getUser()));

        when(bookThreadMapper.toResponse(reply.getThread()))
                .thenReturn(BookThreadResponseFactory.validResponse(reply.getThread()));

        // Act
        var result = bookThreadReplyMapper.toResponse(reply);

        // Assert
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
    void updateEntity_withValidEntityAndNullRequest_shouldPreserveEntity() {
        // Arrange
        BookThreadReplyRequest request = null;
        var reply = BookThreadReplyFactory.validReply();
        var newReply = SerializationUtils.clone(reply);

        // Act
        bookThreadReplyMapper.updateEntity(newReply, request);

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
    void updateEntity_withValidEntityAndValidRequest_shouldUpdatedEntity() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();
        var newReply = SerializationUtils.clone(reply);
        var request = BookThreadReplyRequestFactory.validRequest();

        when(userMapper.toEntity(request.getUsername()))
                .thenReturn(UserFactory.validUser(request.getUsername()));

        when(bookThreadMapper.toEntity(request.getThreadId()))
                .thenReturn(BookThreadFactory.validThread(request.getThreadId()));

        // Act
        bookThreadReplyMapper.updateEntity(newReply, request);

        // Assert
        assertEquals(reply.getId(), newReply.getId());
        assertEquals(request.getContent(), newReply.getContent());
        assertEquals(request.getUsername(), newReply.getUser().getUsername());
        assertEquals(request.getThreadId(), newReply.getThread().getId());
        assertEquals(reply.getIsContainingChildren(), newReply.getIsContainingChildren());
        assertEquals(reply.getCreatedAt(), newReply.getCreatedAt());
        verify(userMapper, times(1)).toEntity(request.getUsername());
        verify(bookThreadMapper, times(1)).toEntity(request.getThreadId());
    }

}
