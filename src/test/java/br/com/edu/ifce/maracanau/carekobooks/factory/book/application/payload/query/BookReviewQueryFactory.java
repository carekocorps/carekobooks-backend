package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class BookReviewQueryFactory {

    private BookReviewQueryFactory() {
    }

    public static URI validURI(BookReview review, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/api/v1/books/reviews")
                .queryParam("username", review.getUser().getUsername())
                .queryParam("score", review.getScore())
                .queryParam("bookId", review.getBook().getId())
                .queryParam("createdBefore", review.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", review.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUri();
    }

}
