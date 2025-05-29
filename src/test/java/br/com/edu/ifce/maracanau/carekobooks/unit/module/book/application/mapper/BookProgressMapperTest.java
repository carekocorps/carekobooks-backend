package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
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
class BookProgressMapperTest {

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookProgressMapper bookProgressMapper = Mappers.getMapper(BookProgressMapper.class);

    @Test
    void toModel_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookProgressRequestFactory.validRequest();
        var progress = BookProgressFactory.validProgress(request);

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(progress.getUser());

        when(bookMapper.toModel(request.getBookId()))
                .thenReturn(progress.getBook());

        // Act
        var result = bookProgressMapper.toModel(request);

        // Assert
        assertEquals(progress.getStatus(), result.getStatus());
        assertEquals(progress.getIsFavorite(), result.getIsFavorite());
        assertEquals(progress.getScore(), result.getScore());
        assertEquals(progress.getPageCount(), result.getPageCount());
        assertEquals(progress.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(progress.getBook().getId(), result.getBook().getId());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookMapper, times(1)).toModel(request.getBookId());
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidResponse() {
        // Arrange
        var progress = BookProgressFactory.validProgress();

        when(userMapper.toResponse(progress.getUser()))
                .thenReturn(UserResponseFactory.validResponse(progress.getUser()));

        when(bookMapper.toResponse(progress.getBook()))
                .thenReturn(BookResponseFactory.validResponse(progress.getBook()));

        // Act
        var result = bookProgressMapper.toResponse(progress);

        // Assert
        assertEquals(progress.getId(), result.getId());
        assertEquals(progress.getStatus(), result.getStatus());
        assertEquals(progress.getIsFavorite(), result.getIsFavorite());
        assertEquals(progress.getScore(), result.getScore());
        assertEquals(progress.getPageCount(), result.getPageCount());
        assertEquals(progress.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(progress.getBook().getId(), result.getBook().getId());
        assertEquals(progress.getCreatedAt(), result.getCreatedAt());
        assertEquals(progress.getUpdatedAt(), result.getUpdatedAt());
        verify(userMapper, times(1)).toResponse(progress.getUser());
        verify(bookMapper, times(1)).toResponse(progress.getBook());
    }

    @Test
    void updateModel_withValidModelAndValidRequest_shouldUpdateModel() {
        // Arrange
        var progress = BookProgressFactory.validProgress();
        var newProgress = SerializationUtils.clone(progress);
        var request = BookProgressRequestFactory.validRequest();

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(UserFactory.validUser(request.getUsername()));

        when(bookMapper.toModel(request.getBookId()))
                .thenReturn(BookFactory.validBook(request.getBookId()));

        // Act
        bookProgressMapper.updateModel(newProgress, request);

        // Assert
        assertEquals(progress.getId(), newProgress.getId());
        assertEquals(request.getStatus(), newProgress.getStatus());
        assertEquals(request.getIsFavorite(), newProgress.getIsFavorite());
        assertEquals(request.getScore(), newProgress.getScore());
        assertEquals(request.getPageCount(), newProgress.getPageCount());
        assertEquals(request.getUsername(), newProgress.getUser().getUsername());
        assertEquals(request.getBookId(), newProgress.getBook().getId());
        assertEquals(progress.getCreatedAt(), newProgress.getCreatedAt());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookMapper, times(1)).toModel(request.getBookId());
    }

}
