package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookThreadRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookThreadMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    void toModel_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookThreadRequestFactory.validRequest();
        var thread = BookThreadFactory.validThread(request);

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(thread.getUser());

        when(bookMapper.toModel(request.getBookId()))
                .thenReturn(thread.getBook());

        // Act
        var result = bookThreadMapper.toModel(request);

        // Assert
        assertEquals(thread.getTitle(), result.getTitle());
        assertEquals(thread.getDescription(), result.getDescription());
        assertEquals(thread.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(thread.getBook().getId(), result.getBook().getId());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookMapper, times(1)).toModel(thread.getBook().getId());
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidResponse() {
        // Arrange
        var thread = BookThreadFactory.validThread();

        when(userMapper.toResponse(thread.getUser()))
                .thenReturn(UserResponseFactory.validResponse(thread.getUser()));

        when(bookMapper.toResponse(thread.getBook()))
                .thenReturn(BookResponseFactory.validResponse(thread.getBook()));

        // Act
        var result = bookThreadMapper.toResponse(thread);

        // Assert
        assertEquals(thread.getTitle(), result.getTitle());
        assertEquals(thread.getDescription(), result.getDescription());
        assertEquals(thread.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(thread.getBook().getId(), result.getBook().getId());
        assertEquals(thread.getCreatedAt(), result.getCreatedAt());
        assertEquals(thread.getUpdatedAt(), result.getUpdatedAt());
        verify(userMapper, times(1)).toResponse(thread.getUser());
        verify(bookMapper, times(1)).toResponse(thread.getBook());
    }

    @Test
    void updateModel_WithValidModelAndValidRequest_shouldUpdateModel() {
        // Arrange
        var thread = BookThreadFactory.validThread();
        var newThread = SerializationUtils.clone(thread);
        var request = BookThreadRequestFactory.validRequest();

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(UserFactory.validUser(request.getUsername()));

        when(bookMapper.toModel(request.getBookId()))
                .thenReturn(BookFactory.validBook(request.getBookId()));

        // Act
        bookThreadMapper.updateModel(newThread, request);

        // Assert
        assertEquals(thread.getId(), newThread.getId());
        assertEquals(request.getTitle(), newThread.getTitle());
        assertEquals(request.getDescription(), newThread.getDescription());
        assertEquals(request.getUsername(), newThread.getUser().getUsername());
        assertEquals(request.getBookId(), newThread.getBook().getId());
        assertEquals(thread.getCreatedAt(), newThread.getCreatedAt());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookMapper, times(1)).toModel(request.getBookId());
    }

    @Test
    void toModel_withNonExistingBookId_shouldReturnNull() {
        // Arrange
        var threadId = 1L;

        when(bookThreadRepository.findById(threadId))
                .thenReturn(Optional.empty());

        // Act
        var result = bookThreadMapper.toModel(threadId);

        // Assert
        assertNull(result);
        verify(bookThreadRepository, times(1)).findById(threadId);
    }

    @Test
    void toModel_withExistingBookId_shouldReturnValidModel() {
        // Arrange
        var thread = BookThreadFactory.validThread();

        when(bookThreadRepository.findById(thread.getId()))
                .thenReturn(Optional.of(thread));

        // Act
        var result = bookThreadMapper.toModel(thread.getId());

        // Assert
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
