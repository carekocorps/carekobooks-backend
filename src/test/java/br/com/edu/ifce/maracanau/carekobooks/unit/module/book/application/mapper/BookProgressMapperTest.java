package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
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
class BookProgressMapperTest {

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookProgressMapper bookProgressMapper = Mappers.getMapper(BookProgressMapper.class);

    @Test
    void toEntity_withNullRequest_shouldReturnNull() {
        // Arrange
        BookProgressRequest request = null;

        // Act
        var result = bookProgressMapper.toEntity(request);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void toEntity_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookProgressRequestFactory.validRequest();
        var progress = BookProgressFactory.validProgress(request);

        when(userMapper.toEntity(request.getUsername()))
                .thenReturn(progress.getUser());

        when(bookMapper.toEntity(request.getBookId()))
                .thenReturn(progress.getBook());

        // Act
        var result = bookProgressMapper.toEntity(request);

        // Assert
        assertEquals(progress.getStatus(), result.getStatus());
        assertEquals(progress.getIsFavorite(), result.getIsFavorite());
        assertEquals(progress.getScore(), result.getScore());
        assertEquals(progress.getPageCount(), result.getPageCount());
        assertEquals(progress.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(progress.getBook().getId(), result.getBook().getId());
        verify(userMapper, times(1)).toEntity(request.getUsername());
        verify(bookMapper, times(1)).toEntity(request.getBookId());
    }

    @Test
    void toResponse_withNullModel_shouldReturnNull() {
        // Arrange
        BookProgress progress = null;

        // Act
        var result = bookProgressMapper.toResponse(progress);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toSimplifiedResponse(any(User.class));
        verify(bookMapper, never()).toSimplifiedResponse(any(Book.class));
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidResponse() {
        // Arrange
        var progress = BookProgressFactory.validProgress();

        when(userMapper.toSimplifiedResponse(progress.getUser()))
                .thenReturn(SimplifiedUserResponseFactory.validResponse(progress.getUser()));

        when(bookMapper.toSimplifiedResponse(progress.getBook()))
                .thenReturn(SimplifiedBookResponseFactory.validResponse(progress.getBook()));

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
        verify(userMapper, times(1)).toSimplifiedResponse(progress.getUser());
        verify(bookMapper, times(1)).toSimplifiedResponse(progress.getBook());
    }

    @Test
    void updateEntity_withValidEntityAndNullRequest_shouldPreserveEntity() {
        // Arrange
        BookProgressRequest request = null;
        var progress = BookProgressFactory.validProgress();
        var newProgress = SerializationUtils.clone(progress);

        // Act
        bookProgressMapper.updateEntity(newProgress, request);

        // Assert
        assertEquals(progress.getId(), newProgress.getId());
        assertEquals(progress.getStatus(), newProgress.getStatus());
        assertEquals(progress.getIsFavorite(), newProgress.getIsFavorite());
        assertEquals(progress.getScore(), newProgress.getScore());
        assertEquals(progress.getPageCount(), newProgress.getPageCount());
        assertEquals(progress.getUser().getUsername(), newProgress.getUser().getUsername());
        assertEquals(progress.getBook().getId(), newProgress.getBook().getId());
        assertEquals(progress.getCreatedAt(), newProgress.getCreatedAt());
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void updateEntity_withValidEntityAndValidRequest_shouldUpdateEntity() {
        // Arrange
        var progress = BookProgressFactory.validProgress();
        var newProgress = SerializationUtils.clone(progress);
        var request = BookProgressRequestFactory.validRequest();

        when(userMapper.toEntity(request.getUsername()))
                .thenReturn(UserFactory.validUser(request.getUsername()));

        when(bookMapper.toEntity(request.getBookId()))
                .thenReturn(BookFactory.validBook(request.getBookId()));

        // Act
        bookProgressMapper.updateEntity(newProgress, request);

        // Assert
        assertEquals(progress.getId(), newProgress.getId());
        assertEquals(request.getStatus(), newProgress.getStatus());
        assertEquals(request.getIsFavorite(), newProgress.getIsFavorite());
        assertEquals(request.getScore(), newProgress.getScore());
        assertEquals(request.getPageCount(), newProgress.getPageCount());
        assertEquals(request.getUsername(), newProgress.getUser().getUsername());
        assertEquals(request.getBookId(), newProgress.getBook().getId());
        assertEquals(progress.getCreatedAt(), newProgress.getCreatedAt());
        verify(userMapper, times(1)).toEntity(request.getUsername());
        verify(bookMapper, times(1)).toEntity(request.getBookId());
    }

}
