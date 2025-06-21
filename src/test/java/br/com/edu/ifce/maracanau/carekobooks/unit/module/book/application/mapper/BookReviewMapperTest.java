package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.request.BookReviewRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
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

@UnitTest
@ExtendWith(MockitoExtension.class)
class BookReviewMapperTest {

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookReviewMapper bookReviewMapper = Mappers.getMapper(BookReviewMapper.class);

    @Test
    void toEntity_withNullReviewRequest_shouldReturnNullReview() {
        // Arrange
        BookReviewRequest reviewRequest = null;

        // Act
        var result = bookReviewMapper.toEntity(reviewRequest);

        // Assert
        assertNull(result);
        verify(userMapper, never()).toEntity(any(String.class));
        verify(bookMapper, never()).toEntity(any(Long.class));
    }

    @Test
    void toEntity_withValidReviewRequest_shouldReturnReview() {
        // Arrange
        var reviewRequest = BookReviewRequestFactory.validRequest();
        var review = BookReviewFactory.validReview(reviewRequest);

        when(userMapper.toEntity(reviewRequest.getUsername()))
                .thenReturn(review.getUser());

        when(bookMapper.toEntity(reviewRequest.getBookId()))
                .thenReturn(review.getBook());

        // Act
        var result = bookReviewMapper.toEntity(reviewRequest);

        // Assert
        assertNotNull(result);
        assertEquals(reviewRequest.getTitle(), result.getTitle());
        assertEquals(reviewRequest.getContent(), result.getContent());
        assertEquals(reviewRequest.getScore(), result.getScore());
        assertEquals(reviewRequest.getUsername(), result.getUser().getUsername());
        assertEquals(reviewRequest.getBookId(), result.getBook().getId());
        verify(userMapper, times(1)).toEntity(reviewRequest.getUsername());
        verify(bookMapper, times(1)).toEntity(reviewRequest.getBookId());
    }

    @Test
    void toResponse_withNullReview_shouldReturnNullReviewResponse() {
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
    void toResponse_withValidReview_shouldReturnReviewResponse() {
        // Arrange
        var review = BookReviewFactory.validReview();

        when(userMapper.toSimplifiedResponse(review.getUser()))
                .thenReturn(SimplifiedUserResponseFactory.validResponse(review.getUser()));

        when(bookMapper.toSimplifiedResponse(review.getBook()))
                .thenReturn(SimplifiedBookResponseFactory.validResponse(review.getBook()));

        // Act
        var result = bookReviewMapper.toResponse(review);

        // Assert
        assertNotNull(result);
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
    void updateEntity_withValidReviewAndNullReviewRequest_shouldPreserveReview() {
        // Arrange
        BookReviewRequest reviewRequest = null;
        var review = BookReviewFactory.validReview();
        var newReview = SerializationUtils.clone(review);

        // Act
        bookReviewMapper.updateEntity(newReview, reviewRequest);

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
    void updateEntity_withValidReviewAndValidReviewRequest_shouldUpdateReview() {
        // Arrange
        var reviewRequest = BookReviewRequestFactory.validRequest();
        var review = BookReviewFactory.validReview();
        var newReview = SerializationUtils.clone(review);

        when(userMapper.toEntity(reviewRequest.getUsername()))
                .thenReturn(UserFactory.validUser(reviewRequest.getUsername()));

        when(bookMapper.toEntity(reviewRequest.getBookId()))
                .thenReturn(BookFactory.validBook(reviewRequest.getBookId()));

        // Act
        bookReviewMapper.updateEntity(newReview, reviewRequest);

        // Assert
        assertEquals(review.getId(), newReview.getId());
        assertEquals(reviewRequest.getTitle(), newReview.getTitle());
        assertEquals(reviewRequest.getContent(), newReview.getContent());
        assertEquals(reviewRequest.getScore(), newReview.getScore());
        assertEquals(reviewRequest.getUsername(), newReview.getUser().getUsername());
        assertEquals(reviewRequest.getBookId(), newReview.getBook().getId());
        assertEquals(review.getCreatedAt(), newReview.getCreatedAt());
        verify(userMapper, times(1)).toEntity(reviewRequest.getUsername());
        verify(bookMapper, times(1)).toEntity(reviewRequest.getBookId());
    }

}
