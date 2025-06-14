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
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

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
    void find_withNonExistingReview_shouldReturnEmptyReviewResponse() {
        // Arrange
        var reviewId = Math.abs(new Random().nextLong()) + 1;

        when(bookReviewRepository.findById(reviewId))
                .thenReturn(Optional.empty());

        // Act
        var result = bookReviewService.find(reviewId);

        // Assert
        assertTrue(result.isEmpty());
        verify(bookReviewRepository, times(1)).findById(reviewId);
        verify(bookReviewMapper, never()).toResponse(any(BookReview.class));
    }

    @Test
    void find_withExistingReview_shouldReturnReviewResponse() {
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
    void create_withValidReviewRequestAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookReviewRequestFactory.validRequest();
            var review = BookReviewFactory.validReview(request);

            when(bookReviewMapper.toEntity(request))
                    .thenReturn(review);

            doNothing()
                    .when(bookReviewValidator)
                    .validate(review);

            when(bookReviewRepository.save(review))
                    .thenReturn(review);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class))
                    .thenThrow(BookReviewModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookReviewModificationForbiddenException.class, () -> bookReviewService.create(request));
            verify(bookReviewMapper, times(1)).toEntity(request);
            verify(bookReviewValidator, times(1)).validate(review);
            verify(bookReviewRepository, times(1)).save(review);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class), times(1));
            verify(bookReviewMapper, never()).toResponse(any(BookReview.class));
        }
    }

    @Test
    void create_withValidReviewRequestAndAuthorizedUser_shouldReturnReviewResponse() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var request = BookReviewRequestFactory.validRequest();
            var review = BookReviewFactory.validReview(request);
            var response = BookReviewResponseFactory.validResponse(review);

            when(bookReviewMapper.toEntity(request))
                    .thenReturn(review);

            doNothing()
                    .when(bookReviewValidator)
                    .validate(review);

            when(bookReviewRepository.save(review))
                    .thenReturn(review);

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            when(bookReviewMapper.toResponse(review))
                    .thenReturn(response);

            // Act
            var result = bookReviewService.create(request);

            // Assert
            assertEquals(response, result);
            verify(bookReviewMapper, times(1)).toEntity(request);
            verify(bookReviewValidator, times(1)).validate(review);
            verify(bookReviewRepository, times(1)).save(review);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class), times(1));
            verify(bookReviewMapper, times(1)).toResponse(review);
        }
    }

    @Test
    void update_withNonExistingReview_shouldFail() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var reviewId = Math.abs(new Random().nextLong()) + 1;
            var updateRequest = BookReviewRequestFactory.validRequest();

            // Act && Assert
            assertThrows(BookReviewNotFoundException.class, () -> bookReviewService.update(reviewId, updateRequest));
            verify(bookReviewRepository, times(1)).findById(reviewId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(bookReviewMapper, never()).updateEntity(any(BookReview.class), any(BookReviewRequest.class));
            verify(bookReviewValidator, never()).validate(any(BookReview.class));
            verify(bookReviewRepository, never()).save(any(BookReview.class));
            verify(bookReviewMapper, never()).toResponse(any(BookReview.class));
        }
    }

    @Test
    void update_withExistingReviewAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var review = BookReviewFactory.validReview();
            var reviewId = review.getId();
            var updateRequest = BookReviewRequestFactory.validRequest();

            when(bookReviewRepository.findById(reviewId))
                    .thenReturn(Optional.of(review));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class))
                    .thenThrow(BookReviewModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookReviewModificationForbiddenException.class, () -> bookReviewService.update(reviewId, updateRequest));
            verify(bookReviewRepository, times(1)).findById(reviewId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class), times(1));
            verify(bookReviewMapper, never()).updateEntity(any(BookReview.class), any(BookReviewRequest.class));
            verify(bookReviewValidator, never()).validate(any(BookReview.class));
            verify(bookReviewRepository, never()).save(any(BookReview.class));
            verify(bookReviewMapper, never()).toResponse(any(BookReview.class));
        }
    }

    @Test
    void update_withExistingReviewAndValidUpdateRequestAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var review = BookReviewFactory.validReview();
            var updateRequest = BookReviewRequestFactory.validRequest();

            var updatedReview = BookReviewFactory.updatedReview(review, updateRequest);
            var updatedReviewResponse = BookReviewResponseFactory.validResponse(updatedReview);

            when(bookReviewRepository.findById(review.getId()))
                    .thenReturn(Optional.of(review));

            doNothing()
                    .when(bookReviewMapper)
                    .updateEntity(review, updateRequest);

            doNothing()
                    .when(bookReviewValidator)
                    .validate(review);

            when(bookReviewRepository.save(review))
                    .thenReturn(updatedReview);

            when(bookReviewMapper.toResponse(updatedReview))
                    .thenReturn(updatedReviewResponse);

            // Act
            var result = bookReviewService.update(review.getId(), updateRequest);

            // Assert
            assertNotNull(result);
            assertEquals(updatedReviewResponse, result);
            verify(bookReviewRepository, times(1)).findById(review.getId());
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class), times(1));
            verify(bookReviewMapper, times(1)).updateEntity(review, updateRequest);
            verify(bookReviewValidator, times(1)).validate(review);
            verify(bookReviewRepository, times(1)).save(review);
            verify(bookReviewMapper, times(1)).toResponse(updatedReview);
        }
    }

    @Test
    void delete_withNonExistingReview_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var reviewId = Math.abs(new Random().nextLong()) + 1;

            when(bookReviewRepository.findById(reviewId))
                    .thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(BookReviewNotFoundException.class, () -> bookReviewService.delete(reviewId));
            verify(bookReviewRepository).findById(reviewId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
        }
    }

    @Test
    void delete_withExistingReviewAndUnauthorizedUser_shouldModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var review = BookReviewFactory.validReview();
            var reviewId = review.getId();

            when(bookReviewRepository.findById(reviewId))
                    .thenReturn(Optional.of(review));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class))
                    .thenThrow(BookReviewModificationForbiddenException.class);

            // Act && Assert
            assertThrows(BookReviewModificationForbiddenException.class, () -> bookReviewService.delete(reviewId));
            verify(bookReviewRepository, times(1)).findById(reviewId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class), times(1));
        }
    }

    @Test
    void delete_withExistingReviewAndAuthorizedUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var review = BookReviewFactory.validReview();
            var reviewId = review.getId();

            when(bookReviewRepository.findById(reviewId))
                    .thenReturn(Optional.of(review));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act
            bookReviewService.delete(reviewId);

            // Assert
            verify(bookReviewRepository, times(1)).findById(reviewId);
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(review.getUser().getKeycloakId(), BookReviewModificationForbiddenException.class), times(1));
        }
    }

}
