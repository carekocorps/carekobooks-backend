package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
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
    void toEntity_withNullRequest_shouldReturnNull() {
        // Arrange
        BookProgressRequest request = null;

        // Act
        var result = bookActivityMapper.toEntity(request);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookProgressRequestFactory.validRequest();
        var activity = BookActivityFactory.validActivity(request);

        when(userMapper.toEntity(request.getUsername()))
                .thenReturn(activity.getUser());

        when(bookMapper.toEntity(request.getBookId()))
                .thenReturn(activity.getBook());

        // Act
        var result = bookActivityMapper.toEntity(request);

        // Assert
        assertEquals(activity.getStatus(), result.getStatus());
        assertEquals(activity.getPageCount(), result.getPageCount());
        assertEquals(activity.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(activity.getBook().getId(), result.getBook().getId());
        verify(userMapper, times(1)).toEntity(request.getUsername());
        verify(bookMapper, times(1)).toEntity(request.getBookId());
    }

    @Test
    void toResponse_withNullEntity_shouldReturnNull() {
        // Arrange
        BookActivity activity = null;

        // Act
        var result = bookActivityMapper.toResponse(activity);

        // Assert
        assertNull(result);
    }

    @Test
    void toResponse_withValidEntity_shouldReturnValidResponse() {
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
