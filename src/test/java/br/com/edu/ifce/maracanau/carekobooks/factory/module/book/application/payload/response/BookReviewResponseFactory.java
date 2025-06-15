package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
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
        response.setUser(SimplifiedUserResponseFactory.validResponse(review.getUser()));
        response.setBook(SimplifiedBookResponseFactory.validResponse(review.getBook()));
        return response;
    }

}
