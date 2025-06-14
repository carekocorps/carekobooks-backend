package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookReviewFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;

public class BookReviewRequestFactory {

    private BookReviewRequestFactory() {
    }

    public static BookReviewRequest validRequest(BookReview review) {
        var request = new BookReviewRequest();
        request.setTitle(review.getTitle());
        request.setContent(review.getContent());
        request.setScore(review.getScore());
        request.setUsername(review.getUser().getUsername());
        request.setBookId(review.getBook().getId());
        return request;
    }

    public static BookReviewRequest validRequest() {
        var review = BookReviewFactory.validReview();
        return validRequest(review);
    }

    public static BookReviewRequest validRequest(Integer score) {
        var request = validRequest();
        request.setScore(score);
        return request;
    }

    public static BookReviewRequest invalidRequestByBlankTitle() {
        var request = validRequest();
        request.setTitle(null);
        return request;
    }

    public static BookReviewRequest invalidRequestByTitleExceedingMaxLength() {
        var request = validRequest();
        request.setTitle("a".repeat(256));
        return request;
    }

    public static BookReviewRequest invalidRequestByBlankContent() {
        var request = validRequest();
        request.setContent(null);
        return request;
    }

    public static BookReviewRequest invalidRequestByContentExceedingMaxLength() {
        var request = validRequest();
        request.setContent("a".repeat(5001));
        return request;
    }

    public static BookReviewRequest invalidRequestByNullScore() {
        var request = validRequest();
        request.setScore(null);
        return request;
    }

    public static BookReviewRequest invalidRequestByNegativeScore() {
        var request = validRequest();
        request.setScore(-1);
        return request;
    }

    public static BookReviewRequest invalidRequestByScoreExceedingMaxValue() {
        var request = validRequest();
        request.setScore(101);
        return request;
    }

    public static BookReviewRequest invalidRequestByBlankUsername() {
        var request = validRequest();
        request.setUsername(null);
        return request;
    }

    public static BookReviewRequest invalidRequestByUsernameExceedingMaxLength() {
        var request = validRequest();
        request.setUsername("a".repeat(51));
        return request;
    }

    public static BookReviewRequest invalidRequestByUsernameRegexMismatch() {
        var request = validRequest();
        request.setUsername("example-user");
        return request;
    }

    public static BookReviewRequest invalidRequestByBlankBookId() {
        var request = validRequest();
        request.setBookId(null);
        return request;
    }

}
