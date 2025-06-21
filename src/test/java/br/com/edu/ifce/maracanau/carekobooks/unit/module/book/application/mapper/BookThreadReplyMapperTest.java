package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookThreadReplyRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.BookThreadResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
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
        assertThat(result).isNull();
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
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(reply.getContent());
        assertThat(result.getUser().getUsername()).isEqualTo(reply.getUser().getUsername());
        assertThat(result.getThread().getId()).isEqualTo(reply.getThread().getId());
        assertThat(result.getIsContainingChildren()).isEqualTo(reply.getIsContainingChildren());
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
        assertThat(result).isNull();
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
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(reply.getId());
        assertThat(result.getContent()).isEqualTo(reply.getContent());
        assertThat(result.getUser().getId()).isEqualTo(reply.getUser().getId());
        assertThat(result.getThread().getId()).isEqualTo(reply.getThread().getId());
        assertThat(result.getIsContainingChildren()).isEqualTo(reply.getIsContainingChildren());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(reply.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(reply.getUpdatedAt());
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
        assertThat(newReply.getId()).isEqualTo(reply.getId());
        assertThat(newReply.getContent()).isEqualTo(reply.getContent());
        assertThat(newReply.getUser().getUsername()).isEqualTo(reply.getUser().getUsername());
        assertThat(newReply.getThread().getId()).isEqualTo(reply.getThread().getId());
        assertThat(newReply.getIsContainingChildren()).isEqualTo(reply.getIsContainingChildren());
        assertThat(newReply.getCreatedAt()).isEqualToIgnoringNanos(reply.getCreatedAt());
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
        assertThat(newReply.getId()).isEqualTo(reply.getId());
        assertThat(newReply.getContent()).isEqualTo(replyRequest.getContent());
        assertThat(newReply.getUser().getUsername()).isEqualTo(replyRequest.getUsername());
        assertThat(newReply.getThread().getId()).isEqualTo(replyRequest.getThreadId());
        assertThat(newReply.getIsContainingChildren()).isEqualTo(reply.getIsContainingChildren());
        assertThat(newReply.getCreatedAt()).isEqualToIgnoringNanos(reply.getCreatedAt());
        verify(userMapper, times(1)).toEntity(replyRequest.getUsername());
        verify(bookThreadMapper, times(1)).toEntity(replyRequest.getThreadId());
    }

}
