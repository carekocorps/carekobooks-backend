package br.com.edu.ifce.maracanau.carekobooks.module.book.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewUserConflictException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookReviewQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookReviewValidator implements BaseValidator<BookReview> {

    private final BookReviewRepository bookReviewRepository;

    public void validate(BookReview review) {
        if (isUserEmpty(review)) {
            throw new UserNotFoundException();
        }

        if (isBookEmpty(review)) {
            throw new BookNotFoundException();
        }

        if (isUserReviewDuplicated(review)) {
            throw new BookReviewUserConflictException();
        }
    }

    private boolean isUserEmpty(BookReview review) {
        return review.getUser() == null;
    }

    private boolean isBookEmpty(BookReview review) {
        return review.getBook() == null;
    }

    private boolean isUserReviewDuplicated(BookReview bookReview) {
        var query = new BookReviewQuery();
        query.setUsername(bookReview.getUser().getUsername());
        query.setBookId(bookReview.getBook().getId());

        var reviews = bookReviewRepository.findAll(query.getSpecification());
        return !reviews.isEmpty();
    }

}
