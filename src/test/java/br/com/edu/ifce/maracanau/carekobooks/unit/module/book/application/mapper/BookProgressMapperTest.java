package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookProgressFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
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

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookProgressMapperTest {

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookProgressMapper bookProgressMapper = Mappers.getMapper(BookProgressMapper.class);

    @Test
    void toEntity_withNullProgressRequest_shouldReturnNullProgress() {
        // Arrange
        BookProgressRequest progressRequest = null;

        // Act
        var result = bookProgressMapper.toEntity(progressRequest);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void toEntity_withValidProgressRequest_shouldReturnProgress() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.validRequest();
        var progress = BookProgressFactory.validProgress(progressRequest);

        when(userMapper.toEntity(progressRequest.getUsername()))
                .thenReturn(progress.getUser());

        when(bookMapper.toEntity(progressRequest.getBookId()))
                .thenReturn(progress.getBook());

        // Act
        var result = bookProgressMapper.toEntity(progressRequest);

        // Assert
        assertNotNull(result);
        assertEquals(progress.getStatus(), result.getStatus());
        assertEquals(progress.getIsFavorite(), result.getIsFavorite());
        assertEquals(progress.getScore(), result.getScore());
        assertEquals(progress.getPageCount(), result.getPageCount());
        assertEquals(progress.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(progress.getBook().getId(), result.getBook().getId());
        verify(userMapper, times(1)).toEntity(progressRequest.getUsername());
        verify(bookMapper, times(1)).toEntity(progressRequest.getBookId());
    }

    @Test
    void toResponse_withNullProgress_shouldReturnProgressResponse() {
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
    void toResponse_withValidProgress_shouldReturnProgressResponse() {
        // Arrange
        var progress = BookProgressFactory.validProgress();

        when(userMapper.toSimplifiedResponse(progress.getUser()))
                .thenReturn(SimplifiedUserResponseFactory.validResponse(progress.getUser()));

        when(bookMapper.toSimplifiedResponse(progress.getBook()))
                .thenReturn(SimplifiedBookResponseFactory.validResponse(progress.getBook()));

        // Act
        var result = bookProgressMapper.toResponse(progress);

        // Assert
        assertNotNull(result);
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
    void updateEntity_withValidProgressAndNullProgressRequest_shouldPreserveProgress() {
        // Arrange
        BookProgressRequest progressRequest = null;
        var progress = BookProgressFactory.validProgress();
        var newProgress = SerializationUtils.clone(progress);

        // Act
        bookProgressMapper.updateEntity(newProgress, progressRequest);

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
    void updateEntity_withValidProgressAndValidProgressRequest_shouldUpdateProgress() {
        // Arrange
        var progressRequest = BookProgressRequestFactory.validRequest();
        var progress = BookProgressFactory.validProgress();
        var newProgress = SerializationUtils.clone(progress);

        when(userMapper.toEntity(progressRequest.getUsername()))
                .thenReturn(UserFactory.validUser(progressRequest.getUsername()));

        when(bookMapper.toEntity(progressRequest.getBookId()))
                .thenReturn(BookFactory.validBook(progressRequest.getBookId()));

        // Act
        bookProgressMapper.updateEntity(newProgress, progressRequest);

        // Assert
        assertEquals(progress.getId(), newProgress.getId());
        assertEquals(progressRequest.getStatus(), newProgress.getStatus());
        assertEquals(progressRequest.getIsFavorite(), newProgress.getIsFavorite());
        assertEquals(progressRequest.getScore(), newProgress.getScore());
        assertEquals(progressRequest.getPageCount(), newProgress.getPageCount());
        assertEquals(progressRequest.getUsername(), newProgress.getUser().getUsername());
        assertEquals(progressRequest.getBookId(), newProgress.getBook().getId());
        assertEquals(progress.getCreatedAt(), newProgress.getCreatedAt());
        verify(userMapper, times(1)).toEntity(progressRequest.getUsername());
        verify(bookMapper, times(1)).toEntity(progressRequest.getBookId());
    }

}
