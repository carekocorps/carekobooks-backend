package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request.BookReviewRequestFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response.BookReviewResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookReviewMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookReviewService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookReviewValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookReviewServiceTest {

    @Mock
    private BookReviewRepository bookReviewRepository;

    @Mock
    private BookReviewValidator bookReviewValidator;

    @Mock
    private BookReviewMapper bookReviewMapper;

    @InjectMocks
    private BookReviewService bookReviewService;

    @Test
    void find_withNonExistingReview_shouldReturnEmpty() {
        // Arrange
        var id = 1L;

        when(bookReviewRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = bookReviewService.find(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookReviewRepository, times(1)).findById(id);
        verify(bookReviewMapper, never()).toResponse(any(BookReview.class));
    }

    @Test
    void find_withExistingReview_shouldReturnValidResponse() {
        // Arrange
        var review = BookReviewFactory.validReview();
        var response = BookReviewResponseFactory.validResponse(review);

        when(bookReviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));

        when(bookReviewMapper.toResponse(review))
                .thenReturn(response);

        // Act
        var result = bookReviewService.find(review.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(result.get(), response);
        verify(bookReviewRepository, times(1)).findById(review.getId());
        verify(bookReviewMapper, times(1)).toResponse(review);
    }

    @Test
    void create_withValidReview_shouldReturnValidResponse() {
        // Arrange
        var request = BookReviewRequestFactory.validRequest();
        var review = BookReviewFactory.validReview(request);
        var response = BookReviewResponseFactory.validResponse(review);

        when(bookReviewMapper.toModel(request))
                .thenReturn(review);

        doNothing()
                .when(bookReviewValidator)
                .validate(review);

        when(bookReviewRepository.save(review))
                .thenReturn(review);

        when(bookReviewMapper.toResponse(review))
                .thenReturn(response);

        // Act
        var result = bookReviewService.create(request);

        // Assert
        assertEquals(response, result);
        verify(bookReviewMapper, times(1)).toModel(request);
        verify(bookReviewValidator, times(1)).validate(review);
        verify(bookReviewRepository, times(1)).save(review);
        verify(bookReviewMapper, times(1)).toResponse(review);
    }

    @Test
    void update_withNonExistingReview_shouldFail() {
        // Arrange
        var id = 1L;
        var request = BookReviewRequestFactory.validRequest();

        // Act && Assert
        assertThrows(BookReviewNotFoundException.class, () -> bookReviewService.update(id, request));
        verify(bookReviewRepository, times(1)).findById(id);
        verify(bookReviewMapper, never()).updateModel(any(BookReview.class), any(BookReviewRequest.class));
        verify(bookReviewValidator, never()).validate(any(BookReview.class));
        verify(bookReviewRepository, never()).save(any(BookReview.class));
        verify(bookReviewMapper, never()).toResponse(any(BookReview.class));
    }

    @Test
    void update_withExistingReview_shouldPass() {
        // Arrange
        var review = BookReviewFactory.validReview();
        var request = BookReviewRequestFactory.validRequest();
        var updatedReview = BookReviewFactory.updatedReview(review, request);
        var updatedReviewResponse = BookReviewResponseFactory.validResponse(updatedReview);

        when(bookReviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));

        doNothing()
                .when(bookReviewMapper)
                .updateModel(review, request);

        doNothing()
                .when(bookReviewValidator)
                .validate(review);

        when(bookReviewRepository.save(review))
                .thenReturn(updatedReview);

        when(bookReviewMapper.toResponse(updatedReview))
                .thenReturn(updatedReviewResponse);

        // Act
        var result = bookReviewService.update(review.getId(), request);

        // Assert
        assertEquals(updatedReviewResponse, result);
        verify(bookReviewRepository, times(1)).findById(review.getId());
        verify(bookReviewMapper, times(1)).updateModel(review, request);
        verify(bookReviewValidator, times(1)).validate(review);
        verify(bookReviewRepository, times(1)).save(review);
        verify(bookReviewMapper, times(1)).toResponse(updatedReview);
    }

    @Test
    void delete_withNonExistingReview_shouldFail() {
        // Arrange
        var id = 1L;

        when(bookReviewRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(BookReviewNotFoundException.class, () -> bookReviewService.delete(id));
        verify(bookReviewRepository).findById(id);
    }

    @Test
    void delete_withExistingReviewAndUserIsUnauthorized_shouldFail() {
        // Arrange
        var review = BookReviewFactory.validReview();
        var reviewId = review.getId();

        when(bookReviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(review.getUser().getUsername()))
                    .thenReturn(true);

            assertThrows(BookReviewModificationForbiddenException.class, () -> bookReviewService.delete(reviewId));
        }

        verify(bookReviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void delete_withExistingReviewAndUserIsAuthorized_shouldPass() {
        // Arrange
        var review = BookReviewFactory.validReview();
        var reviewId = review.getId();

        when(bookReviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        // Act && Assert
        try (var mockedStatic = mockStatic(AuthenticatedUserProvider.class)) {
            mockedStatic
                    .when(() -> AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(review.getUser().getUsername()))
                    .thenReturn(false);

            assertDoesNotThrow(() -> bookReviewService.delete(reviewId));
        }

        verify(bookReviewRepository, times(1)).findById(reviewId);
    }

}
