package br.com.edu.ifce.maracanau.carekobooks.unit.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator.BookReviewValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewUserConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookReviewValidatorTest {

    @Mock
    private BookReviewRepository bookReviewRepository;

    @InjectMocks
    private BookReviewValidator bookReviewValidator;

    @Test
    void validate_withValidReview_shouldPass() {
        // Arrange
        var review = BookReviewFactory.validReview();

        // Act && Assert
        assertDoesNotThrow(() -> bookReviewValidator.validate(review));
    }

    @Test
    void validate_withInvalidReviewByEmptyUser_shouldFail() {
        // Arrange
        var review = BookReviewFactory.invalidReviewByEmptyUser();

        // Act && Assert
        assertThrows(UserNotFoundException.class, () -> bookReviewValidator.validate(review));
    }

    @Test
    void validate_withInvalidReviewByEmptyBook_shouldFail() {
        // Arrange
        var review = BookReviewFactory.invalidReviewByEmptyBook();

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookReviewValidator.validate(review));
    }

    @Test
    void validate_withInvalidReviewByUserDuplicatedReview_shouldFail() {
        // Arrange
        var existingReview = BookReviewFactory.validReview();
        var review = BookReviewFactory.validReview();
        review.setUser(existingReview.getUser());
        review.setBook(existingReview.getBook());

        when(bookReviewRepository.findAll(ArgumentMatchers.<Specification<BookReview>>any()))
                .thenReturn(List.of(existingReview));

        // Act & Assert
        assertThrows(BookReviewUserConflictException.class, () -> bookReviewValidator.validate(review));
    }

}
