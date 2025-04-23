package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookReviewQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookReviewValidator implements BaseValidator<BookReview> {

    private final BookReviewRepository bookReviewRepository;

    public void validate(BookReview bookReview) {
        if (isUserEmpty(bookReview)) {
            throw new NotFoundException("User not found");
        }

        if (isBookEmpty(bookReview)) {
            throw new NotFoundException("Book not found");
        }

        if (isUserReviewDuplicated(bookReview)) {
            throw new BadRequestException("User has already reviewed this book");
        }
    }

    private boolean isUserEmpty(BookReview bookReview) {
        return bookReview.getUser() == null;
    }

    private boolean isBookEmpty(BookReview bookReview) {
        return bookReview.getBook() == null;
    }

    private boolean isUserReviewDuplicated(BookReview bookReview) {
        var query = new BookReviewQuery();
        query.setUsername(bookReview.getUser().getUsername());
        query.setBookId(bookReview.getBook().getId());

        var bookReviews = bookReviewRepository.findAll(query.getSpecification());
        return !bookReviews.isEmpty();
    }

}
