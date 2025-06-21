package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookThreadRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookThreadMapperTest {

    @Mock
    private BookThreadRepository bookThreadRepository;

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookThreadMapper bookThreadMapper = Mappers.getMapper(BookThreadMapper.class);

    @Test
    void toEntity_withNullThreadRequest_shouldReturnNullThread() {
        // Arrange
        BookThreadRequest threadRequest = null;

        // Act
        var result = bookThreadMapper.toEntity(threadRequest);

        // Assert
        assertThat(result).isNull();
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void toEntity_withValidThreadRequest_shouldReturnThread() {
        // Arrange
        var threadRequest = BookThreadRequestFactory.validRequest();
        var thread = BookThreadFactory.validThread(threadRequest);

        when(userMapper.toEntity(threadRequest.getUsername()))
                .thenReturn(thread.getUser());

        when(bookMapper.toEntity(threadRequest.getBookId()))
                .thenReturn(thread.getBook());

        // Act
        var result = bookThreadMapper.toEntity(threadRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(thread.getTitle());
        assertThat(result.getDescription()).isEqualTo(thread.getDescription());
        assertThat(result.getUser().getUsername()).isEqualTo(thread.getUser().getUsername());
        assertThat(result.getBook().getId()).isEqualTo(thread.getBook().getId());
        verify(userMapper, times(1)).toEntity(threadRequest.getUsername());
        verify(bookMapper, times(1)).toEntity(thread.getBook().getId());
    }

    @Test
    void toResponse_withNullThread_shouldReturnNullThreadResponse() {
        // Arrange
        BookThread thread = null;

        // Act
        var result = bookThreadMapper.toResponse(thread);

        // Assert
        assertThat(result).isNull();
        verify(userMapper, never()).toSimplifiedResponse(any(User.class));
        verify(bookMapper, never()).toSimplifiedResponse(any(Book.class));
    }

    @Test
    void toResponse_withValidThread_shouldReturnThreadResponse() {
        // Arrange
        var thread = BookThreadFactory.validThread();

        when(userMapper.toSimplifiedResponse(thread.getUser()))
                .thenReturn(SimplifiedUserResponseFactory.validResponse(thread.getUser()));

        when(bookMapper.toSimplifiedResponse(thread.getBook()))
                .thenReturn(SimplifiedBookResponseFactory.validResponse(thread.getBook()));

        // Act
        var result = bookThreadMapper.toResponse(thread);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(thread.getTitle());
        assertThat(result.getDescription()).isEqualTo(thread.getDescription());
        assertThat(result.getUser().getUsername()).isEqualTo(thread.getUser().getUsername());
        assertThat(result.getBook().getId()).isEqualTo(thread.getBook().getId());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(thread.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(thread.getUpdatedAt());
        verify(userMapper, times(1)).toSimplifiedResponse(thread.getUser());
        verify(bookMapper, times(1)).toSimplifiedResponse(thread.getBook());
    }

    @Test
    void updateEntity_withValidThreadAndNullThreadRequest_shouldPreserveThread() {
        // Arrange
        BookThreadRequest threadRequest = null;
        var thread = BookThreadFactory.validThread();
        var newThread = SerializationUtils.clone(thread);

        // Act
        bookThreadMapper.updateEntity(newThread, threadRequest);

        // Assert
        assertThat(newThread.getId()).isEqualTo(thread.getId());
        assertThat(newThread.getTitle()).isEqualTo(thread.getTitle());
        assertThat(newThread.getDescription()).isEqualTo(thread.getDescription());
        assertThat(newThread.getUser().getUsername()).isEqualTo(thread.getUser().getUsername());
        assertThat(newThread.getBook().getId()).isEqualTo(thread.getBook().getId());
        assertThat(newThread.getCreatedAt()).isEqualToIgnoringNanos(thread.getCreatedAt());
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void updateEntity_withValidThreadAndValidThreadRequest_shouldUpdateThread() {
        // Arrange
        var threadRequest = BookThreadRequestFactory.validRequest();
        var thread = BookThreadFactory.validThread();
        var newThread = SerializationUtils.clone(thread);

        when(userMapper.toEntity(threadRequest.getUsername()))
                .thenReturn(UserFactory.validUser(threadRequest.getUsername()));

        when(bookMapper.toEntity(threadRequest.getBookId()))
                .thenReturn(BookFactory.validBook(threadRequest.getBookId()));

        // Act
        bookThreadMapper.updateEntity(newThread, threadRequest);

        // Assert
        assertThat(newThread.getId()).isEqualTo(thread.getId());
        assertThat(newThread.getTitle()).isEqualTo(threadRequest.getTitle());
        assertThat(newThread.getDescription()).isEqualTo(threadRequest.getDescription());
        assertThat(newThread.getUser().getUsername()).isEqualTo(threadRequest.getUsername());
        assertThat(newThread.getBook().getId()).isEqualTo(threadRequest.getBookId());
        assertThat(newThread.getCreatedAt()).isEqualToIgnoringNanos(thread.getCreatedAt());
        verify(userMapper, times(1)).toEntity(threadRequest.getUsername());
        verify(bookMapper, times(1)).toEntity(threadRequest.getBookId());
    }

    @Test
    void toEntity_withNonExistingBookId_shouldReturnNullThread() {
        // Arrange
        var threadId = Math.abs(new Random().nextLong()) + 1;

        when(bookThreadRepository.findById(threadId))
                .thenReturn(Optional.empty());

        // Act
        var result = bookThreadMapper.toEntity(threadId);

        // Assert
        assertThat(result).isNull();
        verify(bookThreadRepository, times(1)).findById(threadId);
    }

    @Test
    void toEntity_withExistingBookId_shouldReturnValidThread() {
        // Arrange
        var thread = BookThreadFactory.validThread();

        when(bookThreadRepository.findById(thread.getId()))
                .thenReturn(Optional.of(thread));

        // Act
        var result = bookThreadMapper.toEntity(thread.getId());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(thread.getId());
        assertThat(result.getTitle()).isEqualTo(thread.getTitle());
        assertThat(result.getDescription()).isEqualTo(thread.getDescription());
        assertThat(result.getUser().getUsername()).isEqualTo(thread.getUser().getUsername());
        assertThat(result.getBook().getId()).isEqualTo(thread.getBook().getId());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(thread.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(thread.getUpdatedAt());
        verify(bookThreadRepository, times(1)).findById(thread.getId());
    }

}
