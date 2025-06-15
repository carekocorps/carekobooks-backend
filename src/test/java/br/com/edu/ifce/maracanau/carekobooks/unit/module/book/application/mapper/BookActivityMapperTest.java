package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookActivityMapperTest {

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private BookActivityMapper bookActivityMapper = Mappers.getMapper(BookActivityMapper.class);

    @Test
    void toEntity_withNullActivityRequest_shouldReturnNullActivity() {
        // Arrange
        BookProgressRequest progressRequest = null;

        // Act
        var result = bookActivityMapper.toEntity(progressRequest);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void toEntity_withValidActivityRequest_shouldReturnActivity() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.validRequest();
        var activity = BookActivityFactory.validActivity(progressRequest);

        when(userMapper.toEntity(progressRequest.getUsername()))
                .thenReturn(activity.getUser());

        when(bookMapper.toEntity(progressRequest.getBookId()))
                .thenReturn(activity.getBook());

        // Act
        var result = bookActivityMapper.toEntity(progressRequest);

        // Assert
        assertEquals(activity.getStatus(), result.getStatus());
        assertEquals(activity.getPageCount(), result.getPageCount());
        assertEquals(activity.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(activity.getBook().getId(), result.getBook().getId());
        verify(userMapper, times(1)).toEntity(progressRequest.getUsername());
        verify(bookMapper, times(1)).toEntity(progressRequest.getBookId());
    }

    @Test
    void toResponse_withNullActivity_shouldReturnNullActivityResponse() {
        // Arrange
        BookActivity activity = null;

        // Act
        var result = bookActivityMapper.toResponse(activity);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toSimplifiedResponse(any(User.class));
        verify(bookMapper, never()).toSimplifiedResponse(any(Book.class));
    }

    @Test
    void toResponse_withValidActivity_shouldReturnActivityResponse() {
        // Arrange
        var activity = BookActivityFactory.validActivity();

        when(userMapper.toSimplifiedResponse(activity.getUser()))
                .thenReturn(SimplifiedUserResponseFactory.validResponse(activity.getUser()));

        when(bookMapper.toSimplifiedResponse(activity.getBook()))
                .thenReturn(SimplifiedBookResponseFactory.validResponse(activity.getBook()));

        // Act
        var result = bookActivityMapper.toResponse(activity);

        // Assert
        assertEquals(activity.getId(), result.getId());
        assertEquals(activity.getStatus(), result.getStatus());
        assertEquals(activity.getPageCount(), result.getPageCount());
        assertEquals(activity.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(activity.getBook().getId(), result.getBook().getId());
        assertEquals(activity.getCreatedAt(), result.getCreatedAt());
        assertEquals(activity.getUpdatedAt(), result.getUpdatedAt());
        verify(userMapper, times(1)).toSimplifiedResponse(activity.getUser());
        verify(bookMapper, times(1)).toSimplifiedResponse(activity.getBook());
    }

}
