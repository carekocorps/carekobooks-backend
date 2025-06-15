package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookThreadRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        assertNull(result);
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
        assertNotNull(result);
        assertEquals(thread.getTitle(), result.getTitle());
        assertEquals(thread.getDescription(), result.getDescription());
        assertEquals(thread.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(thread.getBook().getId(), result.getBook().getId());
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
        assertNull(result);
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
        assertNotNull(result);
        assertEquals(thread.getTitle(), result.getTitle());
        assertEquals(thread.getDescription(), result.getDescription());
        assertEquals(thread.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(thread.getBook().getId(), result.getBook().getId());
        assertEquals(thread.getCreatedAt(), result.getCreatedAt());
        assertEquals(thread.getUpdatedAt(), result.getUpdatedAt());
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
        assertEquals(thread.getId(), newThread.getId());
        assertEquals(thread.getTitle(), newThread.getTitle());
        assertEquals(thread.getDescription(), newThread.getDescription());
        assertEquals(thread.getUser().getUsername(), newThread.getUser().getUsername());
        assertEquals(thread.getBook().getId(), newThread.getBook().getId());
        assertEquals(thread.getCreatedAt(), newThread.getCreatedAt());
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
        assertEquals(thread.getId(), newThread.getId());
        assertEquals(threadRequest.getTitle(), newThread.getTitle());
        assertEquals(threadRequest.getDescription(), newThread.getDescription());
        assertEquals(threadRequest.getUsername(), newThread.getUser().getUsername());
        assertEquals(threadRequest.getBookId(), newThread.getBook().getId());
        assertEquals(thread.getCreatedAt(), newThread.getCreatedAt());
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
        assertNull(result);
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
        assertNotNull(result);
        assertEquals(thread.getId(), result.getId());
        assertEquals(thread.getTitle(), result.getTitle());
        assertEquals(thread.getDescription(), result.getDescription());
        assertEquals(thread.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(thread.getBook().getId(), result.getBook().getId());
        assertEquals(thread.getCreatedAt(), result.getCreatedAt());
        assertEquals(thread.getUpdatedAt(), result.getUpdatedAt());
        verify(bookThreadRepository, times(1)).findById(thread.getId());
    }

}
