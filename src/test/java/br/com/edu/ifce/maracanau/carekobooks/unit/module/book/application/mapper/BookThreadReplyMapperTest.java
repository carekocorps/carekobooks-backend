package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookThreadReplyRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookThreadResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadReplyMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
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
    void toModel_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookThreadReplyRequestFactory.validRequest();
        var reply = BookThreadReplyFactory.validReply(request);

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(reply.getUser());

        when(bookThreadMapper.toModel(request.getThreadId()))
                .thenReturn(reply.getThread());

        // Act
        var result = bookThreadReplyMapper.toModel(request);

        // Assert
        assertEquals(reply.getContent(), result.getContent());
        assertEquals(reply.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(reply.getThread().getId(), result.getThread().getId());
        assertEquals(reply.getIsContainingChildren(), result.getIsContainingChildren());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookThreadMapper, times(1)).toModel(request.getThreadId());
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidResponse() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();

        when(userMapper.toResponse(reply.getUser()))
                .thenReturn(UserResponseFactory.validResponse(reply.getUser()));

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
        verify(userMapper, times(1)).toResponse(reply.getUser());
        verify(bookThreadMapper, times(1)).toResponse(reply.getThread());
    }

    @Test
    void updateModel_withValidModelAndValiRequest_shouldUpdatedModel() {
        // Arrange
        var reply = BookThreadReplyFactory.validReply();
        var newReply = SerializationUtils.clone(reply);
        var request = BookThreadReplyRequestFactory.validRequest();

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(UserFactory.validUser(request.getUsername()));

        when(bookThreadMapper.toModel(request.getThreadId()))
                .thenReturn(BookThreadFactory.validThread(request.getThreadId()));

        // Act
        bookThreadReplyMapper.updateModel(newReply, request);

        // Assert
        assertEquals(reply.getId(), newReply.getId());
        assertEquals(request.getContent(), newReply.getContent());
        assertEquals(request.getUsername(), newReply.getUser().getUsername());
        assertEquals(request.getThreadId(), newReply.getThread().getId());
        assertEquals(reply.getIsContainingChildren(), newReply.getIsContainingChildren());
        assertEquals(reply.getCreatedAt(), newReply.getCreatedAt());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookThreadMapper, times(1)).toModel(request.getThreadId());
    }

}
