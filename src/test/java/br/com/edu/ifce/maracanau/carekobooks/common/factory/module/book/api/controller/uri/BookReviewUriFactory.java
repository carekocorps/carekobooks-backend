package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.api.controller.uri;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import org.springframework.web.util.UriComponentsBuilder;

public class BookReviewUriFactory {

    private BookReviewUriFactory() {
    }

    public static String validUri() {
        return UriComponentsBuilder
                .fromPath("/v1/books/reviews")
                .build()
                .toUriString();
    }

    public static String validUri(Long reviewId) {
        return UriComponentsBuilder
                .fromPath("/v1/books/reviews")
                .pathSegment(String.valueOf(reviewId))
                .build()
                .toUriString();
    }

    public static String validQueryUri(BookReview review, String orderBy, boolean isAscendingOrder) {
        return UriComponentsBuilder
                .fromPath("/v1/books/reviews")
                .queryParam("username", review.getUser().getUsername())
                .queryParam("score", review.getScore())
                .queryParam("bookId", review.getBook().getId())
                .queryParam("createdBefore", review.getCreatedAt().toLocalDate())
                .queryParam("createdAfter", review.getCreatedAt().toLocalDate())
                .queryParam("orderBy", orderBy)
                .queryParam("isAscendingOrder", isAscendingOrder)
                .build()
                .toUriString();
    }

}
