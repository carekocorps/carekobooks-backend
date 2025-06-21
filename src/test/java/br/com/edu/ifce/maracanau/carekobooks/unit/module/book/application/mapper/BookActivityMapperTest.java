package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookProgressRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookActivityFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
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
        assertThat(result).isNull();
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
        assertThat(activity.getStatus()).isEqualTo(result.getStatus());
        assertThat(activity.getPageCount()).isEqualTo(result.getPageCount());
        assertThat(activity.getUser().getUsername()).isEqualTo(result.getUser().getUsername());
        assertThat(activity.getBook().getId()).isEqualTo(result.getBook().getId());
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
        assertThat(result).isNull();
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
        assertThat(activity.getId()).isEqualTo(result.getId());
        assertThat(activity.getStatus()).isEqualTo(result.getStatus());
        assertThat(activity.getPageCount()).isEqualTo(result.getPageCount());
        assertThat(activity.getUser().getUsername()).isEqualTo(result.getUser().getUsername());
        assertThat(activity.getBook().getId()).isEqualTo(result.getBook().getId());
        assertThat(activity.getCreatedAt()).isEqualToIgnoringNanos(result.getCreatedAt());
        assertThat(activity.getUpdatedAt()).isEqualToIgnoringNanos(result.getUpdatedAt());
        verify(userMapper, times(1)).toSimplifiedResponse(activity.getUser());
        verify(bookMapper, times(1)).toSimplifiedResponse(activity.getBook());
    }

}
