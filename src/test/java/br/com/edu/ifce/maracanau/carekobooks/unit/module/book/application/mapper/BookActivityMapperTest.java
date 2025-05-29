package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
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
    void toModel_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookProgressRequestFactory.validRequest();
        var activity = BookActivityFactory.validActivity(request);

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(activity.getUser());

        when(bookMapper.toModel(request.getBookId()))
                .thenReturn(activity.getBook());

        // Act
        var result = bookActivityMapper.toModel(request);

        // Assert
        assertEquals(activity.getStatus(), result.getStatus());
        assertEquals(activity.getPageCount(), result.getPageCount());
        assertEquals(activity.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(activity.getBook().getId(), result.getBook().getId());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookMapper, times(1)).toModel(request.getBookId());
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidResponse() {
        // Arrange
        var activity = BookActivityFactory.validActivity();

        when(userMapper.toResponse(activity.getUser()))
                .thenReturn(UserResponseFactory.validResponse(activity.getUser()));

        when(bookMapper.toResponse(activity.getBook()))
                .thenReturn(BookResponseFactory.validResponse(activity.getBook()));

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
        verify(userMapper, times(1)).toResponse(activity.getUser());
        verify(bookMapper, times(1)).toResponse(activity.getBook());
    }

}
