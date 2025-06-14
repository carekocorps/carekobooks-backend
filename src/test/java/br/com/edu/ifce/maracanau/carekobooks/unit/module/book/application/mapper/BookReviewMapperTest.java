package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookReviewRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookReviewMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
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
class BookReviewMapperTest {

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookReviewMapper bookReviewMapper = Mappers.getMapper(BookReviewMapper.class);

    @Test
    void toEntity_withNullRequest_shouldReturnNull() {
        // Arrange
        BookReviewRequest review = null;

        // Act
        var result = bookReviewMapper.toEntity(review);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void toEntity_withValidRequest_shouldReturnValidEntity() {
        // Arrange
        var request = BookReviewRequestFactory.validRequest();
        var review = BookReviewFactory.validReview(request);

        when(userMapper.toEntity(request.getUsername()))
                .thenReturn(review.getUser());

        when(bookMapper.toEntity(request.getBookId()))
                .thenReturn(review.getBook());

        // Act
        var result = bookReviewMapper.toEntity(request);

        // Assert
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getContent(), result.getContent());
        assertEquals(request.getScore(), result.getScore());
        assertEquals(request.getUsername(), result.getUser().getUsername());
        assertEquals(request.getBookId(), result.getBook().getId());
        verify(userMapper, times(1)).toEntity(request.getUsername());
        verify(bookMapper, times(1)).toEntity(request.getBookId());
    }

    @Test
    void toResponse_withNullEntity_shouldReturnNull() {
        // Arrange
        BookReview review = null;

        // Act
        var result = bookReviewMapper.toResponse(review);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toSimplifiedResponse(any(User.class));
        verify(bookMapper, never()).toSimplifiedResponse(any(Book.class));
    }

    @Test
    void toResponse_withValidEntity_shouldReturnValidResponse() {
        // Arrange
        var review = BookReviewFactory.validReview();

        when(userMapper.toSimplifiedResponse(review.getUser()))
                .thenReturn(SimplifiedUserResponseFactory.validResponse(review.getUser()));

        when(bookMapper.toSimplifiedResponse(review.getBook()))
                .thenReturn(SimplifiedBookResponseFactory.validResponse(review.getBook()));

        // Act
        var result = bookReviewMapper.toResponse(review);

        // Assert
        assertEquals(review.getId(), result.getId());
        assertEquals(review.getTitle(), result.getTitle());
        assertEquals(review.getContent(), result.getContent());
        assertEquals(review.getScore(), result.getScore());
        assertEquals(review.getUser().getUsername(), result.getUser().getUsername());
        assertEquals(review.getBook().getId(), result.getBook().getId());
        assertEquals(review.getCreatedAt(), result.getCreatedAt());
        verify(userMapper, times(1)).toSimplifiedResponse(review.getUser());
        verify(bookMapper, times(1)).toSimplifiedResponse(review.getBook());
    }

    @Test
    void updateEntity_withValidEntityAndNullRequest_shouldPreserveEntity() {
        // Arrange
        BookReviewRequest request = null;
        var review = BookReviewFactory.validReview();
        var newReview = SerializationUtils.clone(review);

        // Act
        bookReviewMapper.updateEntity(newReview, request);

        // Assert
        assertEquals(review.getId(), newReview.getId());
        assertEquals(review.getTitle(), newReview.getTitle());
        assertEquals(review.getContent(), newReview.getContent());
        assertEquals(review.getScore(), newReview.getScore());
        assertEquals(review.getUser().getUsername(), newReview.getUser().getUsername());
        assertEquals(review.getBook().getId(), newReview.getBook().getId());
        assertEquals(review.getCreatedAt(), newReview.getCreatedAt());
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void updateEntity_withValidEntityAndValidRequest_shouldUpdateEntity() {
        // Arrange
        var review = BookReviewFactory.validReview();
        var newReview = SerializationUtils.clone(review);
        var request = BookReviewRequestFactory.validRequest();

        when(userMapper.toEntity(request.getUsername()))
                .thenReturn(UserFactory.validUser(request.getUsername()));

        when(bookMapper.toEntity(request.getBookId()))
                .thenReturn(BookFactory.validBook(request.getBookId()));

        // Act
        bookReviewMapper.updateEntity(newReview, request);

        // Assert
        assertEquals(review.getId(), newReview.getId());
        assertEquals(request.getTitle(), newReview.getTitle());
        assertEquals(request.getContent(), newReview.getContent());
        assertEquals(request.getScore(), newReview.getScore());
        assertEquals(request.getUsername(), newReview.getUser().getUsername());
        assertEquals(request.getBookId(), newReview.getBook().getId());
        assertEquals(review.getCreatedAt(), newReview.getCreatedAt());
        verify(userMapper, times(1)).toEntity(request.getUsername());
        verify(bookMapper, times(1)).toEntity(request.getBookId());
    }

}
