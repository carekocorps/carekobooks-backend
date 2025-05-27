package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;

public class BookReviewResponseFactory {

    private BookReviewResponseFactory() {
    }

    public static BookReviewResponse validResponse(BookReview review) {
        var response = new BookReviewResponse();
        response.setId(review.getId());
        response.setTitle(review.getTitle());
        response.setContent(review.getContent());
        response.setScore(review.getScore());
        response.setUser(UserResponseFactory.validResponse(review.getUser()));
        response.setBook(BookResponseFactory.validResponse(review.getBook()));
        return response;
    }

}
