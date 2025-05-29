package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.request.BookReviewRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.BookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookReviewMapper;
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
class BookReviewMapperTest {

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @InjectMocks
    private BookReviewMapper bookReviewMapper = Mappers.getMapper(BookReviewMapper.class);

    @Test
    void toModel_withValidRequest_shouldReturnValidModel() {
        // Arrange
        var request = BookReviewRequestFactory.validRequest();
        var review = BookReviewFactory.validReview(request);

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(review.getUser());

        when(bookMapper.toModel(request.getBookId()))
                .thenReturn(review.getBook());

        // Act
        var result = bookReviewMapper.toModel(request);

        // Assert
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getContent(), result.getContent());
        assertEquals(request.getScore(), result.getScore());
        assertEquals(request.getUsername(), result.getUser().getUsername());
        assertEquals(request.getBookId(), result.getBook().getId());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookMapper, times(1)).toModel(request.getBookId());
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidResponse() {
        // Arrange
        var review = BookReviewFactory.validReview();

        when(userMapper.toResponse(review.getUser()))
                .thenReturn(UserResponseFactory.validResponse(review.getUser()));

        when(bookMapper.toResponse(review.getBook()))
                .thenReturn(BookResponseFactory.validResponse(review.getBook()));

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
        verify(userMapper, times(1)).toResponse(review.getUser());
        verify(bookMapper, times(1)).toResponse(review.getBook());
    }

    @Test
    void updateModel_withValidModelAndValidRequest_shouldUpdateModel() {
        // Arrange
        var review = BookReviewFactory.validReview();
        var newReview = SerializationUtils.clone(review);
        var request = BookReviewRequestFactory.validRequest();

        when(userMapper.toModel(request.getUsername()))
                .thenReturn(UserFactory.validUser(request.getUsername()));

        when(bookMapper.toModel(request.getBookId()))
                .thenReturn(BookFactory.validBook(request.getBookId()));

        // Act
        bookReviewMapper.updateModel(newReview, request);

        // Assert
        assertEquals(review.getId(), newReview.getId());
        assertEquals(request.getTitle(), newReview.getTitle());
        assertEquals(request.getContent(), newReview.getContent());
        assertEquals(request.getScore(), newReview.getScore());
        assertEquals(request.getUsername(), newReview.getUser().getUsername());
        assertEquals(request.getBookId(), newReview.getBook().getId());
        assertEquals(review.getCreatedAt(), newReview.getCreatedAt());
        verify(userMapper, times(1)).toModel(request.getUsername());
        verify(bookMapper, times(1)).toModel(request.getBookId());
    }

}
